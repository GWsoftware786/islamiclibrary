package com.gwsoftware.alahazratkakalam.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.gwsoftware.alahazratkakalam.R;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        TextView textView = findViewById(R.id.note_text_2);
       // textView.setMovementMethod(LinkMovementMethod.getInstance());

    }
}