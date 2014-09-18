package ch.opengis.sharemylocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
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

        if (sms_sharing && sms_number != "") {
            Utils.share_via_sms(sms_number, generate_message(intent));
        }

        boolean http_sharing = prefs.getBoolean(context.getString(R.string.http_sharing), true);
        String post_url = prefs.getString(context.getString(R.string.post_url), "");

        if (http_sharing && post_url != "") {
            Utils.share_via_http(post_url, generate_message(intent));
        }
    }

    private String generate_message(Intent intent){
        return intent.getExtras().toString();
    }
}
