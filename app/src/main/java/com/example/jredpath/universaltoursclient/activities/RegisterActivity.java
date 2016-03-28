package com.example.jredpath.universaltoursclient.activities;

/**
 * Created by JRedpath on 22/03/2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jredpath.universaltoursclient.R;
import com.example.jredpath.universaltoursclient.model.UserModel;
import com.example.jredpath.universaltoursclient.util.JSONBuilder;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private EditText firstname;
    private EditText lastname;
    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnRegister;
    private TextView loginLink;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstname = (EditText) findViewById(R.id.input_name);
        lastname = (EditText) findViewById(R.id.input_lastname);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        loginLink = (TextView) findViewById(R.id.link_login);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Submitting New Tour, Please wait...");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate() == true) {
                    new RegisterAsyncTask().execute("http://10.0.2.2:8080/UTFYPServer/utv1/users");
                }else{
                    onSignupFailed();
                }
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    public class RegisterAsyncTask extends AsyncTask<String, String, UserModel> {

        String charset = "UTF-8";
        HttpURLConnection conn;
        OutputStreamWriter dos;
        URL urlObj;
        UserModel um = null;
        int responseCode;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            um = new UserModel(firstname.getText().toString(),
//                    lastname.getText().toString(),
//                    inputEmail.getText().toString(),
//                    inputPassword.getText().toString());

            dialog.show();
        }

        @Override
        protected UserModel doInBackground(String... params) {
            try {
                urlObj = new URL(params[0]);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

                dos = new OutputStreamWriter(conn.getOutputStream(), charset);

                dos.write(JSONBuilder.getJson(um));
                dos.flush();
                dos.close();

                responseCode = conn.getResponseCode();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return um;
        }

        @Override
        protected void onPostExecute(UserModel u) {
            super.onPostExecute(u);
            dialog.dismiss();
            if (responseCode == 200 || responseCode == 204) {
                Toast.makeText(getApplicationContext(), "Response Code:" + responseCode + ":- Account Created Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onSignupSuccess() {
        btnRegister.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnRegister.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String lastName = lastname.getText().toString();
        String firstName = firstname.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            firstname.setError("at least 3 characters");
            valid = false;
        } else {
            firstname.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            inputPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }
}