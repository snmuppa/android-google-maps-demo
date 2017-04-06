package com.example.mapdemo.utils;

import android.content.Context;

import com.example.mapdemo.requests.PushRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.ui.IconGenerator;

import java.util.Hashtable;

public class PushUtilTracker {

    // DO NOT declare this hashtable as static since it should only last the lifetime of an Activity.
    Hashtable<String, Marker> userMarkerMap;

    public PushUtilTracker() {
        userMarkerMap = new Hashtable<String, Marker>();
    }

    public void handleMarkerUpdates(Context context, PushRequest pushRequest,
                                    GoogleMap map) {
        Marker marker = userMarkerMap.get(pushRequest.markerId);

        // If marker already exists, simply update the position and title.
        if (marker != null) {
            marker.setPosition(pushRequest.mapLocation);
            marker.setTitle(pushRequest.title);
        } else {
            // Otherwise, create a new speech bubble and use the data from the push
            // to create the new marker.
            BitmapDescriptor icon = MapUtils
                    .createBubble(context, IconGenerator.STYLE_BLUE, pushRequest.title);
            marker = MapUtils.addMarker(map, pushRequest.mapLocation, pushRequest.title,
                    pushRequest.snippet, icon);
            //MapUtils.dropPinEffect(marker);
            userMarkerMap.put(pushRequest.markerId, marker);
        }
    }
}