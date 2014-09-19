package ch.opengis.sharemylocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;


public class ShareActivity extends Activity {

    static final String TAG = "ShareMyLocation";
    Intent GPSintent;
    LocationManager locationManager;
    SharedPreferences prefs;
    SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    Switch sharing_toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        StrictMode.ThreadPolicy policy = StrictMode.ThreadPolicy.LAX;
        StrictMode.setThreadPolicy(policy);

        GPSintent = new Intent(this, GPSService.class);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sharing_toggle = (Switch) findViewById(R.id.sharing_toggle);
        sharing_toggle.setChecked(GPSService.IS_RUNNING);
        Log.d(TAG, "GPS service running: " + GPSService.IS_RUNNING);


        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                        if (key.equals(getString(R.string.sync_frequency))) {
                            Log.d(TAG, "Restarting sharing service due to share frequency change");
                            reset_sharing();
                        }
                    }
                };
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(ShareActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // called by onclick in the xml
    public void toggle_sharing(View view) {
        if (sharing_toggle.isChecked()) {
            start_sharing();
        } else {
            stop_sharing();
        }
    }

    private void reset_sharing(){
        if (GPSService.IS_RUNNING){
            stopService(GPSintent);
            startService(GPSintent);
        }
    }

    private void stop_sharing() {
        stopService(GPSintent);
    }

    private void start_sharing() {
        // Get Location Manager and check for GPS location services
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startService(GPSintent);
        }
        else {
            // disable the switch, so if the use cancels the dialog we have consistent state
            sharing_toggle.setChecked(false);

            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.location_services_inactive);
            builder.setMessage(R.string.enable_gps);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                }
            });

            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }
}
