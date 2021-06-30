package com.example.dripirrigationcontrol.ui.monitoring;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dripirrigationcontrol.R;
import com.example.dripirrigationcontrol.ui.adapter.AdapterControlling;
import com.example.dripirrigationcontrol.ui.adapter.AdapterMonitoring;
import com.example.dripirrigationcontrol.ui.models.Solenoid;
import com.example.dripirrigationcontrol.ui.models.TalanganAir;
import com.example.dripirrigationcontrol.ui.monitoring.MonitoringViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MonitoringFragment extends Fragment {
    ArrayList<TalanganAir> arrayOfTA = new ArrayList<>();



    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("device1");

    private MonitoringViewModel monitoringViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        monitoringViewModel =
                new ViewModelProvider(this).get(MonitoringViewModel.class);
        View root = inflater.inflate(R.layout.fragment_monitoring, container, false);
        final TextView gardenHumidityTxt = root.findViewById(R.id.gardenHumidity);
        final TextView gardenTemperatureTxt = root.findViewById(R.id.gardenTemperature);

        monitoringViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        // create TA object
        for (int i=0; i<20;i++){
            String name = "Talangan Air - " + (i+1);
            String firebaseKey = "TA" + (i+1);
            arrayOfTA.add(new TalanganAir(name,"LOW",0,"ON", firebaseKey));
        }

        // read from firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("data_firebase", "--" + dataSnapshot.child("CMD").getValue());

                    // set data to text view
                    gardenHumidityTxt.setText(String.format("%.2f", (double) dataSnapshot.child("humidity").getValue()));
                    gardenTemperatureTxt.setText(String.format("%.2f", (double) dataSnapshot.child("temperature").getValue()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("data_firebase", "Failed to read value.", databaseError.toException());
            }
        });


        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {



        // setup recyclerview
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycleTA);

        AdapterMonitoring adapterMonitoring = new AdapterMonitoring(getActivity(), arrayOfTA);

        recyclerView.setAdapter(adapterMonitoring);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2,
                GridLayoutManager.VERTICAL, false); // set jadi 2 kolom
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setClipToPadding(false);

        // read from firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("data_firebase", "--" + dataSnapshot.child("CMD").getValue());


                    // iterate over ph
                    DataSnapshot phDataSnapshot = dataSnapshot.child("ph"); // get ph
                    for (DataSnapshot postSnapshot : phDataSnapshot.getChildren()){
                        //Log.d("data_firebase", "onDataChange: " + postSnapshot);

                        setPHbyFirebaseKey(postSnapshot.getKey(), (long) postSnapshot.getValue());
                    }


                    // iterate over soil
                    DataSnapshot soilDataSnapshot = dataSnapshot.child("soil_humidity"); // get soil
                    for (DataSnapshot postSnapshot : soilDataSnapshot.getChildren()){
                        //Log.d("data_firebase", "onDataChange: " + postSnapshot);

                        setSoilHumidityByFirebaseKey(postSnapshot.getKey(), String.valueOf(postSnapshot.getValue()));
                    }

                    // iterate over solenoid
                    DataSnapshot solenoidDataSnapshot = dataSnapshot.child("solenoid"); // get solenoid
                    for (DataSnapshot postSnapshot : solenoidDataSnapshot.getChildren()){
                        //Log.d("data_firebase", "onDataChange: " + postSnapshot);

                        setSolenoidByFirebaseKey(postSnapshot.getKey(), String.valueOf(postSnapshot.getValue()));
                    }


                    // need this to update to recycle view
                    adapterMonitoring.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("data_firebase", "Failed to read value.", databaseError.toException());
            }
        });

        // adapterMonitoring.notifyDataSetChanged();

    }

    private void setPHbyFirebaseKey(String firebaseKey, long phValue){
        for (int i=0; i<20;i++){

            if (arrayOfTA.get(i).getFirebaseKey().equals(firebaseKey)){
                //Log.d("data_firebase", "setPHbyFirebaseKey: " + firebaseKey);
                arrayOfTA.get(i).setPhValue(phValue);
            }
        }
    }


    private void setSoilHumidityByFirebaseKey(String firebaseKey, String soilHumidity){
        for (int i=0; i<20;i++){

            if (arrayOfTA.get(i).getFirebaseKey().equals(firebaseKey)){
                //Log.d("data_firebase", "setPHbyFirebaseKey: " + firebaseKey);
                arrayOfTA.get(i).setSoilHumidity(soilHumidity);
            }
        }
    }

    private void setSolenoidByFirebaseKey(String firebaseKey, String solenoidStatus){
        for (int i=0; i<20;i++){

            if (arrayOfTA.get(i).getFirebaseKey().equals(firebaseKey)){
                //Log.d("data_firebase", "setPHbyFirebaseKey: " + firebaseKey);
                arrayOfTA.get(i).setSolenoidStatus(solenoidStatus);
            }
        }
    }
}
