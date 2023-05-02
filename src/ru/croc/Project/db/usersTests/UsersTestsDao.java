package ru.croc.Project.db.usersTests;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UsersTestsDao {

    Connection connection;

    public UsersTestsDao(Connection connection) {
        this.connection = connection;
    }

    public void createOne(UsersTests usersTests) throws SQLException {
        String sql = "INSERT INTO USERS_TESTS (USER_ID, TEST_ID, IS_PASSED, PASS_DATE) VALUES (?, ?, ?, ?);";
        try (PreparedStatement createStatement = connection.prepareStatement(sql)) {
            createStatement.setInt(1, usersTests.getUserId());
            createStatement.setInt(2, usersTests.getTestId());
            createStatement.setBoolean(3, usersTests.isPassed());
            createStatement.setTimestamp(4, Timestamp.valueOf(usersTests.getPassDate()));

            createStatement.execute();
        }
    }

    public void createAll(Collection<UsersTests> usersTestsCollection) throws SQLException {
        if (usersTestsCollection == null || usersTestsCollection.size() < 1) { return; }

        StringBuilder baseSql = new StringBuilder("INSERT INTO USERS_TESTS (USER_ID, TEST_ID, IS_PASSED, PASS_DATE) VALUES (?, ?, ?, ?)");
        String repetable = ", (?, ?, ?, ?)";
        for (int i = 1; i < usersTestsCollection.size(); i++) {
            baseSql.append(repetable);
        }

        try (PreparedStatement createStatement = connection.prepareStatement(baseSql.toString())) {
            int i = 0;
            for (UsersTests usersTests : usersTestsCollection) {
                createStatement.setInt(i + 1, usersTests.getUserId());
                createStatement.setInt(i + 2, usersTests.getTestId());
                createStatement.setBoolean(i + 3, usersTests.isPassed());
                createStatement.setTimestamp(i + 4, Timestamp.valueOf(usersTests.getPassDate()));
                i = i + 4;
            }
            createStatement.execute();
        }
    }

    public Collection<UsersTests> readAll(int userId) throws SQLException {
        String sql = "SELECT * FROM USERS_TESTS ut WHERE ut.USER_ID = ?;";
        ArrayList<UsersTests> usersTestsArrayList = new ArrayList<>();
        try (PreparedStatement readStatement = connection.prepareStatement(sql)) {
            readStatement.setInt(1, userId);
            try (ResultSet resultSet = readStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int testId = resultSet.getInt("test_id");
                    boolean isPassed = resultSet.getBoolean("is_passed");
                    Timestamp passDate = resultSet.getTimestamp("pass_date");

                    usersTestsArrayList.add(new UsersTests(id, userId, testId, isPassed, passDate.toLocalDateTime()));
                }
            }
        }
        return usersTestsArrayList;
    }

    public Collection<UsersTests> readPassed(int userId) throws SQLException {
        String sql = "SELECT * FROM USERS_TESTS ut WHERE ut.USER_ID = ? and IS_PASSED  = TRUE;";
        ArrayList<UsersTests> usersTestsArrayList = new ArrayList<>();
        try (PreparedStatement readStatement = connection.prepareStatement(sql)) {
            readStatement.setInt(1, userId);
            try (ResultSet resultSet = readStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int testId = resultSet.getInt("test_id");
                    boolean isPassed = resultSet.getBoolean("is_passed");
                    Timestamp passDate = resultSet.getTimestamp("pass_date");

                    usersTestsArrayList.add(new UsersTests(id, userId, testId, isPassed, passDate.toLocalDateTime()));
                }
            }
        }
        return usersTestsArrayList;
    }
}
