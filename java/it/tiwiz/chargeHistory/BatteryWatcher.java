package it.tiwiz.chargeHistory;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BatteryWatcher extends Service {
    private static final String INITIAL_PERCENTAGE_KEY = "initial_percentage";
    private static final String INITIAL_POWER_TYPE_KEY = "initial_power_type";
    private static final String INITIAL_TIME_KEY = "initial_time";
    private static final String DATE_FORMAT = "HH:mm:ss";
    private static final String TIMEZONE = "GMT";

    private static final int INITIAL_PERCENTAGE_DEFAULT_VALUE = -1;
    private static final int INITIAL_POWER_TYPE_DEFAULT_VALUE = -1;
    private static final int INITIAL_TIME_DEFAULT_VALUE = -1;

    private BatteryDetailsReceiver batteryReceiver = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;
    }
    @Override
    public void onCreate(){
        super.onCreate();

        batteryReceiver = new BatteryDetailsReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver,filter);

    }

    public void onDestroy (){
        super.onDestroy();

        if(null != batteryReceiver){
            unregisterReceiver(batteryReceiver);
        }
    }

    private class BatteryDetailsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int pluggedStatus = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);

            if(pluggedStatus != 0){ //status 0 is battery

                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                final int initialPercentage = prefs.getInt(INITIAL_PERCENTAGE_KEY,INITIAL_PERCENTAGE_DEFAULT_VALUE);
                int currentBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);

                boolean isBatteryCharged = currentBatteryLevel == 100;

                if(isBatteryCharged){
                    //gets data from SharedPreferences
                    //deletes SharedPreferences
                    //uploads data
                    if(initialPercentage != INITIAL_PERCENTAGE_DEFAULT_VALUE){
                        int initialPluggedStatus = prefs.getInt(INITIAL_POWER_TYPE_KEY,INITIAL_POWER_TYPE_DEFAULT_VALUE);
                        //if stored pluggedStatus is different than the one we have now
                        //user must have changed charge type, so we cannot perform the analysis
                        if(initialPluggedStatus != pluggedStatus) return;
                        //calculates the time battery has been charging
                        long currentTimeStamp = System.currentTimeMillis();
                        long initialTimeStamp = prefs.getLong(INITIAL_TIME_KEY,INITIAL_TIME_DEFAULT_VALUE);
                        //calculates the right amount of time spent on charging
                        String chargeDuration = "N\\A";
                        if(initialPercentage != INITIAL_TIME_DEFAULT_VALUE){
                            currentTimeStamp = currentTimeStamp - initialTimeStamp;
                            Date now = new Date();
                            now.setTime(currentTimeStamp);
                            //correctly formats time
                            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                            sdf.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
                            chargeDuration = sdf.format(now);
                        }

                        //erases data
                        saveDataLocally(prefs, INITIAL_PERCENTAGE_DEFAULT_VALUE,
                                INITIAL_POWER_TYPE_DEFAULT_VALUE, INITIAL_TIME_DEFAULT_VALUE);

                        //kills the service
                        Intent killServiceIntent = new Intent(context,BatteryWatcher.class);
                        stopService(killServiceIntent);
                    }
                }else{
                    //if SharedPreferences are still empty
                    //saves data
                    if(initialPercentage == INITIAL_PERCENTAGE_DEFAULT_VALUE){
                        //no percentage has been saved
                        //gets current timestamp
                        long currentTimeStamp = System.currentTimeMillis();
                        //saves current data
                        saveDataLocally(prefs,currentBatteryLevel,pluggedStatus,currentTimeStamp);
                    }
                }
            }
        }

        private void saveDataLocally(SharedPreferences prefs, int batteryLevel, int pluggedStatus, long currentTimeStamp){

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(INITIAL_PERCENTAGE_KEY,batteryLevel);
            editor.putInt(INITIAL_POWER_TYPE_KEY,pluggedStatus);
            editor.putLong(INITIAL_TIME_KEY,currentTimeStamp);
            editor.commit();
            MyLog.d("Deletion complete");
        }
    }
}
