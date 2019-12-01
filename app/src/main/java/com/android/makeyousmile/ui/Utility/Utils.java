package com.android.makeyousmile.ui.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.makeyousmile.R;
import com.android.makeyousmile.ui.Activities.MainActivity;


public class Utils {


    private ProgressDialog progressDialog = null;
    private static final String TAG = MainActivity.class.getName();



    public synchronized static final Utils getInstance() {
        return SingletonHolder._instance;
    }

    private static final class SingletonHolder {
        static final Utils _instance = new Utils();
    }



    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void ShowDialogeY(Context context){

        progressDialog = new ProgressDialog(context, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progressdialog);
        progressDialog.setCancelable(false);
    }

    public void ShowProgress(Context context){
        if (progressDialog != null && progressDialog.isShowing()) {
            //is running
            HideProgress();
        }else {
            ShowDialogeY(context);
        }
    }
    public void HideProgress(){
        progressDialog.hide();
        progressDialog.dismiss();
    }
    public void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public void setBoolean(String key, Boolean value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public String getDefaults(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }
    public Boolean getBoolean(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
    public void ClearSharedPrefs(Context context){
        SharedPreferences preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
    }
}
