package org.zmonkey.beacon;

import android.app.ListActivity;
import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.SharedPreferences;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * User: corey
 * Date: 3/1/12
 * Time: 2:12 PM
 */
public class SettingsActivity extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, OPTIONS));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                switch(position){
                    case 0:
                        makeApiKeyDialog();
                }
            }
        });
    }

    public void makeApiKeyDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("API Key");
        alert.setMessage("Enter your API Key");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MainActivity.main.setApiKey(input.getText().toString());
                MainActivity.main.refreshDisplay();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
    static final String[] OPTIONS = new String[] {
            "API Key",
    };
}