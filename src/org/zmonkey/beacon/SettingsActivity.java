package org.zmonkey.beacon;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * User: corey
 * Date: 3/1/12
 * Time: 2:12 PM
 */
public class SettingsActivity extends PreferenceActivity {
    private static final int MENU_APIKEY = 0;
    private static final int MENU_USEGPS = 1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        //TODO: REMOVE? SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

}