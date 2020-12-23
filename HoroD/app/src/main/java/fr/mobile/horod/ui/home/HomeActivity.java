package fr.mobile.horod.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import fr.mobile.horod.R;
import fr.mobile.horod.ui.maps.MapsActivity;
import fr.mobile.horod.ui.search.SearchActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void MapActivity(View view) {
        Intent intentmap = new Intent(HomeActivity.this, MapsActivity.class);
        startActivity(intentmap);
    }

    public void SearchActivity(View view) {
        Intent intentSearch = new Intent(HomeActivity.this, SearchActivity.class);
        startActivity(intentSearch);
    }
}