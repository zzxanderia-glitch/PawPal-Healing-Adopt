package healing.pet.dao;

import healing.pet.model.Animal;
import healing.pet.model.Cat;
import healing.pet.model.Dog;
import healing.pet.util.DBUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAOImpl implements PetDAO {

    @Override
    public List<Animal> getAllPets() throws SQLException {
        List<Animal> list = new ArrayList<>();
        String sql = "SELECT * FROM pet";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                int age = rs.getInt("age");
                String breed = rs.getString("breed"); // 提取品种
                String story = rs.getString("story");
                String photoPath = rs.getString("photo_path");
                int status = rs.getInt("status");     // 提取状态码 (0,1,2)

                // 扩展字段
                String detailStory = rs.getString("detail_story");
                String habits = rs.getString("habits");
                String preference = rs.getString("preference");

                Animal animal = null;
                // 💡 组长注意：构造函数参数顺序必须与 Animal 类完全一致
                if ("cat".equalsIgnoreCase(type) || "猫".equals(type)) {
                    animal = new Cat(id, name, age, breed, story, photoPath, status, detailStory, habits, preference);
                } else if ("dog".equalsIgnoreCase(type) || "狗".equals(type)) {
                    animal = new Dog(id, name, age, breed, story, photoPath, status, detailStory, habits, preference);
                }

                if (animal != null) list.add(animal);
            }
        }
        return list;
    }

    @Override
    public void updatePetStatus(int id, int status) throws SQLException {
        String sql = "UPDATE pet SET status = ? WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
    }
}