package com.yp.demoato;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class DRLogin extends AppCompatActivity {
    public static final String URL_DRIVER_LOGIN = "http://feeds.expressindia.com/test/loginDriver.php";
    // variable for shared preferences.
    NetworkCheckerListener networkCheckerListener = new NetworkCheckerListener();
    SharedPreferences sharedpreferences;
    HttpURLConnection urlConnection = null;
    private EditText etMobile,etPwd;
    private Button btLogin;
    private String mobile,cod,sid;
    InputStream is = null;
    String result = null;
    String line = null;
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        etMobile= (EditText) findViewById(R.id.etMobile);
        etPwd= (EditText) findViewById(R.id.etPwd);

        btLogin = (Button) findViewById(R.id.btlogin);

        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(LangBar.SHARED_PREFS, Context.MODE_PRIVATE);
        // getting data from shared prefs and
        mobile = sharedpreferences.getString(LangBar.MOBILENO, null);
        cod = sharedpreferences.getString(LangBar.LANCODE, null);
        sid = sharedpreferences.getString(DRForm1.SID, null);

    }
    public void loginDriver(View view) {
        checkLoginStatus();
    }

  private void checkLoginStatus(){
      final ProgressDialog progressDialog = new ProgressDialog(this);
      progressDialog.setMessage("Authenticating .Please wait...");
      progressDialog.show();


      final String etmob = etMobile.getText().toString().trim();
      final String etpwd = etPwd.getText().toString().trim();


      StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DRIVER_LOGIN,
              new Response.Listener<String>() {
                  @Override
                  public void onResponse(String response) {
                      progressDialog.dismiss();
                      Integer frm4Status;
                      try {
                          JSONObject objsp = new JSONObject(response);

                                 if (!objsp.getBoolean("error")) {
                                  //if record update successfully
                                  Toast.makeText(DRLogin.this, ""+objsp.getString("message"), Toast.LENGTH_SHORT).show();
                                     Intent myIntent = new Intent(getBaseContext(),   DRMap.class);
                                     startActivity(myIntent);

                              } else {
                                  //if record update failed
                                  Toast.makeText(DRLogin.this, "Incorrect Detail", Toast.LENGTH_SHORT).show();



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
                      Toast.makeText(DRLogin.this,"Failed To Connect.Please check INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                  }
              })
      {

          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
              Map<String, String> params = new HashMap<>();
              params.put("sidchk1", sid);
              params.put("mobchk1", etmob);
              params.put("pwdchk1", etpwd);
              return params;
          }
      };

      VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

  }
}
