package healing.pet.dao;

import healing.pet.model.Animal;
import java.sql.SQLException;
import java.util.List;

public interface PetDAO {
    // 接口里必须声明 throws SQLException，否则实现类会报错
    List<Animal> getAllPets() throws SQLException;
    void updatePetStatus(int id, int status) throws SQLException;
}