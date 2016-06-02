package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {

    private static String forecastText;
    private final String APP_HASHTAG =" #SunshineApp";
    private ShareActionProvider shareActionProvider;
    Intent sendIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem share = menu.findItem(R.id.action_share);

        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(share);
        sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, forecastText + APP_HASHTAG);
        sendIntent.setType("text/plain");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareActionProvider.setShareIntent(sendIntent);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        else {

            /** SHARE BUTTONS */

            if (id == R.id.action_share) {
//                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//
//                String city = settings.getString(getString(R.string.location_city_key), getString(R.string.default_city_name));
//                String country = settings.getString(getString(R.string.location_country_key), getString(R.string.default_country_code));
//
//                String preferredLocation = city + ", " + country;
//                Uri mapUri = Uri.parse("geo:0,0?q=" + preferredLocation);
//
//                Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
//
//                if (intent.resolveActivity(getPackageManager()) != null) {
//
//                    startActivity(intent);
//                }

                startActivity(sendIntent);


                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            TextView forecast = (TextView) rootView.findViewById(R.id.detailed_forecast);
            forecastText = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            forecast.setText(forecastText);

            return rootView;
        }

    }
}
