package com.prac.lostfound;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocoderUtils {
    public static LatLng getLatLngFromAddress(Context context, String addressStr) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(addressStr, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                Log.e("GeocoderUtils", "No results found for address: " + addressStr);
            }
        } catch (IOException e) {
            Log.e("GeocoderUtils", "Geocoding IOException: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("GeocoderUtils", "Invalid address input: " + e.getMessage());
        }
        return null;
    }
}
