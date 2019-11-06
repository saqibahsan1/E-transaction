package com.android.makeyousmile.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.makeyousmile.R;
import com.android.makeyousmile.ui.model.DeliveryBoy;
import com.android.makeyousmile.ui.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class DeliveryBoyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DeliveryBoy> items = new ArrayList<>();
    private Context context;

    public DeliveryBoyAdapter(Context context) {
        this.context = context;
    }


    public void setDeliveryBoy(List<DeliveryBoy> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_deliveryboy, parent, false);
        return new CustomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomeViewHolder customViewHolder = (CustomeViewHolder) holder;
        DeliveryBoy organization = items.get(position);
        customViewHolder.name.setText(organization.getName());
        customViewHolder.contact.setText(organization.getContactNumber());
        customViewHolder.addess.setText(organization.getAddress());
        customViewHolder.cnic.setText(organization.getCnic());
        customViewHolder.vehicle.setText(organization.getVechicle());
        customViewHolder.liecence.setText(organization.getLiecence());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private class CustomeViewHolder extends RecyclerView.ViewHolder {
        private TextView name, cnic, contact, addess, liecence, vehicle;

        CustomeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contactNumber);
            addess = itemView.findViewById(R.id.address);
            cnic = itemView.findViewById(R.id.cnic);
            liecence = itemView.findViewById(R.id.licence);
            vehicle = itemView.findViewById(R.id.vehicle);
        }
    }

}
