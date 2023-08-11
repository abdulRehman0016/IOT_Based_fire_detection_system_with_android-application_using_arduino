package com.example.iotbasedfiredetectionsystem;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;




public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView Flame;
    TextView Temp;
    TextView Hum;
    TextView CoD;
    TextView Smoke;
    Button Refresh;
    MediaPlayer player;

    String thingurl = "https://api.thingspeak.com/channels/2036298/feeds.json?api_key=KYZ154RB6JZBMYTB&results=2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.thingresponse);
        Flame = findViewById(R.id.flamev);
        Temp = findViewById(R.id.Temperaturev);
        Hum = findViewById(R.id.Humidityv);
        CoD = findViewById(R.id.Codetectorv);
        Smoke = findViewById(R.id.Smokev);
        Refresh = findViewById(R.id.refreshbutton);
        player = MediaPlayer.create(this,R.raw.ring);
        getData();


        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function

                getData();
                ha.postDelayed(this, 5000);
            }
        }, 5000);
//        Refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                startActivity(getIntent());
//            }
//        });
//
//    }


    }
    private void getData(){
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, thingurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        Date currentTime = Calendar.getInstance().getTime();
                        String formattedDate = DateFormat.getDateInstance().format(currentTime);
                        try {
                            jsonArray = response.getJSONArray("feeds");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                textView.setText("Data is Created At: " + jsonObject.getString("created_at") + " " + currentTime);
                                //Flame.setText(jsonObject.getString("field1"));
                                Temp.setText(jsonObject.getString("field2") + " °C");
                                Hum.setText(jsonObject.getString("field3") + " %");
                                // CoD.setText(jsonObject.getString("field4") + " Range out of 1023");
                                // Smoke.setText(jsonObject.getString("field5") + " Range out of 1023");
                                String flamedata = jsonObject.getString("field1");
                                String Codata = jsonObject.getString("field1");
                                String Smokedata = jsonObject.getString("field1");

                                int dataf = Integer.valueOf(flamedata);
                                if (dataf == 2) {
                                    Flame.setText("No Fire");
                                } else if (dataf == 1) {
                                    Flame.setText("Distant Fire");
                                    player.start();
                                } else if (dataf == 0) {
                                    Flame.setText("Close Fire!!!!!");
                                    player.start();
                                }

                                int dataco = Integer.valueOf(Codata);
                                if (dataco >= 0 && dataco <= 341) {
                                    CoD.setText("No Co Detected");
                                } else if (dataco >= 342 && dataco <= 682) {
                                    CoD.setText("Co Detected");
//                                    player.start();
                                } else if (dataco >= 683 && dataco <= 1023) {
                                    CoD.setText("Co Detected Highly");
                                    player.start();
                                }

                                int datasmoke = Integer.valueOf(Smokedata);
                                if (datasmoke >= 0 && datasmoke <= 255.75) {
                                    Smoke.setText("No Smoke Detected");
                                } else if (datasmoke >= 256.75 && datasmoke <= 511.) {
                                    Smoke.setText("Smoke Detected");
                                    player.start();
                                } else if (datasmoke >= 512.75 && datasmoke <= 1023) {
                                    Smoke.setText("Smoke Detected Highly");
                                    player.start();
                                }

                            }

                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "errror", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mani", "Error: " + error.getMessage());
            }
        });

        requestQueue.add(req);
    }
}

//{
//        "channel": {
//        "id": 2036298,
//        "name": "IOT Based Fire Detection System",
//        "latitude": "0.0",
//        "longitude": "0.0",
//        "field1": "Flame",
//        "field2": "Temperature",
//        "field3": "Humidity",
//        "field4": "CO detector",
//        "field5": "Smoke",
//        "field6": "Spare",
//        "created_at": "2023-02-16T19:46:24Z",
//        "updated_at": "2023-02-17T12:18:46Z",
//        "last_entry_id": 166
//        },
//        "feeds": [
//        {
//        "created_at": "2023-03-07T08:17:55Z",
//        "entry_id": 165,
//        "field1": "1023",
//        "field2": "26.70",
//        "field3": "60.70",
//        "field4": "350",
//        "field5": "158",
//        "field6": "0"
//        },
//        {
//        "created_at": "2023-03-07T08:18:21Z",
//        "entry_id": 166,
//        "field1": "1023",
//        "field2": "26.80",
//        "field3": "60.60",
//        "field4": "340",
//        "field5": "154",
//        "field6": "0"
//        }
//        ]
//        }