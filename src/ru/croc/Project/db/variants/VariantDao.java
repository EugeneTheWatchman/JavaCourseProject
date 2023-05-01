package ru.croc.Project.db.variants;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;


public class VariantDao {

    Connection connection;

    public VariantDao(Connection connection) {
        this.connection = connection;
    }
    public void create(Variant variant) throws SQLException {
        String sql = "INSERT INTO VARIANTS (test_id, TEXT, IS_RIGHT) VALUES (?, ?, ?);";
        try (PreparedStatement createStatement = connection.prepareStatement(sql)) {
            createStatement.setInt(1, variant.getTestId());
            createStatement.setString(2, variant.getText());
            createStatement.setBoolean(3, variant.isRight());

            createStatement.execute();
        }
    }

    public Collection<Variant> read(int testId) throws SQLException {
        ArrayList<Variant> variants = new ArrayList<>();

        String sql = "select * from VARIANTS v where v.test_id = ?;";
        try (PreparedStatement readStatement = connection.prepareStatement(sql)) {
            readStatement.setInt(1, testId);
            try (ResultSet resultSet = readStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String text = resultSet.getString(("text"));
                    boolean isRight = resultSet.getBoolean("is_right");

                    variants.add(new Variant(id, testId, text, isRight));
                }
            }
        }
        return variants;
    }

    public void update(Variant variant) throws SQLException {
        String sql = "UPDATE VARIANTS v SET test_id = ?, TEXT = ?, IS_RIGHT = ?\n" +
                "WHERE ? = v.ID;\n";
        try (PreparedStatement updateStatement = connection.prepareStatement(sql)) {
            updateStatement.setInt(1, variant.getTestId());
            updateStatement.setString(2, variant.getText());
            updateStatement.setBoolean(3, variant.isRight());
            updateStatement.setInt(4, variant.getId());

            updateStatement.execute();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM VARIANTS v \n" +
                "WHERE v.id = ?;\n";
        PreparedStatement deleteStatement = connection.prepareStatement(sql);
        deleteStatement.setInt(1, id);

        deleteStatement.execute();
    }

    public void deleteByTestId(int id) throws SQLException {
        String sql = "DELETE FROM VARIANTS v \n" +
                "WHERE v.test_id = ?;\n";
        PreparedStatement deleteStatement = connection.prepareStatement(sql);
        deleteStatement.setInt(1, id);

        deleteStatement.execute();
    }
}
