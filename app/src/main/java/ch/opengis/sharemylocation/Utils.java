package ch.opengis.sharemylocation;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
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

        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(number, null, msg, null, null);
    }

    public static void share_via_http(String uri, String msg) {
        Log.d(ShareActivity.TAG, "Sending http:");
        Log.d(ShareActivity.TAG, uri);
        Log.d(ShareActivity.TAG, msg);
    }

    public static String generate_test_message() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        String lat = "46.78987181969633";
        String lon = "9.252955913543701";
        return currentDateandTime + "," + lat + "," + lon;
    }
}
