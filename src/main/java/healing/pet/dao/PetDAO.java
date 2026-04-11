package healing.pet.dao;

import healing.pet.model.Animal;
import java.sql.SQLException;
import java.util.List;

public interface PetDAO {
    List<Animal> getAllPets() throws SQLException;
    void updatePetStatus(int id, int status) throws SQLException;
    void addPet(Animal pet) throws SQLException;
    void deletePet(int id) throws SQLException;
    void updatePet(Animal pet) throws SQLException;
    Animal getPetById(int id) throws SQLException;
}
