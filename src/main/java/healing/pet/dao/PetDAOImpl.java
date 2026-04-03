package main.java.healing.pet.dao;

import healing.pet.dao.PetDAO;
import healing.pet.model.Animal;
import main.java.healing.pet.model.Cat;
import main.java.healing.pet.model.Dog;
import main.java.healing.pet.util.DBUtils;
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
                String story = rs.getString("story");
                String photoPath = rs.getString("photo_path");
                String breed = rs.getString("breed");

                // 组长新增字段
                String detailStory = rs.getString("detail_story");
                String habits = rs.getString("habits");
                String preference = rs.getString("preference");

                Animal animal = null;
                if ("cat".equalsIgnoreCase(type) || "猫".equals(type)) {
                    animal = new Cat(id, name, age, story, photoPath, detailStory, habits, preference);
                } else if ("dog".equalsIgnoreCase(type) || "狗".equals(type)) {
                    animal = new Dog(id, name, age, story, photoPath, breed, detailStory, habits, preference);
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