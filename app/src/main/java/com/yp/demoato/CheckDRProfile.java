package com.yp.demoato;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    HttpURLConnection urlConnection = null;
    InputStream is=null;
    String result=null;
    String line=null;
    public static final String SID = "SID";
    String sidno,sf_sid,mobnum = null;
    String getlangauge;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drprofilecheck);
        String getMobileNumber = getIntent().getStringExtra("authcode");
        getlangauge = getIntent().getStringExtra("langcode");

        sharedpreferences = getSharedPreferences(LangBar.SHARED_PREFS, Context.MODE_PRIVATE);
        sf_sid = sharedpreferences.getString(SID, null);
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
            //Toast.makeText(getApplicationContext(), "Fail 1"+e.toString(), Toast.LENGTH_LONG).show();
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
           // Toast.makeText(getApplicationContext(), "Fail 2"+e.toString(), Toast.LENGTH_LONG).show();
        }

        try {
            JSONObject objson = new JSONObject(result);
            JSONArray jsonArray = objson.getJSONArray("msg_responce");
          //  Toast.makeText(getApplicationContext(), "check1" + jsonArray.length(), Toast.LENGTH_SHORT).show();
            int count = 0;
            int form1 = 0, form2 = 0, form3 = 0, form4 = 0, flagForm = 0;

            String totalForm = null;
            if (jsonArray.length() == 0) {
                Intent myIntent = new Intent(getBaseContext(), DRForm1.class);
                myIntent.putExtra("authcode", getMobileNumber);
                myIntent.putExtra("langcode", getlangauge);
                startActivity(myIntent);
            } else {
                //Toast.makeText(getApplicationContext(), "User Registration" + getMobileNumber, Toast.LENGTH_LONG).show();
                //If driver not registered then
                //Intent myIntent = new Intent(getBaseContext(),   DRForm1.class);
                // myIntent.putExtra("authcode",getMobileNumber);
                // myIntent.putExtra("langcode",getlangauge);
                // startActivity(myIntent);

            for (count = 0; count < jsonArray.length(); count++) {

                JSONObject obj = jsonArray.getJSONObject(count);
                mobnum = obj.getString("mob");
                sidno = obj.getString("sid");
                form1 = obj.getInt("formOne");
                form2 = obj.getInt("formTwo");
                form3 = obj.getInt("formThree");
                form4 = obj.getInt("formFour");
                flagForm = obj.getInt("flag");
                // Toast.makeText(getApplicationContext(), "check1" + mobnum, Toast.LENGTH_SHORT).show();
                totalForm = form1 + "" + form2 + "" + form3 + "" + form4;
                Toast.makeText(getApplicationContext(), "First " + totalForm, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(SID, sidno);
                // to save our data with key and value.
                editor.apply();
            }






            switch (totalForm) {

                case "4000":
                case "4040":
                case "4004":
                case "4044":
                    // Toast.makeText(getApplicationContext(), "Form 2 again Active- " + totalForm, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), "Form 2 Active- " + totalForm, Toast.LENGTH_SHORT).show();
                    Intent my2Intent = new Intent(getBaseContext(),   DRForm2.class);
                    my2Intent.putExtra("authcode",getMobileNumber);
                    my2Intent.putExtra("langcode",getlangauge);
                    startActivity(my2Intent);
                    break;
                case "4400":
                case "4404":
                   // Toast.makeText(getApplicationContext(), "Form 3 Active- " + totalForm, Toast.LENGTH_SHORT).show();
                    Intent my3Intent = new Intent(getBaseContext(),   DRForm3.class);
                    my3Intent.putExtra("authcode",getMobileNumber);
                    my3Intent.putExtra("langcode",getlangauge);
                    startActivity(my3Intent);
                    break;
                case "4440":
                    //Toast.makeText(getApplicationContext(), "Form 4 Active- " + totalForm, Toast.LENGTH_SHORT).show();
                    Intent my4Intent = new Intent(getBaseContext(),   DRForm4.class);
                    my4Intent.putExtra("authcode",getMobileNumber);
                    my4Intent.putExtra("langcode",getlangauge);
                    startActivity(my4Intent);
                    break;
                case "4444":
                    //Toast.makeText(getApplicationContext(), "Login " + totalForm, Toast.LENGTH_SHORT).show();
                    Intent my5Intent = new Intent(getBaseContext(),   Pending.class);
                    my5Intent.putExtra("authcode",getMobileNumber);
                    my5Intent.putExtra("langcode",getlangauge);
                    startActivity(my5Intent);
                    break;
                case "5555":
                    //Toast.makeText(getApplicationContext(), "Login " + totalForm, Toast.LENGTH_SHORT).show();
                    Intent my6Intent = new Intent(getBaseContext(),   DRLogin.class);
                    my6Intent.putExtra("authcode",getMobileNumber);
                    my6Intent.putExtra("langcode",getlangauge);
                    startActivity(my6Intent);
                    break;
            }
        }



            //spinner_fn();

        }
        catch(Exception e)
        {

            Log.e("Fail 3", e.toString());
           // Toast.makeText(getApplicationContext(), "Fail 3"+e.toString(), Toast.LENGTH_LONG).show();

        }
    }


}
