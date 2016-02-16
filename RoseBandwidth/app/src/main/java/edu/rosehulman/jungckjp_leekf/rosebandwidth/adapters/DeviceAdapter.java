package edu.rosehulman.jungckjp_leekf.rosebandwidth.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.io.IOException;
import java.util.HashMap;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Device;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.DeviceCustomization;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.API;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.MainActivity;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.R;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.Constants;

/**
 * Created by leekf on 1/12/2016.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private final Firebase mDevicesRef;
    private Context mContext;
//    private RecyclerView mRecyclerView;
    private API mAPI;

    private HashMap<String, DeviceCustomization> mCustomizations;

    public DeviceAdapter(Context context) throws IOException {
        mContext = context;
//        mRecyclerView = rView;

        mAPI = API.getInstance((MainActivity)context);

        mDevicesRef = new Firebase(Constants.DEVICES_PATH);

        mCustomizations = new HashMap<String, DeviceCustomization>();

        Query query = mDevicesRef.orderByChild("user").equalTo(mAPI.getCurrentUser());
        query.addChildEventListener(new DevicesChildEventListener());
        
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Device device = mAPI.getDevices().get(position);
        String key = getDeviceKey(device);

        if (mCustomizations.size() > 0) {

        DeviceCustomization deviceCustomization = mCustomizations.get(key);

        if (deviceCustomization != null) {
            if (deviceCustomization.getNickname().equals("")) {
                holder.mName.setText(mAPI.getDevices().get(position).getName());
            } else {
                holder.mName.setText(deviceCustomization.getNickname());
            }

            if (!deviceCustomization.getImageResId().equals("")) {
                String imageRes = deviceCustomization.getImageResId();
                int resourceID = mContext.getResources().getIdentifier(imageRes, "drawable", mContext.getPackageName());
                holder.mImageView.setImageDrawable(mContext.getResources().getDrawable(resourceID));
            } else {
                holder.mImageView.setImageResource(R.drawable.laptop);
            }
        } else {
            DeviceCustomization customization = new DeviceCustomization(key, "", device.getImageRes(), mAPI.getCurrentUser());
            firebasePush(customization);
            holder.mName.setText(mAPI.getDevices().get(position).getName());
        }

        holder.mUsage.setText(mAPI.getDevices().get(position).getUsageAmount() + " MB");
        }
        else {
            DeviceCustomization customization = new DeviceCustomization(key, "", device.getImageRes(), mAPI.getCurrentUser());
            firebasePush(customization);
            holder.mName.setText(mAPI.getDevices().get(position).getName());
        }
    }

    public void firebasePush(DeviceCustomization deviceCustomization) {
//        Firebase ref = mDevicesRef.push();
        Firebase deviceCustomizationRef = new Firebase(Constants.DEVICES_PATH + "/" + deviceCustomization.getUid() + "/");
        deviceCustomizationRef.setValue(deviceCustomization);
    }

    public void firebaseEdit(DeviceCustomization deviceCustomization, String uid) {
        // Since there is only 1 editable field, we set it directly by tunneling down the path 1 more level.
        Firebase deviceCustomizationRef = new Firebase(Constants.DEVICES_PATH + "/" + uid + "/");
        deviceCustomizationRef.setValue(deviceCustomization);
    }

    @SuppressLint("InflateParams")
    public void showNameDialog(final Device device) {
        DialogFragment df = new DialogFragment() {
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Edit Device Name");

                View view = getActivity().getLayoutInflater().inflate(R.layout.device_edit, null);
                builder.setView(view);
                final EditText deviceNicknameEditText = (EditText) view.findViewById(R.id.dialog_edit_name);
                deviceNicknameEditText.setHint("Device Nickname");

                String key = getDeviceKey(device);

                Log.d(Constants.TAG, key);
                final DeviceCustomization deviceCustomization = mCustomizations.get(key);

                if (!deviceCustomization.getNickname().equals("")) {
                    deviceNicknameEditText.setText(deviceCustomization.getNickname());
                } else {
                    deviceNicknameEditText.setText(device.getName());
                }

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceNickname = deviceNicknameEditText.getText().toString();
                        deviceCustomization.setNickname(deviceNickname);
                        if (mCustomizations.get(deviceCustomization.getKey()) == null) {
                            firebasePush(deviceCustomization);
                        } else {
                            firebaseEdit(deviceCustomization, deviceCustomization.getUid());
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
//                if (deviceCustomization != null) {
//                    builder.setNeutralButton(R.string.remove, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            showDeleteConfirmationDialog(course);
//                        }
//                    });
//                }
                return builder.create();
            }
        };
        df.show(((MainActivity) mContext).getSupportFragmentManager(), "editname");
    }

    @SuppressLint("InflateParams")
    public void showImageDialog(final Device device) {
        DialogFragment df = new DialogFragment() {
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Edit Device Image");

                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_image_picker, null);
                builder.setView(view);
                final ImageButton iPhoneButton = (ImageButton) view.findViewById(R.id.iPhoneButton);
                final ImageButton laptopButton = (ImageButton) view.findViewById(R.id.laptopButton);
                final ImageButton desktopButton = (ImageButton) view.findViewById(R.id.desktopButton);
                final ImageButton iPadButton = (ImageButton) view.findViewById(R.id.ipadButton);
                final ImageButton ps4Button = (ImageButton) view.findViewById(R.id.ps4Button);
                final ImageButton xboxButton = (ImageButton) view.findViewById(R.id.xboxButton);

                String key = getDeviceKey(device);

                Log.d(Constants.TAG, key);

                final DeviceCustomization deviceCustomization = mCustomizations.get(key);

                iPhoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deviceCustomization.setImageResId("iphone");
                        firebaseEdit(deviceCustomization, deviceCustomization.getUid());
                        dismiss();
                    }
                });

                laptopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deviceCustomization.setImageResId("laptop");
                        firebaseEdit(deviceCustomization, deviceCustomization.getUid());
                        dismiss();
                    }
                });

                desktopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deviceCustomization.setImageResId("desktop");
                        firebaseEdit(deviceCustomization, deviceCustomization.getUid());
                        dismiss();
                    }
                });

                iPadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deviceCustomization.setImageResId("ipad");
                        firebaseEdit(deviceCustomization, deviceCustomization.getUid());
                        dismiss();
                    }
                });

                ps4Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deviceCustomization.setImageResId("ps4");
                        firebaseEdit(deviceCustomization, deviceCustomization.getUid());
                        dismiss();
                    }
                });

                xboxButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deviceCustomization.setImageResId("xbox");
                        firebaseEdit(deviceCustomization, deviceCustomization.getUid());
                        dismiss();
                    }
                });
                return builder.create();
            }
        };
        df.show(((MainActivity) mContext).getSupportFragmentManager(), "editname");
    }


    @NonNull
    private String getDeviceKey(Device device) {
        return device.getMacAddress().replace(":", "").substring(0,6);
    }

    @Override
    public int getItemCount() {
        return mAPI.getDevices().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private ImageView mImageView;
        private TextView mName;
        private TextView mUsage;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView)itemView.findViewById(R.id.device_image);
            mName = (TextView)itemView.findViewById(R.id.device_name);
            mUsage = (TextView)itemView.findViewById(R.id.device_usage);
            itemView.setOnLongClickListener(this);

            mImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showImageDialog(mAPI.getDevices().get(getAdapterPosition()));
                    return true;
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            showNameDialog(mAPI.getDevices().get(getAdapterPosition()));
            return true;
        }
    }

    class DevicesChildEventListener implements ChildEventListener {

        private void add(DataSnapshot dataSnapshot) {
            DeviceCustomization deviceCustomization = dataSnapshot.getValue(DeviceCustomization.class);
            deviceCustomization.setKey(dataSnapshot.getKey());
            mCustomizations.put(deviceCustomization.getUid(), deviceCustomization);
        }

        private int remove(String key) {
            mCustomizations.remove(key);
            return -1;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(Constants.TAG, "My customization: " + dataSnapshot);
            add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            remove(dataSnapshot.getKey());
            add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            int position = remove(dataSnapshot.getKey());
            if (position >= 0) {
                notifyItemRemoved(position);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // empty
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("TAG", "onCancelled. Error: " + firebaseError.getMessage());

        }
    }

    private class ButtonOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
//            mContext.getResources().getDrawable(deviceCustomization.getImageResId())
        }
    }
}
