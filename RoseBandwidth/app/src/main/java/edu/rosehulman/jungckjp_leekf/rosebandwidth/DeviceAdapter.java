package edu.rosehulman.jungckjp_leekf.rosebandwidth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by leekf on 1/12/2016.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private API mAPI;

    public DeviceAdapter(Context context, RecyclerView rView) throws IOException {
        mContext = context;
        mRecyclerView = rView;

        mAPI = API.getInstance((MainActivity)context);

//        Device testDevice = new Device("Laptop", "ee:80:f6...", 901, 0);
//        mDevices.add(testDevice);
//        testDevice = new Device("iPhone", "ee:80:f6...", 4000, 0);
//        mDevices.add(testDevice);
//        testDevice = new Device("PS4", "ee:80:f6...", 100000, 0);
//        mDevices.add(testDevice);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText(mAPI.mDevices.get(position).getName());
        holder.mUsage.setText(mAPI.mDevices.get(position).getUsageAmount() + " MB");
    }

    @Override
    public int getItemCount() {
        return mAPI.mDevices.size();
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
