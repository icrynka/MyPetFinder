package com.example.mypetfinder.adapters;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypetfinder.R;
import com.example.mypetfinder.entities.Pet;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetViewHolder> {

    private List<Pet> pets;
    private Timer timer;
    private List<Pet> petsSource;

    public PetsAdapter(List<Pet> pets) {

        this.pets = pets;
        petsSource = pets;

    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PetViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {

        holder.setPet(pets.get(position));

    }

    @Override
    public int getItemCount() {
        return pets.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class PetViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textTelephone, textDateTime;
        LinearLayout layoutPet;
        RoundedImageView imagePet;


        PetViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textTelephone = itemView.findViewById(R.id.textTelephone);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            imagePet = itemView.findViewById(R.id.imagePet);

        }

        void setPet(Pet pet){
            textTitle.setText(pet.getTitle());
            if(pet.getTelephone().trim().isEmpty()){
                textTelephone.setVisibility(View.GONE);
            }else {
                textTelephone.setText(pet.getTelephone());
            }
            textDateTime.setText(pet.getDateTime());


            if(pet.getImagePath()!= null){
                imagePet.setImageBitmap(BitmapFactory.decodeFile(pet.getImagePath()));
                imagePet.setVisibility(View.VISIBLE);
            }else{
                imagePet.setVisibility(View.GONE);
            }
        }
    }

    public void searchPets (final String searchKeyword){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()){
                    pets = petsSource;
                }else {
                    ArrayList<Pet> temp = new ArrayList<>();
                    for (Pet pet : petsSource) {
                        if (pet.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                            || pet.getTelephone().toLowerCase().contains(searchKeyword.toLowerCase())
                            || pet.getPetText().toLowerCase().contains(searchKeyword.toLowerCase()) ){
                            temp.add(pet);
                        }
                    }
                    pets = temp;
                }
                new Handler(Looper.getMainLooper()).post(PetsAdapter.this::notifyDataSetChanged);

            }
        },500);
    }
    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
