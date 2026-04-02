package com.healing.pet.dao;

import com.healing.pet.model.Animal;
import java.util.List;

// 宠物DAO接口
public interface PetDAO {
    // 1. 获取所有宠物（返回Animal列表，多态）
    List<Animal> getAllPets();

    // 2. 更新宠物状态（待领养/已领养）
    void updateStatus(int id, String status);
}