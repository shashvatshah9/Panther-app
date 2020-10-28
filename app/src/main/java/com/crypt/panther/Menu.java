package com.crypt.panther;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by SSHAH on 6/12/2017.
 */

public class Menu {
    private Button btnChangeEmail, btnChangePassword, btnSendResetEmail, btnRemoveUser,
            changeEmail, changePassword, sendEmail, remove, signOut, messages, download;

    private Button fn;

    private EditText oldEmail, newEmail, password, newPassword;

    btnChangeEmail = (Button) findViewById(R.id.change_email_button);
    btnChangePassword = (Button) findViewById(R.id.change_password_button);
    btnSendResetEmail = (Button) findViewById(R.id.sending_pass_reset_button);
    btnRemoveUser = (Button) findViewById(R.id.remove_user_button);
    changeEmail = (Button) findViewById(R.id.changeEmail);
    changePassword = (Button) findViewById(R.id.changePass);
    sendEmail = (Button) findViewById(R.id.send);
    remove = (Button) findViewById(R.id.remove);
    signOut = (Button) findViewById(R.id.sign_out);
    download = (Button) findViewById(R.id.download);
    fn = (Button) findViewById(R.id.filenames);

    oldEmail = (EditText) findViewById(R.id.old_email);
    newEmail = (EditText) findViewById(R.id.new_email);
    password = (EditText) findViewById(R.id.password);
    newPassword = (EditText) findViewById(R.id.newPassword);

    oldEmail.setVisibility(View.GONE);
    newEmail.setVisibility(View.GONE);
    password.setVisibility(View.GONE);
    newPassword.setVisibility(View.GONE);
    changeEmail.setVisibility(View.GONE);
    changePassword.setVisibility(View.GONE);
    sendEmail.setVisibility(View.GONE);
    remove.setVisibility(View.GONE);

    progressBar = (ProgressBar) findViewById(R.id.progressBar);

    if (progressBar != null) {
        progressBar.setVisibility(View.GONE);
    }

    btnChangeEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (user.isEmailVerified()) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.VISIBLE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.GONE);
                remove.setVisibility(View.GONE);
            } else {
                Toast.makeText(getApplicationContext(), "Please verify your e-mail address to use the service", Toast.LENGTH_SHORT);
            }
        }
    });

    changeEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null && !newEmail.getText().toString().trim().equals("")) {
                user.updateEmail(newEmail.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                signOut();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
            } else if (newEmail.getText().toString().trim().equals("")) {
                newEmail.setError("Enter email");
                progressBar.setVisibility(View.GONE);
            }
        }
    });

    btnChangePassword.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(user.isEmailVerified()) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.VISIBLE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.VISIBLE);
                sendEmail.setVisibility(View.GONE);
                remove.setVisibility(View.GONE);
            } else{
                Toast.makeText(getApplicationContext(), "Please verify your e-mail address to use the service", Toast.LENGTH_SHORT);
            }
        }
    });

    changePassword.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null && !newPassword.getText().toString().trim().equals("")) {
                if (newPassword.getText().toString().trim().length() < 6) {
                    newPassword.setError("Password too short, enter minimum 6 characters");
                    progressBar.setVisibility(View.GONE);
                } else {
                    user.updatePassword(newPassword.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                        signOut();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            } else if (newPassword.getText().toString().trim().equals("")) {
                newPassword.setError("Enter password");
                progressBar.setVisibility(View.GONE);
            }
        }
    });

    btnSendResetEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (user.isEmailVerified()) {
                oldEmail.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.VISIBLE);
                remove.setVisibility(View.GONE);
            } else{
                Toast.makeText(getApplicationContext(), "Please verify your e-mail address to use the service", Toast.LENGTH_SHORT);
            }
        }
    });

    sendEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            if (!oldEmail.getText().toString().trim().equals("")) {
                auth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
            } else {
                oldEmail.setError("Enter email");
                progressBar.setVisibility(View.GONE);
            }
        }
    });

    btnRemoveUser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null) {
                user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, SignupActivity.class));
                                finish();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
            }
        }
    });

    signOut.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signOut();
        }
    });

    messages = (Button) findViewById(R.id.messages);

    messages.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent x = new Intent(MainActivity.this, RecipientSelector.class);
            x.putExtra("key","mess");
            startActivity(x);
        }
    });

    download.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, RecipientSelector.class);
            i.putExtra("key","down");
            startActivity(i);
        }
    });

    fn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, RecipientSelector.class);
            i.putExtra("key","filename");
            startActivity(i);
        }
    });
}
