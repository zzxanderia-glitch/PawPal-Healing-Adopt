package com.healing.pet.dao;

import com.healing.pet.model.Animal;
import java.util.List;

public interface PetDAO {
    // 获取所有宠物
    List<Animal> getAllPets();

    // 更新宠物领养状态
    void updatePetStatus(int id, int status);
}