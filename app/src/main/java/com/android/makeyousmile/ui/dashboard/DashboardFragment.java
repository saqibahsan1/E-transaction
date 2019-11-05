package com.android.makeyousmile.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.makeyousmile.R;
import com.android.makeyousmile.databinding.FragmentDashboardBinding;
import com.android.makeyousmile.ui.activity.DeliveryBoyActivity;
import com.android.makeyousmile.ui.activity.DonationActivity;
import com.android.makeyousmile.ui.activity.OrganizationActivity;
import com.android.makeyousmile.ui.activity.RestaurantActivity;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_dashboard,container,false);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        binding.organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), OrganizationActivity.class));
            }
        });


        binding.restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RestaurantActivity.class));
            }
        });

        binding.donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DonationActivity.class));
            }
        });

        binding.deliveryBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DeliveryBoyActivity.class));
            }
        });




        return binding.getRoot();
    }
}