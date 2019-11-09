package com.android.makeyousmile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.makeyousmile.R;
import com.android.makeyousmile.databinding.ActivityOrdersBinding;
import com.android.makeyousmile.ui.Utility.OrderItemListner;
import com.android.makeyousmile.ui.Utility.Utils;
import com.android.makeyousmile.ui.adapter.OrderAdapter;
import com.android.makeyousmile.ui.model.Orders;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrdersActivity extends AppCompatActivity implements OrderItemListner {

    ActivityOrdersBinding binding;
    DatabaseReference myRef, myRefUser;
    private List<Orders> donationList = new ArrayList<>();
    private OrderAdapter mAdapter;
    private Orders orders;
    private String id;
    private int requestCode = 2;
    PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myRef = FirebaseDatabase.getInstance().getReference("Orders");
        myRefUser = FirebaseDatabase.getInstance().getReference("UserOrders");
        initRecyclerView(binding.RecyclerView);

        if (Utils.getInstance().getBoolean("isAdmin", getApplicationContext())) {
            getData();
        } else {
            binding.RecyclerView.setVisibility(View.GONE);
            binding.addLayout.setVisibility(View.VISIBLE);
            binding.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orders = new Orders();
                    if (TextUtils.isEmpty(binding.name.getText())) {
                        binding.name.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.addressText.getText())) {
                        binding.addressText.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.contactNumber.getText())) {
                        binding.contactNumber.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.food.getText())) {
                        binding.food.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.quantity.getText())) {
                        binding.quantity.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.payment.getText())) {
                        binding.payment.setError("Field is empty");
                        return;
                    }
                    Utils.getInstance().ShowProgress(OrdersActivity.this);
                    orders.setName(binding.name.getText().toString());
                    orders.setAddress(binding.addressText.getText().toString());
                    orders.setContactNumber(binding.contactNumber.getText().toString());
                    orders.setQuantity(binding.quantity.getText().toString());
                    orders.setFoodtype(binding.food.getText().toString());
                    orders.setPayment(binding.payment.getText().toString());
                    orders.setStatus("Pending");
                    orders.setToken(Utils.getInstance().getDefaults("token", getApplicationContext()));
                    orders.setOrderName(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext()));
                    id = myRef.push().getKey();
                    if (id != null) {
                        myRef.child(id).setValue(orders);
                        myRefUser.child(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext())).child(id).setValue(orders);
                        binding.quantity.setText("");
                        binding.quantity.setText(null);
                        binding.contactNumber.setText("");
                        binding.contactNumber.setText(null);
                        binding.addressText.setText("");
                        binding.addressText.setText(null);
                        binding.name.setText("");
                        binding.name.setText(null);
                        binding.food.setText("");
                        binding.food.setText(null);
                        binding.payment.setText("");
                        binding.payment.setText(null);
                        Toast.makeText(OrdersActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        Utils.getInstance().HideProgress();
                    }
                }
            });

        }


//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                getLastIndexValue();
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.RecyclerView.getVisibility() == View.VISIBLE) {
                    binding.RecyclerView.setVisibility(View.GONE);
                    binding.addLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.RecyclerView.setVisibility(View.VISIBLE);
                    binding.addLayout.setVisibility(View.GONE);
                    getDataByUSer();
                }
            }
        });

        binding.addressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);
                Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placeFields)
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setLocationRestriction(RectangularBounds.newInstance(
                                new LatLng(24.926294, 67.022095),
                                new LatLng(30.3753, 69.3451)))
                        .setCountry("pakistan")
                        .build(OrdersActivity.this);

                startActivityForResult(autocompleteIntent, requestCode);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (resultCode == AutocompleteActivity.RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(intent);
            place.getName();
            place.getAddress();
            binding.addressText.setText(place.getAddress());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(intent);
        } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
            // The user canceled the operation.
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

//    private void getLastIndexValue() {
//        Query lastQuery = myRef.orderByKey().limitToLast(1);
//        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Orders value;
//                donationList.clear();
//                for (DataSnapshot organization : dataSnapshot.getChildren()) {
//                    value = organization.getValue(Orders.class);
//                    donationList.add(value);
//                    mAdapter.setOrders(donationList);
//                    mAdapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle possible errors.
//            }
//        });
//
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView(RecyclerView recyclerViewLabel) {
        // Initialize Places.

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDY49VZ7edqENmoWEubZ8_NmqmXP2LvPZI");
        }

        placesClient = Places.createClient(this);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new OrderAdapter(getApplicationContext(), this);
        recyclerViewLabel.setLayoutManager(mLayoutManager);
        recyclerViewLabel.setAdapter(mAdapter);
    }


    private void getData() {
        Utils.getInstance().ShowProgress(OrdersActivity.this);
        binding.RecyclerView.setVisibility(View.VISIBLE);
        binding.addLayout.setVisibility(View.GONE);
        binding.fab.setVisibility(View.GONE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                donationList.clear();
                for (DataSnapshot organization : dataSnapshot.getChildren()) {
                    Orders value = organization.getValue(Orders.class);
                    value.setKey(organization.getKey());
                    donationList.add(value);
                }
                mAdapter.setOrders(donationList);
                mAdapter.notifyDataSetChanged();
                Utils.getInstance().HideProgress();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Utils.getInstance().HideProgress();
            }
        });

    }


    private void getDataByUSer() {
        Utils.getInstance().ShowProgress(OrdersActivity.this);
        binding.RecyclerView.setVisibility(View.VISIBLE);
        binding.addLayout.setVisibility(View.GONE);
        myRefUser.child(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                donationList.clear();
                for (DataSnapshot organization : dataSnapshot.getChildren()) {
                    Orders value = organization.getValue(Orders.class);
                    donationList.add(value);
                }
                mAdapter.setOrders(donationList);
                mAdapter.notifyDataSetChanged();
                Utils.getInstance().HideProgress();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Utils.getInstance().HideProgress();
            }
        });

    }


    @Override
    public void onOrderItemClicked(final Orders orders, final String status) {
        Orders order = new Orders();
        order.setName(orders.getName());
        order.setAddress(orders.getAddress());
        order.setContactNumber(orders.getContactNumber());
        order.setQuantity(orders.getQuantity());
        order.setFoodtype(orders.getFoodtype());
        order.setPayment(orders.getPayment());
        order.setStatus(status);
        order.setToken(Utils.getInstance().getDefaults("token", getApplicationContext()));
        order.setOrderName(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext()));
        myRef.child(orders.getKey()).setValue(order);
            myRefUser.child(orders.getOrderName()).child(orders.getKey()).setValue(order);
        mAdapter.notifyDataSetChanged();
    }

}
