package com.example.youtubevideotitlefromurl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youtubevideotitlefromurl.util.TitleCallable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private EditText urlEdt;
    private Button submitButton;
    private TextView resultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize views
        urlEdt = (EditText) findViewById(R.id.url_edt);
        submitButton = (Button) findViewById(R.id.submit_button);
        resultTextView = (TextView) findViewById(R.id.result_textview);

        //check for intent from youtube app
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.contains("text")) {
                handleSentText(intent);
            }
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = urlEdt.getText().toString().trim();
                if(url.length()==0){
                    urlEdt.setError("Can't be empty");
                }
                else{
                    // for url patterns like <www.youtube.com/watch?v=VfkMEPXjBl4> (from web)
                    // or </www.youtu.be/VfkMEPXjBl4> (from app)
                    if (url.contains("youtube") || url.contains("youtu.be")) {
                        String title = getVideoTitle(url);
                        if (title == null) {
                            Toast.makeText(getApplicationContext(), "Unexpected error. Try again later. ",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String result = "VIDEO TITLE FROM URL : \n" + title;
                            resultTextView.setText(result);
                        }

                    }
                    //invalid url : display a toast
                    else {
                        Toast.makeText(getApplicationContext(), "Enter Valid YouTube video url",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });




    }

    private String getVideoTitle(String url) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> future = executorService.submit(new TitleCallable(url));
        String titleFromUrl = null;
        try {
            titleFromUrl = future.get();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Exception occurred \n"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }


        executorService.shutdown();
        return titleFromUrl;

    }


    //handling the intent from youtube app
    private void handleSentText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            //set the url edit text to this text
            urlEdt.setText(sharedText);

        }
    }
}
