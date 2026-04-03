package healing.pet.service;
import healing.pet.model.User;

public class PetService {
    public String updatePet(main.java.healing.pet.model.User user) {
        return user.isAdmin() ? "宠物信息修改成功" : "无权限：只有管理员可以修改宠物信息";
    }

    public String viewPet() {
        return "查看宠物信息：一只可爱的橘猫正在向你撒娇！";
    }
}
