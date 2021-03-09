package com.yp.demoato;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DRForm1 extends AppCompatActivity {
    HttpURLConnection urlConnection = null;
    InputStream is=null;
    String result=null;
    String line=null;
    Context context;
    Resources resources;
    TextView txtreg,txtmob,txtgtmob,txtfname,txtlname,txtemail,txtgender,txtaddress,txtpincode,txtcity,txtalmob,txtstepfirst;
    Button btstep1;
    EditText etfname,etlname,etemail,etgender,etaddress,etcity,etpincode,etalmob;
    String getlangauge, getMobileNumber,getsid;
    public static final String URL_SAVE_DRIVERPROFILE = "http://feeds.expressindia.com/test/saveDriverInfo.php";
    //database helper object
    private DBHelper db;

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String Lancode,Mobileno,Sid;
    // key for storing password.
    public static final String SID = "SID";
    //Internet check start
    NetworkCheckerListener networkCheckerListener = new NetworkCheckerListener();
    //Internet check end
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_user);
        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(LangBar.SHARED_PREFS, Context.MODE_PRIVATE);
        // getting data from shared prefs and
        getlangauge = sharedpreferences.getString(LangBar.LANCODE, null);
        getMobileNumber = sharedpreferences.getString(LangBar.MOBILENO, null);

        Sid = sharedpreferences.getString(Sid, null);

        //End shareprefernec
        btstep1 =(Button) findViewById(R.id.btstep1);
        txtreg = (TextView) findViewById(R.id.txtregistration);
        txtmob = (TextView) findViewById(R.id.txtmobile);
        txtfname = (TextView) findViewById(R.id.editfname);
        txtlname = (TextView) findViewById(R.id.editlname);
        txtemail = (TextView) findViewById(R.id.editemail);
        txtgender = (TextView) findViewById(R.id.editgender);
        txtaddress = (TextView) findViewById(R.id.editaddress);
        txtpincode = (TextView) findViewById(R.id.editpincode);
        txtcity = (TextView) findViewById(R.id.editcity);
        txtalmob = (TextView) findViewById(R.id.editaltmob);
        txtstepfirst = (TextView) findViewById(R.id.btstep1);
        txtgtmob =(TextView) findViewById(R.id.editmobile);

        etfname = (EditText) findViewById(R.id.editfname);
        etlname = (EditText) findViewById(R.id.editlname);
        etemail = (EditText) findViewById(R.id.editemail);
        etgender = (EditText) findViewById(R.id.editgender);
        etaddress = (EditText) findViewById(R.id.editaddress);
        etpincode = (EditText) findViewById(R.id.editpincode);
        etcity = (EditText) findViewById(R.id.editcity);
        etalmob = (EditText) findViewById(R.id.editaltmob);

        txtgtmob.setText(getMobileNumber);
        //Below code for session set as per language code   START
        if (SessionManager.getLanguage(getApplicationContext()).length() != 0)
            context = SessionManager.setLocale(DRForm1.this , SessionManager.getLanguage(getApplicationContext()));
        else
            context = SessionManager.setLocale(DRForm1.this , "en");
        resources = context.getResources();

        txtreg.setText(resources.getString(R.string.registration));
        txtmob.setText(resources.getString(R.string.mobile));
        txtfname.setHint(resources.getString(R.string.fname));
        txtlname.setHint(resources.getString(R.string.lname));
        txtemail.setHint(resources.getString(R.string.emailid));
        txtgender.setHint(resources.getString(R.string.gender));
        txtaddress.setHint(resources.getString(R.string.address));
        txtcity.setHint(resources.getString(R.string.city));
        txtpincode.setHint(resources.getString(R.string.pincode));
        txtalmob.setHint(resources.getString(R.string.alternate_mobile));
        txtstepfirst.setHint(resources.getString(R.string.STEP_FIRST));
        btstep1.setText(resources.getString(R.string.STEP_FIRST));
        //Below code for session set as per language code   END
        //Check Whether Driver already register or not-

}

private void checkDriverRegistration(){
    //Check Whether Driver already register or not-------------------------------START------------------------------------------------------
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
    try {

        URL url = new URL("http://feeds.expressindia.com/test/syncDriverInfo.php?id="+getMobileNumber+"&&lnc="+getlangauge);
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
            Intent myIntent = new Intent(getBaseContext(),   DRForm2.class);
            myIntent.putExtra("authcode","Registered");
            startActivity(myIntent);
        } else {
            //Toast.makeText(getApplicationContext(), "User Registration" + getMobileNumber, Toast.LENGTH_LONG).show();
            //If driver not registered then

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


//Check Whether Driver already register or not-------------------------------END------------------------------------------------------

}
    @Override
    protected void onStart() {
        //Internet check start------
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkCheckerListener, filter);
        //Internet check end--------
        //check activity load whether driver basic info exist or not
        checkDriverRegistration();
        super.onStart();
    }

    @Override
    protected void onStop() {
        //Internet check start------
        unregisterReceiver(networkCheckerListener);
        //Internet check end--------
        super.onStop();
    }


    public void saveDriverRegistration(View view) {
        saveDriverInfo();
    }

    private void saveDriverInfo(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting Record .Please wait...");
        progressDialog.show();

        final String fname = etfname.getText().toString().trim();
        final String lname = etlname.getText().toString().trim();
        final String altmobile = etalmob.getText().toString().trim();
        final String gender = etgender.getText().toString().trim();
        final String email = etemail.getText().toString().trim();
        final String addr = etaddress.getText().toString().trim();
        final String city = etcity.getText().toString().trim();
        final String pincode = etpincode.getText().toString().trim();
        final String mobile = txtgtmob.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_DRIVERPROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                getsid = obj.getString("sidno");
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(SID, getsid);
                                // to save our data with key and value.
                                editor.apply();
                                //End SharePreference
                                Toast.makeText(DRForm1.this,"Record Updated Successfully", Toast.LENGTH_SHORT).show();
                                Intent myIntent = new Intent(getBaseContext(),   DRForm2.class);
                                myIntent.putExtra("authcode","Registered");
                                startActivity(myIntent);
                            } else {
                                Toast.makeText(DRForm1.this,"Please Try Again Later!", Toast.LENGTH_SHORT).show();
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
                        //on error storing the name to sqlite with status unsynced
                        //saveNameToLocalStorage(obj.getString("username"),obj.getString("age"),obj.getDouble("latitude"),obj.getDouble("longitude"), NAME_NOT_SYNCED_WITH_SERVER);
                        Toast.makeText(DRForm1.this,"Failed To Connect.Please check INTERNET CONNECTION", Toast.LENGTH_LONG).show();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lname", lname);
                params.put("fname", fname);
                params.put("mob", mobile);
                params.put("gender", gender);
                params.put("email", email);
                params.put("addr", addr);
                params.put("city", city);
                params.put("pincode", pincode);
                params.put("altmobile", altmobile);
                params.put("lang", getlangauge);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
