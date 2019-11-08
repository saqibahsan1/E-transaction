package com.android.makeyousmile.ui.Utility;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PlaceFieldArrayAdapter extends ArrayAdapter<PlaceField>
        implements AdapterView.OnItemClickListener {

    public PlaceFieldArrayAdapter(Context context, List<PlaceField> placeFields) {
        super(context, android.R.layout.simple_list_item_multiple_choice, placeFields);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        PlaceField placeField = getItem(position);
        updateView(view, placeField);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlaceField placeField = getItem(position);
        placeField.checked = !placeField.checked;
        updateView(view, placeField);
    }

    private void updateView(View view, PlaceField placeField) {
        if (view instanceof CheckedTextView) {
            CheckedTextView checkedTextView = (CheckedTextView) view;
            checkedTextView.setText(placeField.field.toString());
            checkedTextView.setChecked(placeField.checked);
        }
    }
}
