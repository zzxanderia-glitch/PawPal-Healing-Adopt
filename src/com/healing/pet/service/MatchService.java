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
        String careGuide = pet.getCareGuide().toLowerCase();

        if (prefs.getPersonality() != null && careGuide.contains(prefs.getPersonality().toLowerCase())) {
            score += 30;
        }
        if (prefs.getLivingSpace() != null && careGuide.contains(prefs.getLivingSpace().toLowerCase())) {
            score += 20;
        }
        if (prefs.getCompanionTime() != null && careGuide.contains(prefs.getCompanionTime().toLowerCase())) {
            score += 25;
        }
        return score;
    }

    // 临时模拟数据（等成员A上传DAO后删除此方法）
    private List<Animal> getMockPets() {
        List<Animal> list = new ArrayList<>();
        // 假设 Cat 和 Dog 有构造器 (String name, String careGuide, ...)
        // 如果没有，请根据实际 Animal 类的构造器调整
        list.add(new Cat(0032,"咪咪", 2, "温顺，适合公寓，需要每天陪伴", "cat1.jpg"));
        list.add(new Dog(0076,"旺财", 1,"活泼，需要大空间，喜欢运动", "dog1.jpg", "金毛"));
        list.add(new Cat(0123,"小花", 1,"独立，适合小空间，不需要太多陪伴", "cat2.jpg"));
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