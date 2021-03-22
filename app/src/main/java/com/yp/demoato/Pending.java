package com.yp.demoato;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Pending extends AppCompatActivity {
    // variable for shared preferences.
    NetworkCheckerListener networkCheckerListener = new NetworkCheckerListener();
    SharedPreferences sharedpreferences;
    HttpURLConnection urlConnection = null;
    InputStream is = null;
    String result = null;
    String line = null;
    Context context;
    Resources resources;

    private String mobile,sid,cod;
    private Integer totalForm = null;
    private ImageView imgstatus1,imgstatus2,imgstatus3,imgstatus4;
    private TextView txtregistration,txtstatus1,txtstatus2,txtstatus3,txtstatus4,txtstandplate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(LangBar.SHARED_PREFS, Context.MODE_PRIVATE);
        // getting data from shared prefs and
        mobile = sharedpreferences.getString(LangBar.MOBILENO, null);
        cod = sharedpreferences.getString(LangBar.LANCODE, null);
        sid = sharedpreferences.getString(DRForm1.SID, null);


        imgstatus1 = (ImageView) findViewById(R.id.imgstatus1);
        imgstatus2 = (ImageView) findViewById(R.id.imgstatus2);
        imgstatus3 = (ImageView) findViewById(R.id.imgstatus3);
        imgstatus4 = (ImageView) findViewById(R.id.imgstatus4);

        txtregistration= (TextView) findViewById(R.id.txtregistration);
        txtstatus1= (TextView) findViewById(R.id.txtstatus1);
        txtstatus2= (TextView) findViewById(R.id.txtstatus2);
        txtstatus3= (TextView) findViewById(R.id.txtstatus3);
        txtstatus4= (TextView) findViewById(R.id.txtstatus4);
        txtstandplate= (TextView) findViewById(R.id.txtstand);

        //Below code for session set as per language code   START
        if (SessionManager.getLanguage(getApplicationContext()).length() != 0)
            context = SessionManager.setLocale(Pending.this, SessionManager.getLanguage(getApplicationContext()));
        else
            context = SessionManager.setLocale(Pending.this, "en");

     //   txtstatus1.setText(resources.getString(R.string.status1));
      //  txtstatus2.setText(resources.getString(R.string.status2));
      //  txtstatus3.setText(resources.getString(R.string.status3));
     //   txtstatus4.setText(resources.getString(R.string.status4));
      //  txtregistration.setText(resources.getString(R.string.penregistration));
//        txtstandplate.setText(resources.getString(R.string.standplate));


    }

    private void checkDriverRegistration() {
        //Check Whether Driver already register or not-------------------------------START------------------------------------------------------
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {

            URL url = new URL("http://feeds.expressindia.com/test/checkDriverFormFullStatus.php?mob=" + mobile);
            // Toast.makeText(getApplicationContext(), mobile + "Form Status : " + sid, Toast.LENGTH_SHORT).show();
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            is = urlConnection.getInputStream();
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            // Toast.makeText(getApplicationContext(), "Fail 1" + e.toString(), Toast.LENGTH_LONG).show();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("Fail 2", e.toString());
            //Toast.makeText(getApplicationContext(), "Fail 2" + e.toString(), Toast.LENGTH_LONG).show();
        }

        try {
            JSONObject objson2 = new JSONObject(result);
            JSONArray jsonArray2 = objson2.getJSONArray("msg_responce");

            int count = 0;
            Integer frmOne = null, frmTwo = null, frmThree = null, frmFour = null;
            for (count = 0; count < jsonArray2.length(); count++) {
                JSONObject obj = jsonArray2.getJSONObject(count);

                frmOne = obj.getInt("formOne");
                frmTwo = obj.getInt("formTwo");
                frmThree = obj.getInt("formThree");
                frmFour = obj.getInt("formFour");
                if(frmOne==4){
                    imgstatus1.setBackgroundResource(R.drawable.ic_baseline_check_circle_green);
                } else if(frmOne!=4){
                    imgstatus1.setBackgroundResource(R.drawable.ic_baseline_error_amber);
                }
                if(frmTwo==4){
                    imgstatus2.setBackgroundResource(R.drawable.ic_baseline_check_circle_green);
                } else if(frmTwo!=4){
                    imgstatus2.setBackgroundResource(R.drawable.ic_baseline_error_amber);
                }
                if(frmThree==4){
                    imgstatus3.setBackgroundResource(R.drawable.ic_baseline_check_circle_green);
                } else if(frmThree!=4){
                    imgstatus3.setBackgroundResource(R.drawable.ic_baseline_error_amber);
                }
                if(frmFour==4){
                    imgstatus4.setBackgroundResource(R.drawable.ic_baseline_check_circle_green);
                } else if(frmFour!=4){
                    imgstatus4.setBackgroundResource(R.drawable.ic_baseline_error_amber);
                }

                totalForm=frmOne+frmTwo+frmThree+frmFour;
                 Toast.makeText(getApplicationContext(),"Total Count = "+totalForm, Toast.LENGTH_SHORT).show();
            }
            //spinner_fn();

            //Toast.makeText(getApplicationContext(), rcnum+"-"+insunum+" - " +puc+"="+totalSelfDoc, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            Log.e("Fail 3", e.toString());
            //  Toast.makeText(getApplicationContext(), "Fail 3" + e.toString(), Toast.LENGTH_LONG).show();

        }


    }
    @Override
    protected void onStart() {
        //Internet check start------
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkCheckerListener, filter);
        //Internet check end--------
        checkDriverRegistration();
        super.onStart();
    }
    }
