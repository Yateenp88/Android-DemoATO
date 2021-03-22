package com.yp.demoato;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class DRMap extends AppCompatActivity {
    //MAp Start
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    //Map End
    public static final String URL_SAVE_DRIVERVEHICLE_LOCATION = "http://feeds.expressindia.com/test/saveDriverVehicleLoc.php";
    // variable for shared preferences.
    NetworkCheckerListener networkCheckerListener = new NetworkCheckerListener();
    SharedPreferences sharedpreferences;
    HttpURLConnection urlConnection = null;
    InputStream is = null;
    String result = null;
    String line = null;
    Context context;
    Resources resources;
    Boolean switchDuty=false;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 20000;//20sec
    private String mobile,sid,cod;
    private Double getLat,getLong;
    private TextView txtonoff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivermap);
        //Below code for session set as per language code   START
        if (SessionManager.getLanguage(getApplicationContext()).length() != 0)
            context = SessionManager.setLocale(DRMap.this, SessionManager.getLanguage(getApplicationContext()));
        else
            context = SessionManager.setLocale(DRMap.this, "en");



        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(LangBar.SHARED_PREFS, Context.MODE_PRIVATE);
        // getting data from shared prefs and
        mobile = sharedpreferences.getString(LangBar.MOBILENO, null);
        cod = sharedpreferences.getString(LangBar.LANCODE, null);
        sid = sharedpreferences.getString(DRForm1.SID, null);
        txtonoff=(TextView) findViewById(R.id.txtonoff);

        //MP
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDriverView);
        //Initialise fused location
        client = LocationServices.getFusedLocationProviderClient(this);
        //Check Permission
        if (ActivityCompat.checkSelfPermission(DRMap.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When permission granted
            getLocation();
        } else {
            //When permission denied
            //request permission

            ActivityCompat.requestPermissions(DRMap.this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        //MAp End
        Switch onOffSwitch = (Switch)  findViewById(R.id.switchDuty);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchDuty= isChecked;

                if (isChecked) {
                    txtonoff.setText("ON");
                   // Toast.makeText(DRMap.this,"On Duty " , Toast.LENGTH_SHORT).show();
                } else {
                    txtonoff.setText("OFF");
                   // Toast.makeText(DRMap.this,"Off Duty" , Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    //Registration page will redirect with message
    public void btngpstracker(View v) {
        Intent myIntent = new Intent(getBaseContext(),   DriverActivity.class);
        myIntent.putExtra("value1","India");
        startActivity(myIntent);

    }
// Will send Location detail to Mysql
    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);

                if(!switchDuty){
                    //Duty Off

                } else {
                    // Duty ON
                    //Get current location
                    getLocation();
                    //Save driver location in mysql
                    saveDriveLocation();
                  //  Toast.makeText(DRMap.this, "This method is run every 20 seconds" + mobile,Toast.LENGTH_SHORT).show();
                }

            }
        }, delay);
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }


    //Map Start++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    //sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //initialise lat long
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            getLat=location.getLatitude();
                            getLong=location.getLongitude();
                           // Toast.makeText(DRMap.this,"Your "+ location.getLatitude()+" & "+location.getLongitude(), Toast.LENGTH_LONG).show();
                            //create marker option
                            MarkerOptions options = new MarkerOptions().position(latLng).title("My Location");
                           // MarkerOptions options = new MarkerOptions().position(latLng).title(resources.getString(R.string.myloc));
                            //zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                            //Add marker on map
                            googleMap.addMarker(options);

                        }
                    });
                }

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //When permission granted
                //call method
                getLocation();
            }
        }

    }
    //Map End+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private void saveDriveLocation() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting Record .Please wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_DRIVERVEHICLE_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Integer frm4Status;
                        try {
                            JSONObject objsp = new JSONObject(response);
                            JSONArray jsonArray2 = objsp.getJSONArray("msg_responce");
                            // Toast.makeText(DRForm3.this, "Check1"+jsonArray2, Toast.LENGTH_SHORT).show();

                            if (!objsp.getBoolean("error")) {
                                //if record update successfully

                            } else {
                                //if record update failed
                                Toast.makeText(DRMap.this, "Please Try Again Later!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //on error storing the name to cloud
                        Toast.makeText(DRMap.this,"Failed To Connect.Please check INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("gtdrflg", "1");
                params.put("gtdrsid", sid);
                params.put("gtdrmob", mobile);
                params.put("gtdrlat", String.valueOf(getLat));
                params.put("gtdrlon", String.valueOf(getLong));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


}
