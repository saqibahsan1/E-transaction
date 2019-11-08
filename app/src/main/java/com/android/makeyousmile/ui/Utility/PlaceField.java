package com.android.makeyousmile.ui.Utility;

import com.google.android.libraries.places.api.model.Place;

public class PlaceField {
        final Place.Field field;
        boolean checked;

        public PlaceField(Place.Field field) {
            this.field = field;
        }
    }