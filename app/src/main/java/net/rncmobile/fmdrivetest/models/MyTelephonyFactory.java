package net.rncmobile.fmdrivetest.models;

import android.content.Context;

public class MyTelephonyFactory {
    private static volatile MyTelephonyFactory instance = null;

    private MyTelephony myTelephony;

    private MyTelephonyFactory() { }

    public static MyTelephonyFactory getInstance() {
        synchronized (MyTelephonyFactory.class) {
            if (MyTelephonyFactory.instance == null) {
                MyTelephonyFactory.instance = new MyTelephonyFactory();
            }
        }
        return MyTelephonyFactory.instance;
    }

    public IMyTelephony get(Context context) {
        if(myTelephony == null)
            myTelephony = new MyTelephony(context);
        return myTelephony;
    }
}
