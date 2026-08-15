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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etUsername, etNewPassword, etConfirmPassword;
    Button btnResetPassword;
    TextView tvBackToLogin;

    private static final String URL =
            "http://10.11.57.114/sanketsetuAPI/forgotpassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etUsername = findViewById(R.id.etUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        btnResetPassword.setOnClickListener(v -> resetPassword());

        tvBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ForgotPasswordActivity.this,
                    LoginActivity.class
            );
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );
            startActivity(intent);
            finish();
        });
    }

    private void resetPassword() {

        String username = etUsername.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Enter username");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError("Enter new password");
            etNewPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Password must be at least 6 characters");
            etNewPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm password");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Resetting password...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,

                response -> {

                    progressDialog.dismiss();

                    try {

                        JSONObject json = new JSONObject(response);

                        boolean success = json.optBoolean(
                                "success",
                                false
                        );

                        String message = json.optString(
                                "message",
                                "Password reset failed"
                        );

                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                message,
                                Toast.LENGTH_LONG
                        ).show();

                        if (success) {

                            Intent intent = new Intent(
                                    ForgotPasswordActivity.this,
                                    LoginActivity.class
                            );

                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                            );

                            startActivity(intent);
                            finish();
                        }

                    } catch (Exception e) {

                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                "Invalid server response",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                },

                error -> {

                    progressDialog.dismiss();

                    String message = "Unable to connect to server";

                    if (error.networkResponse != null) {
                        message += " (" +
                                error.networkResponse.statusCode +
                                ")";
                    }

                    Toast.makeText(
                            ForgotPasswordActivity.this,
                            message,
                            Toast.LENGTH_LONG
                    ).show();
                }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("username", username);
                params.put("newpassword", newPassword);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}