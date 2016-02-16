package edu.rosehulman.jungckjp_leekf.rosebandwidth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.R;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.MainActivity;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments.AlarmsFragment;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Alarm;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.API;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.Constants;

/**
 * Created by jonathan on 1/16/16.
 */
public class AlarmsAdapter  extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder>{
    private final Context mContext;
    private final RecyclerView mRecyclerView;
    private API mAPI;
    public Firebase mAlarmsRef;
    public Firebase mUsersRef;
    private AlarmsFragment mAlarmsFragment;

    private ArrayList<Alarm> mAlarms = new ArrayList<Alarm>();

    public AlarmsAdapter(Context context, RecyclerView recyclerView, AlarmsFragment alarmsFragment) {
        mContext = context;
        mRecyclerView = recyclerView;
        mAlarmsFragment = alarmsFragment;

        try {
            mAPI = API.getInstance((MainActivity) mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mAlarmsRef = new Firebase(Constants.ALARMS_PATH);

        Query query = mAlarmsRef.orderByChild("user").equalTo(mAPI.getCurrentUser());
        query.addChildEventListener(new AlarmsChildEventListener());
    }

    @Override
    public AlarmsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_view, parent, false);
        return new ViewHolder(view);
    }

    public String getTypeString(int type){
        if(type == 0){
            return "%";
        } else if(type == 1){
            return "MB";
        } else if(type == 2){
            return "GB";
        }
        return "";
    }

    @Override
    public void onBindViewHolder(AlarmsAdapter.ViewHolder holder, int position) {
        Alarm alarm = mAlarms.get(position);
        holder.mSwitch.setChecked(alarm.isEnabled());
//        holder.mSwitch.setEnabled(alarm.isEnabled());
        holder.mUsage.setText(alarm.getAmount() + getTypeString(alarm.getType()));
        holder.mName.setText(alarm.getName());
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public void firebasePush(Alarm alarm) {
        // Create a new auto-ID for a alarm in the alarms path
        Firebase ref = mAlarmsRef.push();
        // Add the course to the alarms path
        ref.setValue(alarm);

        // Add the course to the owners path
//        Map<String, Object> map = new HashMap<>();
//        map.put(ref.getKey(), true);
//        // See https://www.firebase.com/docs/android/guide/saving-data.html for this method.
//        mOwnerRef.child(Owner.COURSES).updateChildren(map);
    }

    public void firebaseEdit(Alarm alarm) {
        Firebase courseNameRef = new Firebase(Constants.ALARMS_PATH + "/" + alarm.getKey());
        courseNameRef.setValue(alarm);
    }

    public void firebaseRemove(Alarm alarm) {
        mAlarmsRef.child(alarm.getKey()).removeValue();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private final TextView mUsage;
        private final TextView mName;
        private final Switch mSwitch;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnLongClickListener(this);
            mName = (TextView)itemView.findViewById(R.id.alarm_name);
            mSwitch = (Switch)itemView.findViewById(R.id.alarm_toggle);
            mUsage = (TextView)itemView.findViewById(R.id.device_usage);

            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Alarm alarm = mAlarms.get(getAdapterPosition());
                    alarm.setEnabled(isChecked);
                    firebaseEdit(alarm);
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            mAlarmsFragment.showAlarmDialog(mAlarms.get(getAdapterPosition()));
            return true;
        }
    }

    class AlarmsChildEventListener implements ChildEventListener {

        private void add(DataSnapshot dataSnapshot) {
            Alarm alarm = dataSnapshot.getValue(Alarm.class);
            alarm.setKey(dataSnapshot.getKey());
            mAlarms.add(alarm);
            Collections.sort(mAlarms);
        }

        private int remove(String key) {
            for (Alarm alarm : mAlarms) {
                if (alarm.getKey().equals(key)) {
                    int foundPos = mAlarms.indexOf(alarm);
                    mAlarms.remove(alarm);
                    return foundPos;
                }
            }
            return -1;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(Constants.TAG, "My course: " + dataSnapshot);
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
}
