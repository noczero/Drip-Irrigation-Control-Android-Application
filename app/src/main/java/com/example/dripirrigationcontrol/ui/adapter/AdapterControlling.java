package com.example.dripirrigationcontrol.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dripirrigationcontrol.R;
import com.example.dripirrigationcontrol.ui.models.Solenoid;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterControlling extends RecyclerView.Adapter<AdapterControlling.HolderData> {
    List<Solenoid> listSolenoid;
    LayoutInflater inflater;
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("device1");


    public AdapterControlling(Context context, List<Solenoid> listSolenoid) {
        this.listSolenoid = listSolenoid;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdapterControlling.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.controlling_item, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterControlling.HolderData holder, int position) {
        holder.solenoidNameText.setText(listSolenoid.get(position).getName());
        holder.position = position;
        holder.firebaseKey = listSolenoid.get(position).getfirebaseKey();
        holder.solenoid = listSolenoid.get(position);
    }

    @Override
    public int getItemCount() {
        return listSolenoid.size();
    }


    public class HolderData extends RecyclerView.ViewHolder {
        TextView solenoidNameText;
        SwitchCompat wateringBtn;
        SwitchCompat treatmentBtn;
        Solenoid solenoid;
        int position;
        String firebaseKey;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            solenoidNameText = itemView.findViewById(R.id.solenoidNameText);
            wateringBtn = itemView.findViewById(R.id.wateringButton);
            treatmentBtn = itemView.findViewById(R.id.treatmentButton);


            wateringBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                checkAllWatering();
                Log.d("DEBUG_CONTROLLING", "FirebaseKey Watering Btn : " + firebaseKey);

                if (checkAllTreatment()) {
                    Toast.makeText(itemView.getContext(), "Failed watering, treatment still running", Toast.LENGTH_SHORT).show();
                    wateringBtn.setChecked(false);
                } else {
                    // check status btn
                    if (isChecked) {
                        // set ke false
                        wateringBtn.getTextOn().toString();
                        wateringBtn.setTextOn("On");
                        controlOnlyWatering(databaseReference, solenoid.getIndex());
                        treatmentBtn.setEnabled(false);
                        solenoid.setStatus("WATERING");
                        Toast.makeText(itemView.getContext(), "Start watering", Toast.LENGTH_SHORT).show();
                    } else {
                        wateringBtn.getTextOff().toString();
                        controlOffWater(databaseReference, solenoid.getIndex());
                        treatmentBtn.setEnabled(true);
                        solenoid.setStatus("OFF");
                        Toast.makeText(itemView.getContext(), "Stop watering", Toast.LENGTH_SHORT).show();
                    }
                }


            });

            treatmentBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                checkAllWatering();
                Log.d("DEBUG_CONTROLLING", "FirebaseKey Treatment Btn : " + solenoid.getIndex());
                if (checkAllWatering()) {
                    Toast.makeText(itemView.getContext(), "Failed treatment, watering still running", Toast.LENGTH_SHORT).show();
                    treatmentBtn.setChecked(false);
                } else {
                    // check status btn
                    if (isChecked) {
                        // set ke false
                        treatmentBtn.getTextOn().toString();
                        treatmentBtn.setTextOn("On");
                        controlOnlyTreatment(databaseReference, solenoid.getIndex());
                        wateringBtn.setEnabled(false);
                        solenoid.setStatus("TREATMENT");
                        Toast.makeText(itemView.getContext(), "Start treatment", Toast.LENGTH_SHORT).show();

                    } else {
                        treatmentBtn.getTextOff().toString();
                        controlOffTreat(databaseReference, solenoid.getIndex());
                        wateringBtn.setEnabled(true);
                        solenoid.setStatus("OFF");
                        Toast.makeText(itemView.getContext(), "Stop treatment", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    void controlOnlyWatering(DatabaseReference databaseReference, String solenoidID) {
        databaseReference.child("CMD").setValue("ON,WATER," + solenoidID); // turn on
    }

    void controlOnlyTreatment(DatabaseReference databaseReference, String solenoidID) {
        databaseReference.child("CMD").setValue("ON,TREAT," + solenoidID); // turn on

    }

    void controlOffTreat(DatabaseReference databaseReference, String solenoidID) {
        databaseReference.child("CMD").setValue("OFF,TREAT," + solenoidID); // turn off
    }

    void controlOffWater(DatabaseReference databaseReference, String solenoidID) {
        databaseReference.child("CMD").setValue("OFF,WATER," + solenoidID); // turn off
    }


    boolean checkAllWatering() {
        for (int i = 0; i < listSolenoid.size(); i++) {
            //Log.d("DEBUG_CONTROLLING", "Status: " + listSolenoid.get(i).getStatus() + ",index:" +i );
            String status = listSolenoid.get(i).getStatus();



            if (status != null && status.equals("WATERING")) {
                return true;
            }
        }

        return false;
    }


    boolean checkAllTreatment() {
        for (int i = 0; i < listSolenoid.size(); i++) {
            //Log.d("DEBUG_CONTROLLING", "Status: " + listSolenoid.get(i).getStatus() + ",index:" +i );
            String status = listSolenoid.get(i).getStatus();


            if (status != null && status.equals("TREATMENT")) {
                return true;
            }
        }

        return false;
    }

}
