package com.apkglobal.implementationofsentimentapi;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import static android.R.attr.data;

public class Main2Activity extends AppCompatActivity {
    int pid =111;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageButton btn_speak = (ImageButton) findViewById(R.id.btn_speak);
        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //take local language from user side
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                //show some info while clicking
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"speak");
                //to send the intent with process id to OS
                startActivityForResult(intent,pid);

            }
    });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==pid)
        {
            if(resultCode==RESULT_OK && data!=null)//data coming from intent
            {
                ArrayList<String> arrayList=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                TextView tv_dekho = (TextView) findViewById(R.id.tv_text);
                tv_dekho.setText(arrayList.get(0).toString());
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                    intent.putExtra("text",arrayList.get(0).toString());
                    startActivity(intent);
            }
        }
    }
}
