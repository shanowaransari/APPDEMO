package com.ansari.appdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomeActivity extends AppCompatActivity {

    // =========================================================
    // DRAWER
    // =========================================================

    private DrawerLayout drawerLayout;


    // =========================================================
    // HEADER
    // =========================================================

    private ImageButton btnMenu;
    private ImageButton btnNotification;


    // =========================================================
    // QUICK ACCESS
    // =========================================================

    private LinearLayout cardSignToText;
    private LinearLayout cardTextToSign;
    private LinearLayout cardVoiceToSign;
    private LinearLayout cardEmergency;
    private LinearLayout cardLearnISL;
    private LinearLayout cardPractice;


    // =========================================================
    // BOTTOM NAVIGATION
    // =========================================================

    private LinearLayout navHome;
    private LinearLayout navLearn;
    private LinearLayout navHistory;
    private LinearLayout navProfile;

    private ImageButton navScan;


    // =========================================================
    // LEARNING BANNER
    // =========================================================

    private TextView btnLetsGo;


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        initViews();

        setClickListeners();

        setupDrawerMenu();
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initViews() {

        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout);

        // Header
        btnMenu = findViewById(R.id.btnMenu);
        btnNotification = findViewById(R.id.btnNotification);

        // Quick Access
        cardSignToText = findViewById(R.id.cardSignToText);
        cardTextToSign = findViewById(R.id.cardTextToSign);
        cardVoiceToSign = findViewById(R.id.cardVoiceToSign);
        cardEmergency = findViewById(R.id.cardEmergency);
        cardLearnISL = findViewById(R.id.cardLearnISL);
        cardPractice = findViewById(R.id.cardPractice);

        // Learning Banner
        btnLetsGo = findViewById(R.id.btnLetsGo);

        // Bottom Navigation
        navHome = findViewById(R.id.navHome);
        navLearn = findViewById(R.id.navLearn);
        navScan = findViewById(R.id.navScan);
        navHistory = findViewById(R.id.navHistory);
        navProfile = findViewById(R.id.navProfile);
    }


    // =========================================================
    // CLICK LISTENERS
    // =========================================================

    private void setClickListeners() {

        // =====================================================
        // MENU
        // =====================================================

        btnMenu.setOnClickListener(v -> {

            if (drawerLayout != null) {

                drawerLayout.openDrawer(
                        GravityCompat.START
                );
            }
        });


        // =====================================================
        // NOTIFICATION
        // =====================================================

        btnNotification.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "No new notifications",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // =====================================================
        // SIGN TO TEXT
        // =====================================================

        cardSignToText.setOnClickListener(v -> {

            openSignToText();
        });


        // =====================================================
        // TEXT TO SIGN
        // =====================================================

        cardTextToSign.setOnClickListener(v -> {

            openTextToSign();
        });


        // =====================================================
        // VOICE TO SIGN
        // =====================================================

        cardVoiceToSign.setOnClickListener(v -> {

            openVoiceToSign();
        });


        // =====================================================
        // EMERGENCY
        // =====================================================

        cardEmergency.setOnClickListener(v -> {

            openEmergency();
        });


        // =====================================================
        // LEARN ISL
        // =====================================================

        cardLearnISL.setOnClickListener(v -> {

            openLearnISL();
        });


        // =====================================================
        // PRACTICE
        // =====================================================

        cardPractice.setOnClickListener(v -> {

            openPractice();
        });


        // =====================================================
        // LET'S GO
        // =====================================================

        btnLetsGo.setOnClickListener(v -> {

            openLearnISL();
        });


        // =====================================================
        // BOTTOM HOME
        // =====================================================

        navHome.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "You are already on Home",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // =====================================================
        // BOTTOM LEARN
        // =====================================================

        navLearn.setOnClickListener(v -> {

            openLearnISL();
        });


        // =====================================================
        // BOTTOM SCAN
        // =====================================================

        navScan.setOnClickListener(v -> {

            openSignToText();
        });


        // =====================================================
        // BOTTOM HISTORY
        // =====================================================

        navHistory.setOnClickListener(v -> {

            openHistory();
        });


        // =====================================================
        // BOTTOM PROFILE
        // =====================================================

        navProfile.setOnClickListener(v -> {

            openProfile();
        });
    }


    // =========================================================
    // DRAWER MENU
    // =========================================================

    private void setupDrawerMenu() {

        // =====================================================
        // MY PROFILE
        // =====================================================

        TextView menuProfile =
                findViewById(R.id.menuProfile);

        if (menuProfile != null) {

            menuProfile.setOnClickListener(v -> {

                drawerLayout.closeDrawer(
                        GravityCompat.START
                );

                openProfile();
            });
        }


        // =====================================================
        // NOTIFICATIONS
        // =====================================================

        TextView menuNotifications =
                findViewById(R.id.menuNotifications);

        if (menuNotifications != null) {

            menuNotifications.setOnClickListener(v -> {

                drawerLayout.closeDrawer(
                        GravityCompat.START
                );

                Toast.makeText(
                        HomeActivity.this,
                        "Notifications",
                        Toast.LENGTH_SHORT
                ).show();
            });
        }


        // =====================================================
        // SETTINGS
        // =====================================================

        TextView menuSettings =
                findViewById(R.id.menuSettings);

        if (menuSettings != null) {

            menuSettings.setOnClickListener(v -> {

                drawerLayout.closeDrawer(
                        GravityCompat.START
                );

                Toast.makeText(
                        HomeActivity.this,
                        "Settings",
                        Toast.LENGTH_SHORT
                ).show();
            });
        }


        // =====================================================
        // ABOUT
        // =====================================================

        TextView menuAbout =
                findViewById(R.id.menuAbout);

        if (menuAbout != null) {

            menuAbout.setOnClickListener(v -> {

                // Close drawer
                drawerLayout.closeDrawer(
                        GravityCompat.START
                );

                // Open About Us Activity
                Intent intent = new Intent(
                        HomeActivity.this,
                        AboutUsActivity.class
                );

                startActivity(intent);
            });
        }


        // =====================================================
        // LOGOUT
        // =====================================================

        TextView menuLogout =
                findViewById(R.id.menuLogout);

        if (menuLogout != null) {

            menuLogout.setOnClickListener(v -> {

                showLogoutDialog();
            });
        }
    }


    // =========================================================
    // LOGOUT ALERT DIALOG
    // =========================================================

    private void showLogoutDialog() {

        new AlertDialog.Builder(this)

                .setTitle("Logout")

                .setMessage(
                        "Are you sure you want to logout?"
                )

                .setNegativeButton(
                        "Cancel",
                        (dialog, which) ->
                                dialog.dismiss()
                )

                .setPositiveButton(
                        "Logout",
                        (dialog, which) ->
                                performLogout()
                )

                .show();
    }


    // =========================================================
    // PERFORM LOGOUT
    // =========================================================

    private void performLogout() {

        getSharedPreferences(
                "SanketSetuPrefs",
                MODE_PRIVATE
        )
                .edit()
                .clear()
                .apply();

        Toast.makeText(
                this,
                "Logged out successfully",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent(
                HomeActivity.this,
                LoginActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }


    // =========================================================
    // SIGN TO TEXT
    // =========================================================

    private void openSignToText() {

        Intent intent = new Intent(
                HomeActivity.this,
                SignToTextActivity.class
        );

        startActivity(intent);
    }


    // =========================================================
    // TEXT TO SIGN
    // =========================================================

    private void openTextToSign() {

        Intent intent = new Intent(
                HomeActivity.this,
                TextToSignActivity.class
        );

        startActivity(intent);
    }


    // =========================================================
    // VOICE TO SIGN
    // =========================================================

    private void openVoiceToSign() {

        Intent intent = new Intent(
                HomeActivity.this,
                VoiceToSignActivity.class
        );

        startActivity(intent);
    }


    // =========================================================
    // EMERGENCY
    // =========================================================

    private void openEmergency() {

        Toast.makeText(
                this,
                "Emergency",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================================================
    // LEARN ISL
    // =========================================================

    private void openLearnISL() {

        Toast.makeText(
                this,
                "Learn ISL",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================================================
    // PRACTICE
    // =========================================================

    private void openPractice() {

        Toast.makeText(
                this,
                "Practice",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================================================
    // HISTORY
    // =========================================================

    private void openHistory() {

        Toast.makeText(
                this,
                "History",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================================================
    // PROFILE
    // =========================================================

    private void openProfile() {

        Intent intent = new Intent(
                HomeActivity.this,
                ProfileActivity.class
        );

        startActivity(intent);
    }


    // =========================================================
    // BACK BUTTON
    // =========================================================

    @Override
    public void onBackPressed() {

        if (drawerLayout != null
                && drawerLayout.isDrawerOpen(
                GravityCompat.START
        )) {

            drawerLayout.closeDrawer(
                    GravityCompat.START
            );

        } else {

            super.onBackPressed();
        }
    }
}