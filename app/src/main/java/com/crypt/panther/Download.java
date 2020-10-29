package com.crypt.panther;

import android.app.ProgressDialog;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Download extends AppCompatActivity {

    private String recip;
    private Button button;
    private EditText editText;

    @SuppressWarnings("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://panther-ba4c1.appspot.com");
        final StorageReference spaceRef = storageReference.child("Files");

        recip = getIntent().getExtras().getString("recip");

        Firebase fb = new Firebase("https://panther-ba4c1.firebaseio.com/");

        editText = (EditText) findViewById(R.id.fname);

        button = (Button) findViewById(R.id.download);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String filename = editText.getText().toString();  // to define the file name


                String sizef = filename.substring(filename.indexOf("(") + 1);  //
                    sizef = sizef.substring(0, sizef.indexOf(")"));   //

                final int filesize = Integer.parseInt(sizef);    //

                StorageReference downref = spaceRef.child(filename);
                final ProgressDialog dialogue = new ProgressDialog(Download.this);
                dialogue.setTitle("Downloading File");
                dialogue.show();
                Uploader downlo = new Uploader(getApplicationContext());
                String filename1 = filename.substring(0,filename.indexOf(")"));
                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename1+".jpg");
                File myFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

                downref.getFile(myFile)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                            dialogue.dismiss();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            if (progress == 100) {
                                dialogue.dismiss();
                            } else {
                                dialogue.setMessage("Downloaded " + (int) progress + "%");
                            }
                        }
                    });

                try {
                     f = downlo.downloader(myFile, filename1, filesize);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        
    }
}
