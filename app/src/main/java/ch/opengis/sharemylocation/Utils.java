package ch.opengis.sharemylocation;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by marco on 17.09.14.
 */
public class Utils {

    public static void share_via_sms(String number, String msg) {
        Log.d(ShareActivity.TAG, "Sending sms:");
        Log.d(ShareActivity.TAG, number);
        Log.d(ShareActivity.TAG, msg);

        //SmsManager sm = SmsManager.getDefault();
        //sm.sendTextMessage(number, null, msg, null, null);
    }

    public static void share_via_http(String uri, String msg) {
        Log.d(ShareActivity.TAG, "Sending http:");
        Log.d(ShareActivity.TAG, uri);
        Log.d(ShareActivity.TAG, msg);
    }

    public static String generate_test_message(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location loc = null;

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        for (int i=providers.size()-1; i>=0; i--) {
            loc = lm.getLastKnownLocation(providers.get(i));
            if (loc != null) break;
        }
        
        return generate_message_body(loc);
    }

    public static String generate_message(Intent intent){
        Bundle b = intent.getExtras();
        Location loc = (Location)b.get(LocationManager.KEY_LOCATION_CHANGED);
        return generate_message_body(loc);
    }

    private static String generate_message_body(Location loc) {
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
