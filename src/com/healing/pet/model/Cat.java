package com.healing.pet.model;

public class Cat extends Animal {
    private String careGuide;
    private String personality;

    public Cat(int id, String name, int age, String story, String photoPath) {
        super(id, name, age, story, photoPath);
        this.careGuide = story;
        this.personality = extractPersonality(story);
    }

    private String extractPersonality(String story) {
        if (story.contains("温顺")) return "温顺";
        if (story.contains("独立")) return "独立";
        if (story.contains("粘人")) return "粘人";
        return "温顺";
    }

    @Override
    public String getVoice() { return "喵~ 想要一个温暖的抱抱。"; }

    @Override
    public String getCareGuide() { return careGuide; }
}