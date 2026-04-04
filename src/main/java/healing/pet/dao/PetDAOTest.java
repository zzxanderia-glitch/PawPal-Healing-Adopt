package healing.pet.dao;
import healing.pet.model.Animal;
import java.sql.SQLException;
import java.util.List;


public class PetDAOTest {
    public static void main(String[] args) {
        PetDAO dao = new healing.pet.dao.PetDAOImpl();

        try {
            List<Animal> pets = dao.getAllPets();
            System.out.println("========================================");
            System.out.println("✅ 测试成功！共加载：" + pets.size() + " 只宠物");
            System.out.println("========================================");

            for (Animal pet : pets) {
                System.out.println("\n名称：" + pet.getName());
                System.out.println("详细故事：" + pet.getDetailStory());
                System.out.println("习性：" + pet.getHabits());
                System.out.println("生活偏好：" + pet.getPreference());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}