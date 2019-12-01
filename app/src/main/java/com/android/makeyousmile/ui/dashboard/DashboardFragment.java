package com.android.makeyousmile.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.makeyousmile.R;
import com.android.makeyousmile.databinding.FragmentDashboardBinding;
import com.android.makeyousmile.ui.Utility.Utils;
import com.android.makeyousmile.ui.Activities.TransactionActivity;
import com.android.makeyousmile.ui.Activities.UsersActivity;

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

        if (Utils.getInstance().getBoolean("isAdmin", getContext())) {
            binding.user.setVisibility(View.VISIBLE);
            binding.admin.setVisibility(View.GONE);
            binding.admin1.setVisibility(View.GONE);
            binding.scrollView.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
        }else {
            binding.user.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            binding.scrollView.setBackground(getResources().getDrawable(R.drawable.bg));
            layoutParams.setMargins(0, 0, 0, 10);
            binding.user.setLayoutParams(layoutParams);
            binding.admin1.setVisibility(View.INVISIBLE);
            binding.admin.setVisibility(View.INVISIBLE);
        }

        binding.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UsersActivity.class));
            }
        });

        binding.donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TransactionActivity.class));
            }
        });

        return binding.getRoot();
    }
}