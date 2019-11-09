package com.android.makeyousmile.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.makeyousmile.R;
import com.android.makeyousmile.databinding.ActivityDeliveryBoyBinding;
import com.android.makeyousmile.databinding.ActivityDonationBinding;
import com.android.makeyousmile.ui.Utility.DonaationItemListner;
import com.android.makeyousmile.ui.Utility.Utils;
import com.android.makeyousmile.ui.adapter.DeliveryBoyAdapter;
import com.android.makeyousmile.ui.adapter.DonationAdapter;
import com.android.makeyousmile.ui.model.DeliveryBoy;
import com.android.makeyousmile.ui.model.Donation;
import com.android.makeyousmile.ui.model.Organization;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DonationActivity extends AppCompatActivity implements DonaationItemListner {

    ActivityDonationBinding binding;
    DatabaseReference myRef,myRefUser;
    private List<Donation> donationList = new ArrayList<>();
    private DonationAdapter mAdapter;
    private Donation donation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_donation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myRef = FirebaseDatabase.getInstance().getReference("Donation");
        myRefUser = FirebaseDatabase.getInstance().getReference("UserDonation");
        initRecyclerView(binding.RecyclerView);

        if (Utils.getInstance().getBoolean("isAdmin", getApplicationContext())) {
            getData();
        }else {
            binding.RecyclerView.setVisibility(View.GONE);
            binding.addLayout.setVisibility(View.VISIBLE);


            binding.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    donation = new Donation();
                    if (TextUtils.isEmpty(binding.name.getText())) {
                        binding.name.setError("Field is empty");
                        return;
                    }else if (TextUtils.isEmpty(binding.address.getText())) {
                        binding.address.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.contactNumber.getText())) {
                        binding.contactNumber.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.food.getText())) {
                        binding.food.setError("Field is empty");
                        return;
                    }else if (TextUtils.isEmpty(binding.quantity.getText())) {
                        binding.quantity.setError("Field is empty");
                        return;
                    }
                    donation.setName(binding.name.getText().toString());
                    donation.setAddress(binding.address.getText().toString());
                    donation.setContactNumber(binding.contactNumber.getText().toString());
                    donation.setQuantity(binding.quantity.getText().toString());
                    donation.setFoodtype(binding.food.getText().toString());
                    donation.setStatus("Pending");
                    donation.setToken(Utils.getInstance().getDefaults("token",getApplicationContext()));
                    donation.setOrderName(Utils.getInstance().getDefaults("userDisplayName",getApplicationContext()));
                    String id = myRef.push().getKey();
                    if (id != null) {
                        myRef.child(id).setValue(donation);
                        myRefUser.child(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext())).child(id).setValue(donation);
                        binding.quantity.setText("");
                        binding.quantity.setText(null);
                        binding.contactNumber.setText("");
                        binding.contactNumber.setText(null);
                        binding.address.setText("");
                        binding.address.setText(null);
                        binding.name.setText("");
                        binding.name.setText(null);
                        binding.food.setText("");
                        binding.food.setText(null);
                        Toast.makeText(DonationActivity.this, "Donation Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.RecyclerView.getVisibility() == View.VISIBLE){
                    binding.RecyclerView.setVisibility(View.GONE);
                    binding.addLayout.setVisibility(View.VISIBLE);
                }else {
                    binding.RecyclerView.setVisibility(View.VISIBLE);
                    binding.addLayout.setVisibility(View.GONE);
                    getDataBYUser();
                }


            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void initRecyclerView(RecyclerView recyclerViewLabel) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new DonationAdapter(getApplicationContext(),this);
        recyclerViewLabel.setLayoutManager(mLayoutManager);
        recyclerViewLabel.setAdapter(mAdapter);
    }


    private void getData() {
        binding.RecyclerView.setVisibility(View.VISIBLE);
        binding.addLayout.setVisibility(View.GONE);
        binding.fab.setVisibility(View.GONE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                donationList.clear();
                for (DataSnapshot organization : dataSnapshot.getChildren()) {
                    Donation value = organization.getValue(Donation.class);
                    value.setKey(organization.getKey());
                    donationList.add(value);
                }
                mAdapter.setDonation(donationList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getDataBYUser() {
        binding.RecyclerView.setVisibility(View.VISIBLE);
        binding.addLayout.setVisibility(View.GONE);
        myRefUser.child(Utils.getInstance().getDefaults("userDisplayName",getApplicationContext())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                donationList.clear();
                for (DataSnapshot organization : dataSnapshot.getChildren()) {
                    Donation value = organization.getValue(Donation.class);
                    donationList.add(value);
                }
                mAdapter.setDonation(donationList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onOrderItemClicked(Donation orders, String status) {
        Donation order = new Donation();
        order.setName(orders.getName());
        order.setAddress(orders.getAddress());
        order.setContactNumber(orders.getContactNumber());
        order.setQuantity(orders.getQuantity());
        order.setFoodtype(orders.getFoodtype());
        order.setStatus(status);
        order.setToken(Utils.getInstance().getDefaults("token", getApplicationContext()));
        order.setOrderName(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext()));
        myRef.child(orders.getKey()).setValue(order);
        myRefUser.child(orders.getOrderName()).child(orders.getKey()).setValue(order);
        mAdapter.notifyDataSetChanged();
    }
}
