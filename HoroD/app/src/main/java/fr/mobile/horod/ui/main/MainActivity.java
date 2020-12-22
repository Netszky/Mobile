package fr.mobile.horod.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import fr.mobile.horod.R;
import fr.mobile.horod.ui.home.HomeActivity;
import fr.mobile.horod.ui.maps.MapsActivity;

public class MainActivity extends AppCompatActivity {

    private Timer chrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chrono = new Timer();
        chrono.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent myIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(myIntent);
                finish();
            }
        }, 2000);
    }
}