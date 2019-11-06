package com.android.makeyousmile.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.makeyousmile.R;
import com.android.makeyousmile.databinding.ActivityDeliveryBoyBinding;
import com.android.makeyousmile.ui.Utility.Utils;
import com.android.makeyousmile.ui.adapter.DeliveryBoyAdapter;
import com.android.makeyousmile.ui.adapter.RestaurantAdapter;
import com.android.makeyousmile.ui.model.DeliveryBoy;
import com.android.makeyousmile.ui.model.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryBoyActivity extends AppCompatActivity {

    ActivityDeliveryBoyBinding binding;
    DatabaseReference myRef;
    private DeliveryBoy organization;
    private List<DeliveryBoy> deliveryBoyList = new ArrayList<>();
    private DeliveryBoyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_boy);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myRef = FirebaseDatabase.getInstance().getReference("DeliveryBoy");
        initRecyclerView(binding.RecyclerView);

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                organization = new DeliveryBoy();
                if (TextUtils.isEmpty(binding.name.getText())) {
                    binding.name.setError("Field is empty");
                    return;
                } else if (TextUtils.isEmpty(binding.email.getText())) {
                    binding.email.setError("Field is empty");
                    return;
                } else if (TextUtils.isEmpty(binding.address.getText())) {
                    binding.address.setError("Field is empty");
                    return;
                } else if (TextUtils.isEmpty(binding.cnic.getText())) {
                    binding.cnic.setError("Field is empty");
                    return;
                } else if (TextUtils.isEmpty(binding.contactNumber.getText())) {
                    binding.contactNumber.setError("Field is empty");
                    return;
                } else if (TextUtils.isEmpty(binding.licence.getText())) {
                    binding.licence.setError("Field is empty");
                    return;
                }else if (TextUtils.isEmpty(binding.vehicle.getText())) {
                    binding.vehicle.setError("Field is empty");
                    return;
                }
                organization.setName(binding.name.getText().toString());
                organization.setEmail(binding.email.getText().toString());
                organization.setAddress(binding.address.getText().toString());
                organization.setCnic(binding.cnic.getText().toString());
                organization.setContactNumber(binding.contactNumber.getText().toString());
                organization.setLiecence(binding.licence.getText().toString());
                organization.setVechicle(binding.vehicle.getText().toString());
                String id = myRef.push().getKey();

                if (id != null) {
                    myRef.child(id).setValue(organization);
                    binding.email.setText("");
                    binding.email.setText(null);
                    binding.licence.setText("");
                    binding.licence.setText(null);
                    binding.vehicle.setText("");
                    binding.vehicle.setText(null);
                    binding.contactNumber.setText("");
                    binding.contactNumber.setText(null);
                    binding.address.setText("");
                    binding.address.setText(null);
                    binding.name.setText("");
                    binding.name.setText(null);
                    binding.cnic.setText("");
                    binding.cnic.setText(null);
                }

            }
        });



        getData();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.RecyclerView.getVisibility() == View.VISIBLE){
                    binding.RecyclerView.setVisibility(View.GONE);
                    binding.addLayout.setVisibility(View.VISIBLE);
                }else {
                    binding.RecyclerView.setVisibility(View.VISIBLE);
                    binding.addLayout.setVisibility(View.GONE);
                    getData();
                }
            }
        });

    }


    private void initRecyclerView(RecyclerView recyclerViewLabel) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new DeliveryBoyAdapter(getApplicationContext());
        recyclerViewLabel.setLayoutManager(mLayoutManager);
        recyclerViewLabel.setAdapter(mAdapter);
    }

    private void getData() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deliveryBoyList.clear();
                for (DataSnapshot organization : dataSnapshot.getChildren()) {
                    DeliveryBoy value = organization.getValue(DeliveryBoy.class);
                    deliveryBoyList.add(value);
                }
                mAdapter.setDeliveryBoy(deliveryBoyList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
}
