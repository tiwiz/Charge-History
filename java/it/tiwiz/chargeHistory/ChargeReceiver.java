package it.tiwiz.chargeHistory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ChargeReceiver extends BroadcastReceiver {
    public ChargeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final Intent serviceIntent = new Intent(context,BatteryWatcher.class);
        String intentAction = intent.getAction();

        if(intentAction.equals(Intent.ACTION_POWER_CONNECTED))
            context.startService(serviceIntent);
        else if(intentAction.equals(Intent.ACTION_POWER_DISCONNECTED))
            context.stopService(serviceIntent);
    }
}
