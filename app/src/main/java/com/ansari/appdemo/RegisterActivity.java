package com.ansari.appdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
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

    // =========================
    // EditTexts
    // =========================

    EditText etFullName;
    EditText etUsername;
    EditText etEmail;
    EditText etMobile;
    EditText etPassword;

    // =========================
    // Other Views
    // =========================

    Spinner spLanguage;

    CheckBox cbTerms;

    ImageView ivBack;
    ImageView ivPasswordEye;

    LinearLayout btnRegister;
    LinearLayout btnGoogle;
    LinearLayout btnPhone;

    TextView tvLogin;
    TextView tvTerms;


    // =========================
    // Password Visibility
    // =========================

    boolean passwordVisible = false;


    // =========================
    // Shared Preferences
    // =========================

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        // ==========================================
        // FIND VIEWS
        // ==========================================

        etFullName = findViewById(R.id.etFullName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);

        spLanguage = findViewById(R.id.spLanguage);

        cbTerms = findViewById(R.id.cbTerms);

        ivBack = findViewById(R.id.ivBack);
        ivPasswordEye = findViewById(R.id.ivPasswordEye);

        btnRegister = findViewById(R.id.btnRegister);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnPhone = findViewById(R.id.btnPhone);

        tvLogin = findViewById(R.id.tvLogin);
        tvTerms = findViewById(R.id.tvTerms);


        // ==========================================
        // SHARED PREFERENCES
        // ==========================================

        sharedPreferences = getSharedPreferences(
                "SanketSetuPrefs",
                MODE_PRIVATE
        );


        // ==========================================
        // LANGUAGE SPINNER
        // ==========================================

        String[] languages = {
                "Select Language",
                "English",
                "Hindi",
                "Marathi",
                "Gujarati",
                "Tamil",
                "Telugu",
                "Kannada",
                "Bengali"
        };


        ArrayAdapter<String> languageAdapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        languages
                );


        spLanguage.setAdapter(languageAdapter);


        // ==========================================
        // BACK BUTTON
        // ==========================================

        ivBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();

            }
        });


        // ==========================================
        // PASSWORD SHOW / HIDE
        // ==========================================

        ivPasswordEye.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (passwordVisible) {

                    // Hide password

                    etPassword.setInputType(
                            InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD
                    );

                    ivPasswordEye.setImageResource(
                            R.drawable.off_eyes
                    );

                    passwordVisible = false;

                } else {

                    // Show password

                    etPassword.setInputType(
                            InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    );

                    ivPasswordEye.setImageResource(
                            R.drawable.on_eyes
                    );

                    passwordVisible = true;
                }

                // Cursor end par rahega
                etPassword.setSelection(
                        etPassword.getText().length()
                );
            }
        });


        // ==========================================
        // REGISTER BUTTON
        // ==========================================

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                registerUser();

            }
        });


        // ==========================================
        // LOGIN NOW
        // ==========================================

        tvLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RegistrationActivity.this,
                        LoginActivity.class
                );

                startActivity(intent);

                finish();
            }
        });


        // ==========================================
        // TERMS
        // ==========================================

        tvTerms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(
                        RegistrationActivity.this,
                        "Terms & Conditions and Privacy Policy",
                        Toast.LENGTH_SHORT
                ).show();

            }
        });


        // ==========================================
        // GOOGLE
        // ==========================================

        btnGoogle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(
                        RegistrationActivity.this,
                        "Google registration will be available soon",
                        Toast.LENGTH_SHORT
                ).show();

            }
        });


        // ==========================================
        // PHONE
        // ==========================================

        btnPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(
                        RegistrationActivity.this,
                        "Phone registration will be available soon",
                        Toast.LENGTH_SHORT
                ).show();

            }
        });

    }


    // =========================================================
    // REGISTER USER
    // =========================================================

    private void registerUser() {


        // ==========================================
        // GET VALUES
        // ==========================================

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
        // VALIDATION
        // ==========================================

        if (fullName.isEmpty()) {

            etFullName.setError("Enter your full name");
            etFullName.requestFocus();

            return;
        }


        if (username.isEmpty()) {

            etUsername.setError("Enter username");
            etUsername.requestFocus();

            return;
        }


        if (email.isEmpty()) {

            etEmail.setError("Enter email address");
            etEmail.requestFocus();

            return;
        }


        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            etEmail.setError("Enter valid email address");
            etEmail.requestFocus();

            return;
        }


        if (mobile.isEmpty()) {

            etMobile.setError("Enter mobile number");
            etMobile.requestFocus();

            return;
        }


        if (mobile.length() != 10) {

            etMobile.setError(
                    "Mobile number must be 10 digits"
            );

            etMobile.requestFocus();

            return;
        }


        if (password.isEmpty()) {

            etPassword.setError("Create password");
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


        if (language.equals("Select Language")) {

            Toast.makeText(
                    RegistrationActivity.this,
                    "Please select your language",
                    Toast.LENGTH_SHORT
            ).show();

            spLanguage.requestFocus();

            return;
        }


        if (!cbTerms.isChecked()) {

            Toast.makeText(
                    RegistrationActivity.this,
                    "Please accept Terms & Conditions",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // ==========================================
        // REGISTER API
        // ==========================================

        registerToServer(
                fullName,
                username,
                email,
                mobile,
                password,
                language
        );
    }


    // =========================================================
    // SEND DATA TO PHP SERVER
    // =========================================================

    private void registerToServer(
            String fullName,
            String username,
            String email,
            String mobile,
            String password,
            String language) {


        // ==========================================
        // REQUEST PARAMETERS
        // ==========================================

        RequestParams params = new RequestParams();


        params.put("name", fullName);

        params.put("username", username);

        params.put("email", email);

        params.put("mobile", mobile);

        params.put("password", password);

        params.put("language", language);


        // ==========================================
        // ASYNC HTTP CLIENT
        // ==========================================

        AsyncHttpClient client = new AsyncHttpClient();


        client.post(
                Urls.registerUserURL,
                params,
                new JsonHttpResponseHandler() {


                    // ==========================================
                    // SUCCESS
                    // ==========================================

                    @Override
                    public void onSuccess(
                            int statusCode,
                            Header[] headers,
                            JSONObject response) {

                        try {

                            boolean success =
                                    response.getBoolean("success");

                            String message =
                                    response.optString(
                                            "message",
                                            "Registration completed"
                                    );


                            if (success) {

                                Toast.makeText(
                                        RegistrationActivity.this,
                                        message,
                                        Toast.LENGTH_LONG
                                ).show();


                                // ==================================
                                // SAVE USER DATA
                                // ==================================

                                SharedPreferences.Editor editor =
                                        sharedPreferences.edit();


                                editor.putBoolean(
                                        "isLogin",
                                        true
                                );

                                editor.putString(
                                        "name",
                                        fullName
                                );

                                editor.putString(
                                        "username",
                                        username
                                );

                                editor.putString(
                                        "email",
                                        email
                                );

                                editor.putString(
                                        "mobile",
                                        mobile
                                );

                                editor.putString(
                                        "language",
                                        language
                                );


                                editor.apply();


                                // ==================================
                                // OPEN MAIN ACTIVITY
                                // ==================================

                                Intent intent =
                                        new Intent(
                                                RegistrationActivity.this,
                                                MainActivity.class
                                        );


                                intent.setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK |
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                                );


                                startActivity(intent);

                                finish();


                            } else {

                                Toast.makeText(
                                        RegistrationActivity.this,
                                        message,
                                        Toast.LENGTH_LONG
                                ).show();
                            }


                        } catch (Exception e) {

                            Toast.makeText(
                                    RegistrationActivity.this,
                                    "Response error: "
                                            + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }


                    // ==========================================
                    // FAILURE RESPONSE
                    // ==========================================

                    @Override
                    public void onFailure(
                            int statusCode,
                            Header[] headers,
                            Throwable throwable,
                            JSONObject errorResponse) {


                        Toast.makeText(
                                RegistrationActivity.this,
                                "Server error: "
                                        + throwable.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }

                }
        );
    }
}