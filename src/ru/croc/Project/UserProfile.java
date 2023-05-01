package ru.croc.Project;

import ru.croc.Project.db.tests.Test;
import ru.croc.Project.db.users.User;
import ru.croc.Project.db.users.UserDao;
import ru.croc.Project.db.usersTests.UsersTests;
import ru.croc.Project.db.usersTests.UsersTestsDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class UserProfile {

    private User user;
    private Collection<UsersTests> usersTestsCollection = new ArrayList<>();

    private UserProfile(User user, Collection<UsersTests> usersTestsCollection) {
        this.user = user;
        this.usersTestsCollection = usersTestsCollection;
    }
    private static User readUserFromDB(String login, Connection connection) {
        try {
            UserDao userDao = new UserDao(connection);
            User user = userDao.read(login);

            if (user != null) { return user; }

            user = new User(-1, login, false);
            userDao.create(user);
            return user;

        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public static UserProfile logIn(String login, Connection connection) {
        User user = readUserFromDB(login, connection);
        return readTestsFromDB(user, connection);
    }
    private static UserProfile readTestsFromDB(User user, Connection connection) {
        try {
            UsersTestsDao usersTestsDao = new UsersTestsDao(connection);

            Collection<UsersTests> usersTestsCollection = usersTestsDao.readAll(user.getId());

            return new UserProfile(user, usersTestsCollection);
        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public void commitToDB(Connection connection) {
        UsersTestsDao usersTestsDao = new UsersTestsDao(connection);
        try {
            Collection<UsersTests> oldUsersTests = usersTestsDao.readAll(this.user.getId());
            Collection<UsersTests> newUsersTests = new ArrayList<>();

            // соберём только новые для того, чтобы добавить только их
            outerloop:
            for (UsersTests usersTest : this.usersTestsCollection) {
                if (oldUsersTests.contains(usersTest)) {
                    continue;
                }

                for (UsersTests oldUsersTest : oldUsersTests) {
                    if (oldUsersTest.getPassDate().equals(usersTest.getPassDate())) {
                        continue outerloop;
                    }
                }
                newUsersTests.add(usersTest);
            }

            // может быть ошибка если каким-то образом будут 2 теста с одинаковыми user_id, test_id, pass_date
            usersTestsDao.createAll(newUsersTests);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser() {
        return user;
    }

    public void addTest(int testId, boolean isPassed) {
        LocalDateTime passedTime = LocalDateTime.now();
        UsersTests userTest = new UsersTests(-1, this.user.getId(), testId, isPassed, passedTime);
        usersTestsCollection.add(userTest);
    }

    public Collection<UsersTests> getUsersTestsCollection() {
        return usersTestsCollection;
    }
}
