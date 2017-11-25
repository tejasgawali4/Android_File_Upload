package org.bmnepali.imageupload;

import android.Manifest;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.UUID;

import static org.bmnepali.imageupload.MainActivity.BASE_URL;

/**
 * Created by Carl_johnson on 20-11-2017.
 */

public class MultiUploadOptions extends AppCompatActivity {

    Button btnSendEmail;
    static final int PICKFILE_RESULT_CODE = 0;
    static final int PICKCAMERA_RESULT_CODE = 1;
    static final int PICKGALLARY_RESULT_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiuploadoptions);

        btnSendEmail = (Button) findViewById(R.id.btnSendEmail);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog();
            }
        });
    }


    private void startDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.select_file_dialog, null);
        dialogBuilder.setView(dialogView);

/*        dialogBuilder.setTitle("Options");
        dialogBuilder.setMessage("Select option to upload");*/

        ImageButton mbtnGallary = (ImageButton) dialogView.findViewById(R.id.btnGallary);
        ImageButton mbtnFiles = (ImageButton) dialogView.findViewById(R.id.btnFiles);
        ImageButton mbtnCamera = (ImageButton) dialogView.findViewById(R.id.btnCamera);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        mbtnGallary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 2);//one can be replaced with any action code
                b.dismiss();
            }
        });

        mbtnFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeFiles = new Intent(Intent.ACTION_GET_CONTENT);
                takeFiles.setType("file*//*");
                takeFiles.setType("camera*//*");
                takeFiles.setType("image*//*");
                takeFiles.setType("video*//*");
                startActivityForResult(takeFiles, PICKFILE_RESULT_CODE);
                b.dismiss();
            }
        });

        mbtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);//zero can be replaced with any action code*/
                b.dismiss();
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        String filePath;

        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {

                    AllowRunTimePermission();

                    Uri picUri = imageReturnedIntent.getData();
                    filePath = getPath(picUri);
                    Log.d("picUri", picUri.toString());
                    Log.d("filePath", filePath);

                    Toast.makeText(getApplicationContext(), "Path" + filePath, Toast.LENGTH_SHORT).show();
                    FileUploadFunction(filePath);
                }

                break;
            case PICKCAMERA_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri picUri = imageReturnedIntent.getData();
                    filePath = getPath(picUri);
                    Log.d("picUri", picUri.toString());
                    Log.d("filePath", filePath);

                    Toast.makeText(getApplicationContext(), "Path" + filePath, Toast.LENGTH_SHORT).show();
                    FileUploadFunction(filePath);
                }
                break;
            case PICKGALLARY_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri picUri = imageReturnedIntent.getData();
                    filePath = getPath(picUri);
                    Log.d("picUri", picUri.toString());
                    Log.d("filePath", filePath);

                    Toast.makeText(getApplicationContext(), "Path" + filePath, Toast.LENGTH_SHORT).show();
                    FileUploadFunction(filePath);
                }
                break;
        }
    }

    public void FileUploadFunction(String filename) {

        String NameHolder, PathHolder = filename, ID;

        if (PathHolder == null) {

            Toast.makeText(this, "Please move your file to internal storage & try again.", Toast.LENGTH_LONG).show();

        } else {

            try {

                ID = UUID.randomUUID().toString();
                NameHolder = UUID.randomUUID().toString();

                /*FIle Name To Store In database*/
                Log.d("NameHolder" , NameHolder);
                /*Full Path Information*/
                Log.d("PathHolder" , filename);


                new MultipartUploadRequest(this, ID, BASE_URL)
                        .addFileToUpload(PathHolder, "file")
                        .addParameter("name", NameHolder)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();

            } catch (Exception exception) {

                Toast.makeText(this,"Exception :-" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void AllowRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MultiUploadOptions.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {

            Toast.makeText(MultiUploadOptions.this,"READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MultiUploadOptions.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MultiUploadOptions.this,"Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MultiUploadOptions.this,"Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

}
