package edu.rosehulman.jungckjp_leekf.rosebandwidth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jonathan on 1/16/16.
 */
public class AlarmsAdapter  extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder>{
    private final Context mContext;
    private final RecyclerView mRecyclerView;

    private ArrayList<Alarm> mAlarms = new ArrayList<Alarm>();

    public AlarmsAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;

        Alarm testAlarm = new Alarm(500, true, "MB");
        mAlarms.add(testAlarm);
        testAlarm = new Alarm(4, false, "GB");
        mAlarms.add(testAlarm);
        testAlarm = new Alarm(273, true, "MB");
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
        holder.mUsage.setText(alarm.getAmount() + alarm.getType());
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mUsage;
        private final Switch mSwitch;

        public ViewHolder(View itemView) {
            super(itemView);

            mUsage = (TextView)itemView.findViewById(R.id.alarm_amount);
            mSwitch = (Switch)itemView.findViewById(R.id.alarm_toggle);
        }
    }
}
