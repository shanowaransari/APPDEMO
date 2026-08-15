package com.ansari.appdemo;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomeActivity extends AppCompatActivity {

    // Drawer
    private DrawerLayout drawerLayout;
    private LinearLayout navigationDrawer;

    // Header
    private ImageButton btnMenu, btnNotification;

    // Quick Access Cards
    private LinearLayout cardSignToText, cardTextToSign, cardVoiceToSign;
    private LinearLayout cardEmergency, cardLearnISL, cardPractice;

    // Bottom Navigation
    private LinearLayout navHome, navLearn, navHistory, navProfile;
    private ImageButton navScan;

    // Learning Banner
    private TextView btnLetsGo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        initViews();
        setClickListeners();
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initViews() {

        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationDrawer = findViewById(R.id.navigationDrawer);


        // Header
        btnMenu = findViewById(R.id.btnMenu);
        btnNotification = findViewById(R.id.btnNotification);


        // Quick Access Cards
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


        // -----------------------------------------------------
        // HAMBURGER MENU → OPEN DRAWER
        // -----------------------------------------------------

        btnMenu.setOnClickListener(v -> {

            drawerLayout.openDrawer(navigationDrawer);

        });


        // -----------------------------------------------------
        // NOTIFICATION
        // -----------------------------------------------------

        btnNotification.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Notifications",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // DRAWER MENU
        // =====================================================

        LinearLayout menuHome = findViewById(R.id.menuHome);
        LinearLayout menuProfile = findViewById(R.id.menuProfile);
        LinearLayout menuAbout = findViewById(R.id.menuAbout);
        LinearLayout menuLogout = findViewById(R.id.menuLogout);


        // Drawer → Home
        menuHome.setOnClickListener(v -> {

            drawerLayout.closeDrawer(navigationDrawer);

            Toast.makeText(
                    HomeActivity.this,
                    "Home",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Drawer → My Profile
        menuProfile.setOnClickListener(v -> {

            drawerLayout.closeDrawer(navigationDrawer);

            navigateToProfile();

        });


        // Drawer → About Us
        menuAbout.setOnClickListener(v -> {

            drawerLayout.closeDrawer(navigationDrawer);

            Toast.makeText(
                    HomeActivity.this,
                    "About Us",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Drawer → Logout
        menuLogout.setOnClickListener(v -> {

            drawerLayout.closeDrawer(navigationDrawer);

            Toast.makeText(
                    HomeActivity.this,
                    "Logout",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // QUICK ACCESS
        // =====================================================


        // Sign → Text
        cardSignToText.setOnClickListener(v -> {

            navigateToSignToText();

        });


        // Text → Sign
        cardTextToSign.setOnClickListener(v -> {

            navigateToTextToSign();

        });


        // Voice → Sign
        cardVoiceToSign.setOnClickListener(v -> {

            navigateToVoiceToSign();

        });


        // Emergency
        cardEmergency.setOnClickListener(v -> {

            navigateToEmergency();

        });


        // Learn ISL
        cardLearnISL.setOnClickListener(v -> {

            navigateToLearnISL();

        });


        // Practice
        cardPractice.setOnClickListener(v -> {

            navigateToPractice();

        });


        // Let's Go
        btnLetsGo.setOnClickListener(v -> {

            navigateToLearnISL();

        });


        // =====================================================
        // BOTTOM NAVIGATION
        // =====================================================


        // Home
        navHome.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "You are already on Home",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Learn
        navLearn.setOnClickListener(v -> {

            navigateToLearnISL();

        });


        // Scan
        navScan.setOnClickListener(v -> {

            navigateToSignToText();

        });


        // History
        navHistory.setOnClickListener(v -> {

            navigateToHistory();

        });


        // Profile
        navProfile.setOnClickListener(v -> {

            navigateToProfile();

        });

    }


    // =========================================================
    // NAVIGATION METHODS
    // =========================================================


    private void navigateToSignToText() {

        Toast.makeText(
                this,
                "Opening Sign → Text",
                Toast.LENGTH_SHORT
        ).show();

        // Later:
        // Intent intent = new Intent(HomeActivity.this, SignToTextActivity.class);
        // startActivity(intent);

    }


    private void navigateToTextToSign() {

        Toast.makeText(
                this,
                "Opening Text → Sign",
                Toast.LENGTH_SHORT
        ).show();

        // Later:
        // Intent intent = new Intent(HomeActivity.this, TextToSignActivity.class);
        // startActivity(intent);

    }


    private void navigateToVoiceToSign() {

        Toast.makeText(
                this,
                "Opening Voice → Sign",
                Toast.LENGTH_SHORT
        ).show();

        // Later:
        // Intent intent = new Intent(HomeActivity.this, VoiceToSignActivity.class);
        // startActivity(intent);

    }


    private void navigateToEmergency() {

        Toast.makeText(
                this,
                "Opening Emergency",
                Toast.LENGTH_SHORT
        ).show();

        // Later:
        // Intent intent = new Intent(HomeActivity.this, EmergencyActivity.class);
        // startActivity(intent);

    }


    private void navigateToLearnISL() {

        Toast.makeText(
                this,
                "Opening Learn ISL",
                Toast.LENGTH_SHORT
        ).show();

        // Later:
        // Intent intent = new Intent(HomeActivity.this, LearnISLActivity.class);
        // startActivity(intent);

    }


    private void navigateToPractice() {

        Toast.makeText(
                this,
                "Opening Practice",
                Toast.LENGTH_SHORT
        ).show();

        // Later:
        // Intent intent = new Intent(HomeActivity.this, PracticeActivity.class;
        // startActivity(intent);

    }


    private void navigateToHistory() {

        Toast.makeText(
                this,
                "Opening History",
                Toast.LENGTH_SHORT
        ).show();

        // Later:
        // Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
        // startActivity(intent);

    }


    private void navigateToProfile() {

        Toast.makeText(
                this,
                "Opening Profile",
                Toast.LENGTH_SHORT
        ).show();

        // Later:
        // Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        // startActivity(intent);

    }

}