package com.yp.demoato;

import android.app.Application;
import android.content.Context;
//This File related to language switch case
public class ApplicationNew extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(SessionManager.onChange(base, "en"));
    }
}
