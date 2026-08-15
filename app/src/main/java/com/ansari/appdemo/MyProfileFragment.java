package com.ansari.appdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class MyProfileFragment extends Fragment {


    // ==========================================
    // VIEWS
    // ==========================================

    ImageView ivProfile;
    ImageView ivCamera;

    TextView tvChangePhoto;
    TextView tvLogout;
    TextView tvChangePassword;
    TextView tvPrivacy;
    TextView tvAbout;

    EditText etName;
    EditText etUsername;
    EditText etEmail;
    EditText etMobile;

    Spinner spLanguage;

    LinearLayout btnSave;


    // ==========================================
    // SHARED PREFERENCES
    // ==========================================

    SharedPreferences preferences;


    // ==========================================
    // CAMERA
    // ==========================================

    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {

                        if (isGranted) {
                            openCamera();
                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    "Camera permission is required",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    }
            );


    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() ==
                                requireActivity().RESULT_OK &&
                                result.getData() != null) {

                            Bundle extras =
                                    result.getData().getExtras();

                            if (extras != null) {

                                Bitmap bitmap =
                                        (Bitmap) extras.get("data");

                                if (bitmap != null) {

                                    ivProfile.setImageBitmap(bitmap);

                                    saveProfileImage(bitmap);
                                }
                            }
                        }
                    }
            );


    // ==========================================
    // GALLERY
    // ==========================================

    private final ActivityResultLauncher<String> galleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri != null) {

                            ivProfile.setImageURI(uri);

                            saveGalleryUri(uri);
                        }

                    });


    // ==========================================
    // ON CREATE VIEW
    // ==========================================

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_my_profile,
                container,
                false
        );


        // ==========================================
        // FIND VIEWS
        // ==========================================

        ivProfile =
                view.findViewById(R.id.ivProfile);

        ivCamera =
                view.findViewById(R.id.ivCamera);

        tvChangePhoto =
                view.findViewById(R.id.tvChangePhoto);

        tvLogout =
                view.findViewById(R.id.tvLogout);

        tvChangePassword =
                view.findViewById(R.id.tvChangePassword);

        tvPrivacy =
                view.findViewById(R.id.tvPrivacy);

        tvAbout =
                view.findViewById(R.id.tvAbout);

        etName =
                view.findViewById(R.id.etName);

        etUsername =
                view.findViewById(R.id.etUsername);

        etEmail =
                view.findViewById(R.id.etEmail);

        etMobile =
                view.findViewById(R.id.etMobile);

        spLanguage =
                view.findViewById(R.id.spLanguage);

        btnSave =
                view.findViewById(R.id.btnSave);


        // ==========================================
        // SHARED PREFERENCES
        // ==========================================

        preferences =
                requireActivity().getSharedPreferences(
                        "SanketSetuPrefs",
                        Context.MODE_PRIVATE
                );


        // ==========================================
        // LANGUAGE SPINNER
        // ==========================================

        setupLanguageSpinner();


        // ==========================================
        // LOAD PROFILE
        // ==========================================

        loadProfile();


        // ==========================================
        // CAMERA BUTTON
        // ==========================================

        ivCamera.setOnClickListener(v ->
                showImagePicker()
        );


        // ==========================================
        // CHANGE PHOTO
        // ==========================================

        tvChangePhoto.setOnClickListener(v ->
                showImagePicker()
        );


        // ==========================================
        // SAVE PROFILE
        // ==========================================

        btnSave.setOnClickListener(v ->
                saveProfile()
        );


        // ==========================================
        // LOGOUT
        // ==========================================

        tvLogout.setOnClickListener(v ->
                showLogoutDialog()
        );


        // ==========================================
        // CHANGE PASSWORD
        // ==========================================

        tvChangePassword.setOnClickListener(v -> {

            Toast.makeText(
                    requireContext(),
                    "Change Password",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // ==========================================
        // PRIVACY
        // ==========================================

        tvPrivacy.setOnClickListener(v -> {

            Toast.makeText(
                    requireContext(),
                    "Privacy Policy",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // ==========================================
        // ABOUT
        // ==========================================

        tvAbout.setOnClickListener(v -> {

            Toast.makeText(
                    requireContext(),
                    "SanketSetu - Digital Indian Sign Language",
                    Toast.LENGTH_LONG
            ).show();

        });


        return view;
    }


    // =========================================================
    // LANGUAGE SPINNER
    // =========================================================

    private void setupLanguageSpinner() {

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


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        languages
                );


        spLanguage.setAdapter(adapter);
    }


    // =========================================================
    // LOAD PROFILE
    // =========================================================

    private void loadProfile() {


        String name =
                preferences.getString(
                        "name",
                        ""
                );


        String username =
                preferences.getString(
                        "username",
                        ""
                );


        String email =
                preferences.getString(
                        "email",
                        ""
                );


        String mobile =
                preferences.getString(
                        "mobile",
                        ""
                );


        String language =
                preferences.getString(
                        "language",
                        "Select Language"
                );


        etName.setText(name);

        etUsername.setText(username);

        etEmail.setText(email);

        etMobile.setText(mobile);


        // ==========================================
        // SET LANGUAGE
        // ==========================================

        ArrayAdapter adapter =
                (ArrayAdapter) spLanguage.getAdapter();

        if (adapter != null) {

            int position =
                    adapter.getPosition(language);

            if (position >= 0) {

                spLanguage.setSelection(position);
            }
        }


        // ==========================================
        // LOAD SAVED PHOTO
        // ==========================================

        String imageUri =
                preferences.getString(
                        "profileImage",
                        ""
                );


        if (!imageUri.isEmpty()) {

            try {

                ivProfile.setImageURI(
                        Uri.parse(imageUri)
                );

            } catch (Exception e) {

                ivProfile.setImageResource(
                        R.drawable.ic_person
                );
            }
        }
    }


    // =========================================================
    // SAVE PROFILE
    // =========================================================

    private void saveProfile() {


        String name =
                etName.getText()
                        .toString()
                        .trim();


        String username =
                etUsername.getText()
                        .toString()
                        .trim();


        String email =
                etEmail.getText()
                        .toString()
                        .trim();


        String mobile =
                etMobile.getText()
                        .toString()
                        .trim();


        String language =
                spLanguage.getSelectedItem()
                        .toString();


        // ==========================================
        // VALIDATION
        // ==========================================

        if (name.isEmpty()) {

            etName.setError(
                    "Enter your name"
            );

            etName.requestFocus();

            return;
        }


        if (username.isEmpty()) {

            etUsername.setError(
                    "Enter username"
            );

            etUsername.requestFocus();

            return;
        }


        if (email.isEmpty()) {

            etEmail.setError(
                    "Enter email"
            );

            etEmail.requestFocus();

            return;
        }


        if (mobile.isEmpty()) {

            etMobile.setError(
                    "Enter mobile number"
            );

            etMobile.requestFocus();

            return;
        }


        if (mobile.length() != 10) {

            etMobile.setError(
                    "Enter valid 10 digit number"
            );

            etMobile.requestFocus();

            return;
        }


        if (language.equals("Select Language")) {

            Toast.makeText(
                    requireContext(),
                    "Select your language",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // ==========================================
        // SAVE
        // ==========================================

        SharedPreferences.Editor editor =
                preferences.edit();


        editor.putString(
                "name",
                name
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


        Toast.makeText(
                requireContext(),
                "Profile updated successfully ✓",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================================================
    // IMAGE PICKER DIALOG
    // =========================================================

    private void showImagePicker() {

        String[] options = {

                "📷  Camera",
                "🖼  Gallery",
                "❌  Cancel"

        };


        AlertDialog.Builder builder =
                new AlertDialog.Builder(
                        requireContext()
                );


        builder.setTitle(
                "Change Profile Photo"
        );


        builder.setItems(
                options,
                (dialog, which) -> {

                    if (which == 0) {

                        checkCameraPermission();

                    } else if (which == 1) {

                        openGallery();

                    } else {

                        dialog.dismiss();
                    }

                });


        builder.show();
    }


    // =========================================================
    // CAMERA PERMISSION
    // =========================================================

    private void checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {

            openCamera();

        } else {

            cameraPermissionLauncher.launch(
                    Manifest.permission.CAMERA
            );
        }
    }


    // =========================================================
    // OPEN CAMERA
    // =========================================================

    private void openCamera() {

        Intent intent =
                new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE
                );


        if (intent.resolveActivity(
                requireActivity().getPackageManager()
        ) != null) {

            cameraLauncher.launch(intent);

        } else {

            Toast.makeText(
                    requireContext(),
                    "Camera not available",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =========================================================
    // OPEN GALLERY
    // =========================================================

    private void openGallery() {

        galleryLauncher.launch(
                "image/*"
        );
    }


    // =========================================================
    // SAVE GALLERY URI
    // =========================================================

    private void saveGalleryUri(Uri uri) {

        preferences.edit()
                .putString(
                        "profileImage",
                        uri.toString()
                )
                .apply();


        Toast.makeText(
                requireContext(),
                "Profile photo updated ✓",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================================================
    // SAVE CAMERA IMAGE
    // =========================================================

    private void saveProfileImage(Bitmap bitmap) {

        try {

            String path =
                    MediaStore.Images.Media.insertImage(
                            requireActivity()
                                    .getContentResolver(),
                            bitmap,
                            "SanketSetu_Profile",
                            "Profile Photo"
                    );


            if (path != null) {

                preferences.edit()
                        .putString(
                                "profileImage",
                                path
                        )
                        .apply();


                Toast.makeText(
                        requireContext(),
                        "Profile photo updated ✓",
                        Toast.LENGTH_SHORT
                ).show();
            }


        } catch (Exception e) {

            Toast.makeText(
                    requireContext(),
                    "Unable to save photo",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =========================================================
    // LOGOUT DIALOG
    // =========================================================

    private void showLogoutDialog() {

        new AlertDialog.Builder(
                requireContext()
        )
                .setTitle("Logout")
                .setMessage(
                        "Are you sure you want to logout?"
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .setPositiveButton(
                        "Logout",
                        (dialog, which) -> logout()
                )
                .show();
    }


    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {


        preferences.edit()
                .clear()
                .apply();


        Intent intent =
                new Intent(
                        requireActivity(),
                        MyProfileFragment.class
                );


        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );


        startActivity(intent);


        requireActivity().finish();
    }
}