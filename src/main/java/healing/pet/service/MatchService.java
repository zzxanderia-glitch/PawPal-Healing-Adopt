package healing.pet.service;

import healing.pet.dao.PetDAO;
import healing.pet.dao.PetDAOImpl;
import healing.pet.model.Animal;
import healing.pet.model.Cat;
import healing.pet.model.Dog;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.sql.SQLException;

/**
 * 萌友速配服务 - 基于加权余弦相似度的智能匹配
 * 配合 UserPreferences 使用，计算用户与宠物的契合度
 */
public class MatchService {

    // 各维度权重（与 UserPreferences 的 6 个维度顺序严格对应）
    // 顺序：[性格，空间，陪伴，护理，掉毛，预算]
    private static final double[] WEIGHTS = {0.25, 0.20, 0.20, 0.15, 0.10, 0.10};

    // 注入 DAO
    private PetDAO petDAO;

    public MatchService() {
        petDAO = new PetDAOImpl();
    }

    /**
     * 根据用户偏好返回按契合度降序排列的宠物列表
     * @param preferences 用户问卷结果（6 维度 1~5 分）
     * @return 排序后的宠物列表
     */
    public List<Animal> match(UserPreferences preferences) throws SQLException {
        List<Animal> allPets = petDAO.getAllPets();

        // 过滤：只保留状态为 0（待领养）的宠物
        allPets = allPets.stream()
                .filter(pet -> pet.getStatus() == 0)
                .collect(Collectors.toList());

        // 为每只宠物计算特征向量
        Map<Animal, double[]> petVectors = new HashMap<>();
        for (Animal pet : allPets) {
            petVectors.put(pet, extractPetVector(pet));
        }

        // 获取用户向量
        double[] userVec = preferences.toVector();

        // 按相似度降序排序
        return allPets.stream()
                .sorted((a, b) -> {
                    double simA = cosineSimilarityWithWeights(userVec, petVectors.get(a));
                    double simB = cosineSimilarityWithWeights(userVec, petVectors.get(b));
                    return Double.compare(simB, simA);
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据用户偏好返回带匹配度分数的结果列表
     * @param preferences 用户偏好
     * @return 包含宠物和匹配度的结果列表
     */
    public List<MatchResult> matchWithScore(UserPreferences preferences) throws SQLException {
        List<Animal> allPets = petDAO.getAllPets();
        List<MatchResult> results = new ArrayList<>();
        double[] userVec = preferences.toVector();

        for (Animal pet : allPets) {
            // 过滤：只保留状态为 0（待领养）的宠物
            if (pet.getStatus() != 0) continue;
            
            double[] petVec = extractPetVector(pet);
            double similarity = cosineSimilarityWithWeights(userVec, petVec);
            results.add(new MatchResult(pet, similarity));
        }

        results.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));
        return results;
    }

    /**
     * 从 Animal 对象中提取 6 维特征向量 (1~5 分)
     * 顺序：[性格，空间，陪伴，护理，掉毛，预算]
     */
    private double[] extractPetVector(Animal pet) {
        int personality = 3;
        int livingSpace = 3;
        int companionTime = 3;
        int healthCare = 3;
        int shedding = 3;
        int budget = 3;

        // 获取描述文本：优先使用 getCareGuide()
        String description = "";
        try {
            String careGuide = pet.getCareGuide();
            if (careGuide != null && !careGuide.isEmpty()) {
                description = careGuide.toLowerCase();
            }
        } catch (Exception e) {
            // 忽略
        }

        // 如果 careGuide 为空，尝试通过反射获取 story 字段
        if (description.isEmpty()) {
            try {
                Field storyField = pet.getClass().getDeclaredField("story");
                storyField.setAccessible(true);
                Object storyValue = storyField.get(pet);
                if (storyValue != null) {
                    description = storyValue.toString().toLowerCase();
                }
            } catch (Exception ignored) {}
        }

        // 如果描述文本为空，使用默认值
        if (description.isEmpty()) {
            if (pet instanceof Dog) {
                description = "活泼 需要运动 需要陪伴 掉毛";
            } else if (pet instanceof Cat) {
                description = "独立 安静 适合小空间 不掉毛";
            }
        }

        // ---------- 关键词打分规则 ----------
        // 1. 性格合拍
        if (description.contains("温顺") || description.contains("粘人") ||
                description.contains("温柔") || description.contains("亲人")) {
            personality = 5;
        } else if (description.contains("活泼") || description.contains("好动") ||
                description.contains("热情")) {
            personality = 4;
        } else if (description.contains("独立") || description.contains("高冷") ||
                description.contains("安静")) {
            personality = 2;
        } else if (description.contains("佛系") || description.contains("随缘")) {
            personality = 3;
        }

        // 2. 居住空间需求
        if (description.contains("大空间") || description.contains("别墅") ||
                description.contains("院子") || description.contains("需要运动")) {
            livingSpace = 5;
        } else if (description.contains("楼房") || description.contains("公寓") ||
                description.contains("普通")) {
            livingSpace = 4;
        } else if (description.contains("小户型") || description.contains("小空间") ||
                description.contains("适合公寓")) {
            livingSpace = 3;
        } else if (description.contains("宿舍") || description.contains("单间")) {
            livingSpace = 2;
        } else if (description.contains("桌面") || description.contains("笼养")) {
            livingSpace = 1;
        }

        // 3. 陪伴时间需求
        if (description.contains("每天陪伴") || description.contains("需要陪伴") ||
                description.contains("粘人") || description.contains("喜欢互动")) {
            companionTime = 5;
        } else if (description.contains("偶尔陪伴") || description.contains("佛系")) {
            companionTime = 3;
        } else if (description.contains("独立") || description.contains("不需陪伴") ||
                description.contains("安静")) {
            companionTime = 2;
        }

        // 4. 健康护理需求
        if (description.contains("长毛") || description.contains("护理") ||
                description.contains("定期") || description.contains(" grooming")) {
            healthCare = 4;
        } else if (description.contains("短毛") || description.contains("皮实") ||
                description.contains("好养")) {
            healthCare = 2;
        }

        // 5. 掉毛程度（分数越高表示越能容忍掉毛）
        if (description.contains("不掉毛") || description.contains("无毛") ||
                description.contains("泰迪") || description.contains("贵宾")) {
            shedding = 1;
        } else if (description.contains("少掉毛") || description.contains("短毛")) {
            shedding = 2;
        } else if (description.contains("掉毛") || description.contains("换毛") ||
                description.contains("长毛")) {
            shedding = 4;
        }

        // 6. 预算（根据品种和体型粗略估计）
        if (pet instanceof Dog) {
            Dog dog = (Dog) pet;
            String breed = "";
            try {
                Field breedField = Dog.class.getDeclaredField("breed");
                breedField.setAccessible(true);
                Object breedValue = breedField.get(dog);
                if (breedValue != null) {
                    breed = breedValue.toString().toLowerCase();
                }
            } catch (Exception ignored) {}

            if (breed.contains("金毛") || breed.contains("拉布拉多") || breed.contains("大型")) {
                budget = 5;
            } else if (breed.contains("柯基") || breed.contains("中型")) {
                budget = 4;
            } else {
                budget = 3;
            }
        } else if (pet instanceof Cat) {
            budget = 3;
        }

        return new double[]{personality, livingSpace, companionTime,
                healthCare, shedding, budget};
    }


    public static void main(String[] args) throws SQLException {
        MatchService service = new MatchService();
        UserPreferences prefs = new UserPreferences(4, 4, 5, 3, 2, 3);
        List<Animal> result = service.match(prefs);

        System.out.println("匹配结果（按契合度从高到低）：");
        for (int i = 0; i < result.size(); i++) {
            Animal pet = result.get(i);
            System.out.println((i+1) + ". " + pet.getName() + " (" + pet.getClass().getSimpleName() + ")");
        }
    }

    /**
     * 加权余弦相似度计算
     * @param userVec 用户偏好向量
     * @param petVec 宠物特征向量
     * @return 相似度 0~1
     */
    private double cosineSimilarityWithWeights(double[] userVec, double[] petVec) {
        double dot = 0.0, normUser = 0.0, normPet = 0.0;
        for (int i = 0; i < userVec.length; i++) {
            double weightedUser = userVec[i] * WEIGHTS[i];
            double weightedPet = petVec[i] * WEIGHTS[i];
            dot += weightedUser * weightedPet;
            normUser += weightedUser * weightedUser;
            normPet += weightedPet * weightedPet;

        }
        if (normUser == 0 || normPet == 0) return 0;
        return dot / (Math.sqrt(normUser) * Math.sqrt(normPet));
    }

    /**
     * 匹配结果内部类
     */
    public static class MatchResult {
        private Animal pet;
        private double similarity;

        public MatchResult(Animal pet, double similarity) {
            this.pet = pet;
            this.similarity = similarity;
        }

        public Animal getPet() { return pet; }
        public double getSimilarity() { return similarity; }
        public String getMatchPercent() { return String.format("%.0f%%", similarity * 100); }
    }
}
