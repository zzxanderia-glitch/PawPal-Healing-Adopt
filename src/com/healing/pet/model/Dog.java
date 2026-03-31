package com.healing.pet.model;

public class Dog extends Animal {
    private String breed;
    private String careGuide;
    private String personality;

    public Dog(int id, String name, int age, String story, String photoPath, String breed) {
        super(id, name, age, story, photoPath);
        this.breed = breed;
        this.careGuide = story;
        this.personality = extractPersonality(story);
    }

    private String extractPersonality(String story) {
        if (story.contains("活泼")) return "活泼";
        if (story.contains("聪明")) return "聪明";
        return "活泼";
    }

    @Override
    public String getVoice() { return "汪汪！主人带我去散步吧~"; }

    @Override
    public String getCareGuide() { return careGuide; }
}
