package healing.pet.model;

import healing.pet.model.Animal;

public class Cat extends Animal {
    public Cat(int id, String name, int age, String breed, String story, String photoPath,
               int status, String detailStory, String habits, String preference) {
        super(id, name, age, breed, story, photoPath, status, detailStory, habits, preference);
    }

    @Override
    public String getVoice() { return "喵~"; }
    @Override
    public String getCareGuide() { return "猫咪喜欢安静的环境，需要准备抓板。"; }
}