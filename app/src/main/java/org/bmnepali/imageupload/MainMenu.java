package org.bmnepali.imageupload;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    Button mbtnChooseUpload , mbtnMultiUploadOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mbtnChooseUpload = (Button) findViewById(R.id.btnChooseImg);
        mbtnMultiUploadOptions = (Button) findViewById(R.id.btnMultiUpload);

        mbtnChooseUpload.setOnClickListener(this);
        mbtnMultiUploadOptions.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mbtnChooseUpload)
        {
            Intent i = new Intent(this , MainActivity.class);
            startActivity(i);
        }
        else if(v == mbtnMultiUploadOptions)
        {
            Intent i = new Intent(this , MultiUploadOptions.class);
            startActivity(i);
        }
    }
}
