package edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.R;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.MainActivity;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.adapters.AlarmsAdapter;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Alarm;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.API;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmsFragment extends Fragment {
    public AlarmsAdapter mAdapter;
    private API mAPI;

    public AlarmsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView recyclerView = (RecyclerView)inflater.inflate(R.layout.fragment_alerts, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        try {
            mAPI = API.getInstance((MainActivity) getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }


        FloatingActionButton fab = ((MainActivity) getActivity()).getFab();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlarmDialog(null);
            }
        });
        fab.setVisibility(View.VISIBLE);

        mAdapter = new AlarmsAdapter(getContext(), recyclerView, this);
        recyclerView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return recyclerView;
    }

    @SuppressLint("InflateParams")
    public void showAlarmDialog(final Alarm alarm) {

        DialogFragment df = new DialogFragment() {
            @Override
            @NonNull
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(alarm == null ? R.string.dialog_alarm_add_title : R.string.dialog_alarm_edit_title));
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_alarm_add_edit, null, false);
                builder.setView(view);
                final EditText alarmNameEditText = (EditText) view.findViewById(R.id.dialog_alarm_name);
                final EditText alarmAmountEditText = (EditText) view.findViewById(R.id.dialog_alarm_amount);
                final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.usage_type_buttons);

                final RadioButton percentButton = (RadioButton) view.findViewById(R.id.percent_button);
                final RadioButton mbButton = (RadioButton) view.findViewById(R.id.mb_button);
                final RadioButton gbButton = (RadioButton) view.findViewById(R.id.gb_button);


                if (alarm != null) {
                    alarmNameEditText.setText(alarm.getName());
                    alarmAmountEditText.setText(String.valueOf(alarm.getAmount()));
                    if(alarm.getType() == 0){
                        percentButton.setChecked(true);
                    }else if(alarm.getType() == 1){
                        mbButton.setChecked(true);
                    }else if(alarm.getType() == 2) {
                        gbButton.setChecked(true);
                    }
                }else{
                    percentButton.setChecked(true);
                }

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String alarmName = alarmNameEditText.getText().toString();
                        if(alarmName.equals("")){
                            alarmName = "New Alarm";
                        }

                        float alarmAmount = 0;
                        if(!alarmAmountEditText.getText().toString().equals("")){
                            alarmAmount = Float.parseFloat(alarmAmountEditText.getText().toString());
                        }

                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        int type = 0;

                        if(selectedId == percentButton.getId()){
                            type = 0;
                        } else if(selectedId == mbButton.getId()){
                            type = 1;
                        } else if(selectedId == gbButton.getId()){
                            type = 2;
                        }

                        if (alarm == null) {
                            Alarm alarm1 = new Alarm(alarmName, alarmAmount, true, type, mAPI.getCurrentUser());
                            Log.d(Constants.TAG, alarm1.getName());
                            Log.d(Constants.TAG, alarm1.getAmount() + " " + alarm1.getType() + " " + alarm1.getUser() + " " + alarm1.isEnabled());
                            mAdapter.firebasePush(alarm1);
                        } else {
                            alarm.setName(alarmName);
                            alarm.setAmount(alarmAmount);
                            alarm.setEnabled(true);
                            alarm.setType(type);
                            mAdapter.firebaseEdit(alarm);
                        }
                        dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                if (alarm != null) {
                    builder.setNeutralButton(R.string.remove, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showDeleteConfirmationDialog(alarm);
                        }
                    });
                }
                return builder.create();
            }
        };
        df.show(getActivity().getSupportFragmentManager(), "add_edit_assignment");
    }

    private void showDeleteConfirmationDialog(final Alarm alarm) {
        DialogFragment df = new DialogFragment() {
            @Override
            @NonNull
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.remove_question_format, alarm.getName()));
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.firebaseRemove(alarm);
                        dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                return builder.create();
            }
        };
        df.show(getActivity().getSupportFragmentManager(), "confirm");
    }
}
