package com.example.dripirrigationcontrol.ui.controlling;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dripirrigationcontrol.R;
import com.example.dripirrigationcontrol.ui.adapter.AdapterControlling;
import com.example.dripirrigationcontrol.ui.models.Solenoid;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ControllingFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AdapterControlling adapterControlling;
    List<Solenoid> listSolenoid = new ArrayList<>();

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("device1");

    private ControllingViewModel controllingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controllingViewModel =
                new ViewModelProvider(this).get(ControllingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_controlling, container, false);


        for (int i =0; i<20;i++){
            String solenoidName = "Solenoid Primer - " + (i+1);
            String firebaseKey = "TA" + (i+1);
            String index = String.valueOf(i+1);
            listSolenoid.add(new Solenoid(solenoidName,firebaseKey,index));
        }

        // final TextView textView = root.findViewById(R.id.text_dashboard);
        final Button waterPumpBtn = root.findViewById(R.id.waterPumpBtn);

        waterPumpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                controlWaterPumpBtn(databaseReference);
                Toast.makeText(v.getContext(), "Turn OFF water pump", Toast.LENGTH_SHORT).show();
            }
        });

        controllingViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);

            }
        });


        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.ryData);


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapterControlling = new AdapterControlling(getActivity(), listSolenoid);
        recyclerView.setAdapter(adapterControlling);




        // read from firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("data_firebase", "--" + dataSnapshot.child("CMD").getValue());

                    // get command
                    String raw_command = String.valueOf(dataSnapshot.child("CMD").getValue());

                    // split by
                    String[] split_command = raw_command.split(","); // {ON, WATER, 2}

                    // solenoid
                    // iterate over soil
                    DataSnapshot soilDataSnapshot = dataSnapshot.child("soil_humidity"); // get soil
                    for (DataSnapshot postSnapshot : soilDataSnapshot.getChildren()){
                        //Log.d("data_firebase", "onDataChange: " + postSnapshot);

                        // setState(postSnapshot.getKey(), split_command[1]); // set state
                    }


                    // need this to update to recycle view
                    // adapterControlling.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("data_firebase", "Failed to read value.", databaseError.toException());
            }
        });


        adapterControlling.notifyDataSetChanged();

    }

    private void controlWaterPumpBtn(final DatabaseReference updateData){
        updateData.child("CMD").setValue("OFF,OFF,20");
    }

    private void setState(String firebaseKey, String command){
        for (int i=0; i<20;i++){

            if (listSolenoid.get(i).getfirebaseKey().equals(firebaseKey)){
                //Log.d("data_firebase", "setPHbyFirebaseKey: " + firebaseKey);
                listSolenoid.get(i).setStatus(command);
            }
        }
    }
}
