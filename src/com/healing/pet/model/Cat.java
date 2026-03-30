package com.healing.pet.model;

public class Cat extends Animal {
    public Cat(int id, String name, int age, String story, String photoPath) {
        super(id, name, age, story, photoPath);
    }

    @Override
    public String getVoice() { return "喵~ 想要一个温暖的抱抱。"; }

    @Override
    public String getCareGuide() { return "猫咪喜欢安静，给它准备好舒适的猫窝。"; }
}