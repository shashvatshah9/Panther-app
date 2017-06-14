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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FileSelector extends AppCompatActivity {

    private StorageReference mStorageRef;
    private DatabaseReference mDBRef;
    private EditText editText;
    private ImageView imgView;
    private Uri imgUri;
    private String recip;
    private static boolean s_persistenceInitialized = false;
    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST__CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selector);

        mStorageRef = FirebaseStorage.getInstance().getReference();



        imgView = (ImageView) findViewById(R.id.image);
        editText = (EditText) findViewById(R.id.edittext);

        Intent g = getIntent();
        Bundle extras = g.getExtras();
        recip = extras.getString("recip");

    }

    public void btnBrowse_Click(View v){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Image"), REQUEST__CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST__CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUri = data.getData();
            try{
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imgView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @SuppressWarnings("VisibleForTests")
    public void btnUpload_Click(View view){
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            if(imgUri!=null){
                final ProgressDialog dialogue = new ProgressDialog(this);
                dialogue.setTitle("Uploading Image");
                dialogue.show();

                //Get the storage reference
                StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + "/" + "to:" + recip + "_by:" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + System.currentTimeMillis() + "." + getImageExt(imgUri));

                //Add file to reference
                ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        dialogue.dismiss();
                        Toast.makeText(getApplicationContext(), "Successfully Uploaded!", Toast.LENGTH_SHORT).show();

                        ImageUpload imgupload = new ImageUpload(editText.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                        String uploadid = mDBRef.push().getKey();
                        mDBRef.child(uploadid).setValue(imgupload);

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                dialogue.dismiss();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        })

                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                double progress = 100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount();
                                dialogue.setMessage("Uploaded " + (int) progress);

                            }
                        });
            }
            else{
                Toast.makeText(getApplicationContext(), "Please Select a File", Toast.LENGTH_SHORT).show();
            }
        else
            Toast.makeText(getApplicationContext(), "Please Login first, to Upload the File", Toast.LENGTH_SHORT);
    }
}
