package com.android.makeyousmile.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.makeyousmile.R;
import com.android.makeyousmile.ui.Utility.Utils;
import com.android.makeyousmile.ui.model.Donation;
import com.android.makeyousmile.ui.model.Organization;

import java.util.ArrayList;
import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Donation> items = new ArrayList<>();
    private Context context;

    public DonationAdapter(Context context) {
        this.context = context;
    }



    public void setDonation(List<Donation> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_donation, parent, false);
        return new CustomeViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomeViewHolder customViewHolder = (CustomeViewHolder) holder;
        Donation organization=items.get(position);

        if (!Utils.getInstance().getBoolean("isAdmin", context)){
            customViewHolder.orderName.setVisibility(View.GONE);
        }

        customViewHolder.name.setText(organization.getName());
        customViewHolder.contact.setText(organization.getContactNumber());
        customViewHolder.addess.setText(organization.getAddress());
        customViewHolder.foodtype.setText(organization.getFoodtype());
        customViewHolder.quantity.setText(organization.getQuantity());

        customViewHolder.orderName.setText("Donation By :"+ organization.getOrderName());

        customViewHolder.status.setText(organization.getStatus());

        if (organization.getStatus().equalsIgnoreCase("Pending")){
            customViewHolder.statusColor.setTextColor(Color.parseColor("#ffcc0000"));
            customViewHolder.status.setTextColor(Color.parseColor("#ffcc0000"));
        }else {
            customViewHolder.statusColor.setTextColor(Color.parseColor("#ff669900"));
            customViewHolder.status.setTextColor(Color.parseColor("#ff669900"));
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private class CustomeViewHolder extends RecyclerView.ViewHolder {
        private TextView name, foodtype, contact, addess, quantity,orderName,status,statusColor;

        CustomeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contactNumber);
            addess = itemView.findViewById(R.id.address);
            foodtype = itemView.findViewById(R.id.food);
            quantity = itemView.findViewById(R.id.quantity);
            orderName = itemView.findViewById(R.id.orderedName);
            status = itemView.findViewById(R.id.status);
            statusColor = itemView.findViewById(R.id.textColor);
        }
    }

}
