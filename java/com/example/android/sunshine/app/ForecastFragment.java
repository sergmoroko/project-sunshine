package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
    private final String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Kirovohrad,UA&mode=json&units=metric&cnt=7&APPID=bbfbe8a6c70eec4c4f3bf5b409dae2ba";
    final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";
    private final String OPEN_WEATHER_MAP_KEY = "bbfbe8a6c70eec4c4f3bf5b409dae2ba";
    private final String FORECAST_LOCATION = "Kirovohrad,UA";
    private final String RESPONCE_MODE = "json";
    private final String UNITS_OF_MEASURE = "metric";
    private final String DAYS_IN_RESPONCE_QTY = "7";



    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            new FetchWeatherTask().execute(FORECAST_LOCATION);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


       // MenuItem refreshBtn = (MenuItem) rootView.findViewById(R.id.action_refresh);


        // Mock data
        ArrayList<String> fakeList = new ArrayList<>();
        fakeList.add("Today - Sunny, 18/20");
        fakeList.add("Tomorrow - Sunny, 18/23");
        fakeList.add("Weds - Sunny, 18/22");
        fakeList.add("Thurs - Sunny, 13/22");
        fakeList.add("Frids - Sunny, 16/21");
        fakeList.add("Sats - Sunny, 20/26");
        fakeList.add("Suns - Sunny, 19/20");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,
                R.id.list_item_forecast_textview, fakeList);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_forecast);
        listView.setAdapter(arrayAdapter);

        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String forecastLocation = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            // Construct the URL for the OpenWeatherMap query

            Uri.Builder builder = Uri.parse(FORECAST_BASE_URL).buildUpon();
//            builder.scheme("http")
//                    .appendPath("api.openweathermap.org")
//                    .appendPath("data")
//                    .appendPath("2.5")
//                    .appendPath("forecast")
//                    .appendPath("daily")
//            builder.appendEncodedPath(FORECAST_BASE_URL)
                    builder.appendQueryParameter("q", forecastLocation)
                    .appendQueryParameter("mode", RESPONCE_MODE)
                    .appendQueryParameter("units", UNITS_OF_MEASURE)
                    .appendQueryParameter("cnt", DAYS_IN_RESPONCE_QTY)
                    .appendQueryParameter("APPID", OPEN_WEATHER_MAP_KEY);

            try {

                URL url = new URL(builder.build().toString());
                Log.d("wtf" , url.toString());

                Log.d("wtf" , "url ok");

                //url = new URL(FORECAST_URL);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                Log.d("wtf" , "connect ok");

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.d("wtf" , "ok");
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            Log.d("test", forecastJsonStr);
            return forecastJsonStr;
        }
    }
}
