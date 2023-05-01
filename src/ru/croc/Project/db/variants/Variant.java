package ru.croc.Project.db.variants;

public class Variant {
    private int id;
    private int testId;
    private String text;
    private boolean isRight;

    public Variant(int id, int testId, String text, boolean isRight) {
        this.id = id;
        this.testId = testId;
        this.text = text;
        this.isRight = isRight;
    }

    public Variant() {
        text = null;
    }

    public int getId() {
        return id;
    }

    public int getTestId() {
        return testId;
    }

    public String getText() {
        return text;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

}
