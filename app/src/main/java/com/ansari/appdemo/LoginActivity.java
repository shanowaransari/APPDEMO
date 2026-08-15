package com.ansari.appdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ansari.appdemo.Comman.Urls;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView btnLogin, tvForgotPassword, tvRegister;
    private ImageView ivBack, ivPasswordEye;
    private LinearLayout btnGoogle, btnPhone;

    private boolean passwordVisible = false;

    private SharedPreferences preferences;

    private static final String PREF_NAME = "SanketSetuPrefs";
    private static final String KEY_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(
                PREF_NAME,
                MODE_PRIVATE
        );

        // Check login status
        boolean isLoggedIn = preferences.getBoolean(
                KEY_LOGGED_IN,
                false
        );

        if (isLoggedIn) {
            openHome();
            return;
        }

        setContentView(R.layout.activity_login);

        initViews();
        setClickListeners();
    }

    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initViews() {

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);

        ivBack = findViewById(R.id.ivBack);
        ivPasswordEye = findViewById(R.id.ivPasswordEye);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnPhone = findViewById(R.id.btnPhone);
    }

    // =========================================================
    // CLICK LISTENERS
    // =========================================================

    private void setClickListeners() {

        // Back
        ivBack.setOnClickListener(v -> finish());

        // Password visibility
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

            etPassword.setSelection(
                    etPassword.getText().length()
            );
        });

        // Login
        btnLogin.setOnClickListener(v -> loginUser());

        // Forgot password
        tvForgotPassword.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    ForgotPasswordActivity.class
            );

            startActivity(intent);
        });

        // Register
        tvRegister.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegistrationActivity.class
            );

            startActivity(intent);
        });

        // Google
        btnGoogle.setOnClickListener(v -> {

            Toast.makeText(
                    LoginActivity.this,
                    "Google Login coming soon",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Phone
        btnPhone.setOnClickListener(v -> {

            Toast.makeText(
                    LoginActivity.this,
                    "Phone Login coming soon",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    // =========================================================
    // LOGIN USER
    // =========================================================

    private void loginUser() {

        String login = etEmail.getText()
                .toString()
                .trim();

        String password = etPassword.getText()
                .toString()
                .trim();

        if (login.isEmpty()) {

            etEmail.setError(
                    "Enter username or email"
            );

            etEmail.requestFocus();

            return;
        }

        if (password.isEmpty()) {

            etPassword.setError(
                    "Enter password"
            );

            etPassword.requestFocus();

            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Please Wait...");

        new Thread(() -> {

            HttpURLConnection connection = null;

            try {

                URL url = new URL(
                        Urls.LOGIN_URL
                );

                connection =
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
                        "login=" +
                                URLEncoder.encode(
                                        login,
                                        "UTF-8"
                                ) +
                                "&password=" +
                                URLEncoder.encode(
                                        password,
                                        "UTF-8"
                                );

                OutputStream outputStream =
                        connection.getOutputStream();

                outputStream.write(
                        postData.getBytes("UTF-8")
                );

                outputStream.flush();
                outputStream.close();

                int responseCode =
                        connection.getResponseCode();

                BufferedReader reader;

                if (responseCode >= 200 &&
                        responseCode < 400) {

                    reader = new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()
                            )
                    );

                } else {

                    reader = new BufferedReader(
                            new InputStreamReader(
                                    connection.getErrorStream()
                            )
                    );
                }

                StringBuilder response =
                        new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                String serverResponse =
                        response.toString();

                runOnUiThread(() -> {

                    btnLogin.setEnabled(true);
                    btnLogin.setText("Login  →");

                    try {

                        JSONObject json =
                                new JSONObject(
                                        serverResponse
                                );

                        String status =
                                json.optString(
                                        "status",
                                        "error"
                                );

                        String message =
                                json.optString(
                                        "message",
                                        "Login failed"
                                );

                        if (status.equalsIgnoreCase(
                                "success"
                        )) {

                            saveUserData(json);

                            showWelcomeDialog();

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
                            "Server connection failed: " +
                                    e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }

        }).start();
    }

    // =========================================================
    // SAVE USER DATA
    // =========================================================

    private void saveUserData(JSONObject json) {

        JSONObject user =
                json.optJSONObject("user");

        SharedPreferences.Editor editor =
                preferences.edit();

        if (user != null) {

            editor.putString(
                    "user_id",
                    user.optString(
                            "user_id",
                            ""
                    )
            );

            editor.putString(
                    "name",
                    user.optString(
                            "name",
                            ""
                    )
            );

            editor.putString(
                    "username",
                    user.optString(
                            "username",
                            ""
                    )
            );

            editor.putString(
                    "email",
                    user.optString(
                            "email",
                            ""
                    )
            );

            editor.putString(
                    "mobile",
                    user.optString(
                            "mobile",
                            ""
                    )
            );

            editor.putString(
                    "language",
                    user.optString(
                            "language",
                            "English"
                    )
            );
        }

        // IMPORTANT
        // Login successful
        editor.putBoolean(
                KEY_LOGGED_IN,
                true
        );

        editor.apply();
    }

    // =========================================================
    // WELCOME DIALOG
    // =========================================================

    private void showWelcomeDialog() {

        new AlertDialog.Builder(
                LoginActivity.this
        )
                .setTitle(
                        "Welcome to SanketSetu! 🎉"
                )
                .setMessage(
                        "Welcome to our app!\n\n" +
                                "We're happy to have you with us."
                )
                .setPositiveButton(
                        "Continue",
                        (dialog, which) -> openHome()
                )
                .setCancelable(false)
                .show();
    }

    // =========================================================
    // OPEN HOME
    // =========================================================

    private void openHome() {

        Intent intent = new Intent(
                LoginActivity.this,
                HomeActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }
}