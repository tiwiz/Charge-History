package it.tiwiz.chargeHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Roby on 18/12/13.
 */
public class ChargeDataAdapter extends ArrayAdapter{

    private Context mContext;
    private List<ChargerElement> mElements;

    public ChargeDataAdapter(Context mContext, List<ChargerElement> mElements){

        super(mContext,R.layout.charge_element,mElements);
        this.mContext = mContext;
        this.mElements = mElements;

    }

    @Override
    public int getCount() {
        return mElements.size();
    }

    @Override
    public Object getItem(int i) {
        return mElements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parentViewGroup) {

        ViewHolder holder;
        if(position >= mElements.size()) return null;

        if(null == contentView){
            //if a view is not created, we allocate it
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = inflater.inflate(R.layout.charge_element,parentViewGroup,false);
            //we create the holder that will store our references id
            holder = new ViewHolder();
            holder.imgChargeType = (ImageView) contentView.findViewById(R.id.imgChargeType);
            holder.txtDeviceName = (TextView) contentView.findViewById(R.id.txtDeviceName);
            holder.txtElapsedChargedTime = (TextView) contentView.findViewById(R.id.txtElapsedChargedTime);
            holder.txtDetails = (TextView) contentView.findViewById(R.id.txtDetails);
            //we store the holder so that we can retrieve it later
            contentView.setTag(holder);
        }else
            holder = (ViewHolder) contentView.getTag(); //we retrieve the holder, so that we can fill it

        //gets the needed data
        final ChargerElement thisElement = mElements.get(position);
        final String detailsString = mContext.getString(R.string.param_detail_string_sync_data, thisElement.getAndroidVersion(),thisElement.getStartPercentage(),thisElement.getDate());

        //fills the fields in order to get a full element
        holder.txtDeviceName.setText(thisElement.getDevice());
        holder.txtElapsedChargedTime.setText(thisElement.getElapsedChargeTime());
        holder.txtDetails.setText(detailsString);
        holder.imgChargeType.setImageResource(thisElement.getChargeImageIconID());

        return contentView;
    }

    static class ViewHolder{
        TextView txtDeviceName, txtElapsedChargedTime, txtDetails;
        ImageView imgChargeType;
    }
}
