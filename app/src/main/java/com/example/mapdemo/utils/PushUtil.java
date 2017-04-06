package com.example.mapdemo.utils;


// see https://github.com/rogerhu/parse-server-push-marker-example/blob/master/cloud/main.js

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.ParseCloud;
import com.parse.ParseInstallation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PushUtil {

    // Send location date, title of icon, marker ID, and userID and execute Parse cloud code.
    public static JSONObject getPayloadFromMarker(Marker marker) throws JSONException {
        LatLng position = marker.getPosition();

        JSONObject location = new JSONObject();
        location.put("lat", position.latitude);
        location.put("long", position.longitude);

        JSONObject payload = new JSONObject();
        payload.put("location", location);
        payload.put("title", marker.getTitle());
        payload.put("snippet", marker.getSnippet());

        // Disambiguate this marker ID as created by the installation and the unique marker ID given to it.
        String installationId = ParseInstallation.getCurrentInstallation().getInstallationId();

        String markerId = installationId + "_" + marker.getId();
        payload.put("markerId", markerId);

        // Use so we can discard push notifications sent to ourselves.
        payload.put("installationId", installationId);
        return payload;
    }

    public static void sendPushNotification(Marker marker, String channelName) {
        try {
            JSONObject payload = getPayloadFromMarker(marker);
            HashMap<String, String> data = new HashMap<>();
            data.put("customData", payload.toString());
            data.put("channel", channelName);

            // The code that processes this function is listed at:
            // https://github.com/rogerhu/parse-server-push-marker-example/blob/master/cloud/main.js
            ParseCloud.callFunctionInBackground("pushToChannel", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}