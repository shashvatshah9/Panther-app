package com.crypt.panther;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class FileSelector2 extends AppCompatActivity {

    private String recip;
    private ImageView mImageView;
    private static final int FILE_SELECT_CODE = 0;
    private String path = null;
    private File f = null;
    private Button browse;
    private Button upload;
    private TextView tv;
    private FileInputStream fis;
    private StorageReference spaceReference;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selector2);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://panther-ba4c1.appspot.com");

        mImageView = (ImageView) findViewById(R.id.imageView1);
        browse = (Button) findViewById(R.id.Browse);
        upload = (Button) findViewById(R.id.Upload);
        tv = (TextView) findViewById(R.id.tv);

        spaceReference = storageReference.child("Files");

        // To create bytearray, we can replace this later
        // mImageView.setDrawingCacheEnabled(true);
        // mImageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // mImageView.layout(0, 0, mImageView.getMeasuredWidth(), mImageView.getMeasuredHeight());
        // mImageView.buildDrawingCache();
        // Bitmap bitmap = Bitmap.createBitmap(mImageView.getDrawingCache());
        // ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        // byte[] data = outputStream.toByteArray();

        recip = getIntent().getExtras().getString("recip");

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fileUploader();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressWarnings("VisibleForTests")
    private void fileUploader() throws Exception {

        final ProgressDialog dialogue = new ProgressDialog(this);
        dialogue.setTitle("Uploading Image");
        dialogue.show();

        //Can also put files directly by passing uri as shown below
        //ref.putFile(uri);

        InputStream is = getContentResolver().openInputStream(uri);
        FileInputStream fis = (FileInputStream) is;
        byte[] upload = new byte[is.available()];
        fis.read(upload);

        Compressor comp = new Compressor();
        Uploader uplo = new Uploader(getApplicationContext());
        final elem element = uplo.Upload(upload);

        final String s = "/" + "to:" + recip + "_by:" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "-(" + String.valueOf(element.size) +/* ":" + String.valueOf(elobj.size) +*/ ").wav";

        StorageReference ref = spaceReference.child(s);

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        Log.d("UPLOAD","Upload will start below");
        UploadTask uploadTask = ref.putBytes(element.data);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                firebaseDatabase.getReference().push()
                    .setValue(new ChatMessage(s,
                            FirebaseAuth.getInstance()
                                    .getCurrentUser()
                                    .getEmail(), recip)
                    );
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                if (progress == 100) {
                    dialogue.dismiss();
                    Log.d("UPLOADED", "Upload task completed");
                } else {
                    dialogue.setMessage("Uploaded " + (int) progress + "%");
                }
            }
        });

    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
            Toast.makeText(getApplicationContext(), "Inside showfilechooser", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Please Install a File Manager", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    try{
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        mImageView.setImageBitmap(bm);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}