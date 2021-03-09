package com.yp.demoato;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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

public class CheckDRProfile extends AppCompatActivity {
    HttpURLConnection urlConnection = null;
    InputStream is=null;
    String result=null;
    String line=null;
    String getlangauge;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drprofilecheck);
        String getMobileNumber = getIntent().getStringExtra("authcode");
        getlangauge = getIntent().getStringExtra("langcode");
        //Below code for session set as per language code   START
        if (SessionManager.getLanguage(getApplicationContext()).length() != 0)
            context = SessionManager.setLocale(CheckDRProfile.this , SessionManager.getLanguage(getApplicationContext()));
        else
            context = SessionManager.setLocale(CheckDRProfile.this , "en");
        resources = context.getResources();
        //Below code for session set as per language code   End
        //strlangauge.setText(resources.getString(R.string.langcode));
       // Toast.makeText(getApplicationContext(), ""+getlangauge, Toast.LENGTH_LONG).show();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {

            URL url = new URL("http://feeds.expressindia.com/test/checkDriverFormFullStatus.php?mob="+getMobileNumber);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            is = urlConnection.getInputStream();
        }
        catch (Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Fail 1"+e.toString(), Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
            Toast.makeText(getApplicationContext(), "Fail 2"+e.toString(), Toast.LENGTH_LONG).show();
        }

        try
        {
            JSONObject objson = new JSONObject(result);
            JSONArray jsonArray = objson.getJSONArray("msg_responce");
            int count = 0;

            if(jsonArray.length() == 1){
                //Toast.makeText(getApplicationContext(), "Main Profile" + getMobileNumber, Toast.LENGTH_LONG).show();
                //If Driver already register then
                Intent myIntent = new Intent(getBaseContext(),   MainActivity.class);
                myIntent.putExtra("authcode","Registered");
                startActivity(myIntent);
            } else {
                //Toast.makeText(getApplicationContext(), "User Registration" + getMobileNumber, Toast.LENGTH_LONG).show();
                //If driver not registered then
                Intent myIntent = new Intent(getBaseContext(),   DRForm1.class);
                myIntent.putExtra("authcode",getMobileNumber);
                myIntent.putExtra("langcode",getlangauge);
                startActivity(myIntent);
            }
            for(count=0;count<jsonArray.length();count++)
            {
                //JSONObject obj = jsonArray.getJSONObject(count);
                //struid=obj.getString("uid");
            }
            //spinner_fn();

        }
        catch(Exception e)
        {

            Log.e("Fail 3", e.toString());
            Toast.makeText(getApplicationContext(), "Fail 3"+e.toString(), Toast.LENGTH_LONG).show();

        }
    }


}
