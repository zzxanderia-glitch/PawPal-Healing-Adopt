package healing.pet.model;

public abstract class Animal {
    private int id;
    private String name;
    private int age;
    private String story;
    private String photoPath;

    // 组长新增字段
    private String detailStory;
    private String habits;
    private String preference;

    public Animal(int id, String name, int age, String story, String photoPath,
                  String detailStory, String habits, String preference) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.story = story;
        this.photoPath = photoPath;
        this.detailStory = detailStory;
        this.habits = habits;
        this.preference = preference;
    }

    public abstract String getVoice();
    public abstract String getCareGuide();

    public void displayBasicInfo() {
        System.out.println("【" + name + "】");
        System.out.println("年龄：" + age);
    }

    // getter
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getStory() { return story; }
    public String getPhotoPath() { return photoPath; }
    public String getDetailStory() { return detailStory; }
    public String getHabits() { return habits; }
    public String getPreference() { return preference; }
}