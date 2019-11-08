package com.android.makeyousmile.ui.Utility;

import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlacesFieldSelector {

    private final List<PlaceField> placeFields;

    public PlacesFieldSelector() {
        this(Arrays.asList(Place.Field.values()));
    }

    public PlacesFieldSelector(List<Place.Field> validFields) {
        placeFields = new ArrayList<>();
        for (Place.Field field : validFields) {
            placeFields.add(new PlaceField(field));
        }
    }

    /**
     * Returns all {@link Place.Field} that are selectable.
     */
    public List<Place.Field> getAllFields() {
        List<Place.Field> list = new ArrayList<>();
        for (PlaceField placeField : placeFields) {
            list.add(placeField.field);
        }

        return list;
    }

    /**
     * Returns all {@link Place.Field} values the user selected.
     */
    public List<Place.Field> getSelectedFields() {
        List<Place.Field> selectedList = new ArrayList<>();
        for (PlaceField placeField : placeFields) {
            if (placeField.checked) {
                selectedList.add(placeField.field);
            }
        }

        return selectedList;
    }

    /**
     * Returns a String representation of all selected {@link Place.Field} values. See {@link
     * #getSelectedFields()}.
     */
    public String getSelectedString() {
        StringBuilder builder = new StringBuilder();
        for (Place.Field field : getSelectedFields()) {
            builder.append(field).append("\n");
        }

        return builder.toString();
    }

    //////////////////////////
    // Helper methods below //
    //////////////////////////


}