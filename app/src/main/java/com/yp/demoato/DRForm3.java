package com.yp.demoato;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DRForm3 extends AppCompatActivity {
    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    private String mobile,sid;
    Spinner sp_make,sp_model,sp_type;
    ArrayList<String> arrayList_make,arrayList_type;
    ArrayAdapter<String> arrayAdapter_make,arrayAdapter_type;
    ArrayList<String> arrayList_1,arrayList_2,arrayList_3,arrayList_4,arrayList_5;
    ArrayAdapter<String> arrayAdapter_model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_form3);


        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(LangBar.SHARED_PREFS, Context.MODE_PRIVATE);
        // getting data from shared prefs and
        mobile = sharedpreferences.getString(LangBar.MOBILENO, null);
        sid = sharedpreferences.getString(DRForm1.SID, null);

        sp_make = (Spinner) findViewById(R.id.spmake);
        sp_model = (Spinner) findViewById(R.id.spmodel);
        sp_type = (Spinner) findViewById(R.id.sptype);

        arrayList_type = new ArrayList<>();
        arrayList_type.add("Vehical Type");
        arrayList_type.add("Petrol");
        arrayList_type.add("Disel");
        arrayList_type.add("Electric");
        arrayAdapter_type = new ArrayAdapter<>(getApplication(),android.R.layout.simple_spinner_item,arrayList_type);
        sp_type.setAdapter(arrayAdapter_type);
        //==========================================================
        arrayList_make = new ArrayList<>();
        arrayList_make.add("Vehical Make");
        arrayList_make.add("Bajaj");
        arrayList_make.add("TVS");
        arrayList_make.add("Mahindra");
        arrayList_make.add("Ape");
        arrayList_make.add("Piaggio");
        arrayAdapter_make = new ArrayAdapter<>(getApplication(),android.R.layout.simple_spinner_item,arrayList_make);

        sp_make.setAdapter(arrayAdapter_make);
        //============== child spinner process starts ==================
        arrayList_1 = new ArrayList<>();
        arrayList_1.add("Model");
        arrayList_1.add("Maxima X-wide");
        arrayList_1.add("RE Compact");


        arrayList_2 = new ArrayList<>();
        arrayList_2.add("Model");
        arrayList_2.add("King Auto");
        arrayList_2.add("King Duramax");


        arrayList_3 = new ArrayList<>();
        arrayList_3.add("Model");
        arrayList_3.add("Gio");
        arrayList_3.add("Alfa");
        arrayList_3.add("Treo Yaari");
        arrayList_3.add("Alfa DX");

        arrayList_4 = new ArrayList<>();
        arrayList_4.add("Model");
        arrayList_4.add("Passenger");
        arrayList_4.add("City");

        arrayList_5 = new ArrayList<>();
        arrayList_5.add("Model");
        arrayList_5.add("Ape");
        arrayList_5.add("Ape City Plus");
        arrayList_5.add("E City");
        //==============
    }
}
