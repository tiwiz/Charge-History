package it.tiwiz.chargeHistory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dropbox.sync.android.DbxAccountManager;

public class ChargeReceiver extends BroadcastReceiver {
    public ChargeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final Intent serviceIntent = new Intent(context,BatteryWatcher.class);
        String intentAction = intent.getAction();

        if(intentAction.equals(Intent.ACTION_POWER_CONNECTED)){
            //gets DbxAccountManager reference
            DbxAccountManager dbxAccountManager = DbxAccountManager.getInstance(context.getApplicationContext(),DropboxData.AppKey,DropboxData.AppSecret);
            //starts the service if and only if we already have Dropbox account linked
            if(dbxAccountManager.hasLinkedAccount())
                context.startService(serviceIntent);
        }else if(intentAction.equals(Intent.ACTION_POWER_DISCONNECTED))
            context.stopService(serviceIntent);
    }
}
