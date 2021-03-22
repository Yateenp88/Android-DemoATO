package com.yp.demoato;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.spec.RSAOtherPrimeInfo;

public class DBHelper extends SQLiteOpenHelper {
    //Constants for Database name, table name, and column names
    public static final String DB_NAME = "ATO";
    public static final String TABLE_USER_REGISTRATION = "REGISTRATION";
    public static final String TABLE_USER_LANG = "LANGUAGE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_STATUS = "STATUS";
    public static final String COLUMN_SERVERID= "SERVERID";
    public static final String COLUMN_MOB = "MOB";
    //database version
    private static final int DB_VERSION = 1;

    //Constructor
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final String CREATE_DRIVER_REGISTRATION = "CREATE TABLE " + TABLE_USER_REGISTRATION
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SERVERID + " INTEGER UNIQUE," + COLUMN_MOB + " INTERGER," + "FNAME" +
            " VARCHAR, " + "LNAME" + " VARCHAR," + "EMAILID" + " VARCHAR," + "GENDER" + " VARCHAR," + "ADDRESS" + " VARCHAR,"  +"CITY" + " VARCHAR," +
            "PINCODE" + " INTEGER," + "ALTMOBILE" + " INTERGER," + "DOB" +" VARCHAR,"+
            "AADHAR" + " VARCHAR," + "PANCARD"  + " VARCHAR," + "DRIVING"  + " VARCHAR," + "RC"  + " VARCHAR," + "INSURENCE"  + " VARCHAR," +
            "PUC"  + " VARCHAR," + "ACCNAME"  + " VARCHAR," + "ACCNUM"  + " VARCHAR," + "IFSC"  + " VARCHAR," + "BRANCH"  + " VARCHAR," + "BANKNAME" + " VARCHAR," +
            "VMAKE"  + " VARCHAR," + "VMODEL"  + " VARCHAR," + "VTYPE"  + " VARCHAR," + "STAND"  + " VARCHAR," + "LANGUAGE"  + " VARCHAR," + "RATING"  + " VARCHAR," +
            COLUMN_STATUS +" TINYINT);";

    private static final String CREATE_DRIVER_LANG = "CREATE TABLE " + TABLE_USER_LANG
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SERVERID + " VARCHAR ," + "LANG" +
            " VARCHAR, " + COLUMN_MOB + " INTERGER UNIQUE," + COLUMN_STATUS +
            " TINYINT);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DRIVER_LANG);
        db.execSQL(CREATE_DRIVER_REGISTRATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Persons";
        db.execSQL(sql);
        onCreate(db);

    }


    /*
     * This method is taking two arguments
     * first one is the name that is to be saved
     * second one is the status
     * 0 means the name is synced with the server
     * 1 means the name is not synced with the server
     * */
    public boolean addDriverLang(String sid,String mob, String lang,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SERVERID, sid);
        contentValues.put("MOB", mob);
        contentValues.put("LANG", lang);
        contentValues.put(COLUMN_STATUS, status);
         db.insert(TABLE_USER_LANG, null, contentValues);
         db.close();
         return true;
    }

    public boolean updateDriverLang(String lang, int mob) {
         SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LANG", lang);
         db.update(TABLE_USER_LANG, contentValues, "MOB" + "=" + mob, null);
         db.close();
         return true;
        }

    public boolean updateDriverForm2(String ssid, String saddhar, String spancard, String sdriving) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("AADHAR", saddhar);
        contentValues.put("PANCARD", spancard);
        contentValues.put("DRIVING", sdriving);
        db.update(TABLE_USER_REGISTRATION, contentValues, COLUMN_SERVERID + "=" + ssid, null);
        db.close();
        return true;
    }

    public boolean updateDriverForm3(String ssid, String sstand, String svmake, String svmodel,String svtype,String src,String sinsurence, String spuc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("STAND", sstand);
        contentValues.put("INSURENCE", sinsurence);
        contentValues.put("RC", src);
        contentValues.put("PUC", spuc);
        contentValues.put("VMAKE", svmake);
        contentValues.put("VMODEL", svmodel);
        contentValues.put("VTYPE", svtype);
        db.update(TABLE_USER_REGISTRATION, contentValues, COLUMN_SERVERID + "=" + ssid, null);
        db.close();
        return true;
    }

    public boolean updateDriverForm4(String ssid, String saccname, String saccnum, String sifsc, String sbranch,String sbankname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACCNAME", saccname);
        contentValues.put("ACNUM", saccnum);
        contentValues.put("IFSC", sifsc);
        contentValues.put("BRANCH", sbranch);
        contentValues.put("BANKNAME", sbankname);
        db.update(TABLE_USER_REGISTRATION, contentValues, COLUMN_SERVERID + "=" + ssid, null);
        db.close();
        return true;
    }
    public boolean addDriverInfo(String ssid,String smob,String sfname,String slname,String semail,String sgender,String saddress,String scity,String spin,String salter,String sdob, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SERVERID, ssid);
        contentValues.put(COLUMN_MOB, smob);
        contentValues.put("FNAME", sfname);
        contentValues.put("LNAME", slname);
        contentValues.put("EMAILID", semail);
        contentValues.put("GENDER", sgender);
        contentValues.put("ADDRESS", saddress);
        contentValues.put("CITY", scity);
        contentValues.put("PINCODE", spin);
        contentValues.put("ALTMOBILE", salter);
        contentValues.put("DOB", sdob);
        contentValues.put(COLUMN_STATUS, status);
        db.insert(TABLE_USER_REGISTRATION, null, contentValues);
        db.close();
        return true;
    }
    public boolean deleteDriverInfo(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_REGISTRATION, null, null);
        db.close();
        return true;
    }
    /*
     * This method taking two arguments
     * first one is the id of the name for which
     * we have to update the sync status
     * and the second one is the status that will be changed
     * */
    //public boolean updateNameStatus(int id,String sid, int status) {
       // SQLiteDatabase db = this.getWritableDatabase();
       // ContentValues contentValues = new ContentValues();
        //contentValues.put(COLUMN_SERVERID, sid);
       // contentValues.put(COLUMN_STATUS, status);
       // db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id, null);
       // db.close();
       // return true;
    //}

    /*
     * this method will give us all the name stored in sqlite
     * */
    //public Cursor getNames() {
      //  SQLiteDatabase db = this.getReadableDatabase();
      //  String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
      //  Cursor c = db.rawQuery(sql, null);
       // return c;
    //}

    /*
     * this method is for getting all the unsynced name
     * so that we can sync it with database
     * */
    //public Cursor getUnsyncedNames() {
      //  SQLiteDatabase db = this.getReadableDatabase();
      //  String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0;";
       // Cursor c = db.rawQuery(sql, null);
       // return c;
    //}
}
