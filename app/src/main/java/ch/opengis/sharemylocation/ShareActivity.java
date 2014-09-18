package ch.opengis.sharemylocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;


public class ShareActivity extends Activity {

    static final String TAG = "ShareMyLocation";
    LocationManager locationManager;


    // called by onclick in the xml
    public void toggle_sharing(View view) {
        boolean on = ((Switch) view).isChecked();

        Intent i = new Intent(this, GPSService.class);
        if (on) {
            // Get Location Manager and check for GPS location services
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // disable the switch, so if the use cancels the dialog we have consistent state
                ((Switch) view).setChecked(false);

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
                            }});

                Dialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
            else{
                startService(i);
            }
        } else {
            stopService(i);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Switch sharing_toggle = (Switch) findViewById(R.id.sharing_toggle);
        sharing_toggle.setChecked(GPSService.IS_RUNNING);
        Log.d(TAG, "GPS service running: " + GPSService.IS_RUNNING);
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
}
