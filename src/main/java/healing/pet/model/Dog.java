package healing.pet.model;

public class Dog extends Animal {
    private String breed;
    private String careGuide;
    private String personality;

    public Dog(int id, String name, int age, String story, String photoPath, String breed) {
        super(id, name, age, story, photoPath);
        this.breed = breed;
        this.careGuide = generateCareGuide(story);
        this.personality = extractPersonality(story);
    }

    private String generateCareGuide(String story) {
        if (story == null || story.isEmpty()) {
            return "忠诚的狗狗，等待您的领养。";
        }
        StringBuilder guide = new StringBuilder();
        if (story.contains("活泼")) guide.append("精力充沛，需要每天散步。");
        if (story.contains("聪明")) guide.append("聪明伶俐，易于训练。");
        if (story.contains("温顺")) guide.append("性格温和，适合家庭饲养。");
        return guide.length() > 0 ? guide.toString() : "忠诚的狗狗，等待您的领养。";
    }

    private String extractPersonality(String story) {
        if (story == null || story.isEmpty()) {
            return "活泼";
        }
        if (story.contains("活泼")) return "活泼";
        if (story.contains("聪明")) return "聪明";
        return "活泼";
    }

    public String getPersonality() {
        return personality;
    }

    @Override
    public String getVoice() { return "汪汪！主人带我去散步吧~"; }

    @Override
    public String getCareGuide() { return careGuide; }
}
