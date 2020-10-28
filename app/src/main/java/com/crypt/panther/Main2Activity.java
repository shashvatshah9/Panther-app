package com.crypt.panther;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.util.ArrayList;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private FloatingActionButton fb;
    private FirebaseAuth fa;
    private TextView textView;
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        String userID=null;
        Intent recieve = getIntent();
        Bundle extras = getIntent().getExtras();
        userID = extras.getString("recip");

        textView = (TextView) findViewById(R.id.textView2);
        textView.setText(userID);
        fb = (FloatingActionButton) findViewById(R.id.fab);
        final String finalUserID = userID;

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                    .getReference()
                    .push()
                    .setValue(new ChatMessage(input.getText().toString(),
                            FirebaseAuth.getInstance()
                                    .getCurrentUser()
                                    .getEmail(), finalUserID)
                    );

                // Clear the input
                input.setText("");
            }
        });

        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,

            R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                if((model.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())&&(model.getMessageUser().equals(finalUserID))) || (model.getMessageUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && model.getUserID().equals(finalUserID))) {
                    final TextView messageText = (TextView) v.findViewById(R.id.message_text);
                    TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                    TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                    messageText.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            ClipboardManager cp = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("", messageText.getText().toString());
                            cp.setPrimaryClip(clip);
                            Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    // Set their text
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());

                        // Format the date before showing it
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                            model.getMessageTime()));
                    }
                    
            }
        };

        listOfMessages.setAdapter(adapter);
        //listOfMessages.setScrollBarSize(i[0]);
    }
}
