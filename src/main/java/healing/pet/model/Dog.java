package healing.pet.model;

import healing.pet.model.Animal;

public class Dog extends Animal {
    public Dog(int id, String name, int age, String breed, String story, String photoPath,
               int status, String detailStory, String habits, String preference) {
        // 💡 确保参数传递给父类
        super(id, name, age, breed, story, photoPath, status, detailStory, habits, preference);
    }

    @Override
    public String getVoice() { return "汪汪！"; }
    @Override
    public String getCareGuide() { return "狗狗需要充足的户外活动时间。"; }
}