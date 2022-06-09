package com.example.mypetfinder.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import    androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypetfinder.R;
import com.example.mypetfinder.UserPet;

import com.example.mypetfinder.database.PetsDatabase;
import com.example.mypetfinder.entities.Pet;


import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateActivity extends AppCompatActivity {

    private EditText inputPetTitle, inputTelephone, inputPetText;
    private TextView textDateTime;
    private ImageView imagePet;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_STORAGE_IMAGE = 2;

    private DatabaseReference myDataBase;
    private String USER_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        inputPetTitle = findViewById(R.id.inputPetTitle);
        inputTelephone = findViewById(R.id.inputTelephone);
        inputPetText = findViewById(R.id.inputPetText);

        textDateTime = findViewById(R.id.textDateTime);

        imagePet = findViewById(R.id.imagePet);

        myDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);




        findViewById(R.id.layoutAddImage).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            )!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                        CreateActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSION
                );
            }
        });


        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(new Date())
        );

        ImageView imageSave = findViewById(R.id.imgDone);
        imageSave.setOnClickListener(v -> savePet());

    }

    private void savePet(){
        if(inputPetTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Pet title can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputTelephone.getText().toString().trim().isEmpty()
            && inputPetText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        final Pet pet = new Pet();
        pet.setTitle(inputPetTitle.getText().toString());
        pet.setTelephone(inputTelephone.getText().toString());
        pet.setPetText(inputPetText.getText().toString());
        pet.setDateTime(textDateTime.getText().toString());

        String id = myDataBase.getKey();
        String pet_name = inputPetTitle.getText().toString();
        String telephone = inputTelephone.getText().toString();
        String pet_text = inputPetText.getText().toString();

        UserPet newPet = new UserPet(id, pet_name, telephone, pet_text);
        myDataBase.push().setValue(newPet);


        @SuppressLint("StaticFieldLeak")
        class SavePetTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                PetsDatabase.getPetsDatabase(getApplicationContext()).petDao().insertPet(pet);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SavePetTask().execute();

    }


    public void goBack (View v) {
        Intent imgBack = new Intent (this, MainActivity.class);
        startActivity(imgBack);
    }



    private void selectImage() {
    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    if(intent.resolveActivity(getPackageManager())!= null){
        startActivityForResult(intent, REQUEST_CODE_STORAGE_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else{
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_STORAGE_IMAGE && resultCode == RESULT_OK){
            if (data != null){
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null){
                    try{
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imagePet.setImageBitmap(bitmap);
                        imagePet.setVisibility(View.VISIBLE);
                    }catch (Exception exception){
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


}