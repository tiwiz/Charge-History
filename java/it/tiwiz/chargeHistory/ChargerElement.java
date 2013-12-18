package it.tiwiz.chargeHistory;

import android.os.BatteryManager;

/**
 * Created by Roby on 18/12/13.
 */
public class ChargerElement {

    private String device;
    private String elapsedChargeTime;
    private String androidVersion;
    private String date;
    private int startPercentage;
    private int typeOfCharge;
    private int chargeImageIconID;

    public ChargerElement(){
        this("","","","",0,0);
    }

    public ChargerElement(String device, String elapsedChargeTime, String androidVersion, String date, int startPercentage, int typeOfCharge){
        this.device = device;
        this.elapsedChargeTime = elapsedChargeTime;
        this.androidVersion = androidVersion;
        this.date = date;
        this.startPercentage = startPercentage;
        setTypeOfCharge(typeOfCharge);
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getElapsedChargeTime() {
        return elapsedChargeTime;
    }

    public void setElapsedChargeTime(String elapsedChargeTime) {
        this.elapsedChargeTime = elapsedChargeTime;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStartPercentage() {
        return startPercentage;
    }

    public void setStartPercentage(int startPercentage) {
        this.startPercentage = startPercentage;
    }

    public int getTypeOfCharge() {
        return typeOfCharge;
    }

    public void setTypeOfCharge(int typeOfCharge) {
        this.typeOfCharge = typeOfCharge;

        switch(typeOfCharge){
            case BatteryManager.BATTERY_PLUGGED_AC:
                chargeImageIconID = R.drawable.charge_ac;
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                chargeImageIconID = R.drawable.charge_usb;
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                chargeImageIconID = R.drawable.charge_wireless;
                break;
            default:
                chargeImageIconID = R.drawable.unknown;
                break;
        }
    }

    public int getChargeImageIconID() {
        return chargeImageIconID;
    }

    public void setChargeImageIconID(int chargeImageIconID) {
        this.chargeImageIconID = chargeImageIconID;
    }
}
