package ch.opengis.sharemylocation;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class GPSService extends Service {
    public static boolean IS_RUNNING = false;
    SharedPreferences prefs;
    LocationManager locationManager;
    PendingIntent pendingIntent;

    @Override
    public void onCreate(){
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Intent activeIntent = new Intent(this, ShareLocationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, activeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IS_RUNNING = true;
        String preferredInterval = prefs.getString(getString(R.string.sync_frequency), "15");
        //convert minutes to milliseconds
        long interval = Long.parseLong(preferredInterval) * 1000;
        Log.d(ShareActivity.TAG, "Using interval: " + interval + "ms");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, 0, pendingIntent);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        IS_RUNNING = false;
        locationManager.removeUpdates(pendingIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
