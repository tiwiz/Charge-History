package it.tiwiz.chargeHistory;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;

import java.util.ArrayList;
import java.util.List;

import static it.tiwiz.chargeHistory.SafeCast.long2int;

/**
 * Created by Roby on 18/12/13.
 */
public class DataListFragment extends Fragment{

    DbxAccountManager dbxAccountManager;
    DbxAccount dbxAccount = null;
    Context mContext;
    ChargeDataAdapter mChargeAdapter = null;
    ListView listDropboxSyncData;

    public DataListFragment(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_battery_history, container, false);
        listDropboxSyncData = (ListView) rootView.findViewById(R.id.dropboxSyncList);

        dbxAccountManager = DbxAccountManager.getInstance(mContext,DropboxData.AppKey,DropboxData.AppSecret);

        if(dbxAccountManager.hasLinkedAccount()){
            dbxAccount = dbxAccountManager.getLinkedAccount();
            new DropboxBackgroundLoader().execute();
        }
        return rootView;
    }

    private class DropboxBackgroundLoader extends AsyncTask<Void, Void, Integer>{

        private static final int RESULT_OK = 0;
        private static final int RESULT_DBXEXCEPTION = 100;
        private static final int RESULT_LONG2INTEXCEPTION = 200;

        protected void onPreExecute (){

        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Integer result = RESULT_OK;

            try {
                //opens DataStore
                DbxDatastore dbxDatastore = DbxDatastore.openDefault(dbxAccount);
                //gets Table where we saved our data
                DbxTable dbxTable = dbxDatastore.getTable(Table.TableName);
                //queries all the records
                DbxTable.QueryResult dbxResults = dbxTable.query();
                MyLog.d("Has Results: " + dbxResults.iterator().hasNext());
                dbxDatastore.close();
                //creates a List from these results
                List<DbxRecord> dbxRecordList = dbxResults.asList();
                //converts the DbxRecord List into a ChargerElement List
                ChargerElement tmpElement;
                List<ChargerElement> elementList = new ArrayList<ChargerElement>();

                for(DbxRecord thisRecord : dbxRecordList){
                    tmpElement = new ChargerElement();
                    tmpElement.setStartPercentage(long2int(thisRecord.getLong(Table.HInitialPercentage)));
                    tmpElement.setTypeOfCharge(long2int(thisRecord.getLong(Table.HTypeOfCharge)));
                    tmpElement.setAndroidVersion(thisRecord.getString(Table.HAndroidVersion));
                    tmpElement.setDate(thisRecord.getString(Table.HDate));
                    tmpElement.setElapsedChargeTime(thisRecord.getString(Table.HElapsedChargedTime));
                    tmpElement.setDevice(thisRecord.getString(Table.HDevice));
                    elementList.add(tmpElement);
                    MyLog.d("Elemento inserito");
                }
                //gets the adapter
                mChargeAdapter = new ChargeDataAdapter(mContext,elementList);
                //mChargeAdapter.notifyDataSetChanged();
            } catch (DbxException e) {
                result = RESULT_DBXEXCEPTION;
            } catch(IllegalArgumentException e){
                result = RESULT_LONG2INTEXCEPTION;
            }
            return result;
        }

        protected void onPostExecute(Integer result) {

            listDropboxSyncData.setAdapter(mChargeAdapter);
            mChargeAdapter.notifyDataSetChanged();

        }

    }

}
