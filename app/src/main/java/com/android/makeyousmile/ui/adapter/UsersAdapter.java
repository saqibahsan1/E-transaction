package com.android.makeyousmile.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.makeyousmile.R;
import com.android.makeyousmile.ui.Utility.UserItemListener;
import com.android.makeyousmile.ui.Utility.Utils;
import com.android.makeyousmile.ui.model.UsersModel;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UsersModel> items = new ArrayList<>();
    private Context context;
    UserItemListener userItemListener;

    public UsersAdapter(Context context, UserItemListener userItemListener) {
        this.context = context;
        this.userItemListener = userItemListener;
    }



    public void setOrders(List<UsersModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_order, parent, false);
        return new CustomeViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        CustomeViewHolder customViewHolder = (CustomeViewHolder) holder;
        UsersModel organization=items.get(position);

        if (!Utils.getInstance().getBoolean("isAdmin", context)){
            customViewHolder.orderName.setVisibility(View.GONE);
            customViewHolder.acceptBtn.setVisibility(View.GONE);
            customViewHolder.rejectBtn.setVisibility(View.GONE);
        }
        if (organization.getStatus().equals("accepted") || organization.getStatus().equals("rejected")){
            customViewHolder.acceptBtn.setVisibility(View.GONE);
            customViewHolder.rejectBtn.setVisibility(View.GONE);
        }else {
            if (Utils.getInstance().getBoolean("isAdmin", context)) {
                customViewHolder.acceptBtn.setVisibility(View.VISIBLE);
                customViewHolder.rejectBtn.setVisibility(View.VISIBLE);
            }
        }

        customViewHolder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userItemListener.onOrderItemClicked(items.get(position),"accepted");
            }
        });
        customViewHolder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userItemListener.onOrderItemClicked(items.get(position),"rejected");
            }
        });

        customViewHolder.name.setText(organization.getName());
        customViewHolder.contact.setText(organization.getContactNumber());
        customViewHolder.addess.setText(organization.getAddress());
        customViewHolder.foodtype.setText(organization.getBitcoinBalance());
        customViewHolder.quantity.setText(organization.getEthereumBalance());
        customViewHolder.payment.setText(organization.getPayment());
        customViewHolder.orderName.setText("Requested User "+organization.getOrderName());
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
        private TextView name, foodtype, contact, addess, quantity,payment,orderName,status,statusColor;
        Button acceptBtn,rejectBtn;

        CustomeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contactNumber);
            addess = itemView.findViewById(R.id.address);
            foodtype = itemView.findViewById(R.id.bitcoinBalanance);
            quantity = itemView.findViewById(R.id.setAmount);
            payment = itemView.findViewById(R.id.payment);
            orderName = itemView.findViewById(R.id.orderedName);
            status = itemView.findViewById(R.id.status);
            statusColor = itemView.findViewById(R.id.textColor);
            acceptBtn = itemView.findViewById(R.id.accept);
            rejectBtn = itemView.findViewById(R.id.reject);
        }
    }

}
