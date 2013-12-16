package it.tiwiz.chargeHistory;

import android.util.Log;

/**
 * Created by Roby on 16/12/13.
 */
public class MyLog{

    private final static String LOG_TAG = "ChargeHistoryDebug";
    private final static boolean DEBUG = true;

    public static final void d(String message){
        if(DEBUG)
            Log.d(LOG_TAG,message);
    }
}
