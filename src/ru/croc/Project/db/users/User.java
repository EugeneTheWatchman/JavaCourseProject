package ru.croc.Project.db.users;

public class User {

    private int id;
    private String login;
    private boolean isAdmin;

    public User(int id, String login, boolean isAdmin) {
        this.id = id;
        this.login = login;
        this.isAdmin = isAdmin;
    }

    public User() {};

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }


    // TODO
    // data about passed tests

    /*public ArrayList<Long> passedTestIDs = new ArrayList<>();
    public ArrayList<Long> failedTestIDs = new ArrayList<>();

    public void addTest(long testID, boolean isPassed) {
        //isPassed ? passedTestIDs.add(testID) : failedTestIDs.add(testID); почему-то так не даёт записать
        if (isPassed) {
            passedTestIDs.add(testID);
        } else {
            failedTestIDs.add(testID);
        }
    }*/


}
