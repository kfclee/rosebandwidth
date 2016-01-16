package edu.rosehulman.jungckjp_leekf.rosebandwidth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by leekf on 1/12/2016.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private ArrayList<Device> mDevices = new ArrayList<Device>();
    private Context mContext;
    private RecyclerView mRecyclerView;

    public DeviceAdapter(Context context, RecyclerView rView){
        mContext = context;
        mRecyclerView = rView;

        Device testDevice = new Device("Laptop", "ee:80:f6...", 901, 0);
        mDevices.add(testDevice);
        testDevice = new Device("iPhone", "ee:80:f6...", 4000, 0);
        mDevices.add(testDevice);
        testDevice = new Device("PS4", "ee:80:f6...", 100000, 0);
        mDevices.add(testDevice);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText(mDevices.get(position).getName());
        holder.mUsage.setText(Integer.toString(mDevices.get(position).getUsageAmount()));
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mName;
        private TextView mUsage;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView)itemView.findViewById(R.id.device_image);
            mName = (TextView)itemView.findViewById(R.id.device_name);
            mUsage = (TextView)itemView.findViewById(R.id.device_usage);
        }
    }
}
