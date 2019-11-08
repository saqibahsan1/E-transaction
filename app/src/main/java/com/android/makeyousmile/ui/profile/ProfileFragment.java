package com.android.makeyousmile.ui.profile;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.makeyousmile.R;
import com.android.makeyousmile.databinding.ProfileFragmentBinding;
import com.android.makeyousmile.ui.Activities.Login;
import com.android.makeyousmile.ui.Activities.MainActivity;
import com.android.makeyousmile.ui.Utility.Utils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private ProfileFragmentBinding binding;
    private String displayName,PhoneNumber,email;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.profile_fragment,container,false);


        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Utils.getInstance().setBoolean("isLoggedIn",false,getContext());
                        Utils.getInstance().setBoolean("isAdmin", false, getContext());
                        getActivity().finish();
                        startActivity(new Intent(getContext(), Login.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        displayName=Utils.getInstance().getDefaults("userDisplayName",getActivity());
        email=Utils.getInstance().getDefaults("userEmail",getActivity());
        PhoneNumber=Utils.getInstance().getDefaults("userPhone",getActivity());

        binding.name.setText(displayName);
        binding.id.setText(email);
        binding.title.setText(PhoneNumber);



        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    }

}
