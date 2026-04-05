package healing.pet.model;

/**
 * 宠物抽象基类 —— 组长规范版
 * 整合了状态码、品种以及治愈系详细故事字段
 */
public abstract class Animal {
    private int id;
    private String name;
    private int age;
    private String breed;       // 💡 新增：品种
    private String story;
    private String photoPath;
    private int status;         // 💡 新增：状态 (0-待领养, 1-审核中, 2-已领养)

    // 详细扩展字段
    private String detailStory;
    private String habits;
    private String preference;

    public Animal(int id, String name, int age, String breed, String story, String photoPath,
                  int status, String detailStory, String habits, String preference) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.story = story;
        this.photoPath = photoPath;
        this.status = status;
        this.detailStory = detailStory;
        this.habits = habits;
        this.preference = preference;
    }

    // 抽象方法由子类实现
    public abstract String getVoice();
    public abstract String getCareGuide();

    public void displayBasicInfo() {
        System.out.println("【" + name + "】 (" + breed + ")");
        System.out.println("状态：" + getStatusDesc());
    }

    /**
     * 💡 组长新增：获取状态描述
     */
    public String getStatusDesc() {
        switch (this.status) {
            case 1: return "审核信息中";
            case 2: return "已有新家";
            default: return "等待领养";
        }
    }

    // --- 标准 Getter ---
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getBreed() { return breed; } // 💡 新增
    public String getStory() { return story; }
    public String getPhotoPath() { return photoPath; }
    public int getStatus() { return status; }   // 💡 新增
    public String getDetailStory() { return detailStory; }
    public String getHabits() { return habits; }
    public String getPreference() { return preference; }

    // --- 标准 Setter (部分逻辑需要修改状态时使用) ---
    public void setStatus(int status) { this.status = status; }
    public void setBreed(String breed) { this.breed = breed; }
}