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

public class ShareLocationReceiver extends BroadcastReceiver {
    SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Bundle b = intent.getExtras();
        Location location = (Location)b.get(LocationManager.KEY_LOCATION_CHANGED);
        String hash_salt = prefs.getString(context.getString(R.string.sync_hash_salt), "");

        String message;
        boolean success = false;

        boolean http_sharing = prefs.getBoolean(context.getString(R.string.http_sharing), true);
        String post_url = prefs.getString(context.getString(R.string.post_url), "");
        if (http_sharing && post_url != "") {
            message = Utils.generate_message(location, hash_salt, Utils.OutputFormat.URL_PARAMS);
            if (message != "") {
                success = Utils.share_via_http(post_url, message);
            }
        }

        if (!success){
            boolean sms_sharing = prefs.getBoolean(context.getString(R.string.sms_sharing), true);
            String sms_number = prefs.getString(context.getString(R.string.sms_number), "");
            if (sms_sharing && sms_number != "") {
                message = Utils.generate_message(location, hash_salt, Utils.OutputFormat.JSON);
                if (message != "") {
                    Utils.share_via_sms(sms_number, message);
                }
            }
        }
        else {
            Log.d(ShareActivity.TAG, "http sharing was successful. Skipping SMS sharing");
        }
    }

}
