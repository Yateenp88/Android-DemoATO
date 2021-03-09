package com.yp.demoato;

public class Name {

    private String lang,mob,sid;
    private double latitude,longitude;
    private int status;
    public Name(String sid,String mob, String lang, int status) {
        this.lang = lang;
        this.sid = sid;
        this.status = status;
    }
}
