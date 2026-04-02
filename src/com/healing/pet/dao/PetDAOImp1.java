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

// 宠物DAO实现类
public class PetDAOImpl implements PetDAO {

    @Override
    public List<Animal> getAllPets() {
        List<Animal> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT * FROM pet";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // 从数据库读取字段
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String gender = rs.getString("gender");
                int age = rs.getInt("age");
                String characterTag = rs.getString("character_tag");
                String health = rs.getString("health");
                String description = rs.getString("description");
                String status = rs.getString("status");

                Animal animal = null;

                // ======================
                // 核心：多态创建对象（OOP亮点）
                // ======================
                String story = null;
                String photoPath = null;
                if ("猫".equals(type)) {
                    story = new String();
                    photoPath = new String();
                    animal = new Cat(id, name, age, story, photoPath);
                } else if ("狗".equals(type)) {
                    animal = new Dog(id, name, age, story, photoPath);
                }

                if (animal != null) {
                    list.add(animal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public void updateStatus(int id, String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "UPDATE pet SET status=? WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, pstmt);
        }
    }
}
