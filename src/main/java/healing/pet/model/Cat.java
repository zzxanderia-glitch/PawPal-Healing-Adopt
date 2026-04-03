package main.java.healing.pet.model;

import healing.pet.model.Animal;

public class Cat extends Animal {
    private String careGuide;
    private String personality;

    public Cat(int id, String name, int age, String story, String photoPath,
               String detailStory, String habits, String preference) {
        super(id, name, age, story, photoPath, detailStory, habits, preference);
        this.careGuide = generateCareGuide(story);
        this.personality = extractPersonality(story);
    }

    private String generateCareGuide(String story) {
        if (story == null || story.isEmpty()) return "可爱猫咪，需要关爱";
        StringBuilder sb = new StringBuilder();
        if (story.contains("温顺")) sb.append("性格温顺，适合安静环境。");
        if (story.contains("活泼")) sb.append("活泼好动，需要活动空间。");
        if (story.contains("粘人")) sb.append("喜欢陪伴，需要关爱。");
        if (story.contains("独立")) sb.append("性格独立，适应力强。");
        return sb.length() > 0 ? sb.toString() : "可爱猫咪，需要关爱";
    }

    private String extractPersonality(String story) {
        if (story == null) return "温顺";
        if (story.contains("温顺")) return "温顺";
        if (story.contains("独立")) return "独立";
        if (story.contains("粘人")) return "粘人";
        return "温顺";
    }

    @Override
    public String getVoice() {
        return "喵~ 想要一个温暖的抱抱。";
    }

    @Override
    public String getCareGuide() {
        return careGuide;
    }

    public String getPersonality() {
        return personality;
    }
}