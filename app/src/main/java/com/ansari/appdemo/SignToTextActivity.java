package com.ansari.appdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class SignToTextActivity extends AppCompatActivity {

    private ImageView imgCamera;
    private TextView tvResult;
    private TextView tvMeaning;

    private Button btnCamera;
    private Button btnHello;
    private Button btnThankYou;
    private Button btnHelp;
    private Button btnWater;
    private Button btnEmergency;

    // =========================================================
    // CAMERA RESULT
    // =========================================================

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            Bitmap bitmap =
                                    (Bitmap) result.getData()
                                            .getExtras()
                                            .get("data");

                            if (bitmap != null) {

                                imgCamera.setImageBitmap(bitmap);

                                tvResult.setText(
                                        "Select the detected sign"
                                );

                                tvMeaning.setText(
                                        "Choose a supported sign below."
                                );
                            }
                        }
                    }
            );

    // =========================================================
    // CAMERA PERMISSION
    // =========================================================

    private final ActivityResultLauncher<String> cameraPermission =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    granted -> {

                        if (granted) {

                            openCamera();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Camera permission is required",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
            );

    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_to_text);

        initViews();

        setClickListeners();
    }

    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initViews() {

        imgCamera = findViewById(R.id.imgCamera);

        tvResult = findViewById(R.id.tvResult);

        tvMeaning = findViewById(R.id.tvMeaning);

        btnCamera = findViewById(R.id.btnCamera);

        btnHello = findViewById(R.id.btnHello);

        btnThankYou = findViewById(R.id.btnThankYou);

        btnHelp = findViewById(R.id.btnHelp);

        btnWater = findViewById(R.id.btnWater);

        btnEmergency = findViewById(R.id.btnEmergency);
    }

    // =========================================================
    // CLICK LISTENERS
    // =========================================================

    private void setClickListeners() {

        // CAMERA
        btnCamera.setOnClickListener(v -> {

            if (checkSelfPermission(
                    Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED) {

                openCamera();

            } else {

                cameraPermission.launch(
                        Manifest.permission.CAMERA
                );
            }
        });

        // HELLO
        btnHello.setOnClickListener(v -> {

            showResult(
                    "HELLO",
                    "A friendly greeting"
            );
        });

        // THANK YOU
        btnThankYou.setOnClickListener(v -> {

            showResult(
                    "THANK YOU",
                    "Expression of gratitude"
            );
        });

        // HELP
        btnHelp.setOnClickListener(v -> {

            showResult(
                    "HELP",
                    "Request for assistance"
            );
        });

        // WATER
        btnWater.setOnClickListener(v -> {

            showResult(
                    "WATER",
                    "Request for water"
            );
        });

        // EMERGENCY
        btnEmergency.setOnClickListener(v -> {

            showResult(
                    "EMERGENCY",
                    "Urgent situation"
            );
        });
    }

    // =========================================================
    // OPEN CAMERA
    // =========================================================

    private void openCamera() {

        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        if (intent.resolveActivity(getPackageManager()) != null) {

            cameraLauncher.launch(intent);

        } else {

            Toast.makeText(
                    this,
                    "Camera is not available",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // =========================================================
    // SHOW RESULT
    // =========================================================

    private void showResult(
            String result,
            String meaning
    ) {

        tvResult.setText(result);

        tvMeaning.setText(meaning);
    }
}