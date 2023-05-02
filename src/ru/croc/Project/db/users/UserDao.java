package ru.croc.Project.db.users;


import java.sql.*;

public class UserDao {

    Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }
    public void create(User user) throws SQLException {
        String sql = "insert into users (login, is_admin) values (?, ?);";
        try (PreparedStatement createStatement = connection.prepareStatement(sql)) {
            createStatement.setString(1, user.getLogin());
            createStatement.setBoolean(2, user.isAdmin());

            createStatement.execute();
        }
    }

    public User read(String login) throws SQLException {
        String sql = "select * from users u where u.login = ?;";
        try (PreparedStatement readStatement = connection.prepareStatement(sql)) {
            readStatement.setString(1, login);
            try (ResultSet resultSet = readStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    boolean is_admin = resultSet.getBoolean("is_admin");

                    return new User(id, login, is_admin);
                }
            }
        }
        return null;
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS u SET IS_ADMIN=?, LOGIN = ?\n" +
                     "WHERE ? = u.ID;\n";
        try (PreparedStatement updateStatement = connection.prepareStatement(sql)) {
            updateStatement.setBoolean(1, user.isAdmin());
            updateStatement.setString(2, user.getLogin());
            updateStatement.setInt(3, user.getId());

            updateStatement.execute();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM  USERS u \n" +
                "WHERE u.id = ?;\n";
        PreparedStatement deleteStatement = connection.prepareStatement(sql);
        deleteStatement.setInt(1, id);

        deleteStatement.execute();

    }

}
