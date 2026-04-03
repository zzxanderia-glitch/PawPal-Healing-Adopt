package healing.pet.model;

public class Cat extends Animal {
    private String careGuide;
    private String personality;

    public Cat(int id, String name, int age, String story, String photoPath) {
        super(id, name, age, story, photoPath);
        this.careGuide = generateCareGuide(story);
        this.personality = extractPersonality(story);
    }

    private String generateCareGuide(String story) {
        if (story == null || story.isEmpty()) {
            return "可爱的猫咪，需要您的关爱。";
        }
        StringBuilder guide = new StringBuilder();
        if (story.contains("温顺")) guide.append("性格温顺，适合安静环境。");
        if (story.contains("活泼")) guide.append("活泼好动，需要充足活动空间。");
        if (story.contains("粘人")) guide.append("喜欢陪伴，需要更多关爱。");
        if (story.contains("独立")) guide.append("性格独立，适应能力强。");
        return guide.length() > 0 ? guide.toString() : "可爱的猫咪，需要您的关爱。";
    }

    private String extractPersonality(String story) {
        if (story == null || story.isEmpty()) {
            return "温顺";
        }
        if (story.contains("温顺")) return "温顺";
        if (story.contains("独立")) return "独立";
        if (story.contains("粘人")) return "粘人";
        return "温顺";
    }

    public String getPersonality() {
        return personality;
    }

    @Override
    public String getVoice() { return "喵~ 想要一个温暖的抱抱。"; }

    @Override
    public String getCareGuide() { return careGuide; }
}