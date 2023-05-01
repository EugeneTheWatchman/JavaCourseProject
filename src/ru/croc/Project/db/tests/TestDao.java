package ru.croc.Project.db.tests;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class TestDao {

    Connection connection;

    public TestDao(Connection connection) {
        this.connection = connection;
    }
    public void create(Test test) throws SQLException {
        String sql = "insert into tests (DESCRIPTION, TEXT) values (?, ?);";
        try (PreparedStatement createStatement = connection.prepareStatement(sql)) {
            createStatement.setString(1, test.getDescription());
            createStatement.setString(2, test.getText());

            createStatement.execute();
        }
    }

    public Collection<Test> readAll() throws SQLException {
        String sql = "select * from tests t;";
        Collection<Test> tests = new ArrayList<>();

        try (PreparedStatement readStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = readStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String description = resultSet.getString("description");
                    String text = resultSet.getString(("text"));

                    tests.add(new Test(id, description, text));
                }
            }
        }
        return tests;
    }

    public Test read(int id) throws SQLException {
        String sql = "select * from tests t where t.id = ?;";
        try (PreparedStatement readStatement = connection.prepareStatement(sql)) {
            readStatement.setInt(1, id);
            try (ResultSet resultSet = readStatement.executeQuery()) {
                if (resultSet.next()) {
                    String description = resultSet.getString("description");
                    String text = resultSet.getString(("text"));

                    return new Test(id, description, text);
                }
            }
        }
        return null;
    }

    public Test read(String text) throws SQLException {
        String sql = "select * from tests t where t.text = ?;";
        try (PreparedStatement readStatement = connection.prepareStatement(sql)) {
            readStatement.setString(1, text);
            try (ResultSet resultSet = readStatement.executeQuery()) {
                if (resultSet.next()) {
                    String description = resultSet.getString("description");
                    int id = resultSet.getInt(("id"));

                    return new Test(id, description, text);
                }
            }
        }
        return null;
    }

    public void update(Test test) throws SQLException {
        String sql = "UPDATE TESTS t SET DESCRIPTION = ?, TEXT = ?\n" +
                "WHERE ? = t.ID;\n";
        try (PreparedStatement updateStatement = connection.prepareStatement(sql)) {
            updateStatement.setString(1, test.getDescription());
            updateStatement.setString(2, test.getText());
            updateStatement.setInt(3, test.getId());

            updateStatement.execute();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM TESTS t \n" +
                "WHERE t.id = ?;\n";
        PreparedStatement deleteStatement = connection.prepareStatement(sql);
        deleteStatement.setInt(1, id);

        deleteStatement.execute();

    }
}
