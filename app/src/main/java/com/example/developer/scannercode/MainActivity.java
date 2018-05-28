package com.example.developer.scannercode;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_SCANNER = 1;
    public final static String FORMAT = "format";
    public final static String CONTENT = "content";
    public final static String LOCATION = "location";
    public final static String TIMESTAMP = "time";

    private TextView content;
    private TextView format;
    private TextView location;
    private TextView time;

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

        //location.setText(bundle.getString(LOCATION));
        time.setText(bundle.getString(TIMESTAMP));

        /*
        content.setText(data.getStringExtra(CONTENT));
        format.setText(bundle.getString(FORMAT));


        */

    }
}
