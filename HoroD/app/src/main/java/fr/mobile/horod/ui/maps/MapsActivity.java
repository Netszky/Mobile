package fr.mobile.horod.ui.maps;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.mobile.horod.R;
import fr.mobile.horod.models.ApiHoro;
import fr.mobile.horod.models.Fields;
import fr.mobile.horod.models.Records;
import fr.mobile.horod.ui.search.SearchActivity;
import fr.mobile.horod.util.Constant;
import fr.mobile.horod.util.FastDialog;
import fr.mobile.horod.util.Network;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Records> records;
    private Map<String, Fields> markers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(20.0f);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Fields fields = markers.get(marker.getId());
                Intent map = new Intent(MapsActivity.this, SearchActivity.class);
                String test = String.valueOf(fields.getTarifhor());
                String adresse = String.valueOf(fields.getAdresse());
                map.putExtra("tarif", test);
                map.putExtra("adresse", adresse);
                startActivity(map);
            }
        });

        getData(mMap);
    }
    private void getData(GoogleMap mMap) {

        if (!Network.isNetworkAvailable(MapsActivity.this)) {
            FastDialog.showDialog(
                    MapsActivity.this,
                    FastDialog.SIMPLE_DIALOG,
                    getString(R.string.dialog_network_error)
            );
            return;
        }

        // requête HTTP avec Volley
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constant.URL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        Log.e("volley", json);

                        ApiHoro api = new Gson().fromJson(json, ApiHoro.class);

                        records = api.getRecords();

                        if (records != null && records.size() > 0) {

                            for (int i = 0; i < records.size(); i++) {
                                Fields fields = records.get(i).getFields();
                                // addMarker
                                Marker marker = mMap.addMarker(
                                        new MarkerOptions()
                                                .position(
                                                        new LatLng(
                                                                fields.getGeo_point_2d()[0],
                                                                fields.getGeo_point_2d()[1]
                                                        )
                                                )
                                                .title("Adresse : " + fields.getAdresse() + fields.getArrondt() + " eme" )
                                                .snippet("Tarif Horaire : "+ fields.getTarifhor() + "€")
                                );
                                markers.put(marker.getId(), fields); // pour associer l'identifiant d'un Market aux données (de l'objet Fields)


                                if (i == 0) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                            new LatLng(
                                                    fields.getGeo_point_2d()[0],
                                                    fields.getGeo_point_2d()[1]
                                            )
                                    ));
                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                        new LatLng(
                                                fields.getGeo_point_2d()[0],
                                                fields.getGeo_point_2d()[1]
                                        )
                                ));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String json = new String(error.networkResponse.data);

                Log.e("volley", json);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}