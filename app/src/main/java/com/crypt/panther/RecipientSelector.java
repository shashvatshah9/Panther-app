package com.crypt.panther;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RecipientSelector extends AppCompatActivity {
    private Button select;
    private EditText intendeduser;
    private String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_selector);
        select = (Button) findViewById(R.id.selectb);
        intendeduser = (EditText) findViewById(R.id.intended);

        key = getIntent().getExtras().getString("key");

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m = new Intent(RecipientSelector.this, Download.class);
                Intent n = new Intent(RecipientSelector.this, FileSelector2.class);
                Intent z = new Intent(RecipientSelector.this, Main2Activity.class);
                
                n.putExtra("recip", intendeduser.getText().toString());
                m.putExtra("recip", intendeduser.getText().toString());
                z.putExtra("recip", intendeduser.getText().toString());
                
                if (key.equalsIgnoreCase("mess")) {
                    startActivity(n);
                } else if(key.equalsIgnoreCase("down")) {
                    startActivity(m);
                } else
                    startActivity(z);
                }
            }
        });
    }
}
