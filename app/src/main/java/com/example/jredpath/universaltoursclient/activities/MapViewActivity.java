package com.example.jredpath.universaltoursclient.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.jredpath.universaltoursclient.R;
import com.example.jredpath.universaltoursclient.model.TourModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JRedpath on 27/03/2016.
 */
public class MapViewActivity extends FragmentActivity {

    List<LatLng> tourLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);

        final String allToursURL = getResources().getString(R.string.all_tour_data);

        new PlotLocationsAsyncTask().execute(allToursURL);
    }

    public class PlotLocationsAsyncTask extends AsyncTask<String, String, List<TourModel>> implements OnMapReadyCallback {

        @Override
        protected List<TourModel> doInBackground(String... params) {
            HttpURLConnection connection = null;

            BufferedReader reader;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                JSONArray parentArray = new JSONArray(finalJson);

                List<TourModel> tourModelList = new ArrayList<>();

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").serializeNulls().create();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    TourModel tourModel = gson.fromJson(finalObject.toString(), TourModel.class);

                    tourModelList.add(tourModel);
                }
                return tourModelList;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<TourModel> s) {
            super.onPostExecute(s);

            for (TourModel t : s) {
                tourLocations.add(new LatLng(t.getLatitude(), t.getLongitude()));
            }

            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {

            //googleMap.setMyLocationEnabled(true);
            for (LatLng l : tourLocations) {
                googleMap.addMarker(new MarkerOptions().position(l));
            }

            // Move the camera instantly to hamburg with a zoom of 15.
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

            // Zoom in, animating the camera.
            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }
    }
}
