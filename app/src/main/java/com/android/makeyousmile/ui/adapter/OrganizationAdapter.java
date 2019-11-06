package com.android.makeyousmile.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.makeyousmile.R;
import com.android.makeyousmile.ui.model.Organization;

import java.util.ArrayList;
import java.util.List;

public class OrganizationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Organization> items = new ArrayList<>();
    private Context context;

    public OrganizationAdapter(Context context) {
        this.context = context;
    }



    public void setOrganization(List<Organization> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_bill_row, parent, false);
        return new CustomeViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomeViewHolder customViewHolder = (CustomeViewHolder) holder;
        Organization organization=items.get(position);
        customViewHolder.name.setText(organization.getName());
//        customViewHolder.email.setText(organization.getEmail());
        customViewHolder.contact.setText(organization.getContactNumber());
        customViewHolder.addess.setText(organization.getAddress());
//        customViewHolder.cnic.setText(organization.getCnic());
        customViewHolder.speciality.setText(organization.getSpecilaity());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private class CustomeViewHolder extends RecyclerView.ViewHolder {
        private TextView name, email, contact, addess, cnic,speciality;

        CustomeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
//            email = itemView.findViewById(R.id.email);
            contact = itemView.findViewById(R.id.contactNumber);
            addess = itemView.findViewById(R.id.address);
//            cnic = itemView.findViewById(R.id.cnic);
            speciality = itemView.findViewById(R.id.specilaity);
        }
    }

}
