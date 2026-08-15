package com.ansari.appdemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView ivBack;
    private EditText etFullName, etUsername, etEmail, etMobile, etPassword;
    private ImageView ivPasswordEye;
    private Spinner spLanguage;
    private CheckBox cbTerms;
    private TextView tvTerms, tvLogin;
    private LinearLayout btnRegister, btnGoogle, btnPhone;

    private boolean isPasswordVisible = false;

    // ==========================================
    // PHP API URL
    // ==========================================

    private static final String REGISTER_URL =
            "http://10.11.57.114/sanketsetuAPI/registeruser.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);


        // ==========================================
        // INITIALIZE VIEWS
        // ==========================================

        ivBack = findViewById(R.id.ivBack);

        etFullName = findViewById(R.id.etFullName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);

        ivPasswordEye = findViewById(R.id.ivPasswordEye);

        spLanguage = findViewById(R.id.spLanguage);

        cbTerms = findViewById(R.id.cbTerms);

        tvTerms = findViewById(R.id.tvTerms);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister = findViewById(R.id.btnRegister);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnPhone = findViewById(R.id.btnPhone);


        // ==========================================
        // LANGUAGE SPINNER
        // ==========================================

        String[] languages = {
                "Select Language",
                "English",
                "Hindi",
                "Marathi"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        languages
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spLanguage.setAdapter(adapter);


        // ==========================================
        // BACK BUTTON
        // ==========================================

        ivBack.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RegistrationActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();
        });


        // ==========================================
        // PASSWORD SHOW / HIDE
        // ==========================================

        ivPasswordEye.setOnClickListener(v -> {

            if (isPasswordVisible) {

                etPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                );

                isPasswordVisible = false;

            } else {

                etPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );

                isPasswordVisible = true;
            }

            etPassword.setSelection(
                    etPassword.getText().length()
            );
        });


        // ==========================================
        // REGISTER BUTTON
        // ==========================================

        btnRegister.setOnClickListener(v -> {

            String fullName =
                    etFullName.getText().toString().trim();

            String username =
                    etUsername.getText().toString().trim();

            String email =
                    etEmail.getText().toString().trim();

            String mobile =
                    etMobile.getText().toString().trim();

            String password =
                    etPassword.getText().toString().trim();

            String language =
                    spLanguage.getSelectedItem().toString();


            // ==========================================
            // FULL NAME VALIDATION
            // ==========================================

            if (fullName.isEmpty()) {

                etFullName.setError("Enter Full Name");
                etFullName.requestFocus();
                return;
            }


            // ==========================================
            // USERNAME VALIDATION
            // ==========================================

            if (username.isEmpty()) {

                etUsername.setError("Enter Username");
                etUsername.requestFocus();
                return;
            }


            // ==========================================
            // EMAIL VALIDATION
            // ==========================================

            if (email.isEmpty()) {

                etEmail.setError("Enter Email Address");
                etEmail.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(email)
                    .matches()) {

                etEmail.setError("Enter valid Email Address");
                etEmail.requestFocus();
                return;
            }


            // ==========================================
            // MOBILE VALIDATION
            // ==========================================

            if (mobile.isEmpty()) {

                etMobile.setError("Enter Mobile Number");
                etMobile.requestFocus();
                return;
            }

            if (mobile.length() != 10) {

                etMobile.setError(
                        "Enter valid 10 digit Mobile Number"
                );

                etMobile.requestFocus();
                return;
            }


            // ==========================================
            // PASSWORD VALIDATION
            // ==========================================

            if (password.isEmpty()) {

                etPassword.setError("Create Password");
                etPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {

                etPassword.setError(
                        "Password must contain at least 6 characters"
                );

                etPassword.requestFocus();
                return;
            }


            // ==========================================
            // LANGUAGE VALIDATION
            // ==========================================

            if (language.equals("Select Language")) {

                Toast.makeText(
                        RegistrationActivity.this,
                        "Please select language",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            // ==========================================
            // TERMS & CONDITIONS
            // ==========================================

            if (!cbTerms.isChecked()) {

                Toast.makeText(
                        RegistrationActivity.this,
                        "Please agree to Terms & Conditions",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            // ==========================================
            // CALL REGISTER API
            // ==========================================

            registerUser(
                    fullName,
                    username,
                    email,
                    mobile,
                    password,
                    language
            );
        });


        // ==========================================
        // GOOGLE BUTTON
        // ==========================================

        btnGoogle.setOnClickListener(v -> {

            Toast.makeText(
                    RegistrationActivity.this,
                    "Google Registration clicked",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // ==========================================
        // PHONE BUTTON
        // ==========================================

        btnPhone.setOnClickListener(v -> {

            Toast.makeText(
                    RegistrationActivity.this,
                    "Phone Registration clicked",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // ==========================================
        // TERMS & CONDITIONS
        // ==========================================

        tvTerms.setOnClickListener(v -> {

            Toast.makeText(
                    RegistrationActivity.this,
                    "Terms & Conditions",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // ==========================================
        // LOGIN
        // ==========================================

        tvLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RegistrationActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();
        });
    }


    // =========================================================
    // REGISTER USER USING PHP API
    // =========================================================

    private void registerUser(
            String fullName,
            String username,
            String email,
            String mobile,
            String password,
            String language
    ) {

        AsyncHttpClient client =
                new AsyncHttpClient();


        // ==========================================
        // REQUEST PARAMETERS
        // ==========================================

        RequestParams params =
                new RequestParams();

        params.put("name", fullName);
        params.put("username", username);
        params.put("email", email);
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("language", language);


        // ==========================================
        // POST REQUEST
        // ==========================================

        client.post(
                REGISTER_URL,
                params,
                new JsonHttpResponseHandler() {

                    // =========================================
                    // SUCCESS
                    // =========================================

                    @Override
                    public void onSuccess(
                            int statusCode,
                            Header[] headers,
                            JSONObject response) {

                        try {

                            // Show complete server response
                            // in Logcat for debugging
                            Log.d(
                                    "REGISTER_RESPONSE",
                                    response.toString()
                            );


                            // =================================
                            // IMPORTANT FIX
                            // =================================

                            String status =
                                    response.optString(
                                            "status",
                                            ""
                                    );

                            String message =
                                    response.optString(
                                            "message",
                                            "No message from server"
                                    );


                            // =================================
                            // REGISTRATION SUCCESS
                            // =================================

                            if (status.equalsIgnoreCase("success")) {

                                Toast.makeText(
                                        RegistrationActivity.this,
                                        message,
                                        Toast.LENGTH_LONG
                                ).show();


                                Intent intent =
                                        new Intent(
                                                RegistrationActivity.this,
                                                LoginActivity.class
                                        );

                                startActivity(intent);

                                finish();


                            } else {

                                // =================================
                                // REGISTRATION FAILED
                                // =================================

                                Toast.makeText(
                                        RegistrationActivity.this,
                                        message,
                                        Toast.LENGTH_LONG
                                ).show();
                            }


                        } catch (Exception e) {

                            Toast.makeText(
                                    RegistrationActivity.this,
                                    "Response error: " +
                                            e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();

                            Log.e(
                                    "REGISTER_ERROR",
                                    "Response parsing error",
                                    e
                            );
                        }
                    }


                    // =========================================
                    // FAILURE
                    // =========================================

                    @Override
                    public void onFailure(
                            int statusCode,
                            Header[] headers,
                            Throwable throwable,
                            JSONObject errorResponse) {

                        String errorMessage =
                                throwable.getMessage();


                        if (errorMessage == null) {

                            errorMessage =
                                    "Unable to connect to server";
                        }


                        Toast.makeText(
                                RegistrationActivity.this,
                                "Server Error: " +
                                        errorMessage,
                                Toast.LENGTH_LONG
                        ).show();


                        Log.e(
                                "REGISTER_ERROR",
                                "HTTP Status: " +
                                        statusCode +
                                        " | Error: " +
                                        errorMessage
                        );
                    }
                }
        );
    }
}