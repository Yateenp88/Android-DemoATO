package com.yp.demoato;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LangBar extends AppCompatActivity  {
    String[] Language = { "English", "ಕನ್ನಡ","हिन्दी"};
    private Button btnset;
    private TextView language_dialog;
    private EditText editgetmob;
    private String langstatus,getmobile;
    Context context;
    Resources resources;
    boolean lang_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_langbar);
        language_dialog = (TextView) findViewById(R.id.dialog_language1);
        btnset =(Button) findViewById(R.id.btset) ;
        editgetmob = (EditText) findViewById(R.id.editgetmob);

        String phoneNumber = getIntent().getStringExtra("authcode");

        //Toast.makeText(LangBar.this, phoneNumber, Toast.LENGTH_LONG).show();
        if (SessionManager.getLanguage(getApplicationContext()).length() != 0)
            context = SessionManager.setLocale(LangBar.this , SessionManager.getLanguage(getApplicationContext()));
        else
            context = SessionManager.setLocale(LangBar.this , "en");

        resources = context.getResources();
        language_dialog.setText(resources.getString(R.string.language));
        btnset.setText(resources.getString(R.string.next));
        language_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] Language = {"ENGLISH", "ಕನ್ನಡ"};
                final int checkedItem;
                if(lang_selected)
                {
                    checkedItem=0;
                }else
                {
                    checkedItem=1;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(LangBar.this);
                builder.setTitle("Select a Language...")
                        .setSingleChoiceItems(Language, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(MainActivity.this,""+which,Toast.LENGTH_SHORT).show();
                                language_dialog.setText(Language[which]);
                                lang_selected= Language[which].equals("ENGLISH");
                                //if user select prefered language as English then
                                if(Language[which].equals("ENGLISH"))
                                {
                                    context = SessionManager.setLocale(LangBar.this, "en");
                                    resources = context.getResources();
                                    langstatus = "en";
                                    //text1.setText(resources.getString(R.string.registration));
                                }
                                //if user select prefered language as Hindi then
                                if(Language[which].equals("ಕನ್ನಡ"))
                                {
                                    context = SessionManager.setLocale(LangBar.this, "kn");
                                    resources = context.getResources();
                                    langstatus = "kn";
                                }
                                if(Language[which].equals("हिन्दी"))
                                {
                                    context = SessionManager.setLocale(LangBar.this, "hi");
                                    resources = context.getResources();

                                }
                                if(Language[which].equals("मराठी"))
                                {
                                    context = SessionManager.setLocale(LangBar.this, "mr");
                                    resources = context.getResources();

                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        });

    }


    public void checkUserProfile(View v) {
        if(langstatus==null){
            getmobile =editgetmob.getText().toString().trim();
            Toast.makeText(LangBar.this,"Select Language",Toast.LENGTH_SHORT).show();
        } else {
            // If language selected
           // Toast.makeText(LangBar.this,""+langstatus,Toast.LENGTH_SHORT).show();
            getmobile =editgetmob.getText().toString().trim();
             Intent myIntent = new Intent(getBaseContext(),   CheckDRProfile.class);
             myIntent.putExtra("authcode",getmobile);
            myIntent.putExtra("langcode",langstatus);
             startActivity(myIntent);
        }



    }


}
