package com.ansari.appdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfile,ivCamera;
    TextView tvChangePhoto,tvChangePassword,tvPrivacy,tvAbout,tvLogout;
    EditText etName,etUsername,etEmail,etMobile;
    Spinner spLanguage;
    LinearLayout btnSave;
    SharedPreferences preferences;

    private static final String BASE_URL="http://10.11.57.114/sanketsetuAPI/";
    private static final String PROFILE_URL=BASE_URL+"profile.php";
    private static final String UPDATE_URL=BASE_URL+"updateprofile.php";

    ActivityResultLauncher<String> galleryLauncher;
    ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfile=findViewById(R.id.ivProfile);
        ivCamera=findViewById(R.id.ivCamera);
        tvChangePhoto=findViewById(R.id.tvChangePhoto);
        tvChangePassword=findViewById(R.id.tvChangePassword);
        tvPrivacy=findViewById(R.id.tvPrivacy);
        tvAbout=findViewById(R.id.tvAbout);
        tvLogout=findViewById(R.id.tvLogout);
        etName=findViewById(R.id.etName);
        etUsername=findViewById(R.id.etUsername);
        etEmail=findViewById(R.id.etEmail);
        etMobile=findViewById(R.id.etMobile);
        spLanguage=findViewById(R.id.spLanguage);
        btnSave=findViewById(R.id.btnSave);

        preferences=getSharedPreferences("SanketSetuUser",MODE_PRIVATE);

        setupLanguage();
        loadLocalProfile();
        loadProfileFromServer();

        galleryLauncher=registerForActivityResult(
                new ActivityResultContracts.GetContent(),uri->{
                    if(uri!=null){
                        ivProfile.setImageURI(uri);
                        preferences.edit().putString("profile_image",uri.toString()).apply();
                        Toast.makeText(this,"Profile photo updated",Toast.LENGTH_SHORT).show();
                    }
                });

        cameraLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),result->{
                    if(result.getResultCode()==RESULT_OK&&result.getData()!=null){
                        Bundle extras=result.getData().getExtras();
                        if(extras!=null){
                            Bitmap bitmap=(Bitmap)extras.get("data");
                            if(bitmap!=null){
                                ivProfile.setImageBitmap(bitmap);
                                Toast.makeText(this,"Profile photo updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        ivCamera.setOnClickListener(v->showImageOptions());
        tvChangePhoto.setOnClickListener(v->showImageOptions());
        btnSave.setOnClickListener(v->updateProfile());

        tvChangePassword.setOnClickListener(v->
                startActivity(new Intent(this,ForgotPasswordActivity.class)));

        tvPrivacy.setOnClickListener(v->
                Toast.makeText(this,"Privacy Policy",Toast.LENGTH_SHORT).show());

        tvAbout.setOnClickListener(v->
                Toast.makeText(this,"SanketSetu - About Us",Toast.LENGTH_SHORT).show());

        tvLogout.setOnClickListener(v->showLogoutDialog());
    }

    private void setupLanguage(){
        String[] languages={"English","Hindi","Marathi"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(
                this,android.R.layout.simple_spinner_item,languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLanguage.setAdapter(adapter);
    }

    private void loadLocalProfile(){
        etName.setText(preferences.getString("name",""));
        etUsername.setText(preferences.getString("username",""));
        etEmail.setText(preferences.getString("email",""));
        etMobile.setText(preferences.getString("mobile",""));

        String language=preferences.getString("language","English");
        ArrayAdapter adapter=(ArrayAdapter)spLanguage.getAdapter();
        int position=adapter.getPosition(language);
        if(position>=0)spLanguage.setSelection(position);

        String image=preferences.getString("profile_image","");
        if(!image.isEmpty()){
            try{
                ivProfile.setImageURI(Uri.parse(image));
            }catch(Exception ignored){}
        }
    }

    private void loadProfileFromServer(){
        String username=preferences.getString("username","");

        if(username.isEmpty()){
            Toast.makeText(this,"Username not found",Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("username",username);

        client.post(PROFILE_URL,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode,Header[] headers,JSONObject response){
                try{
                    String status=response.optString("status");

                    if(status.equals("success")){
                        JSONObject user=response.optJSONObject("user");

                        if(user==null){
                            Toast.makeText(ProfileActivity.this,
                                    "User data not found",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String name=user.optString("name","");
                        String userName=user.optString("username","");
                        String email=user.optString("email","");
                        String mobile=user.optString("mobile","");
                        String language=user.optString("language","English");

                        etName.setText(name);
                        etUsername.setText(userName);
                        etEmail.setText(email);
                        etMobile.setText(mobile);

                        ArrayAdapter adapter=(ArrayAdapter)spLanguage.getAdapter();
                        int position=adapter.getPosition(language);
                        if(position>=0)spLanguage.setSelection(position);

                        preferences.edit()
                                .putString("name",name)
                                .putString("username",userName)
                                .putString("email",email)
                                .putString("mobile",mobile)
                                .putString("language",language)
                                .apply();
                    }else{
                        Toast.makeText(ProfileActivity.this,
                                response.optString("message","Profile not found"),
                                Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    Toast.makeText(ProfileActivity.this,
                            "Profile response error",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode,Header[] headers,
                                  Throwable throwable,JSONObject errorResponse){
                Toast.makeText(ProfileActivity.this,
                        "Server connection failed: "+statusCode,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateProfile(){
        String name=etName.getText().toString().trim();
        String username=etUsername.getText().toString().trim();
        String email=etEmail.getText().toString().trim();
        String mobile=etMobile.getText().toString().trim();
        String language=spLanguage.getSelectedItem().toString();

        if(name.isEmpty()){
            etName.setError("Enter your name");
            return;
        }

        if(username.isEmpty()){
            etUsername.setError("Enter username");
            return;
        }

        if(email.isEmpty()||!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Enter valid email");
            return;
        }

        if(mobile.length()!=10){
            etMobile.setError("Enter valid 10 digit mobile number");
            return;
        }

        btnSave.setEnabled(false);

        RequestParams params=new RequestParams();
        params.put("name",name);
        params.put("username",username);
        params.put("email",email);
        params.put("mobile",mobile);
        params.put("language",language);

        new AsyncHttpClient().post(UPDATE_URL,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode,Header[] headers,JSONObject response){
                btnSave.setEnabled(true);

                String message=response.optString("message","Profile update failed");
                Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_LONG).show();

                if(response.optString("status").equals("success")){
                    preferences.edit()
                            .putString("name",name)
                            .putString("username",username)
                            .putString("email",email)
                            .putString("mobile",mobile)
                            .putString("language",language)
                            .apply();
                }
            }

            @Override
            public void onFailure(int statusCode,Header[] headers,
                                  Throwable throwable,JSONObject errorResponse){
                btnSave.setEnabled(true);
                Toast.makeText(ProfileActivity.this,
                        "Server connection failed",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showImageOptions(){
        String[] options={"Camera","Gallery","Cancel"};

        new AlertDialog.Builder(this)
                .setTitle("Change Profile Photo")
                .setItems(options,(dialog,which)->{
                    if(which==0)openCamera();
                    else if(which==1)galleryLauncher.launch("image/*");
                    else dialog.dismiss();
                }).show();
    }

    private void openCamera(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                !=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},101);
            return;
        }

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager())!=null)
            cameraLauncher.launch(intent);
        else
            Toast.makeText(this,"Camera not available",Toast.LENGTH_SHORT).show();
    }

    private void showLogoutDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Logout",(dialog,which)->logout())
                .show();
    }

    private void logout(){
        preferences.edit().clear().apply();

        Intent intent=new Intent(ProfileActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}