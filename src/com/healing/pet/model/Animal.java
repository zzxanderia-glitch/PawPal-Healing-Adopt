package com.healing.pet.model;


/**
 * 宠物抽象基类 —— 体现【抽象】
 * 组长定义：所有具体的宠物（猫、狗）都必须继承此类
 */
public abstract class Animal {
    private int id;             // 数据库ID
    private String name;        // 名字
    private int age;            // 年龄
    private String story;       // 治愈系：宠物的背景故事
    private String photoPath;   // 照片路径

    public Animal(int id, String name, int age, String story, String photoPath) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.story = story;
        this.photoPath = photoPath;
    }

    // 抽象方法：每种动物的叫声不同 —— 体现【多态】
    public abstract String getVoice();

    // 抽象方法：每种动物的照顾指南不同 —— 体现【多态】
    public abstract String getCareGuide();

    // 普通方法：展示宠物的基本信息 —— 体现【封装】
    public void displayBasicInfo() {
        System.out.println("【" + name + "】 的小档案：");
        System.out.println("年龄：" + age + " 岁");
        System.out.println("故事：" + story);
    }

    // Getter 和 Setter 方法 (省略部分)
    public String getName() { return name; }
    public String getPhotoPath() { return photoPath; }
}
