package com.example.jredpath.universaltoursclient.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jredpath.universaltoursclient.R;
import com.example.jredpath.universaltoursclient.model.TourModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
 * Created by JRedpath on 17/03/2016.
 */
public class AllToursFragment extends Fragment {

    private ListView lvTours;
    private ProgressDialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_all_tours, container, false);

        final String allToursURL = rootView.getResources().getString(R.string.all_tour_data);

        lvTours = (ListView) rootView.findViewById(R.id.lvTours);


        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading, Please wait...");

        new AllToursAsyncTask().execute(allToursURL);


        return rootView;
    }

    public class AllToursAsyncTask extends AsyncTask<String, String, List<TourModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

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
            dialog.dismiss();
            TourAdapterWM adapter = new TourAdapterWM(getActivity(), R.layout.all_tours_singlerow, s);
            lvTours.setAdapter(adapter);
        }
    }

    public class TourAdapterWM extends ArrayAdapter {

        private List<TourModel> tourModelList;
        private int resource;
        private LayoutInflater inflater;

        public TourAdapterWM(Context context, int resource, List<TourModel> objects) {
            super(context, resource, objects);
            tourModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(resource, null);

                holder = new ViewHolder();

                holder.tour_id = (TextView) convertView.findViewById(R.id.tour_id);
                holder.tour_name = (TextView) convertView.findViewById(R.id.tour_name);
                holder.tour_date = (TextView) convertView.findViewById(R.id.tour_date);
                holder.organiser = (TextView) convertView.findViewById(R.id.organiser);
                holder.cost_pp = (TextView) convertView.findViewById(R.id.cost_pp);
                holder.max_pp = (TextView) convertView.findViewById(R.id.max_pp);
                holder.desc = (TextView) convertView.findViewById(R.id.desc);

                holder.mapView = (MapView) convertView.findViewById(R.id.mapAdapter);
                holder.mapView.onCreate(getArguments());

                holder.map = holder.mapView.getMap();

                holder.tour_id.setText("Tour Code: " + tourModelList.get(position).getId().toString());
                holder.tour_name.setText(tourModelList.get(position).getTourName());
                holder.tour_date.setText("Tour Date: \n" + tourModelList.get(position).getTourDate());
                holder.organiser.setText("Organiser: " + tourModelList.get(position).getOrganiser());
                holder.cost_pp.setText("Cost/PP: £" + tourModelList.get(position).getCost().toString());
                holder.max_pp.setText("Max Capacity: " + tourModelList.get(position).getMaxPeople().toString());
                holder.desc.setText("Description: \n" + tourModelList.get(position).getDescription());
                holder.location = new LatLng(tourModelList.get(position).getLatitude(), tourModelList.get(position).getLongitude());

                holder.tourLocation = holder.map.addMarker(new MarkerOptions().position(holder.location)
                        .title(tourModelList.get(position).getTourName()));
                holder.map.moveCamera(CameraUpdateFactory.newLatLngZoom(holder.location, 15));
            }
            return convertView;
        }
    }

    class ViewHolder {
        TextView tour_id;
        TextView tour_name;
        TextView tour_date;
        TextView organiser;
        TextView cost_pp;
        TextView max_pp;
        TextView desc;
        MapView mapView;
        GoogleMap map;
        Marker tourLocation;
        LatLng location;
    }
}