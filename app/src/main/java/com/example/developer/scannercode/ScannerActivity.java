package com.example.developer.scannercode;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);

        scannerView = new ZXingScannerView(this);
        contentFrame.addView(scannerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        final String scan_Result = rawResult.getText();
        //Call back data to main activity
        final Intent intent = new Intent();
        intent.putExtra(MainActivity.FORMAT, rawResult.getBarcodeFormat().toString());
        intent.putExtra(MainActivity.CONTENT, rawResult.getText());


        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        builder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);


        /*
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(MainActivity.this);

            }
        });*/
        // set dialog message
        builder.setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                Log.d("onrResume", " "+userInput.getText());

                                setResult(Activity.RESULT_OK, intent);
                                finish();
                                //scannerView.stopCamera();
                                //Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access camera", Toast.LENGTH_LONG).show();
                            }
                        })
                .setNegativeButton("Cancel",

                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });


        builder.setMessage(scan_Result);
        AlertDialog alert = builder.create();
        alert.show();



    }
}
