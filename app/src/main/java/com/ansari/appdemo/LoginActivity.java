package com.ansari.appdemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView btnLogin, tvForgotPassword, tvRegister;
    ImageView ivBack, ivPasswordEye;
    LinearLayout btnGoogle, btnPhone;

    boolean passwordVisible = false;

    // IMPORTANT:
    // Yaha apne laptop ka IPv4 address aur folder name do
    private static final String LOGIN_URL =
            "http://192.168.1.5/sanketsetu/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Find Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);

        ivBack = findViewById(R.id.ivBack);
        ivPasswordEye = findViewById(R.id.ivPasswordEye);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnPhone = findViewById(R.id.btnPhone);


        // Back
        ivBack.setOnClickListener(v -> finish());


        // Password Show / Hide
        ivPasswordEye.setOnClickListener(v -> {

            if (passwordVisible) {

                etPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                );

                passwordVisible = false;

            } else {

                etPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );

                passwordVisible = true;
            }

            etPassword.setSelection(etPassword.length());
        });


        // Login
        btnLogin.setOnClickListener(v -> loginUser());


        // Forgot Password
        tvForgotPassword.setOnClickListener(v -> {

            Toast.makeText(
                    LoginActivity.this,
                    "Forgot Password",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Register
        // Abhi Activity reference nahi denge,
        // isliye RegisterActivity ka compile error nahi aayega.
        tvRegister.setOnClickListener(v -> {

            Toast.makeText(
                    LoginActivity.this,
                    "Create Account",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Google
        btnGoogle.setOnClickListener(v -> {

            Toast.makeText(
                    LoginActivity.this,
                    "Google Login",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Phone
        btnPhone.setOnClickListener(v -> {

            Toast.makeText(
                    LoginActivity.this,
                    "Phone Login",
                    Toast.LENGTH_SHORT
            ).show();

        });
    }


    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        // Email validation
        if (email.isEmpty()) {

            etEmail.setError("Enter email");
            etEmail.requestFocus();
            return;
        }


        // Password validation
        if (password.isEmpty()) {

            etPassword.setError("Enter password");
            etPassword.requestFocus();
            return;
        }


        btnLogin.setEnabled(false);
        btnLogin.setText("Please Wait...");


        new Thread(() -> {

            try {

                URL url = new URL(LOGIN_URL);

                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");

                connection.setDoOutput(true);
                connection.setDoInput(true);

                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                connection.setRequestProperty(
                        "Content-Type",
                        "application/x-www-form-urlencoded"
                );


                String postData =
                        "email=" + URLEncoder.encode(email, "UTF-8") +
                                "&password=" + URLEncoder.encode(password, "UTF-8");


                OutputStream outputStream =
                        connection.getOutputStream();

                outputStream.write(
                        postData.getBytes("UTF-8")
                );

                outputStream.flush();
                outputStream.close();


                int responseCode =
                        connection.getResponseCode();


                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        connection.getInputStream()
                                )
                        );


                StringBuilder response =
                        new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {

                    response.append(line);
                }

                reader.close();

                connection.disconnect();


                String serverResponse =
                        response.toString();


                runOnUiThread(() -> {

                    btnLogin.setEnabled(true);
                    btnLogin.setText("Login  →");

                    try {

                        JSONObject json =
                                new JSONObject(serverResponse);

                        int success =
                                json.getInt("success");

                        String message =
                                json.getString("message");


                        if (success == 1) {

                            Toast.makeText(
                                    LoginActivity.this,
                                    message,
                                    Toast.LENGTH_SHORT
                            ).show();


                            /*
                             * Login successful.
                             *
                             * Abhi HomeActivity ka reference
                             * nahi diya hai, taki compile error
                             * na aaye.
                             */

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    message,
                                    Toast.LENGTH_LONG
                            ).show();
                        }


                    } catch (Exception e) {

                        Toast.makeText(
                                LoginActivity.this,
                                "Invalid server response",
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });


            } catch (Exception e) {

                runOnUiThread(() -> {

                    btnLogin.setEnabled(true);
                    btnLogin.setText("Login  →");

                    Toast.makeText(
                            LoginActivity.this,
                            "Server connection failed",
                            Toast.LENGTH_LONG
                    ).show();

                });

            }

        }).start();
    }
}