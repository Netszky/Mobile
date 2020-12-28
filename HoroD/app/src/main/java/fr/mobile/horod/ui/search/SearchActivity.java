package fr.mobile.horod.ui.search;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.mobile.horod.R;
import fr.mobile.horod.models.ApiHoro;
import fr.mobile.horod.models.Fields;
import fr.mobile.horod.models.Records;
import fr.mobile.horod.util.Constant;
import fr.mobile.horod.util.FastDialog;
import fr.mobile.horod.util.Network;

public class SearchActivity extends AppCompatActivity {
    private EditText arrondissementEditText;
    private ListView ListHoro;
    private TextView textViewPrix;
    private EditText editTextHeure;


    private List<Records> records;
    private List<String> Horodateur = new ArrayList<String>();
    private static final int NOTIF_ID = 123;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayAdapter<String>(
                SearchActivity.this,
                android.R.layout.simple_list_item_1,
                Horodateur
        );
        setContentView(R.layout.activity_search);

        textViewPrix = (TextView) findViewById(R.id.textViewPrix);
        editTextHeure = (EditText) findViewById(R.id.editTextHeure);
        arrondissementEditText = (EditText) findViewById(R.id.arrondissementEditText);
        ListHoro = (ListView) findViewById(R.id.ListHoro);
        ListHoro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tarif = String.valueOf(records.get(position).getFields().getTarifhor());
                textViewPrix.setText(String.format(tarif));
            }
        });

    }
    public void submit(View view) {
        if (arrondissementEditText.getText().toString().isEmpty()){
            FastDialog.showDialog(SearchActivity.this,
                    FastDialog.SIMPLE_DIALOG,
                    "Renseignez un arrondissement");
            return;
        }
        if(!Network.isNetworkAvailable(SearchActivity.this)){
            FastDialog.showDialog(SearchActivity.this,
                    FastDialog.SIMPLE_DIALOG,
                    "Activez les données mobiles");
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = String.format(Constant.URL + "&refine.arrondt=" + arrondissementEditText.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("volley", response);
                Horodateur.clear();
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", error.toString());
                String json = new String(error.networkResponse.data);

                parseJson(json);
            }
        });
        queue.add(stringRequest);
    }
    private void parseJson(String response){
        ApiHoro api = new Gson().fromJson(response, ApiHoro.class);
        records = api.getRecords();

        if (records != null && records.size() > 0) {
            for (int i = 0; i < records.size(); i++) {
                Fields fields = records.get(i).getFields();
                Horodateur.add(String.format(fields.getAdresse() + " Tarif : " + fields.getTarifhor() + " €"));
                adapter.notifyDataSetChanged();
            }
            ListHoro.setAdapter(adapter);
        }
    }

    public void Calcul(View view) {
        if (editTextHeure != null){
            String heuretext = String.valueOf(editTextHeure.getText());
            int heure = Integer.parseInt(heuretext);
            double prix = Double.parseDouble(String.valueOf(textViewPrix.getText()));
            double tarif = prix * heure;
            textViewPrix.setText(String.format("Prix a payer " +  tarif + " €"));

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Notify();
                }
            }, Convert(heure));
            FastDialog.showDialog(SearchActivity.this,
                    FastDialog.SIMPLE_DIALOG,
                    "Notifications O K ");
            }
        if (editTextHeure.getText() == null){
            FastDialog.showDialog(SearchActivity.this,
                    FastDialog.SIMPLE_DIALOG,
                    "Renseignez une heure");
            return;
        }
    }

    public void Notify(){
        Context context = SearchActivity.this;
        Resources res = context.getResources();
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.splashlogo)     // drawable for API 26
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.splashlogo))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Votre Stationnement arrive à expiration")
                .setContentText( "Il vous reste 15 minutes de stationnement"  )
                .setVibrate(new long[] { 0, 500, 110, 500, 110, 450, 110, 200, 110,
                        170, 40, 450, 110, 200, 110, 170, 40, 500 } )
                .setLights(Color.RED, 3000, 3000)
                .build();


        NotificationManager notifManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify( NOTIF_ID, notification );
    }
    public long Convert(Integer time){
        long timeConvert;
        timeConvert = time * 3600000;
        return timeConvert;
    }

}