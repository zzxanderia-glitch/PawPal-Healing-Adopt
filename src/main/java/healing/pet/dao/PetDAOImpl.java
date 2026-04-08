package com.healing.pet.dao;

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
                String breed = rs.getString("breed");
                String story = rs.getString("story");
                String photoPath = rs.getString("photo_path");
                int status = rs.getInt("status");

                String detailStory = rs.getString("detail_story");
                String habits = rs.getString("habits");
                String preference = rs.getString("preference");

                Animal animal = null;
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

    @Override
    public void addPet(Animal pet) throws SQLException {
        String sql = "INSERT INTO pet (name, type, age, breed, story, photo_path, status, detail_story, habits, preference) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String type = (pet instanceof Dog) ? "dog" : "cat";
            pstmt.setString(1, pet.getName());
            pstmt.setString(2, type);
            pstmt.setInt(3, pet.getAge());
            pstmt.setString(4, pet.getBreed());
            pstmt.setString(5, pet.getStory());
            pstmt.setString(6, pet.getPhotoPath());
            pstmt.setInt(7, pet.getStatus());
            pstmt.setString(8, pet.getDetailStory());
            pstmt.setString(9, pet.getHabits());
            pstmt.setString(10, pet.getPreference());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deletePet(int id) throws SQLException {
        String sql = "DELETE FROM pet WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updatePet(Animal pet) throws SQLException {
        String sql = "UPDATE pet SET name=?, type=?, age=?, breed=?, story=?, photo_path=?, detail_story=?, habits=?, preference=? WHERE id=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String type = (pet instanceof Dog) ? "dog" : "cat";
            pstmt.setString(1, pet.getName());
            pstmt.setString(2, type);
            pstmt.setInt(3, pet.getAge());
            pstmt.setString(4, pet.getBreed());
            pstmt.setString(5, pet.getStory());
            pstmt.setString(6, pet.getPhotoPath());
            pstmt.setString(7, pet.getDetailStory());
            pstmt.setString(8, pet.getHabits());
            pstmt.setString(9, pet.getPreference());
            pstmt.setInt(10, pet.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public Animal getPetById(int id) throws SQLException {
        String sql = "SELECT * FROM pet WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String type = rs.getString("type");
                    int age = rs.getInt("age");
                    String breed = rs.getString("breed");
                    String story = rs.getString("story");
                    String photoPath = rs.getString("photo_path");
                    int status = rs.getInt("status");
                    String detailStory = rs.getString("detail_story");
                    String habits = rs.getString("habits");
                    String preference = rs.getString("preference");

                    if ("cat".equalsIgnoreCase(type) || "猫".equals(type)) {
                        return new Cat(id, name, age, breed, story, photoPath, status, detailStory, habits, preference);
                    } else if ("dog".equalsIgnoreCase(type) || "狗".equals(type)) {
                        return new Dog(id, name, age, breed, story, photoPath, status, detailStory, habits, preference);
                    }
                }
            }
        }
        return null;
    }
}
