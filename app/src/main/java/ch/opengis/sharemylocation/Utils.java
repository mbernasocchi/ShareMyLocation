package ch.opengis.sharemylocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by marco on 17.09.14.
 */
public class Utils {

    public static enum OutputFormat {JSON, URL_PARAMS}
    private final static String SERVER_OK_TEXT = "OK";

    public static void share_via_sms(String number, String msg) {
        Log.d(ShareActivity.TAG, "Sending sms:");
        Log.d(ShareActivity.TAG, number);
        Log.d(ShareActivity.TAG, msg);

        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(number, null, msg, null, null);
    }

    public static boolean share_via_http(String base_url, String message) {
        Log.d(ShareActivity.TAG, "Sending http:");
        boolean success = false;
        try {
            URL url = new URL(base_url + "?" + message);
            Log.d(ShareActivity.TAG, url.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            success = readStream(con.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    private static boolean readStream(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(SERVER_OK_TEXT)){
                    Log.d(ShareActivity.TAG, "SERVER_OK_TEXT found");
                    return true;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static String generate_test_message(Context context, OutputFormat outputFormat) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String hash_salt = prefs.getString(context.getString(R.string.sync_hash_salt), "");
        List<String> providers = lm.getProviders(true);
        Location loc = null;

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        for (int i=providers.size()-1; i>=0; i--) {
            loc = lm.getLastKnownLocation(providers.get(i));
            if (loc != null) break;
        }

        return generate_message(loc, hash_salt, outputFormat);
    }

    public static String generate_message(Location location, String hash_salt, OutputFormat outputFormat) {
        if (location == null){
            Log.w(ShareActivity.TAG, "Location is null ");
            return "";
        }
        if (hash_salt.equals("")){
            Log.w(ShareActivity.TAG, "Server hash secret is empty");
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fixTime = sdf.format(location.getTime());

        String altitude = new DecimalFormat("#.#").format(location.getAltitude());
        String speed = new DecimalFormat("#.#").format(location.getSpeed());
        String accuracy = new DecimalFormat("#.#").format(location.getAccuracy());
        String latitude = String.format("%f", location.getLatitude());
        String longitude = String.format("%f", location.getLongitude());

        String data = fixTime + latitude + longitude + altitude + speed + accuracy + hash_salt;
        String hash = md5(data);

        String format;
        if (outputFormat == OutputFormat.URL_PARAMS){
            format = "time=%s&lat=%s&lon=%s&alt=%s&spd=%s&acc=%s&hash=%s";
        }
        else{
            format = "{'time':%s;'lat':%s;'lon':%s;'alt':%s;'spd':%s;'acc':%s;'hash':%s}";
        }
        return String.format(
                format,
                fixTime,
                latitude,
                longitude,
                altitude,
                speed,
                accuracy,
                hash);
    }

    public static String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
