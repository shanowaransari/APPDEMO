package com.ansari.appdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etNewPassword;
    EditText etConfirmPassword;

    Button btnResetPassword;
    TextView tvBackToLogin;

    // Apna actual API URL yaha lagana
    private static final String URL =
            "http://YOUR_IP/SanketSetuAPI/forgot_password.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot);

        // Find Views
        etUsername = findViewById(R.id.etUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // Reset Password
        btnResetPassword.setOnClickListener(v -> resetPassword());

        // Back To Login
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void resetPassword() {

        String username =
                etUsername.getText().toString().trim();

        String newPassword =
                etNewPassword.getText().toString().trim();

        String confirmPassword =
                etConfirmPassword.getText().toString().trim();


        // Username validation
        if (TextUtils.isEmpty(username)) {

            etUsername.setError("Enter username");
            etUsername.requestFocus();
            return;
        }


        // Password validation
        if (TextUtils.isEmpty(newPassword)) {

            etNewPassword.setError("Enter new password");
            etNewPassword.requestFocus();
            return;
        }


        // Minimum password length
        if (newPassword.length() < 6) {

            etNewPassword.setError(
                    "Password must be at least 6 characters"
            );

            etNewPassword.requestFocus();
            return;
        }


        // Confirm password
        if (TextUtils.isEmpty(confirmPassword)) {

            etConfirmPassword.setError(
                    "Confirm your password"
            );

            etConfirmPassword.requestFocus();
            return;
        }


        // Password matching
        if (!newPassword.equals(confirmPassword)) {

            etConfirmPassword.setError(
                    "Passwords do not match"
            );

            etConfirmPassword.requestFocus();
            return;
        }


        // Progress Dialog
        ProgressDialog progressDialog =
                new ProgressDialog(this);

        progressDialog.setMessage(
                "Resetting password..."
        );

        progressDialog.setCancelable(false);
        progressDialog.show();


        // Volley Request
        StringRequest request =
                new StringRequest(
                        Request.Method.POST,
                        URL,

                        response -> {

                            progressDialog.dismiss();

                            try {

                                JSONObject jsonObject =
                                        new JSONObject(response);

                                boolean success =
                                        jsonObject.getBoolean("success");

                                String message =
                                        jsonObject.getString("message");


                                Toast.makeText(
                                        ForgotPasswordActivity.this,
                                        message,
                                        Toast.LENGTH_LONG
                                ).show();


                                if (success) {

                                    Intent intent =
                                            new Intent(
                                                    ForgotPasswordActivity.this,
                                                    LoginActivity.class
                                            );

                                    intent.setFlags(
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    );

                                    startActivity(intent);

                                    finish();
                                }

                            } catch (JSONException e) {

                                Toast.makeText(
                                        ForgotPasswordActivity.this,
                                        "Invalid server response",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        },

                        error -> {

                            progressDialog.dismiss();

                            Toast.makeText(
                                    ForgotPasswordActivity.this,
                                    "Unable to connect to server",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                ) {

                    @Override
                    protected Map<String, String>
                    getParams() {

                        Map<String, String> params =
                                new HashMap<>();

                        params.put(
                                "username",
                                username
                        );

                        params.put(
                                "password",
                                newPassword
                        );

                        return params;
                    }
                };


        RequestQueue queue =
                Volley.newRequestQueue(this);

        queue.add(request);
    }
}