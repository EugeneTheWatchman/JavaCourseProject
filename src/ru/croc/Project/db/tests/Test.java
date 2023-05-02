package ru.croc.Project.db.tests;

public class Test {
    private int id;
    private String description;
    private String text;

    public Test(int id, String description, String text) {
        this.id = id;
        this.description = description;
        this.text = text;
    }

    public Test() {
        description = null;
        text = null;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setText(String text) {
        this.text = text;
    }

}
