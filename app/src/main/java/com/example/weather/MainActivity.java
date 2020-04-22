package com.example.weather;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weather.Model.OpenWeatherMap;
import com.example.weather.Model.Weather;
import com.example.weather.Utils.AskWeatherToAPI;
import com.example.weather.Utils.MetaData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView txtCity;
    TextView txtTemp;
    TextView txtHumidity;
    TextView txtLastUpdate;
    TextView txtDescription;
    //TODO TextView txtLocalTime;
    ImageView weatherIcon;

    Button button;
    EditText locationInput;
    OpenWeatherMap owm = new OpenWeatherMap();

    // Permissions
    // ActivityCompat.checkSelfPermissions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationInput = findViewById(R.id.locationInput);
        button = findViewById(R.id.fetch);

        txtCity = findViewById(R.id.txtCity);
        txtTemp = findViewById(R.id.txtTemp);
        txtHumidity = findViewById(R.id.txtHumidity);
        // txtLocalTime = findViewById(R.id.txtLocalTime);
        txtLastUpdate = findViewById(R.id.txtLastUpdate);
        txtDescription = findViewById(R.id.txtDescription);
        weatherIcon = findViewById(R.id.weatherIcon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchAction(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(button.getWindowToken(), 0);

        String cityName = locationInput.getText().toString();
        new FetchWeather().execute(cityName);
    }

    private class FetchWeather extends AsyncTask<String, Void, List<Object>> {

//        ProgressBar pb = new ProgressBar(MainActivity.this);
        ProgressDialog pd = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Retrieving weather data...");
            pd.show();
        }

        @Override
        protected List<Object> doInBackground(String ...params) {
            // Formatting url to contact API with proper city considering timestamp
            String cityName = params[0];
            String apiString = MetaData.formatRequestURL(cityName);
            String curDate = MetaData.whatIsTheDate();

            String apiAnswer = AskWeatherToAPI.callOpenWeather(apiString);

            // Issuing model objects from JSON
            Gson gson = new Gson();
            Type mtype = new TypeToken<OpenWeatherMap>(){}.getType();
            owm = gson.fromJson(apiAnswer, mtype);
            if (owm == null)
                return null;

            // Accessing JSON element
            Weather index = owm.getWeather().get(0);
            String iconUrl = MetaData.formatIconURL(index.getIcon());
            Bitmap mIcon1 = fetchWeatherIcon(iconUrl);

            // Object list to pass to PostExecute
            List<Object> res = new ArrayList<>();
            res.add(curDate);
            res.add(owm);
            res.add(mIcon1);
            res.add(apiAnswer);
            return res;
        }

//        Override
//        protected void onProgressUpdate(Integer... params) {
//        //Update a progress bar here, or ignore it, it's up to you
//        }

        public Bitmap fetchWeatherIcon(String iconUrl) {
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(iconUrl).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                System.out.println(iconUrl);
            } catch (Exception e) {
                System.err.println("Error" + e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(List<Object> s) {
            super.onPostExecute(s);
            if (s == null) {
                txtCity.setText("");
                txtLastUpdate.setText("Error! City not found, please try again.");
                txtTemp.setText("");
                txtHumidity.setText("");
                txtDescription.setText("");

                pd.dismiss();
                return;
            }
            pd.dismiss();
            System.out.println("----------> API ANSWERED: " + s.get(1));

            txtCity.setText(String.format("%s, %s", owm.getName(), owm.getSys().getCountry()));
            txtLastUpdate.setText(String.format("Last Update : %s", s.get(0)));
            txtTemp.setText(String.format("%d °C", Math.round(owm.getMain().getTemp())));
            txtHumidity.setText(String.format("Humidity : %d %%", owm.getMain().getHumidity()));
            txtDescription.setText(String.format("%s", owm.getWeather().get(0).getDescription()));
            weatherIcon.setImageBitmap((Bitmap) s.get(2));
        }
    }
}

