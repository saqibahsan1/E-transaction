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
import com.android.makeyousmile.databinding.ActivityOrganizationBinding;
import com.android.makeyousmile.ui.Utility.Utils;
import com.android.makeyousmile.ui.adapter.OrganizationAdapter;
import com.android.makeyousmile.ui.model.Organization;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrganizationActivity extends AppCompatActivity {

    DatabaseReference myRef;

    ActivityOrganizationBinding binding;
    private Organization organization;
    private List<Organization> organizationList = new ArrayList<>();
    private OrganizationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_organization);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myRef = FirebaseDatabase.getInstance().getReference("Organization");
        initRecyclerView(binding.RecyclerView);
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                organization = new Organization();
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
                } else if (TextUtils.isEmpty(binding.specilaity.getText())) {
                    binding.specilaity.setError("Field is empty");
                    return;
                }
                Utils.getInstance().ShowProgress(OrganizationActivity.this);
                organization.setName(binding.name.getText().toString());
                organization.setEmail(binding.email.getText().toString());
                organization.setAddress(binding.address.getText().toString());
                organization.setCnic(binding.cnic.getText().toString());
                organization.setContactNumber(binding.contactNumber.getText().toString());
                organization.setSpecilaity(binding.specilaity.getText().toString());
                String id = myRef.push().getKey();
                if (id != null) {
                    myRef.child(id).setValue(organization);
                    binding.email.setText("");
                    binding.email.setText(null);
                    binding.specilaity.setText("");
                    binding.specilaity.setText(null);
                    binding.contactNumber.setText("");
                    binding.contactNumber.setText(null);
                    binding.address.setText("");
                    binding.address.setText(null);
                    binding.name.setText("");
                    binding.name.setText(null);
                    binding.cnic.setText("");
                    binding.cnic.setText(null);
                    Utils.getInstance().HideProgress();
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
        mAdapter = new OrganizationAdapter(getApplicationContext());
        recyclerViewLabel.setLayoutManager(mLayoutManager);
        recyclerViewLabel.setAdapter(mAdapter);
    }


    private void getData(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                organizationList.clear();
                for (DataSnapshot organization : dataSnapshot.getChildren()) {
                    Organization value = organization.getValue(Organization.class);
                    organizationList.add(value);
                }
                mAdapter.setOrganization(organizationList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(OrganizationActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
