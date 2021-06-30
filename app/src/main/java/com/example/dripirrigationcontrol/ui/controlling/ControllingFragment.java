package com.example.dripirrigationcontrol.ui.controlling;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dripirrigationcontrol.R;
import com.example.dripirrigationcontrol.ui.adapter.AdapterControlling;
import com.example.dripirrigationcontrol.ui.models.Solenoid;

import java.util.ArrayList;
import java.util.List;

public class ControllingFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AdapterControlling adapterControlling;
    List<Solenoid> listSolenoid;

    private ControllingViewModel controllingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controllingViewModel =
                new ViewModelProvider(this).get(ControllingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_controlling, container, false);

        // final TextView textView = root.findViewById(R.id.text_dashboard);
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

        listSolenoid = new ArrayList<>();

        for (int i =0; i<20;i++){
            String solenoidName = "Solenoid Primer - " + (i+1);
            String firebaseKey = "TA" + (i+1);
            String index = String.valueOf(i+1);
            listSolenoid.add(new Solenoid(solenoidName,firebaseKey,index));
        }

        // OFF Command
        // listSolenoid.add(new Solenoid("Pompa Air", "water_pump", "22"));

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapterControlling = new AdapterControlling(getActivity(), listSolenoid);
        recyclerView.setAdapter(adapterControlling);
        adapterControlling.notifyDataSetChanged();

        // or  (ImageView) view.findViewById(R.id.foo);
    }
}
