package com.healing.pet.dao;

import com.healing.pet.model.Animal;
import com.healing.pet.model.Cat;
import com.healing.pet.model.Dog;
import com.healing.pet.util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PetDAOImpl implements PetDAO {

    @Override
    public List<Animal> getAllPets() {
        List<Animal> petList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 获取数据库连接
            conn = DBUtils.getConnection();
            String sql = "SELECT * FROM pet";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String type = rs.getString("type");
                String story = rs.getString("story");
                String photoPath = rs.getString("photo_path");
                String breed = rs.getString("breed");

                Animal animal = null;

                // ======================
                // OOP 多态：自动创建猫/狗对象
                // ======================
                if ("cat".equalsIgnoreCase(type)) {
                    animal = new Cat(id, name, age, story, photoPath);
                } else if ("dog".equalsIgnoreCase(type)) {
                    animal = new Dog(id, name, age, story, photoPath, breed);
                }

                if (animal != null) {
                    petList.add(animal);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, pstmt, rs);
        }

        return petList;
    }

    @Override
    public void updatePetStatus(int id, int status) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "UPDATE pet SET status = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, pstmt, null);
        }
    }
}