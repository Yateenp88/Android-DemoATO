package com.yp.demoato;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DRForm2 extends AppCompatActivity implements View.OnClickListener  {
    // variable for shared preferences.
    SharedPreferences sharedpreferences;

    HttpURLConnection urlConnection = null;
    InputStream is = null;
    String result = null;
    String line = null;
    public static final String UPLOAD_URL = "http://feeds.expressindia.com/test/uploadDriverDoc.php";
    public static final String UPLOAD_URL_PIC = "http://feeds.expressindia.com/test/uploadDriverSelfie.php";
    public static final String UPLOAD_KEY = "image";
    public static final String UPLOAD_KEY1 = "nam";
    public static final String UPLOAD_KEY2 = "typ";
    public static final String UPLOAD_KEY3 = "mob";
    public static final String UPLOAD_KEY4 = "sid";

    private Integer totalSelfDoc = null;
    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_IMAGE_REQUEST_PAN = 2;
    private int PICK_IMAGE_REQUEST_DRI = 3;
    private int PICK_IMAGE_REQUEST_PIC = 4;
    private Button btaad;
    private Button btselfdocumnet;
    private Button buttonView, btpanupload, btdriupload, btpic;
    private EditText etaadhar, etpan, etdri;
    private ImageView imageView, imageaadhst, imgpan, imgpanst, imgdri, imgdrist, imgpic, imgpicst;
    private String fname, getpan, getaadhar, getdri, getpic;
    private Bitmap bitmap, bitmap1, bitmap2, bitmap3, bitmap4;
    private String mobile, sid;
    private TextView txtmobile, txtregistration, txtaad, txtpan, txtlic, txtself;
    private Uri filePath, filePath1, filePath2, filePath3, getFilePath4;
    private Integer formstatus = null;

    //Internet check start
    NetworkCheckerListener networkCheckerListener = new NetworkCheckerListener();
    Context context;
    Resources resources;

    //Internet check end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_form2);

        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(LangBar.SHARED_PREFS, Context.MODE_PRIVATE);
        // getting data from shared prefs and
        mobile = sharedpreferences.getString(LangBar.MOBILENO, null);
        sid = sharedpreferences.getString(DRForm1.SID, null);

        btaad = (Button) findViewById(R.id.btaad);
        btselfdocumnet = (Button) findViewById(R.id.btselfdoc);
        //buttonView = (Button) findViewById(R.id.buttonViewImage);
        etaadhar = (EditText) findViewById(R.id.etaadhar);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageaadhst = (ImageView) findViewById(R.id.imgpucst);
        imgpic = (ImageView) findViewById(R.id.imgpic);
        imgpicst = (ImageView) findViewById(R.id.imgpicst);
        btpanupload = (Button) findViewById(R.id.btpan);
        imgpan = (ImageView) findViewById(R.id.imgpan);
        imgpanst = (ImageView) findViewById(R.id.imgpanst);
        etpan = (EditText) findViewById(R.id.etpan);
        btpic = (Button) findViewById(R.id.btpic);
        btdriupload = (Button) findViewById(R.id.btdri);
        imgdri = (ImageView) findViewById(R.id.imgdrv);
        imgdrist = (ImageView) findViewById(R.id.imgdrvst);
        etdri = (EditText) findViewById(R.id.etdrv);

        txtaad = (TextView) findViewById(R.id.txtaad);
        txtpan = (TextView) findViewById(R.id.txtpan);
        txtlic = (TextView) findViewById(R.id.txtlic);
        txtmobile = (TextView) findViewById(R.id.txtmobile);
        txtregistration = (TextView) findViewById(R.id.txtregistration);
        txtself = (TextView) findViewById(R.id.txtself);

        imageView.setOnClickListener(this);
        imgpan.setOnClickListener(this);
        imgdri.setOnClickListener(this);
        imgpic.setOnClickListener(this);

        btaad.setOnClickListener(this);
        btpanupload.setOnClickListener(this);
        btdriupload.setOnClickListener(this);
        btpic.setOnClickListener(this);

        imgpanst.setBackgroundResource(R.drawable.ic_baseline_cloud_upload_24);
        imgdrist.setBackgroundResource(R.drawable.ic_baseline_cloud_upload_24);
        imgpicst.setBackgroundResource(R.drawable.ic_baseline_cloud_upload_24);


        //Below code for session set as per language code   START
        if (SessionManager.getLanguage(getApplicationContext()).length() != 0)
            context = SessionManager.setLocale(DRForm2.this, SessionManager.getLanguage(getApplicationContext()));
        else
            context = SessionManager.setLocale(DRForm2.this, "en");
        resources = context.getResources();
        txtregistration.setText(resources.getString(R.string.registration));
        txtmobile.setText(resources.getString(R.string.uploaddoc));
        txtaad.setText(resources.getString(R.string.adphoto));
        txtpan.setText(resources.getString(R.string.pnphoto));
        txtlic.setText(resources.getString(R.string.drphoto));
        txtself.setText(resources.getString(R.string.selfphoto));
        etaadhar.setHint(resources.getString(R.string.adhrno));
        etpan.setHint(resources.getString(R.string.panno));
        etdri.setHint(resources.getString(R.string.drno));
        btaad.setText(resources.getString(R.string.upload));
        btpanupload.setText(resources.getString(R.string.upload));
        btdriupload.setText(resources.getString(R.string.upload));
        btpic.setText(resources.getString(R.string.upload));



    }



    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void showFileChooserPan() {
        Intent intent1 = new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent1, "Select Picture"), PICK_IMAGE_REQUEST_PAN);
    }

    private void showFileChooserDri() {
        Intent intent2 = new Intent();
        intent2.setType("image/*");
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent2, "Select Picture"), PICK_IMAGE_REQUEST_DRI);
    }

    private void showFileChooserPic() {
        Intent intent3 = new Intent();
        intent3.setType("image/*");
        intent3.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent3, "Select Picture"), PICK_IMAGE_REQUEST_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageView.setBackgroundResource(0);
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST_PAN && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgpan.setBackgroundResource(0);
            filePath1 = data.getData();
            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                imgpan.setImageBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST_DRI && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgdri.setBackgroundResource(0);
            filePath2 = data.getData();
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                imgdri.setImageBitmap(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST_PIC && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgpic.setBackgroundResource(0);
            filePath3 = data.getData();
            try {
                bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath3);
                imgpic.setImageBitmap(bitmap3);
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


    private void uploadImagePan() {
        getpan = etpan.getText().toString().trim();
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DRForm2.this, "Uploading...", null, true, true);
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
                String uploadImage2 = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY3, mobile);
                data.put(UPLOAD_KEY4, sid);
                data.put(UPLOAD_KEY2, "PAN");
                data.put(UPLOAD_KEY1, getpan);
                data.put(UPLOAD_KEY, uploadImage2);
                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }
        imgpanst.setBackgroundResource(R.drawable.ic_baseline_cloud_upload_24);
        UploadImage ui = new UploadImage();
        ui.execute(bitmap1);
    }

    private void uploadImagePic() {

        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DRForm2.this, "Uploading Selfie..", null, true, true);
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
                String uploadImage3 = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY3, mobile);
                data.put(UPLOAD_KEY4, sid);
                data.put(UPLOAD_KEY, uploadImage3);
                String result = rh.sendPostRequest(UPLOAD_URL_PIC, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap3);
    }

    private void uploadImageDri() {
        getdri = etdri.getText().toString().trim();
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DRForm2.this, "Uploading...", null, true, true);
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
                String uploadImage3 = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY3, mobile);
                data.put(UPLOAD_KEY4, sid);
                data.put(UPLOAD_KEY2, "DRI");
                data.put(UPLOAD_KEY1, getdri);
                data.put(UPLOAD_KEY, uploadImage3);
                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }
        imgdrist.setBackgroundResource(R.drawable.ic_baseline_cloud_upload_24);
        UploadImage ui = new UploadImage();
        ui.execute(bitmap2);
    }

    private void uploadImage() {
        getaadhar = etaadhar.getText().toString().trim();
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DRForm2.this, "Uploading...", null, true, true);
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
                String uploadImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY3, mobile);
                data.put(UPLOAD_KEY4, sid);
                data.put(UPLOAD_KEY2, "AAD");
                data.put(UPLOAD_KEY1, getaadhar);
                data.put(UPLOAD_KEY, uploadImage);
                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }

        }
        imageaadhst.setBackgroundResource(R.drawable.ic_baseline_cloud_upload_24);
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public void onClick(View v) {
        if (v == imageView) {
            showFileChooser();
        }
        if (v == imgpan) {
            showFileChooserPan();
        }
        if (v == imgdri) {
            showFileChooserDri();
        }

        if (v == imgpic) {
            showFileChooserPic();
            // uploadImagePic();
        }
        if (v == btaad) {
            uploadImage();
        }
        if (v == btpanupload) {
            uploadImagePan();
        }
        if (v == btdriupload) {
            uploadImageDri();
        }
        if (v == btpic) {
            uploadImagePic();
        }
        if (v == buttonView) {
            viewImage();
        }
    }

    private void checkDriverRegistration() {
        //Check Whether Driver already register or not-------------------------------START------------------------------------------------------
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {

            URL url = new URL("http://feeds.expressindia.com/test/checkDriverDoc.php?mo=" + mobile + "&&si=" + sid);
            Toast.makeText(getApplicationContext(), mobile + "Form Status : " + sid, Toast.LENGTH_SHORT).show();
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            is = urlConnection.getInputStream();
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Fail 1" + e.toString(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(), "Fail 2" + e.toString(), Toast.LENGTH_LONG).show();
        }

        try {
            JSONObject objson = new JSONObject(result);
            JSONArray jsonArray = objson.getJSONArray("msg_responce");
            int count = 0;
            Integer aadhar = null, pancard = null, drivingno = null, selfpic = null;
            for (count = 0; count < jsonArray.length(); count++) {
                JSONObject obj = jsonArray.getJSONObject(count);
                if (!obj.isNull("aad")) {
                    aadhar = obj.getInt("aad");
                } else {
                    aadhar = 4;
                }
                if (!obj.isNull("pan")) {
                    pancard = obj.getInt("pan");
                } else {
                    pancard = 4;
                }

                if (!obj.isNull("dri")) {
                    drivingno = obj.getInt("dri");
                } else {
                    drivingno = 4;
                }
                if (!obj.isNull("pic")) {
                    selfpic = obj.getInt("pic");
                } else {
                    selfpic = 4;
                }


                if (aadhar == 2) {
                    imageaadhst.setBackgroundResource(R.drawable.ic_baseline_verified_user_24);
                } else if (aadhar == 3) {
                    imageaadhst.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
                } else if (aadhar == 1) {
                    imageaadhst.setBackgroundResource(R.drawable.ic_baseline_cloud_done_24);
                }

                if (pancard == 2) {
                    imgpanst.setBackgroundResource(R.drawable.ic_baseline_verified_user_24);
                } else if (pancard == 3) {
                    imgpanst.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
                } else if (pancard == 1) {
                    imgpanst.setBackgroundResource(R.drawable.ic_baseline_cloud_done_24);
                }

                if (drivingno == 2) {
                    imgdrist.setBackgroundResource(R.drawable.ic_baseline_verified_user_24);
                } else if (drivingno == 3) {
                    imgdrist.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
                } else if (drivingno == 1) {
                    imgdrist.setBackgroundResource(R.drawable.ic_baseline_cloud_done_24);
                }
                if (selfpic == 2) {
                    imgpicst.setBackgroundResource(R.drawable.ic_baseline_verified_user_24);
                } else if (selfpic == 3) {
                    imgpicst.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
                } else if (selfpic == 1) {
                    imgpicst.setBackgroundResource(R.drawable.ic_baseline_cloud_done_24);
                }
                // Toast.makeText(getApplicationContext(), mobile+" & "+sid+" Data " + jsonArray, Toast.LENGTH_LONG).show();
            }
            //spinner_fn();
            totalSelfDoc = aadhar + pancard + drivingno + selfpic;
            //Toast.makeText(getApplicationContext(), aadhar+"-"+pancard+" - " +drivingno+"-"+selfpic, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            Log.e("Fail 3", e.toString());
            Toast.makeText(getApplicationContext(), "Fail 3" + e.toString(), Toast.LENGTH_LONG).show();

        }


    }

    private void viewImage() {
        startActivity(new Intent(this, MainActivity.class));
    }


    public void saveDriverFormTwoDocument(View v) {

        //  Toast.makeText(getApplicationContext(), "Total Uploaded Count : "+totalSelfDoc, Toast.LENGTH_SHORT).show();
        if (totalSelfDoc == 8) {
            formstatus = 4;
        } else if (totalSelfDoc == 4) {
            formstatus = 4;
        } else {
            formstatus = 0;
        }
        // Toast.makeText(getApplicationContext(), "Form Status : "+formstatus, Toast.LENGTH_SHORT).show();
        saveFormTwo();
    }

    private void saveFormTwo() {


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {

            URL url = new URL("http://feeds.expressindia.com/test/addFormStatus.php?mob=" + mobile + "&&stu=" + formstatus + "&&fom=2");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            is = urlConnection.getInputStream();
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());

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
            // Toast.makeText(getApplicationContext(), "Fail 2"+e.toString(), Toast.LENGTH_LONG).show();
        }
        try {
            JSONObject objson = new JSONObject(result);
            JSONArray jsonArray = objson.getJSONArray("msg_responce");

            int count = 0;
            int form1 = 0, form2 = 0, form3 = 0, form4 = 0, flagForm = 0;

            String totalForm = null;
            if (jsonArray.length() == 0) {
                Toast.makeText(getApplicationContext(), "Please Try Again!" , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Self Form Submit " , Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getBaseContext(), DRForm3.class);
                startActivity(myIntent);
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
            // Toast.makeText(getApplicationContext(), "Fail 2"+e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    }

