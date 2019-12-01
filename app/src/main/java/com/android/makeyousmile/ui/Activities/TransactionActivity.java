package com.android.makeyousmile.ui.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.makeyousmile.R;
import com.android.makeyousmile.databinding.ActivityDonationBinding;
import com.android.makeyousmile.ui.Utility.DonaationItemListner;
import com.android.makeyousmile.ui.Utility.Utils;
import com.android.makeyousmile.ui.adapter.DonationAdapter;
import com.android.makeyousmile.ui.model.Donation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import java.util.ArrayList;
import java.util.List;

public class DonationActivity extends AppCompatActivity implements DonaationItemListner {

    ActivityDonationBinding binding;
    DatabaseReference myRef,myRefUser;
    private List<Donation> donationList = new ArrayList<>();
    private DonationAdapter mAdapter;
    private Donation donation;
    private int requestCodeAddress = 3;


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
                    } else if (TextUtils.isEmpty(binding.bitcoinBalanance.getText())) {
                        binding.bitcoinBalanance.setError("Field is empty");
                        return;
                    }else if (TextUtils.isEmpty(binding.setAmount.getText())) {
                        binding.setAmount.setError("Field is empty");
                        return;
                    }
                    donation.setName(binding.name.getText().toString());
                    donation.setAddress(binding.address.getText().toString());
                    donation.setContactNumber(binding.contactNumber.getText().toString());
                    donation.setSetAmount(binding.setAmount.getText().toString());
                    donation.setCurrencyType(binding.bitcoinBalanance.getText().toString());
                    donation.setStatus("Pending");
                    donation.setToken(Utils.getInstance().getDefaults("token",getApplicationContext()));
                    donation.setOrderName(Utils.getInstance().getDefaults("userDisplayName",getApplicationContext()));
                    String id = myRef.push().getKey();
                    if (id != null) {
                        myRef.child(id).setValue(donation);
                        myRefUser.child(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext())).child(id).setValue(donation);
                        binding.setAmount.setText("");
                        binding.setAmount.setText(null);
                        binding.contactNumber.setText("");
                        binding.contactNumber.setText(null);
                        binding.address.setText("");
                        binding.address.setText(null);
                        binding.name.setText("");
                        binding.name.setText(null);
                        binding.bitcoinBalanance.setText("");
                        binding.bitcoinBalanance.setText(null);
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

        binding.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonationActivity.this, LocationPickerActivity.class);
                startActivityForResult(intent, requestCodeAddress);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeAddress) {

            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    String address = data.getStringExtra(MapUtility.ADDRESS);
                    binding.address.setText(address);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
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
        Utils.getInstance().ShowProgress(DonationActivity.this);
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
                Utils.getInstance().HideProgress();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getDataBYUser() {
        Utils.getInstance().ShowProgress(DonationActivity.this);
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
                Utils.getInstance().HideProgress();
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
        order.setSetAmount(orders.getSetAmount());
        order.setCurrencyType(orders.getCurrencyType());
        order.setStatus(status);
        order.setToken(Utils.getInstance().getDefaults("token", getApplicationContext()));
        order.setOrderName(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext()));
        myRef.child(orders.getKey()).setValue(order);
        myRefUser.child(orders.getOrderName()).child(orders.getKey()).setValue(order);
        mAdapter.notifyDataSetChanged();
    }
}
