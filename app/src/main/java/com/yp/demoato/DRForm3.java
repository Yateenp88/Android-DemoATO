package com.yp.demoato;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DRForm3 extends AppCompatActivity implements View.OnClickListener{
    private int PICK_IMAGE_REQUEST_RC = 1;
    private int PICK_IMAGE_REQUEST_Ins = 2;
    private int PICK_IMAGE_REQUEST_PUC = 3;
    public static final String UPLOAD_URL_VC = "http://feeds.expressindia.com/test/uploadDriverVehicle.php";
    public static final String URL_SAVE_DRIVERVEHICLE = "http://feeds.expressindia.com/test/uploadDriverVehicleInfo.php";
    public static final String UPLOAD_KEY = "image";
    public static final String UPLOAD_KEY1 = "nam";
    public static final String UPLOAD_KEY2 = "typ";
    public static final String UPLOAD_KEY3 = "mob";
    public static final String UPLOAD_KEY4 = "sid";
    private Bitmap bitmap, bitmap1, bitmap2;
    private Uri filePath, filePath1, filePath2, filePath3;
    // variable for shared preferences.
    NetworkCheckerListener networkCheckerListener = new NetworkCheckerListener();
    SharedPreferences sharedpreferences;
    HttpURLConnection urlConnection = null;
    InputStream is = null;
    String result = null;
    String line = null;
    private String mobile,sid,cod,getrcnumber,getinsurance,getpuc,getmake,getmodel,gettype,getstand;
    private TextView txtregistration,rcphoto,insphoto,pucphoto,txtmobile;
    private EditText etvreg,etins,etpuc;
    private Button btvreg,btins,btpuc,btstep3;
    private ImageView imgvreg,imgins,imgpuc,imgvregst,imginsst,imgpucst;
    private Integer totalSelfDoc = null;
    private Integer formstatus = null;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Spinner sp_make,sp_model,sp_type,sp_stand;
    ArrayList<String> arrayList_make,arrayList_type,arrayList_stand;
    ArrayAdapter<String> arrayAdapter_make,arrayAdapter_type,arrayAdapter_stand;
    ArrayList<String> arrayList_1,arrayList_2,arrayList_3,arrayList_4,arrayList_5;
    ArrayAdapter<String> arrayAdapter_model;
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_form3);


        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(LangBar.SHARED_PREFS, Context.MODE_PRIVATE);
        // getting data from shared prefs and
        mobile = sharedpreferences.getString(LangBar.MOBILENO, null);
        cod = sharedpreferences.getString(LangBar.LANCODE, null);
        sid = sharedpreferences.getString(DRForm1.SID, null);

        txtregistration= (TextView) findViewById(R.id.txtregistration);
        txtmobile= (TextView) findViewById(R.id.txtmobile);
        rcphoto= (TextView) findViewById(R.id.rcphoto);
        insphoto= (TextView) findViewById(R.id.insphoto);
        pucphoto= (TextView) findViewById(R.id.pucphoto);

        etvreg= (EditText) findViewById(R.id.etvreg);
        etins= (EditText) findViewById(R.id.etins);
        etpuc= (EditText) findViewById(R.id.etpuc);

        btstep3 = (Button) findViewById(R.id.btstep3);
        btvreg = (Button) findViewById(R.id.btvreg);
        btins = (Button) findViewById(R.id.btins);
        btpuc = (Button) findViewById(R.id.btpuc);

        imgvreg = (ImageView) findViewById(R.id.imgvreg);
        imgins = (ImageView) findViewById(R.id.imgins);
        imgpuc = (ImageView) findViewById(R.id.imgpuc);

        imgvregst = (ImageView) findViewById(R.id.imgvregst);
        imginsst = (ImageView) findViewById(R.id.imginsst);
        imgpucst = (ImageView) findViewById(R.id.imgpucst);

        sp_make = (Spinner) findViewById(R.id.spmake);
        sp_model = (Spinner) findViewById(R.id.spmodel);
        sp_type = (Spinner) findViewById(R.id.sptype);
        sp_stand = (Spinner) findViewById(R.id.spstand);

        //Below code for session set as per language code   START
        if (SessionManager.getLanguage(getApplicationContext()).length() != 0)
            context = SessionManager.setLocale(DRForm3.this, SessionManager.getLanguage(getApplicationContext()));
        else
            context = SessionManager.setLocale(DRForm3.this, "en");



        resources = context.getResources();
        etvreg.setHint(resources.getString(R.string.vrno));
        etins.setHint(resources.getString(R.string.insdt));
        etpuc.setHint(resources.getString(R.string.pucdt));
        rcphoto.setText(resources.getString(R.string.rcphoto));
        insphoto.setText(resources.getString(R.string.inphoto));
        pucphoto.setText(resources.getString(R.string.puphoto));
        txtregistration.setText(resources.getString(R.string.vregistration));
        btvreg.setText(resources.getString(R.string.upload));
        btins.setText(resources.getString(R.string.upload));
        btpuc.setText(resources.getString(R.string.upload));
        btstep3.setText(resources.getString(R.string.submit));
        etins.setOnClickListener(this);
        etpuc.setOnClickListener(this);
        imgvreg.setOnClickListener(this);
        imgins.setOnClickListener(this);
        imgpuc.setOnClickListener(this);
        btvreg.setOnClickListener(this);
        btins.setOnClickListener(this);
        btpuc.setOnClickListener(this);
        //===============================================================================================================
        arrayList_stand = new ArrayList<>();
        arrayList_stand.add(resources.getString(R.string.stand));
        arrayList_stand.add(resources.getString(R.string.stand1));
        arrayList_stand.add(resources.getString(R.string.stand2));
        arrayList_stand.add(resources.getString(R.string.stand3));


        arrayAdapter_stand = new ArrayAdapter<>(getApplication(),android.R.layout.simple_spinner_item,arrayList_stand);
        sp_stand.setAdapter(arrayAdapter_stand);
        //=========================================================
            arrayList_type = new ArrayList<>();
            arrayList_type.add(resources.getString(R.string.vt));
            arrayList_type.add(resources.getString(R.string.cng));
            arrayList_type.add(resources.getString(R.string.disel));
            arrayList_type.add(resources.getString(R.string.elec));
            arrayList_type.add(resources.getString(R.string.petrol));

        arrayAdapter_type = new ArrayAdapter<>(getApplication(),android.R.layout.simple_spinner_item,arrayList_type);
        sp_type.setAdapter(arrayAdapter_type);
        //==========================================================
        arrayList_make = new ArrayList<>();
        arrayList_make.add(resources.getString(R.string.vmake));
        arrayList_make.add("Bajaj");
        arrayList_make.add("TVS");
        arrayList_make.add("Mahindra");
        arrayList_make.add("Ape");
        arrayList_make.add("Piaggio");
        arrayAdapter_make = new ArrayAdapter<>(getApplication(),android.R.layout.simple_spinner_item,arrayList_make);

        sp_make.setAdapter(arrayAdapter_make);
        //============== child spinner process starts ==================
        arrayList_1 = new ArrayList<>();
        arrayList_1.add(resources.getString(R.string.vmodel));
        arrayList_1.add("Maxima X-wide");
        arrayList_1.add("RE Compact");


        arrayList_2 = new ArrayList<>();
        arrayList_2.add(resources.getString(R.string.vmodel));
        arrayList_2.add("King Auto");
        arrayList_2.add("King Duramax");


        arrayList_3 = new ArrayList<>();
        arrayList_3.add(resources.getString(R.string.vmodel));
        arrayList_3.add("Gio");
        arrayList_3.add("Alfa");
        arrayList_3.add("Treo Yaari");
        arrayList_3.add("Alfa DX");

        arrayList_4 = new ArrayList<>();
        arrayList_4.add(resources.getString(R.string.vmodel));
        arrayList_4.add("Passenger");
        arrayList_4.add("City");

        arrayList_5 = new ArrayList<>();
        arrayList_5.add(resources.getString(R.string.vmodel));
        arrayList_5.add("Ape");
        arrayList_5.add("Ape City Plus");
        arrayList_5.add("E City");
        //==============
        sp_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    arrayAdapter_model=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList_1);
                }
                if(position==1){
                    arrayAdapter_model=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList_2);
                }
                if(position==2){
                    arrayAdapter_model=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList_3);
                }
                if(position==3){
                    arrayAdapter_model=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList_4);
                }
                if(position==4){
                    arrayAdapter_model=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList_5);
                }
                sp_model.setAdapter(arrayAdapter_model);
                getmake= arrayList_make.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                 getmodel = parent.getItemAtPosition(position).toString();
                //Toast.makeText(DRForm3.this,"Selected Country : " + getmake, Toast.LENGTH_SHORT).show();
                // Showing selected spinner item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }



    public void onClick(View v) {
        if (v == imgvreg) {
            showFileChooserRC();
        }
        if (v == imgins) {
            showFileChooserIns();
        }
        if (v == imgpuc) {
            showFileChooserPUC();
        }

        if (v == btvreg) {
            uploadImageRC();
        }
        if (v == btins) {
            uploadImageIN();
        }
        if (v == btpuc) {
            uploadImagePC();
        }

        if (v == etins) {
            getDate();
        }

        if (v == etpuc) {
            getDate2();
        }
    }
    private void getDate(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        etins.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }
    private void getDate2(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        etpuc.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }
    private void showFileChooserRC(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_RC);
    }
    private void showFileChooserIns(){
        Intent intent2 = new Intent();
        intent2.setType("image/*");
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent2, "Select Picture"), PICK_IMAGE_REQUEST_Ins);
    }
    private void showFileChooserPUC(){
        Intent intent3 = new Intent();
        intent3.setType("image/*");
        intent3.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent3, "Select Picture"), PICK_IMAGE_REQUEST_PUC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST_RC && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgvreg.setBackgroundResource(0);
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgvreg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST_Ins && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgins.setBackgroundResource(0);
            filePath1 = data.getData();
            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                imgins.setImageBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST_PUC && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgpuc.setBackgroundResource(0);
            filePath2 = data.getData();
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                imgpuc.setImageBitmap(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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


    @Override
    protected void onStop() {
        //Internet check start------
        unregisterReceiver(networkCheckerListener);
        //Internet check end--------
        super.onStop();
    }
    private void uploadImageRC() {
        getrcnumber = etvreg.getText().toString().trim();
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DRForm3.this, "Uploading ...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage1 = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY3, mobile);
                data.put(UPLOAD_KEY4, sid);
                data.put(UPLOAD_KEY2, "RCN");
                data.put(UPLOAD_KEY1, getrcnumber);
                data.put(UPLOAD_KEY, uploadImage1);
                String result = rh.sendPostRequest(UPLOAD_URL_VC, data);

                return result;
            }
        }
        imgvregst.setBackgroundResource(R.drawable.ic_baseline_cloud_upload_24);
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    private void uploadImageIN() {
        getinsurance = etins.getText().toString().trim();
        class UploadImage2 extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DRForm3.this, "Uploading ...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap2 = params[0];
                String uploadImage2 = getStringImage(bitmap2);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY3, mobile);
                data.put(UPLOAD_KEY4, sid);
                data.put(UPLOAD_KEY2, "INS");
                data.put(UPLOAD_KEY1, getinsurance);
                data.put(UPLOAD_KEY, uploadImage2);
                String result = rh.sendPostRequest(UPLOAD_URL_VC, data);

                return result;
            }
        }
        imginsst.setBackgroundResource(R.drawable.ic_baseline_cloud_upload_24);
        UploadImage2 ui = new UploadImage2();
        ui.execute(bitmap1);
    }
    private void uploadImagePC() {
        getpuc = etpuc.getText().toString().trim();
        class UploadImage3 extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DRForm3.this, "Uploading ...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap3 = params[0];
                String uploadImage3 = getStringImage(bitmap3);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY3, mobile);
                data.put(UPLOAD_KEY4, sid);
                data.put(UPLOAD_KEY2, "PUC");
                data.put(UPLOAD_KEY1, getpuc);
                data.put(UPLOAD_KEY, uploadImage3);
                String result = rh.sendPostRequest(UPLOAD_URL_VC, data);

                return result;
            }
        }
        imginsst.setBackgroundResource(R.drawable.ic_baseline_cloud_upload_24);
        UploadImage3 ui = new UploadImage3();
        ui.execute(bitmap2);
    }


    private void checkDriverRegistration() {
        //Check Whether Driver already register or not-------------------------------START------------------------------------------------------
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {

            URL url = new URL("http://feeds.expressindia.com/test/checkDriverVehic.php?mo=" + mobile + "&&si=" + sid);
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
            Integer rcnum = null, insunum = null, puc = null;
            for (count = 0; count < jsonArray2.length(); count++) {
                JSONObject obj = jsonArray2.getJSONObject(count);
                if (!obj.isNull("rcn")) {
                    rcnum = obj.getInt("rcn");
                } else {
                    rcnum = 4;
                }
                if (!obj.isNull("ins")) {
                    insunum = obj.getInt("ins");
                } else {
                    insunum = 4;
                }

                if (!obj.isNull("puc")) {
                    puc = obj.getInt("puc");
                } else {
                    puc = 4;
                }



                if (rcnum == 2) {
                    imgvregst.setBackgroundResource(R.drawable.ic_baseline_verified_user_24);
                } else if (rcnum == 3) {
                    imgvregst.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
                } else if (rcnum == 1) {
                    imgvregst.setBackgroundResource(R.drawable.ic_baseline_cloud_done_24);
                }

                if (insunum == 2) {
                    imginsst.setBackgroundResource(R.drawable.ic_baseline_verified_user_24);
                } else if (insunum == 3) {
                    imginsst.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
                } else if (insunum == 1) {
                    imginsst.setBackgroundResource(R.drawable.ic_baseline_cloud_done_24);
                }

                if (puc == 2) {
                    imgpucst.setBackgroundResource(R.drawable.ic_baseline_verified_user_24);
                } else if (puc == 3) {
                    imgpucst.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
                } else if (puc == 1) {
                    imgpucst.setBackgroundResource(R.drawable.ic_baseline_cloud_done_24);
                }

                // Toast.makeText(getApplicationContext(), mobile+" & "+sid+" Data " + jsonArray, Toast.LENGTH_LONG).show();
            }
            //spinner_fn();
            totalSelfDoc = rcnum + insunum + puc;
            Toast.makeText(getApplicationContext(), rcnum+"-"+insunum+" - " +puc+"="+totalSelfDoc, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            Log.e("Fail 3", e.toString());
          //  Toast.makeText(getApplicationContext(), "Fail 3" + e.toString(), Toast.LENGTH_LONG).show();

        }


    }

    public void saveDriverVehicle(View v){

        //  Toast.makeText(getApplicationContext(), "Total Uploaded Count : "+totalSelfDoc, Toast.LENGTH_SHORT).show();
        if (totalSelfDoc == 6) {
            formstatus = 4;
        } else if (totalSelfDoc == 3) {
            formstatus = 4;
        } else {
            formstatus = 0;
        }
       Toast.makeText(getApplicationContext(), "Form Status"+totalSelfDoc +": "+formstatus, Toast.LENGTH_SHORT).show();
        saveFormThree();
    }

    private void saveFormThree() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting Record .Please wait...");
        progressDialog.show();


       final String etrcnum = etvreg.getText().toString().trim();
       final String etinsurence = etins.getText().toString().trim();
       final String etpucnum = etpuc.getText().toString().trim();
       final String etvhmake = sp_make.getSelectedItem().toString();
       final String etvhstand = sp_stand.getSelectedItem().toString();
        final String etvhmodel = sp_model.getSelectedItem().toString();
        final String etvhtype = sp_type.getSelectedItem().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_DRIVERVEHICLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Integer frm4Status;
                        try {
                            JSONObject objsp = new JSONObject(response);
                            JSONArray jsonArray2 = objsp.getJSONArray("msg_responce");
                           // Toast.makeText(DRForm3.this, "Check1"+jsonArray2, Toast.LENGTH_SHORT).show();

                            Integer countsp = 0;
                            for (countsp = 0; countsp < jsonArray2.length(); countsp++) {
                                JSONObject obj = jsonArray2.getJSONObject(countsp);
                                frm4Status = obj.getInt("form4status");
                                Toast.makeText(DRForm3.this, "Check2 ="+frm4Status, Toast.LENGTH_SHORT).show();

                                if (!obj.getBoolean("error")) {
                                    //if record update successfully
                                    if (frm4Status == 1) {
                                        Toast.makeText(DRForm3.this, "Record Updated Successfully", Toast.LENGTH_SHORT).show();
                                        Intent frm4Intent = new Intent(getBaseContext(), Pending.class);
                                        startActivity(frm4Intent);
                                    } else if (frm4Status != 1) {
                                        Toast.makeText(DRForm3.this, "Record Updated Successfully", Toast.LENGTH_SHORT).show();
                                        Intent frm4Intent = new Intent(getBaseContext(), DRForm4.class);
                                        startActivity(frm4Intent);
                                    }
                                } else {
                                    //if record update failed
                                    Toast.makeText(DRForm3.this, "Please Try Again Later!", Toast.LENGTH_SHORT).show();
                                 }

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
                        Toast.makeText(DRForm3.this,"Failed To Connect.Please check INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("vsid", sid);
                params.put("vmob", mobile);
                params.put("stand", etvhstand);
                params.put("vmake", etvhmake);
                params.put("vmodel", etvhmodel);
                params.put("vtype", etvhtype);
                params.put("vrcnum", etrcnum);
                params.put("vinsu", etinsurence);
                params.put("vpuc", etpucnum);
                params.put("form3st", String.valueOf(formstatus));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}
