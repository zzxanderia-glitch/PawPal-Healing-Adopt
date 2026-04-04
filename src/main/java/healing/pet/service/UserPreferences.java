package healing.pet.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserPreferences implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 维度常量标识 ====================
    public static final String DIM_PERSONALITY = "personality";
    public static final String DIM_LIVING_SPACE = "livingSpace";
    public static final String DIM_COMPANION_TIME = "companionTime";
    public static final String DIM_HEALTH_CARE = "healthCare";
    public static final String DIM_SHEDDING = "shedding";
    public static final String DIM_BUDGET = "budget";

    // ==================== 维度显示名称 ====================
    public static final Map<String, String> DIM_NAMES = new HashMap<>();

    static {
        DIM_NAMES.put(DIM_PERSONALITY, "性格合拍");
        DIM_NAMES.put(DIM_LIVING_SPACE, "居住空间");
        DIM_NAMES.put(DIM_COMPANION_TIME, "陪伴时间");
        DIM_NAMES.put(DIM_HEALTH_CARE, "健康护理");
        DIM_NAMES.put(DIM_SHEDDING, "掉毛容忍");
        DIM_NAMES.put(DIM_BUDGET, "养宠预算");
    }

    // ==================== 各维度得分（1-5分） ====================
    private int personalityScore;      // 性格合拍度
    private int livingSpaceScore;      // 居住空间适配度
    private int companionTimeScore;    // 陪伴时间充裕度
    private int healthCareScore;       // 健康护理意愿
    private int sheddingScore;         // 掉毛容忍度
    private int budgetScore;           // 养宠预算

    // ==================== 各维度答案文本（用于治愈系报告） ====================
    private String personalityAnswer;
    private String livingSpaceAnswer;
    private String companionTimeAnswer;
    private String healthCareAnswer;
    private String sheddingAnswer;
    private String budgetAnswer;

    // ==================== 扩展支持 ====================
    private Map<String, Integer> extraScores;      // 额外维度分数
    private Map<String, String> extraAnswers;      // 额外维度答案文本
    private long createTime;                        // 创建时间戳
    private String userId;                          // 用户ID（可选）

    // ==================== 构造函数 ====================

    /**
     * 默认构造函数，所有维度初始化为3分（中性）
     */
    public UserPreferences() {
        this.personalityScore = 3;
        this.livingSpaceScore = 3;
        this.companionTimeScore = 3;
        this.healthCareScore = 3;
        this.sheddingScore = 3;
        this.budgetScore = 3;
        this.extraScores = new HashMap<>();
        this.extraAnswers = new HashMap<>();
        this.createTime = System.currentTimeMillis();
    }

    /**
     * 完整构造函数（仅分数）
     */
    public UserPreferences(int personalityScore, int livingSpaceScore, int companionTimeScore,
                           int healthCareScore, int sheddingScore, int budgetScore) {
        this();
        setPersonalityScore(personalityScore);
        setLivingSpaceScore(livingSpaceScore);
        setCompanionTimeScore(companionTimeScore);
        setHealthCareScore(healthCareScore);
        setSheddingScore(sheddingScore);
        setBudgetScore(budgetScore);
    }

    /**
     * 完整构造函数（分数 + 答案文本）
     */
    public UserPreferences(int personalityScore, int livingSpaceScore, int companionTimeScore,
                           int healthCareScore, int sheddingScore, int budgetScore,
                           String personalityAnswer, String livingSpaceAnswer, String companionTimeAnswer,
                           String healthCareAnswer, String sheddingAnswer, String budgetAnswer) {
        this(personalityScore, livingSpaceScore, companionTimeScore,
                healthCareScore, sheddingScore, budgetScore);
        this.personalityAnswer = personalityAnswer;
        this.livingSpaceAnswer = livingSpaceAnswer;
        this.companionTimeAnswer = companionTimeAnswer;
        this.healthCareAnswer = healthCareAnswer;
        this.sheddingAnswer = sheddingAnswer;
        this.budgetAnswer = budgetAnswer;
    }

    // ==================== 分数校验 ====================
    private int validate(int score) {
        return Math.max(1, Math.min(5, score));
    }

    // ==================== Getter & Setter（核心维度） ====================

    public int getPersonalityScore() {
        return personalityScore;
    }

    public void setPersonalityScore(int personalityScore) {
        this.personalityScore = validate(personalityScore);
    }

    public int getLivingSpaceScore() {
        return livingSpaceScore;
    }

    public void setLivingSpaceScore(int livingSpaceScore) {
        this.livingSpaceScore = validate(livingSpaceScore);
    }

    public int getCompanionTimeScore() {
        return companionTimeScore;
    }

    public void setCompanionTimeScore(int companionTimeScore) {
        this.companionTimeScore = validate(companionTimeScore);
    }

    public int getHealthCareScore() {
        return healthCareScore;
    }

    public void setHealthCareScore(int healthCareScore) {
        this.healthCareScore = validate(healthCareScore);
    }

    public int getSheddingScore() {
        return sheddingScore;
    }

    public void setSheddingScore(int sheddingScore) {
        this.sheddingScore = validate(sheddingScore);
    }

    public int getBudgetScore() {
        return budgetScore;
    }

    public void setBudgetScore(int budgetScore) {
        this.budgetScore = validate(budgetScore);
    }

    // ==================== Getter & Setter（答案文本） ====================

    public String getPersonalityAnswer() {
        return personalityAnswer;
    }

    public void setPersonalityAnswer(String personalityAnswer) {
        this.personalityAnswer = personalityAnswer;
    }

    public String getLivingSpaceAnswer() {
        return livingSpaceAnswer;
    }

    public void setLivingSpaceAnswer(String livingSpaceAnswer) {
        this.livingSpaceAnswer = livingSpaceAnswer;
    }

    public String getCompanionTimeAnswer() {
        return companionTimeAnswer;
    }

    public void setCompanionTimeAnswer(String companionTimeAnswer) {
        this.companionTimeAnswer = companionTimeAnswer;
    }

    public String getHealthCareAnswer() {
        return healthCareAnswer;
    }

    public void setHealthCareAnswer(String healthCareAnswer) {
        this.healthCareAnswer = healthCareAnswer;
    }

    public String getSheddingAnswer() {
        return sheddingAnswer;
    }

    public void setSheddingAnswer(String sheddingAnswer) {
        this.sheddingAnswer = sheddingAnswer;
    }

    public String getBudgetAnswer() {
        return budgetAnswer;
    }

    public void setBudgetAnswer(String budgetAnswer) {
        this.budgetAnswer = budgetAnswer;
    }

    // ==================== 动态扩展维度 ====================

    /**
     * 设置额外维度的分数
     */
    public void setExtraScore(String dimension, int score) {
        if (extraScores == null) extraScores = new HashMap<>();
        extraScores.put(dimension, validate(score));
    }

    /**
     * 获取额外维度的分数，默认返回3分
     */
    public int getExtraScore(String dimension) {
        return extraScores != null ? extraScores.getOrDefault(dimension, 3) : 3;
    }

    /**
     * 设置额外维度的答案文本
     */
    public void setExtraAnswer(String dimension, String answer) {
        if (extraAnswers == null) extraAnswers = new HashMap<>();
        extraAnswers.put(dimension, answer);
    }

    /**
     * 获取额外维度的答案文本
     */
    public String getExtraAnswer(String dimension) {
        return extraAnswers != null ? extraAnswers.getOrDefault(dimension, "") : "";
    }

    /**
     * 获取所有额外维度分数
     */
    public Map<String, Integer> getAllExtraScores() {
        return extraScores != null ? new HashMap<>(extraScores) : new HashMap<>();
    }

    // ==================== 向量转换（用于相似度计算） ====================

    /**
     * 将核心维度转换为double数组，顺序与匹配算法对齐
     */
    public double[] toVector() {
        return new double[]{
                personalityScore, livingSpaceScore, companionTimeScore,
                healthCareScore, sheddingScore, budgetScore
        };
    }

    /**
     * 获取所有核心维度分数的Map
     */
    public Map<String, Integer> getBaseScores() {
        Map<String, Integer> map = new HashMap<>();
        map.put(DIM_PERSONALITY, personalityScore);
        map.put(DIM_LIVING_SPACE, livingSpaceScore);
        map.put(DIM_COMPANION_TIME, companionTimeScore);
        map.put(DIM_HEALTH_CARE, healthCareScore);
        map.put(DIM_SHEDDING, sheddingScore);
        map.put(DIM_BUDGET, budgetScore);
        return map;
    }

    /**
     * 获取所有核心维度答案的Map
     */
    public Map<String, String> getBaseAnswers() {
        Map<String, String> map = new HashMap<>();
        map.put(DIM_PERSONALITY, personalityAnswer);
        map.put(DIM_LIVING_SPACE, livingSpaceAnswer);
        map.put(DIM_COMPANION_TIME, companionTimeAnswer);
        map.put(DIM_HEALTH_CARE, healthCareAnswer);
        map.put(DIM_SHEDDING, sheddingAnswer);
        map.put(DIM_BUDGET, budgetAnswer);
        return map;
    }

    // ==================== 历史记录相关 ====================

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // ==================== 匹配度辅助方法 ====================

    /**
     * 计算某个维度的得分描述（用于生成治愈系文案）
     */
    public String getScoreDescription(String dimension) {
        int score = getScoreByDimension(dimension);
        switch (score) {
            case 1:
                return "非常低";
            case 2:
                return "较低";
            case 3:
                return "适中";
            case 4:
                return "较高";
            case 5:
                return "非常高";
            default:
                return "适中";
        }
    }

    /**
     * 根据维度名称获取分数
     */
    public int getScoreByDimension(String dimension) {
        switch (dimension) {
            case DIM_PERSONALITY:
                return personalityScore;
            case DIM_LIVING_SPACE:
                return livingSpaceScore;
            case DIM_COMPANION_TIME:
                return companionTimeScore;
            case DIM_HEALTH_CARE:
                return healthCareScore;
            case DIM_SHEDDING:
                return sheddingScore;
            case DIM_BUDGET:
                return budgetScore;
            default:
                return getExtraScore(dimension);
        }
    }

    /**
     * 获取维度的答案文本（优先使用存储的答案，否则根据分数生成默认描述）
     */
    public String getAnswerByDimension(String dimension) {
        String answer = null;
        switch (dimension) {
            case DIM_PERSONALITY:
                answer = personalityAnswer;
                break;
            case DIM_LIVING_SPACE:
                answer = livingSpaceAnswer;
                break;
            case DIM_COMPANION_TIME:
                answer = companionTimeAnswer;
                break;
            case DIM_HEALTH_CARE:
                answer = healthCareAnswer;
                break;
            case DIM_SHEDDING:
                answer = sheddingAnswer;
                break;
            case DIM_BUDGET:
                answer = budgetAnswer;
                break;
            default:
                answer = getExtraAnswer(dimension);
        }
        return answer != null ? answer : generateDefaultAnswer(dimension, getScoreByDimension(dimension));
    }

    /**
     * 根据分数生成默认答案描述
     */
    private String generateDefaultAnswer(String dimension, int score) {
        String dimName = DIM_NAMES.getOrDefault(dimension, dimension);
        String[] levels = {"非常不喜欢", "不太喜欢", "一般", "比较喜欢", "非常喜欢"};
        return String.format("对%s的偏好程度：%s", dimName, levels[score - 1]);
    }

    // ==================== 治愈系功能 ====================

    /**
     * 获取用户的养宠类型画像（用于生成治愈语录）
     */
    public String getUserPetType() {
        if (personalityScore >= 4 && companionTimeScore >= 4) {
            return "粘人型主人";
        } else if (personalityScore <= 2 && companionTimeScore <= 2) {
            return "独立型主人";
        } else if (livingSpaceScore >= 4) {
            return "空间充足型主人";
        } else if (budgetScore >= 4) {
            return "精致护理型主人";
        } else {
            return "随缘型主人";
        }
    }

    /**
     * 获取治愈系问卷总结文案
     */
    public String getHealingSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("🌟 你的养宠画像：").append(getUserPetType()).append("\n");
        sb.append("💖 你期待一个");

        if (personalityScore >= 4) sb.append("粘人可爱");
        else if (personalityScore <= 2) sb.append("独立安静");
        else sb.append("随性自在");

        sb.append("的小伙伴，");

        if (companionTimeScore >= 4) sb.append("愿意花大量时间陪伴");
        else if (companionTimeScore <= 2) sb.append("希望它有自己的小世界");
        else sb.append("愿意相互陪伴但互不打扰");

        sb.append("。");

        return sb.toString();
    }

    // ==================== 工具方法 ====================

    /**
     * 深拷贝当前对象
     */
    public UserPreferences copy() {
        UserPreferences copy = new UserPreferences(
                personalityScore, livingSpaceScore, companionTimeScore,
                healthCareScore, sheddingScore, budgetScore,
                personalityAnswer, livingSpaceAnswer, companionTimeAnswer,
                healthCareAnswer, sheddingAnswer, budgetAnswer
        );
        copy.extraScores = new HashMap<>(this.extraScores);
        copy.extraAnswers = new HashMap<>(this.extraAnswers);
        copy.createTime = this.createTime;
        copy.userId = this.userId;
        return copy;
    }

    @Override
    public String toString() {
        return String.format("UserPreferences{性格:%d, 空间:%d, 陪伴:%d, 护理:%d, 掉毛:%d, 预算:%d, 画像:%s}",
                personalityScore, livingSpaceScore, companionTimeScore,
                healthCareScore, sheddingScore, budgetScore, getUserPetType());
    }

    /**
     * 生成格式化的问卷结果（用于显示）
     */
    public String toFormattedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("📋 萌友速配问卷结果\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append(String.format("🐕 性格合拍：%d分 - %s\n", personalityScore, getScoreDescription(DIM_PERSONALITY)));
        sb.append(String.format("🏠 居住空间：%d分 - %s\n", livingSpaceScore, getScoreDescription(DIM_LIVING_SPACE)));
        sb.append(String.format("⏰ 陪伴时间：%d分 - %s\n", companionTimeScore, getScoreDescription(DIM_COMPANION_TIME)));
        sb.append(String.format("🩺 健康护理：%d分 - %s\n", healthCareScore, getScoreDescription(DIM_HEALTH_CARE)));
        sb.append(String.format("🧹 掉毛容忍：%d分 - %s\n", sheddingScore, getScoreDescription(DIM_SHEDDING)));
        sb.append(String.format("💰 养宠预算：%d分 - %s\n", budgetScore, getScoreDescription(DIM_BUDGET)));
        sb.append("━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append(getHealingSummary());
        return sb.toString();
    }
}
