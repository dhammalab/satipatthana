package com.dhammalab.satipatthna;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.crittercism.app.Crittercism;
import com.dhammalab.satipatthna.repository.Settings;

/**
 * Created by Ilya on 14.07.2014.
 */
public class BaseActivity extends Activity {

    private Settings settings = SatiApplication.getInstance().getSettings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Crittercism.initialize(getApplicationContext(), "53c54614466eda2cc200000d");
        super.onCreate(savedInstanceState);
    }

    protected Settings getSettings() {
        return settings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_reading) {
            Intent intent = new Intent(this, ReadingMaterialListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_other_analytics) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.action_credits) {
            CreditsDialog dialog = new CreditsDialog();
            dialog.show(getFragmentManager(), CreditsDialog.class.getSimpleName());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
