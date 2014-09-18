package ch.opengis.sharemylocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.security.PublicKey;

public class ShareLocationReceiver extends BroadcastReceiver {
    SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        boolean sms_sharing = prefs.getBoolean(context.getString(R.string.sms_sharing), true);
        String sms_number = prefs.getString(context.getString(R.string.sms_number), "");
        String msg = generate_message(intent);

        if (sms_sharing && sms_number != "") {
            Utils.share_via_sms(sms_number, msg);
        }

        boolean http_sharing = prefs.getBoolean(context.getString(R.string.http_sharing), true);
        String post_url = prefs.getString(context.getString(R.string.post_url), "");

        if (http_sharing && post_url != "") {
            Utils.share_via_http(post_url, msg);
        }
    }

    private String generate_message(Intent intent){
        Bundle b = intent.getExtras();
        Location loc = (Location)b.get(android.location.LocationManager.KEY_LOCATION_CHANGED);
        String msg = String.format(
                "{'time':%d;'lat':%f;'lon':%f;'alt':%f;'spd':%f;'acc':%f}",
                loc.getTime(),
                loc.getLatitude(),
                loc.getLongitude(),
                loc.getAltitude(),
                loc.getSpeed(),
                loc.getAccuracy());
        return msg;
    }
}
