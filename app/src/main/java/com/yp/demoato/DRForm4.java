package com.yp.demoato;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class DRForm4 extends AppCompatActivity  implements View.OnClickListener{
    private ImageView imgpb,imgpbst;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    public static final String UPLOAD_KEY = "image";
    public static final String UPLOAD_KEY1 = "nam";
    public static final String UPLOAD_KEY2 = "typ";
    public static final String UPLOAD_KEY3 = "mob";
    public static final String UPLOAD_KEY4 = "sid";
    private Uri filePath;
    public static final String URL_SAVE_DRIVERBANKING = "http://feeds.expressindia.com/test/saveDriverBanking.php";
    public static final String UPLOAD_URL_VC = "http://feeds.expressindia.com/test/uploadDriverVehicle.php";
    HttpURLConnection urlConnection = null;
    InputStream is=null;
    String result=null;
    String line=null;
    Context context;
    Resources resources;
    EditText etacnam,etacnum,etifsc,etbranch,etbknam;
    TextView txtreg,txtmob,txtgtmob,txtpassbook;
    String getlangauge, getMobileNumber,getsid;
    Button btfinal,btupload;
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
        setContentView(R.layout.activity_driver_form4);
        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(LangBar.SHARED_PREFS, Context.MODE_PRIVATE);
        // getting data from shared prefs and
        getlangauge = sharedpreferences.getString(LangBar.LANCODE, null);
        getMobileNumber = sharedpreferences.getString(LangBar.MOBILENO, null);
        getsid = sharedpreferences.getString(DRForm1.SID, null);
        imgpb = (ImageView) findViewById(R.id.imgpb);
        imgpbst = (ImageView) findViewById(R.id.imgpbst);
        btupload =(Button) findViewById(R.id.btbpupload);
        btfinal =(Button) findViewById(R.id.btfinal);
        txtreg = (TextView) findViewById(R.id.txtregistration1);
        txtmob = (TextView) findViewById(R.id.txtmobile);
        etacnam = (EditText) findViewById(R.id.etacnam);
        etacnum = (EditText) findViewById(R.id.etacnum);
        etifsc = (EditText) findViewById(R.id.etifsc);
        etbranch = (EditText) findViewById(R.id.etbranch);
        etbknam = (EditText) findViewById(R.id.etbknam);
        txtpassbook = (TextView) findViewById(R.id.txtpassbook);
        txtgtmob =(TextView) findViewById(R.id.editmobile);
        txtgtmob.setText(getMobileNumber);
        //
        if (SessionManager.getLanguage(getApplicationContext()).length() != 0)
            context = SessionManager.setLocale(DRForm4.this , SessionManager.getLanguage(getApplicationContext()));
        else
            context = SessionManager.setLocale(DRForm4.this , "en");
        resources = context.getResources();
        txtpassbook.setText(resources.getString(R.string.passbook));
        txtreg.setText(resources.getString(R.string.bankdetail));
        txtmob.setText(resources.getString(R.string.mobile));
        etacnam.setHint(resources.getString(R.string.bankaccname));
        etacnum.setHint(resources.getString(R.string.bankaccnum));
        etbranch.setHint(resources.getString(R.string.bankbranch));
        etifsc.setHint(resources.getString(R.string.bankifsc));
        etbknam.setHint(resources.getString(R.string.bankname));
        btfinal.setText(resources.getString(R.string.final1));
        imgpb.setOnClickListener(this);
        btupload.setOnClickListener(this);
    }


    public void saveDriverRegistration(View view) {
        saveDriverInfo();
    }

    private void saveDriverInfo(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting Record .Please wait...");
        progressDialog.show();

        final String bkaccname = etacnam.getText().toString().trim();
        final String bkaccnum = etacnum.getText().toString().trim();
        final String bkifsc = etifsc.getText().toString().trim();
        final String bkbranch = etbranch.getText().toString().trim();
        final String bkname = etbknam.getText().toString().trim();
        final String mobile = txtgtmob.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_DRIVERBANKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                Toast.makeText(DRForm4.this,"Record Updated Successfully", Toast.LENGTH_SHORT).show();
                                //Intent myIntent = new Intent(getBaseContext(),   DRForm2.class);
                                //myIntent.putExtra("authcode","Registered");
                                //startActivity(myIntent);
                            } else {
                                Toast.makeText(DRForm4.this,"Please Try Again Later!"+getsid, Toast.LENGTH_SHORT).show();
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
                          Toast.makeText(DRForm4.this,"Failed To Connect.Please check INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bkaccname", bkaccname);
                params.put("bkaccnum", bkaccnum);
                params.put("bkifsc", bkifsc);
                params.put("bkbranch", bkbranch);
                params.put("bkname", bkname);
                params.put("sidnum", getsid);
                params.put("mobnum", getMobileNumber);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onClick(View v) {

    }
}
