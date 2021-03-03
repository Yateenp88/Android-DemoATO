package com.yp.demoato;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LangBar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] country = { "English", "Kannada","Hindi"};
    private Button btnset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_langbar);
        String phoneNumber = getIntent().getStringExtra("authcode");
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        final Spinner  spin = (Spinner) findViewById(R.id.spinnerlang);
        spin.setOnItemSelectedListener(this);
        Toast.makeText(LangBar.this, phoneNumber, Toast.LENGTH_LONG).show();
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        btnset = findViewById(R.id.btset);
        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String languageCode = spin.getSelectedItem().toString();
                SessionManager sessionManager = new SessionManager(LangBar.this);
                sessionManager.setLancode(languageCode);





            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
