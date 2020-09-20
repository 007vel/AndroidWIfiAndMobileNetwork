package com.example.dashcam;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final NetworkConfig networkConfig=new NetworkConfig(this);
        Button mobile = findViewById(R.id.buttonMobile);
        Button wifi = findViewById(R.id.buttonWifi);
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               networkConfig.makeHTTPRequestCellular("http://192.168.137.1:18000/accounts", "",true);
               // networkConfig.makeHTTPRequestCellular("http://192.168.137.1", "",true);
            }
        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkConfig.makeHTTPRequestCellular("https://accounts.google.com/.well-known/openid-configuration",
                        "",false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}