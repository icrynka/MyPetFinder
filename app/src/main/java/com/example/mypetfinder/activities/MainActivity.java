package com.example.mypetfinder.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;


import com.example.mypetfinder.R;
import com.example.mypetfinder.adapters.PetsAdapter;
import com.example.mypetfinder.database.PetsDatabase;
import com.example.mypetfinder.entities.Pet;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private RecyclerView petsRecyclerView;
    private List<Pet> petList;
    private PetsAdapter petsAdapter;

    public static final int REQUEST_CODE_ADD_PET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        petsRecyclerView = findViewById(R.id.petsRecyclerView);
        petsRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        petList = new ArrayList<>();
        petsAdapter = new PetsAdapter(petList);
        petsRecyclerView.setAdapter(petsAdapter);

        getPets();

        EditText inputSearch = findViewById(R.id.inputSearch);
           inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                petsAdapter.cancelTimer();

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (petList.size() !=0){
                    petsAdapter.searchPets(s.toString());
                }
            }
        });
    }



/*    public void startNewActivity(View v){
        Intent imageAddPetMain = new Intent(this, CreateActivity.class);
        startActivity(imageAddPetMain);
    }*/

    public void startNewActivity(View v) {
        Intent imageAddPetMain = new Intent(this, CreateActivity.class);
        startActivity(imageAddPetMain);
    }


    private void getPets() {

        @SuppressLint("StaticFieldLeak")
        class GetPetTask extends AsyncTask<Void, Void, List<Pet>> {

            @Override
            protected List<Pet> doInBackground(Void... voids) {
                return PetsDatabase
                        .getPetsDatabase(getApplicationContext())
                        .petDao().getAllPets();
            }

            @Override
            protected void onPostExecute(List<Pet> pets) {
                super.onPostExecute(pets);
                if (petList.size() == 0){
                    petList.addAll(pets);
                    petsAdapter.notifyDataSetChanged();
                }else{
                    petList.add(0, pets.get(0));
                    petsAdapter.notifyItemInserted(0);
                }

                petsRecyclerView.smoothScrollToPosition(0);
            }
        }

        new GetPetTask().execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_PET && resultCode == RESULT_OK){
            getPets();
        }
    }


}