package com.ansari.appdemo;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TextToSignActivity extends AppCompatActivity {

    private EditText etText;
    private Button btnTranslate, btnListen;
    private ImageView imgSign;
    private TextView tvMeaning, tvResult;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_sign);

        // ==============================
        // INITIALIZE VIEWS
        // ==============================

        etText = findViewById(R.id.etText);
        btnTranslate = findViewById(R.id.btnTranslate);
        btnListen = findViewById(R.id.btnListen);

        imgSign = findViewById(R.id.imgSign);
        tvMeaning = findViewById(R.id.tvMeaning);
        tvResult = findViewById(R.id.tvResult);


        // ==============================
        // TEXT TO SPEECH
        // ==============================

        textToSpeech = new TextToSpeech(
                this,
                status -> {

                    if (status == TextToSpeech.SUCCESS) {

                        textToSpeech.setLanguage(Locale.US);

                    }
                }
        );


        // ==============================
        // TRANSLATE BUTTON
        // ==============================

        btnTranslate.setOnClickListener(v -> {

            String text = etText.getText()
                    .toString()
                    .trim();

            if (text.isEmpty()) {

                etText.setError("Enter text");
                etText.requestFocus();
                return;
            }

            showSign(text);
        });


        // ==============================
        // LISTEN BUTTON
        // ==============================

        btnListen.setOnClickListener(v -> {

            String text = etText.getText()
                    .toString()
                    .trim();

            if (text.isEmpty()) {

                Toast.makeText(
                        this,
                        "Enter text first",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            textToSpeech.speak(
                    text,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
            );
        });
    }


    // ==============================
    // SHOW SIGN
    // ==============================

    private void showSign(String text) {

        String input = text
                .trim()
                .toUpperCase(Locale.ROOT);


        if (input.equals("HELLO")) {

            tvResult.setText("HELLO");

            tvMeaning.setText(
                    "A friendly greeting"
            );

            imgSign.setImageResource(
                    R.drawable.hello
            );


        } else if (input.equals("THANK YOU")) {

            tvResult.setText("THANK YOU");

            tvMeaning.setText(
                    "Expression of gratitude"
            );

            imgSign.setImageResource(
                    R.drawable.thank_you
            );


        } else if (input.equals("I LOVE YOU")) {

            tvResult.setText("I LOVE YOU");

            tvMeaning.setText(
                    "Expression of love and affection"
            );

            imgSign.setImageResource(
                    R.drawable.i_love_u
            );


        } else if (input.equals("YES")) {

            tvResult.setText("YES");

            tvMeaning.setText(
                    "Expression of agreement"
            );

            imgSign.setImageResource(
                    R.drawable.yes
            );


        } else if (input.equals("NO")) {

            tvResult.setText("NO");

            tvMeaning.setText(
                    "Expression of disagreement"
            );

            imgSign.setImageResource(
                    R.drawable.no
            );


        } else if (input.equals("PLEASE")) {

            tvResult.setText("PLEASE");

            tvMeaning.setText(
                    "A polite request"
            );

            imgSign.setImageResource(
                    R.drawable.please
            );


        } else if (input.equals("SORRY")) {

            tvResult.setText("SORRY");

            tvMeaning.setText(
                    "Expression of apology"
            );

            imgSign.setImageResource(
                    R.drawable.sorry
            );


        } else if (input.equals("HELP")) {

            tvResult.setText("HELP");

            tvMeaning.setText(
                    "Request for assistance"
            );

            imgSign.setImageResource(
                    R.drawable.help
            );


        } else if (input.equals("GOOD MORNING")) {

            tvResult.setText("GOOD MORNING");

            tvMeaning.setText(
                    "A morning greeting"
            );

            imgSign.setImageResource(
                    R.drawable.goodmorning
            );


        } else if (input.equals("GOODBYE")) {

            tvResult.setText("GOODBYE");

            tvMeaning.setText(
                    "A farewell greeting"
            );

            imgSign.setImageResource(
                    R.drawable.goodbye
            );


        } else {

            tvResult.setText("SIGN NOT AVAILABLE");

            tvMeaning.setText(
                    "This sign is not available yet."
            );

            imgSign.setImageResource(
                    R.drawable.sign_not_found
            );
        }
    }


    // ==============================
    // DESTROY
    // ==============================

    @Override
    protected void onDestroy() {

        if (textToSpeech != null) {

            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();
    }
}