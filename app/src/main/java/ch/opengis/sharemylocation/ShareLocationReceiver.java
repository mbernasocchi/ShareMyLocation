package ch.opengis.sharemylocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ShareLocationReceiver extends BroadcastReceiver {
    SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        boolean sms_sharing = prefs.getBoolean(context.getString(R.string.sms_sharing), true);
        String sms_number = prefs.getString(context.getString(R.string.sms_number), "");
        String message = Utils.generate_message(intent);

        if (sms_sharing && sms_number != "") {
            Utils.share_via_sms(sms_number, message);
        }

        boolean http_sharing = prefs.getBoolean(context.getString(R.string.http_sharing), true);
        String post_url = prefs.getString(context.getString(R.string.post_url), "");

        if (http_sharing && post_url != "") {
            Utils.share_via_http(post_url, message);
        }
    }

}
