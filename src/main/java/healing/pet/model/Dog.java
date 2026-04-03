package main.java.healing.pet.model;

import healing.pet.model.Animal;

public class Dog extends Animal {
    private String breed;
    private String careGuide;
    private String personality;

    public Dog(int id, String name, int age, String story, String photoPath, String breed,
               String detailStory, String habits, String preference) {
        super(id, name, age, story, photoPath, detailStory, habits, preference);
        this.breed = breed;
        this.careGuide = generateCareGuide(story);
        this.personality = extractPersonality(story);
    }

    private String generateCareGuide(String story) {
        if (story == null || story.isEmpty()) return "忠诚狗狗，等待领养";
        StringBuilder sb = new StringBuilder();
        if (story.contains("活泼")) sb.append("精力充沛，需要每天散步。");
        if (story.contains("聪明")) sb.append("聪明伶俐，易于训练。");
        if (story.contains("温顺")) sb.append("性格温和，适合家庭。");
        return sb.length() > 0 ? sb.toString() : "忠诚狗狗，等待领养";
    }

    private String extractPersonality(String story) {
        if (story == null) return "活泼";
        if (story.contains("活泼")) return "活泼";
        if (story.contains("聪明")) return "聪明";
        return "活泼";
    }

    @Override
    public String getVoice() {
        return "汪汪！主人带我去散步吧~";
    }

    @Override
    public String getCareGuide() {
        return careGuide;
    }

    public String getBreed() {
        return breed;
    }

    public String getPersonality() {
        return personality;
    }
}