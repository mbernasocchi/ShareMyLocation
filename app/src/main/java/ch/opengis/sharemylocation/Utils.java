package ch.opengis.sharemylocation;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.text.SimpleDateFormat;
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

        return generate_message(loc);
    }

    public static String generate_message(Location location) {
        if (location == null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fixDateandTime = sdf.format(location.getTime());

        return String.format(
                "{'time':%s;'lat':%f;'lon':%f;'alt':%f;'spd':%f;'acc':%f;'url':%s}",
                fixDateandTime,
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude(),
                location.getSpeed(),
                location.getAccuracy(),
                String.format("http://maps.google.com/?q=%f,%f",
                        location.getLatitude(),
                        location.getLongitude())
        );
    }
}
