package com.android.makeyousmile.ui.Activities;

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
import com.android.makeyousmile.ui.Utility.UserItemListener;
import com.android.makeyousmile.ui.Utility.Utils;
import com.android.makeyousmile.ui.adapter.UsersAdapter;
import com.android.makeyousmile.ui.model.UsersModel;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserItemListener {

    ActivityOrdersBinding binding;
    DatabaseReference myRef, myRefUser;
    private List<UsersModel> donationList = new ArrayList<>();
    private UsersAdapter mAdapter;
    private UsersModel usersModel;
    private String id;
    private int requestCodeAddress = 2;
    PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myRef = FirebaseDatabase.getInstance().getReference("UsersModel");
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
                    usersModel = new UsersModel();
                    if (TextUtils.isEmpty(binding.name.getText())) {
                        binding.name.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.addressText.getText())) {
                        binding.addressText.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.contactNumber.getText())) {
                        binding.contactNumber.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.bitcoinBalanance.getText())) {
                        binding.bitcoinBalanance.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.setAmount.getText())) {
                        binding.setAmount.setError("Field is empty");
                        return;
                    } else if (TextUtils.isEmpty(binding.payment.getText())) {
                        binding.payment.setError("Field is empty");
                        return;
                    }
                    Utils.getInstance().ShowProgress(UsersActivity.this);
                    usersModel.setName(binding.name.getText().toString());
                    usersModel.setAddress(binding.addressText.getText().toString());
                    usersModel.setContactNumber(binding.contactNumber.getText().toString());
                    usersModel.setEthereumBalance(binding.setAmount.getText().toString());
                    usersModel.setBitcoinBalance(binding.bitcoinBalanance.getText().toString());
                    usersModel.setPayment(binding.payment.getText().toString());
                    usersModel.setStatus("Pending");
                    usersModel.setToken(Utils.getInstance().getDefaults("token", getApplicationContext()));
                    usersModel.setOrderName(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext()));
                    id = myRef.push().getKey();
                    if (id != null) {
                        myRef.child(id).setValue(usersModel);
                        myRefUser.child(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext())).child(id).setValue(usersModel);
                        binding.setAmount.setText("");
                        binding.setAmount.setText(null);
                        binding.contactNumber.setText("");
                        binding.contactNumber.setText(null);
                        binding.addressText.setText("");
                        binding.addressText.setText(null);
                        binding.name.setText("");
                        binding.name.setText(null);
                        binding.bitcoinBalanance.setText("");
                        binding.bitcoinBalanance.setText(null);
                        binding.payment.setText("");
                        binding.payment.setText(null);
                        Toast.makeText(UsersActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
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

                Intent intent = new Intent(UsersActivity.this, LocationPickerActivity.class);
                startActivityForResult(intent, requestCodeAddress);
//                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
//                        , Place.Field.LAT_LNG);
//                Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placeFields)
//                        .setTypeFilter(TypeFilter.ADDRESS)
//                        .setLocationRestriction(RectangularBounds.newInstance(
//                                new LatLng(24.926294, 67.022095),
//                                new LatLng(30.3753, 69.3451)))
//                        .setCountry("pakistan")
//                        .build(UsersActivity.this);
//
//                startActivityForResult(autocompleteIntent, requestCodeAddress);
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
                    binding.addressText.setText(address);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

//            Place place = Autocomplete.getPlaceFromIntent(intent);
//            place.getName();
//            place.getAddress();
//            binding.addressText.setText(place.getAddress());
//        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//            Status status = Autocomplete.getStatusFromIntent(intent);
//        } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
//            // The user canceled the operation.
//        }

    }

//    private void getLastIndexValue() {
//        Query lastQuery = myRef.orderByKey().limitToLast(1);
//        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                UsersModel value;
//                donationList.clear();
//                for (DataSnapshot organization : dataSnapshot.getChildren()) {
//                    value = organization.getValue(UsersModel.class);
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
        mAdapter = new UsersAdapter(getApplicationContext(), this);
        recyclerViewLabel.setLayoutManager(mLayoutManager);
        recyclerViewLabel.setAdapter(mAdapter);
    }


    private void getData() {
        Utils.getInstance().ShowProgress(UsersActivity.this);
        binding.RecyclerView.setVisibility(View.VISIBLE);
        binding.addLayout.setVisibility(View.GONE);
        binding.fab.setVisibility(View.GONE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                donationList.clear();
                for (DataSnapshot organization : dataSnapshot.getChildren()) {
                    UsersModel value = organization.getValue(UsersModel.class);
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
        Utils.getInstance().ShowProgress(UsersActivity.this);
        binding.RecyclerView.setVisibility(View.VISIBLE);
        binding.addLayout.setVisibility(View.GONE);
        myRefUser.child(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                donationList.clear();
                for (DataSnapshot organization : dataSnapshot.getChildren()) {
                    UsersModel value = organization.getValue(UsersModel.class);
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
    public void onOrderItemClicked(final UsersModel usersModel, final String status) {
        UsersModel order = new UsersModel();
        order.setName(usersModel.getName());
        order.setAddress(usersModel.getAddress());
        order.setContactNumber(usersModel.getContactNumber());
        order.setEthereumBalance(usersModel.getEthereumBalance());
        order.setBitcoinBalance(usersModel.getBitcoinBalance());
        order.setPayment(usersModel.getPayment());
        order.setStatus(status);
        order.setToken(Utils.getInstance().getDefaults("token", getApplicationContext()));
        order.setOrderName(Utils.getInstance().getDefaults("userDisplayName", getApplicationContext()));
        myRef.child(usersModel.getKey()).setValue(order);
            myRefUser.child(usersModel.getOrderName()).child(usersModel.getKey()).setValue(order);
        mAdapter.notifyDataSetChanged();
    }

}
