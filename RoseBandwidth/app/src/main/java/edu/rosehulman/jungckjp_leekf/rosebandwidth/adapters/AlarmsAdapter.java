package edu.rosehulman.jungckjp_leekf.rosebandwidth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.R;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments.AlarmsFragment;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Alarm;

/**
 * Created by jonathan on 1/16/16.
 */
public class AlarmsAdapter  extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder>{
    private final Context mContext;
    private final RecyclerView mRecyclerView;
    private AlarmsFragment mAlarmsFragment;

    private ArrayList<Alarm> mAlarms = new ArrayList<Alarm>();

    public AlarmsAdapter(Context context, RecyclerView recyclerView, AlarmsFragment alarmsFragment) {
        mContext = context;
        mRecyclerView = recyclerView;
        mAlarmsFragment = alarmsFragment;

        Alarm testAlarm = new Alarm("Alarm 1", 500, true, 1);
        mAlarms.add(testAlarm);
        testAlarm = new Alarm("Alarm 2", 4, false, 2);
        mAlarms.add(testAlarm);
        testAlarm = new Alarm("Alarm 3", 273, true, 0);
        mAlarms.add(testAlarm);
        notifyDataSetChanged();
    }

    @Override
    public AlarmsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmsAdapter.ViewHolder holder, int position) {
        Alarm alarm = mAlarms.get(position);
        holder.mSwitch.setChecked(alarm.isEnabled());
//        holder.mSwitch.setEnabled(alarm.isEnabled());
        holder.mUsage.setText(alarm.getAmount() + alarm.getTypeString());
        holder.mName.setText(alarm.getName());
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
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
        }

        @Override
        public boolean onLongClick(View v) {
            mAlarmsFragment.showAlarmDialog(mAlarms.get(getAdapterPosition()));
            return true;
        }
    }
}
