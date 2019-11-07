package com.android.makeyousmile.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.makeyousmile.R;
import com.android.makeyousmile.databinding.ActivityDonationBinding;
import com.android.makeyousmile.databinding.ActivityOrdersBinding;
import com.android.makeyousmile.ui.Utility.Utils;
import com.android.makeyousmile.ui.adapter.DonationAdapter;
import com.android.makeyousmile.ui.adapter.OrderAdapter;
import com.android.makeyousmile.ui.model.Donation;
import com.android.makeyousmile.ui.model.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    ActivityOrdersBinding binding;
    DatabaseReference myRef,myRefUser;
    private List<Orders> donationList = new ArrayList<>();
    private OrderAdapter mAdapter;
    private Orders orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myRef = FirebaseDatabase.getInstance().getReference("Orders");
        myRefUser = FirebaseDatabase.getInstance().getReference("UserOrders"+Utils.getInstance().getDefaults("userID",getApplicationContext()));
        initRecyclerView(binding.RecyclerView);
        if (Utils.getInstance().getBoolean("isAdmin", getApplicationContext())) {
            getData();
        }else {
            binding.RecyclerView.setVisibility(View.GONE);
            binding.addLayout.setVisibility(View.VISIBLE);
            binding.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orders = new Orders();
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
                    }else if (TextUtils.isEmpty(binding.payment.getText())) {
                        binding.payment.setError("Field is empty");
                        return;
                    }
                    orders.setName(binding.name.getText().toString());
                    orders.setAddress(binding.address.getText().toString());
                    orders.setContactNumber(binding.contactNumber.getText().toString());
                    orders.setQuantity(binding.quantity.getText().toString());
                    orders.setFoodtype(binding.food.getText().toString());
                    orders.setPayment(binding.payment.getText().toString());
                    String id = myRef.push().getKey();
                    if (id != null) {
                        myRef.child(id).setValue(orders);
                        myRefUser.child(id).setValue(orders);
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
                        binding.payment.setText("");
                        binding.payment.setText(null);
                        Toast.makeText(OrdersActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
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
                    getDataByUSer();
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
        mAdapter = new OrderAdapter(getApplicationContext());
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
                    Orders value = organization.getValue(Orders.class);
                    donationList.add(value);
                }
                mAdapter.setOrders(donationList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getDataByUSer() {
        binding.RecyclerView.setVisibility(View.VISIBLE);
        binding.addLayout.setVisibility(View.GONE);
        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                donationList.clear();
                for (DataSnapshot organization : dataSnapshot.getChildren()) {
                    Orders value = organization.getValue(Orders.class);
                    donationList.add(value);
                }
                mAdapter.setOrders(donationList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
