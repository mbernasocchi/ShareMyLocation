package ch.opengis.sharemylocation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class GPSService extends Service {
    SharedPreferences prefs;
    LocationManager locationManager;

    @Override
    public void onCreate(){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String preferredInterval = prefs.getString(getString(R.string.sync_frequency), "15");
        //convert minutes to milliseconds
        float interval = Float.parseFloat(preferredInterval) * 1000;
        Log.d(ShareActivity.TAG, "Using interval: " + interval + "ms");
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, 0);

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
