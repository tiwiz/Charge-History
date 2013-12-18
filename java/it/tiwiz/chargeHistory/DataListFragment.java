package it.tiwiz.chargeHistory;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dropbox.sync.android.DbxAccountManager;

/**
 * Created by Roby on 18/12/13.
 */
public class DataListFragment extends Fragment{

    DbxAccountManager dbxAccountManager;
    Context mContext;

    public DataListFragment(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_battery_history, container, false);

        dbxAccountManager = DbxAccountManager.getInstance(mContext,DropboxData.AppKey,DropboxData.AppSecret);
        return rootView;
    }

}
