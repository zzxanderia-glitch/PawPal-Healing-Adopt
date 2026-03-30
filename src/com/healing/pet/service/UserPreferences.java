package com.healing.pet.service;

public class UserPreferences {
    private String personality;    // 性格偏好：温顺、活泼等
    private String livingSpace;    // 居住面积：公寓、别墅等
    private String companionTime;// 陪伴时间：多、中、少等

    public UserPreferences(String personality, String livingSpace, String companionTime) {
        this.personality = personality;
        this.livingSpace = livingSpace;
        this.companionTime = companionTime;

    }

    // getter 和 setter（可以用 IDEA 自动生成：右键 → Generate → Getter and Setter）
    public String getPersonality() { return personality; }
    public void setPersonality(String personality) { this.personality = personality; }
    public String getLivingSpace() { return livingSpace; }
    public void setLivingSpace(String livingSpace) { this.livingSpace = livingSpace; }
    public String getCompanionTime() { return companionTime; }
    public void setCompanionTime(String companionTime) { this.companionTime = companionTime; }

}
