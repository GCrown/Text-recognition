package com.example.gcrown.textrecognition.module;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gcrown.textrecognition.R;

public class Collention extends AppCompatActivity {

    public TextView textView;
    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collention);
        textView = findViewById(R.id.collection);
        button = findViewById(R.id.collection_delete);
        click();
    }

    public void click(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileStorage.removeCollention();
            }
        });
    }
    }
}
