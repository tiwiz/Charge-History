package it.tiwiz.chargeHistory;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountInfo;
import com.dropbox.sync.android.DbxAccountManager;

public class BatteryHistoryActivity extends Activity {

    DbxAccountManager dbxAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_history);

        //checks for Dropbox account link
        checkDropboxLink();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.battery_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_battery_history, container, false);
            return rootView;
        }
    }

    private void checkDropboxLink(){

        dbxAccountManager = DbxAccountManager.getInstance(getApplicationContext(),DropboxData.AppKey,DropboxData.AppSecret);

        if(!dbxAccountManager.hasLinkedAccount()){
            //no accounts have been linked

            final DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    switch(which){
                        case DialogInterface.BUTTON_NEGATIVE: //exits application
                            finish();
                            break;
                        case DialogInterface.BUTTON_POSITIVE: //links Dropbox account
                            dbxAccountManager.startLink((Activity)BatteryHistoryActivity.this,DropboxData.RequestLink);
                            break;
                    }
                }
            };

            //warns the user
            final AlertDialog.Builder DropboxLinkDialogBuilder = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.dropbox_dialog_title)
                    .setMessage(R.string.dropbox_dialog_message)
                    .setIcon(R.drawable.dropbox_logo_blue)
                    .setPositiveButton(R.string.dropbox_dialog_yes_button,dialogOnClickListener)
                    .setNegativeButton(R.string.dropbox_dialog_no_button,dialogOnClickListener);

            DropboxLinkDialogBuilder.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == DropboxData.RequestLink){
            //we returned to the main Activity after Dropbox Authentication Activity
            if(resultCode == Activity.RESULT_OK){
                DbxAccount dbxAccount = dbxAccountManager.getLinkedAccount();

                DbxAccount.Listener accountChangeListener = new DbxAccount.Listener() {
                    @Override
                    public void onAccountChange(DbxAccount dbxAccount) {
                        DbxAccountInfo dbxAccountInfo = dbxAccount.getAccountInfo();
                        if(null != dbxAccountInfo){
                            String message = getString(R.string.dropbox_link_ok,dbxAccountInfo.userName);
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            dbxAccount.removeListener(this);
                        }
                    }
                };

                dbxAccount.addListener(accountChangeListener);
            }else{
                Toast.makeText(getApplicationContext(),R.string.dropbox_link_fail,Toast.LENGTH_SHORT).show();
                finish();
            }
        }else
            super.onActivityResult(requestCode,resultCode,data);
    }

}
