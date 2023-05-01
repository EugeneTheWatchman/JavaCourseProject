package ru.croc.Project;

import ru.croc.Project.db.tests.Test;
import ru.croc.Project.db.tests.TestDao;
import ru.croc.Project.db.users.User;
import ru.croc.Project.db.users.UserDao;

import java.sql.*;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:h2:tcp://localhost:9092/~/IdeaProjects/JavaCourseProject/db/LearnIt";
        try (Connection conn = DriverManager.getConnection(url, "admin", "admin")) {

            LeartIt.main(conn);

        } catch (SQLException e) {
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
        }
    }
}