package com.example.mypetfinder;

public class UserPet {
    public String id, pet_name, telephone, pet_text;

    public UserPet(){

    }

    public UserPet(String id, String pet_name, String telephone, String pet_text) {
        this.id = id;
        this.pet_name = pet_name;
        this.telephone = telephone;
        this.pet_text = pet_text;
    }
}
