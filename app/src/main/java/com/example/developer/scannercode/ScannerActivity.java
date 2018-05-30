package com.example.developer.scannercode;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView scannerView;
    private Calendar calendar;
    public static final String TAG = "FIRESTORE";
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);

        scannerView = new ZXingScannerView(this);
        contentFrame.addView(scannerView);
    }


    public void addSite(FirebaseFirestore db, String collectionName, Map object){

        db.collection(collectionName).add(object).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID" + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar = Calendar.getInstance();
        String currentDateTimeString = formatter.format(calendar.getTime());
        //Call back data to main activity
        final Intent intent = new Intent();
        intent.putExtra(MainActivity.FORMAT, rawResult.getBarcodeFormat().toString());
        intent.putExtra(MainActivity.CONTENT, rawResult.getText());
        intent.putExtra(MainActivity.TIMESTAMP, currentDateTimeString);


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
                                intent.putExtra(MainActivity.LOCATION, userInput.getText());
                                Log.d("onrResume", "saved "+userInput.getText());
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                                Log.d("onrResume", "finished "+userInput.getText());

                            }
                        })
                .setNegativeButton("Cancel",

                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();

                            }
                        });

        // Create a scan event
        Map<String, Object> event = new HashMap<>();
        event.put("SiteID", "Mathonsi");
        event.put("eventID", 0);
        event.put("description", scan_Result);
        event.put("timeStamp", FieldValue.serverTimestamp());
        event.put("location", "N-S");
        Toast.makeText(this,"POINT SCANNED", Toast.LENGTH_LONG).show();
        addSite(db, "patrolData", event);
        builder.setMessage(scan_Result);
        AlertDialog alert = builder.create();
        alert.show();



    }
}
