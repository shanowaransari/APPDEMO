package com.ansari.appdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceToSignActivity extends AppCompatActivity {

    private Button btnSpeak;
    private TextView tvRecognizedText;
    private ImageView ivSign;
    private TextView tvMeaning;

    // Speech recognition launcher
    private final ActivityResultLauncher<Intent> speechLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK) {

                            Intent data = result.getData();

                            if (data != null) {

                                ArrayList<String> results =
                                        data.getStringArrayListExtra(
                                                RecognizerIntent.EXTRA_RESULTS
                                        );

                                if (results != null && !results.isEmpty()) {

                                    String spokenText =
                                            results.get(0)
                                                    .trim()
                                                    .toLowerCase(
                                                            Locale.getDefault()
                                                    );

                                    if (!spokenText.isEmpty()) {

                                        tvRecognizedText.setText(spokenText);

                                        showSign(spokenText);

                                    } else {

                                        tvRecognizedText.setText(
                                                "Could not understand speech"
                                        );
                                    }
                                }
                            }
                        }
                    }
            );

    // Microphone permission launcher
    private final ActivityResultLauncher<String> microphonePermission =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    granted -> {

                        if (granted) {

                            startVoiceRecognition();

                        } else {

                            tvRecognizedText.setText(
                                    "Microphone permission is required"
                            );
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_voice_to_sign);

        // Find views
        btnSpeak = findViewById(R.id.btnSpeak);
        tvRecognizedText = findViewById(R.id.tvRecognizedText);
        ivSign = findViewById(R.id.ivSign);
        tvMeaning = findViewById(R.id.tvMeaning);

        // Speak button
        btnSpeak.setOnClickListener(v -> {

            if (checkSelfPermission(
                    Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED) {

                startVoiceRecognition();

            } else {

                microphonePermission.launch(
                        Manifest.permission.RECORD_AUDIO
                );
            }
        });
    }

    private void startVoiceRecognition() {

        // Check speech recognition availability
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {

            tvRecognizedText.setText(
                    "Speech recognition is not available"
            );

            return;
        }

        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Speak now..."
        );

        speechLauncher.launch(intent);
    }

    private void showSign(String text) {

        if (text.contains("hello") || text.contains("hi")) {

            ivSign.setImageResource(
                    R.drawable.hello
            );

            tvMeaning.setText("Hello 👋");

        } else if (text.contains("help")) {

            ivSign.setImageResource(
                    R.drawable.help
            );

            tvMeaning.setText("Help 🆘");

        } else if (text.contains("water")) {

            ivSign.setImageResource(
                    R.drawable.water
            );

            tvMeaning.setText("Water 💧");

        } else if (text.contains("doctor")) {

            ivSign.setImageResource(
                    R.drawable.doctor
            );

            tvMeaning.setText("Doctor 🏥");

        } else if (text.contains("emergency")) {

            ivSign.setImageResource(
                    R.drawable.emergency
            );

            tvMeaning.setText("Emergency 🚨");

        } else if (
                text.contains("thank") ||
                        text.contains("thanks")
        ) {

            ivSign.setImageResource(
                    R.drawable.thank_you
            );

            tvMeaning.setText("Thank You 🙏");

        } else {

            ivSign.setImageResource(
                    R.drawable.sign_not_found
            );

            tvMeaning.setText(
                    "Sign not available for this word"
            );
        }
    }
}