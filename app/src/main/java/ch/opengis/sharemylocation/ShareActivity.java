package ch.opengis.sharemylocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.ToggleButton;


public class ShareActivity extends Activity {

    static final String TAG = "ShareMyLocation";

    // called by onclick in the xml
    public void toggle_sharing(View view) {
        boolean on = ((Switch) view).isChecked();

        Intent i = new Intent(this, GPSService.class);
        if (on) {
            // use this to start and trigger a service
            // potentially add data to the intent
            //i.putExtra("KEY1", "Value to be used by the service");
            startService(i);
        } else {
            stopService(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
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
