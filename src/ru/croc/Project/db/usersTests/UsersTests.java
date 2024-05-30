package ru.croc.Project.db.usersTests;

import java.time.LocalDateTime;

public class UsersTests {
    private int id;
    private int user_id;
    private int test_id;
    private boolean is_passed;
    private LocalDateTime pass_date;


    public UsersTests() {}

    public UsersTests(int id, int user_id, int test_id, boolean is_passed, LocalDateTime date) {
        this.id = id;
        this.user_id = user_id;
        this.test_id = test_id;
        this.is_passed = is_passed;
        this.pass_date = date;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return user_id;
    }

    public int getTestId() {
        return test_id;
    }

    public boolean isPassed() {
        return is_passed;
    }

    public LocalDateTime getPassDate() {
        return pass_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public void setTestId(int test_id) {
        this.test_id = test_id;
    }

    public void setPassed(boolean is_passed) {
        this.is_passed = is_passed;
    }

    public void setPassDate(LocalDateTime pass_date) {
        this.pass_date = pass_date;
    }
}
