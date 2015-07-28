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
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ShareLocationReceiver extends BroadcastReceiver {
    SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, R.string.start_register, Toast.LENGTH_SHORT).show();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Bundle b = intent.getExtras();
        Location location = (Location)b.get(LocationManager.KEY_LOCATION_CHANGED);
        String hash_salt = prefs.getString(context.getString(R.string.sync_hash_salt), "");
        String user_name = prefs.getString(context.getString(R.string.user_name), "").replaceAll(" ","_");
        Log.d(ShareActivity.TAG, "username=" + prefs.getString(context.getString(R.string.user_name), ""));
//    	String user_name = "";
//	    try {
//        	user_name = URLEncoder.encode(prefs.getString(context.getString(R.string.user_name), ""), "UTF-8");
//    	} catch (UnsupportedEncodingException e) {
// 	    	System.err.println(e);
//	    }
//        Log.d(ShareActivity.TAG, "username=" + user_name);
//        user_name.replaceAll(" ", "_");
        Log.d(ShareActivity.TAG, "username=" + user_name);

        String message;
        boolean success = false;

        boolean http_sharing = prefs.getBoolean(context.getString(R.string.http_sharing), true);
        String post_url = prefs.getString(context.getString(R.string.post_url), "");
        if (http_sharing && post_url != "") {
            message = Utils.generate_message(location, user_name, hash_salt, Utils.OutputFormat.URL_PARAMS);
            if (message != "") {
                success = Utils.share_via_http(post_url, message);
            }
        }

        if (!success){
            Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
            Log.d(ShareActivity.TAG, "http sharing faild.");

            boolean sms_sharing = prefs.getBoolean(context.getString(R.string.sms_sharing), true);
            String sms_number = prefs.getString(context.getString(R.string.sms_number), "");
            if (sms_sharing && sms_number != "") {
                message = Utils.generate_message(location, user_name, hash_salt, Utils.OutputFormat.JSON);
                if (message != "") {
                    Utils.share_via_sms(sms_number, message);
                }
            }
        }
        else {
            Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show();
            Log.d(ShareActivity.TAG, "http sharing was successful. Skipping SMS sharing");
        }
    }

}
