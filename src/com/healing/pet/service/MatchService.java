package com.healing.pet.service;
import com.healing.pet.model.Animal;
import com.healing.pet.model.Cat;
import com.healing.pet.model.Dog;
//import dao.PetDAO;     // 成员A会提供，先注释掉，后面再用
//import dao.PetDAOImpl;
import java.util.*;
import java.util.stream.Collectors;

public class MatchService {

    // private PetDAO petDAO;  // 等成员A上传后取消注释

    public MatchService() {
        // petDAO = new PetDAOImpl();  // 等成员A上传后取消注释
    }

    /**
     * 根据用户偏好计算契合度并返回排序后的宠物列表
     */
    public List<Animal> match(UserPreferences preferences) {
        // 临时用模拟数据代替 petDAO.getAllPets()
        List<Animal> allPets = getMockPets();   // 等成员A上传后替换为 petDAO.getAllPets()

        Map<Animal, Integer> scoreMap = new HashMap<>();
        for (Animal pet : allPets) {
            int score = calculateScore(pet, preferences);
            scoreMap.put(pet, score);
        }

        return allPets.stream()
                .sorted((a, b) -> scoreMap.get(b) - scoreMap.get(a))
                .collect(Collectors.toList());
    }

    /**
     * 计算单只宠物的契合度得分
     */
    private int calculateScore(Animal pet, UserPreferences prefs) {
        int score = 0;
        String careGuide = pet.getCareGuide();

        if (prefs.getPersonality() != null && careGuide.contains(prefs.getPersonality())) {
            score += 30;
        }
        if (prefs.getLivingSpace() != null) {
            if (careGuide.contains(prefs.getLivingSpace())) {
                score += 20;
            } else if (prefs.getLivingSpace().equals("小户型") && careGuide.contains("公寓")) {
                score += 20;
            } else if (prefs.getLivingSpace().equals("大空间") && careGuide.contains("别墅")) {
                score += 20;
            }
        }
        if (prefs.getCompanionTime() != null && careGuide.contains(prefs.getCompanionTime())) {
            score += 25;
        }
        return score;
    }

    // 临时模拟数据（等成员 A 上传 DAO 后删除此方法）
    private List<Animal> getMockPets() {
        List<Animal> list = new ArrayList<>();
        list.add(new Cat(32, "咪咪", 2, "温顺，适合公寓，需要每天陪伴", "cat1.jpg"));
        list.add(new Dog(76, "旺财", 1, "活泼，需要大空间，喜欢运动", "dog1.jpg", "金毛"));
        list.add(new Cat(123, "小花", 1, "独立，适合小空间，不需要太多陪伴", "cat2.jpg"));
        list.add(new Dog(77, "乐乐", 3, "聪明，适合公寓，陪伴时间适中", "dog2.jpg", "泰迪"));
        list.add(new Cat(99, "小白", 2, "粘人，适合别墅，需要大量陪伴", "cat3.jpg"));
        return list;
    }
    public static void main(String[] args) {
        MatchService service = new MatchService();
        // 模拟用户偏好：喜欢温顺的宠物，住在公寓，陪伴时间多
        UserPreferences prefs = new UserPreferences("温顺", "公寓", "多");
        List<Animal> result = service.match(prefs);

        System.out.println("匹配结果（按契合度从高到低）：");
        for (int i = 0; i < result.size(); i++) {
            Animal pet = result.get(i);
            System.out.println((i+1) + ". " + pet.getName() + " (" + pet.getClass().getSimpleName() + ")");
        }
    }
}