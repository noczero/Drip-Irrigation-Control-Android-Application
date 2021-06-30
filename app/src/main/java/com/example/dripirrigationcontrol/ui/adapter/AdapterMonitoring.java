package com.example.dripirrigationcontrol.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dripirrigationcontrol.R;
import com.example.dripirrigationcontrol.ui.models.TalanganAir;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterMonitoring extends RecyclerView.Adapter<AdapterMonitoring.HolderData> {
    List<TalanganAir> listTalanganAir;
    LayoutInflater inflater;



    public AdapterMonitoring(Context context, List<TalanganAir> listTalanganAir) {
        this.listTalanganAir = listTalanganAir;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdapterMonitoring.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.talangan_air_item, parent, false);
        return new AdapterMonitoring.HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMonitoring.HolderData holder, int position) {
        String name = listTalanganAir.get(position).getName();
        holder.TANameTxt.setText(name);

        String soilHumidity = listTalanganAir.get(position).getSoilHumidity();
        holder.soilHumidityTxt.setText(soilHumidity);

        double phValue = listTalanganAir.get(position).getPhValue();
        holder.phValueTxt.setText(String.valueOf(phValue));

        String solenoidStatus = listTalanganAir.get(position).getSolenoidStatus();
        holder.solenoidStatusTxt.setText(solenoidStatus);

        //String statusPenyiraman = listTalanganAir.get(position).getStatusWatering();
        //holder.statusPenyiramanTxt.setText(statusPenyiraman);

        holder.position = position;
        holder.firebaseKey = listTalanganAir.get(position).getFirebaseKey();
    }

    @Override
    public int getItemCount() {
        return listTalanganAir.size();
    }


    public class HolderData extends RecyclerView.ViewHolder {
        TextView TANameTxt;
        TextView soilHumidityTxt;
        TextView phValueTxt;
        TextView solenoidStatusTxt;
        //TextView statusPenyiramanTxt;
        int position;
        String firebaseKey;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            TANameTxt = itemView.findViewById(R.id.talanganAirName);
            soilHumidityTxt = itemView.findViewById(R.id.soilHumidity);
            phValueTxt = itemView.findViewById(R.id.phValue);
            solenoidStatusTxt = itemView.findViewById(R.id.solenoidStatus);
            //statusPenyiramanTxt = itemView.findViewById(R.id.wateringStatus);
        }
    }



}
