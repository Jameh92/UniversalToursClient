package com.example.jredpath.universaltoursclient.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jredpath.universaltoursclient.activities.MainActivity;
import com.example.jredpath.universaltoursclient.util.JSONBuilder;
import com.example.jredpath.universaltoursclient.R;
import com.example.jredpath.universaltoursclient.model.TourModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by JRedpath on 17/03/2016.
 */
public class SubmitTourFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private EditText txtName, txtOrg, txtCost, txtMax, txtLong, txtLat, txtDesc;
    private TextView txtDate, txtTime;
    private Button submit;
    private ProgressDialog dialog;
    String date;
    String time;
    String endDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_submit_tour, container, false);

        final String allToursURL = rootView.getResources().getString(R.string.all_tour_data);

        txtName = (EditText) rootView.findViewById(R.id.txtName);
        txtOrg = (EditText) rootView.findViewById(R.id.txtOrg);
        txtCost = (EditText) rootView.findViewById(R.id.txtCost);
        txtMax = (EditText) rootView.findViewById(R.id.txtMax);
        Button timeButton = (Button)rootView.findViewById(R.id.timeButton);
        Button dateButton = (Button)rootView.findViewById(R.id.dateButton);
        txtLong = (EditText) rootView.findViewById(R.id.txtLong);
        txtLat = (EditText) rootView.findViewById(R.id.txtLat);
        txtDesc = (EditText) rootView.findViewById(R.id.txtDesc);
        txtDate = (TextView) rootView.findViewById(R.id.txtDate);
        txtTime = (TextView) rootView.findViewById(R.id.txtTime);


        submit = (Button) rootView.findViewById(R.id.btnSubmitTour);

        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Submitting New Tour, Please wait...");

        // Show a timepicker when the timeButton is clicked
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(SubmitTourFragment.this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
                tpd.setTitle("Pick a Tour Time");
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        SubmitTourFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.vibrate(true);
                dpd.setTitle("Pick a Tour Date");
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute(allToursURL);
            }
        });

        return rootView;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = year + "-" + monthOfYear + "-" + dayOfMonth;
        txtDate.setText("Your Tour Date and time is: " + date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        time = hourString+":"+minuteString+":"+secondString;
        txtTime.setText(time);
    }

    public class JSONTask extends AsyncTask<String, String, TourModel> {

        String charset = "UTF-8";
        HttpURLConnection conn;
        OutputStreamWriter dos;
        URL urlObj;
        TourModel tm = null;
        int responseCode;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            endDate = date + "At" + time;

            tm = new TourModel(txtOrg.getText().toString(),
                    txtName.getText().toString(),
                    endDate,
                    Double.parseDouble(txtLong.getText().toString()),
                    Double.parseDouble(txtLat.getText().toString()),
                    Double.parseDouble(txtCost.getText().toString()),
                    Integer.parseInt(txtMax.getText().toString()),
                    txtDesc.getText().toString());

            dialog.show();
        }

        @Override
        protected TourModel doInBackground(String... params) {
            try {
                urlObj = new URL(params[0]);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

                dos = new OutputStreamWriter(conn.getOutputStream(), charset);

                dos.write(JSONBuilder.getJson(tm));
                dos.flush();
                dos.close();

                responseCode = conn.getResponseCode();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return tm;
        }

        @Override
        protected void onPostExecute(TourModel s) {
            super.onPostExecute(s);
            dialog.dismiss();
            //Toast.makeText(con, "Response Code: " + responseCode, Toast.LENGTH_SHORT).show();
        }
    }
}
