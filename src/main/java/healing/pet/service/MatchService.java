package healing.pet.service;
import healing.pet.model.Animal;
import healing.pet.model.Cat;
import healing.pet.model.Dog;
import healing.pet.dao.PetDAO;
import healing.pet.dao.PetDAOImpl;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MatchService {

    private PetDAO petDAO;  // 等成员A上传后取消注释

    public MatchService() {
        petDAO = new PetDAOImpl();  // 等成员A上传后取消注释
    }

    /**
     * 根据用户偏好计算契合度并返回排序后的宠物列表
     */
    public List<Animal> match(UserPreferences preferences) throws SQLException {
        List<Animal> allPets = petDAO.getAllPets();
        
        System.out.println("MatchService: 从数据库获取到 " + allPets.size() + " 只宠物");

        Map<Animal, Integer> scoreMap = new HashMap<>();
        for (Animal pet : allPets) {
            int score = calculateScore(pet, preferences);
            scoreMap.put(pet, score);
            System.out.println("宠物 " + pet.getName() + " 得分：" + score);
        }

        List<Animal> sortedList = allPets.stream()
                .sorted((a, b) -> scoreMap.get(b) - scoreMap.get(a))
                .collect(Collectors.toList());
        
        System.out.println("MatchService: 匹配完成后返回 " + sortedList.size() + " 只宠物");
        return sortedList;
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

    public static void main(String[] args) throws SQLException {
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