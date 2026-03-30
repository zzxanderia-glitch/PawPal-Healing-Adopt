package com.healing.pet.model;

public class Dog extends Animal {
    private String breed; // 品种

    public Dog(int id, String name, int age, String story, String photoPath, String breed) {
        super(id, name, age, story, photoPath);
        this.breed = breed;
    }

    @Override
    public String getVoice() { return "汪汪！主人带我去散步吧~"; }

    @Override
    public String getCareGuide() { return "狗狗需要每天运动，记得按时遛狗哦。"; }
}
