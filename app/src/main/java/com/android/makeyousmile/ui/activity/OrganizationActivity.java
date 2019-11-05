package com.android.makeyousmile.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.makeyousmile.R;
import com.android.makeyousmile.databinding.ActivityOrganizationBinding;
import com.android.makeyousmile.ui.model.AddOrganization;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrganizationActivity extends AppCompatActivity {

    DatabaseReference myRef;
    FirebaseDatabase database;
    ActivityOrganizationBinding binding;
    private AddOrganization organization;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_organization);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();



        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                organization=new AddOrganization();
                organization.setName(binding.name.getText().toString());
                organization.setEmail(binding.email.getText().toString());
                organization.setAddress(binding.address.getText().toString());
                organization.setCnic(binding.cnic.getText().toString());
                organization.setContactNumber(binding.contactNumber.getText().toString());
                organization.setSpecilaity(binding.specilaity.getText().toString());
                myRef.child("Organization").setValue(organization);


            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
}
