package com.example.developer.scannercode;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_SCANNER = 1;
    public final static String FORMAT = "format";
    public final static String CONTENT = "content";
    public final static String LOCATION = "location";
    public final static String TIMESTAMP = "time";
    private static final String TAG = "Firestore" ;

    private TextView content;
    private TextView format;
    private TextView location;
    private TextView time;
    // Access a Cloud Firestore instance from your Activity
    public static final String TAG1 = "FIRESTORE";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        View btnScan = findViewById(R.id.btn_scan);
        
        //Button to setup
       // Button btnSetup = (Button) findViewById(R.id.btn_setup);
        format = (TextView) findViewById(R.id.format);
        content = (TextView) findViewById(R.id.content);
        location = (TextView) findViewById(R.id.location);
        time = (TextView) findViewById(R.id.timestamp);

        //setSupportActionBar(toolbar);
        
        
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivityForResult(intent, REQUEST_SCANNER);
            }
        });

    }

    public void panicButton(View view){
        // Create a panic event
        Map<String, Object> event = new HashMap<>();
        event.put("SiteID", "Mathonsi");
        event.put("eventID", 8);
        event.put("description", "Panic");
        event.put("timeStamp", FieldValue.serverTimestamp());
        event.put("location", "N-S");
        Toast.makeText(this,"PANIC SENT", Toast.LENGTH_LONG).show();
        addSite(db, "patrolData", event);
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


    /*
    public void startSetup(View view){
        Intent intent = new Intent(MainActivity.this, StartsSetUp.class);
        //startActivityForResult(intent, REQUEST_SCANNER);
    }
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = data.getExtras();
        //String content_ = bundle.getString(CONTENT);
        content.setText(bundle.getString(CONTENT));
        // Create a new user with a first and last name


        //location.setText(bundle.getString(LOCATION));
        time.setText(bundle.getString(TIMESTAMP));
        //Push to server




        /*
        content.setText(data.getStringExtra(CONTENT));
        format.setText(bundle.getString(FORMAT));


        */

    }
}
