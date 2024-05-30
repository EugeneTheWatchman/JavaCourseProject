package ru.croc.Project;

import ru.croc.Project.db.tests.Test;
import ru.croc.Project.db.tests.TestDao;
import ru.croc.Project.db.users.User;
import ru.croc.Project.db.users.UserDao;

import java.sql.*;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        String pathToDB = "~/IdeaProjects/JavaCourseProject/db/LearnIt";
        if (args.length > 0) {
            pathToDB = args[0];
        }
        String url = "jdbc:h2:tcp://localhost:9092/" + pathToDB;
        try (Connection conn = DriverManager.getConnection(url, "admin", "admin")) {

            LeartIt.main(conn);

        } catch (SQLException e) {
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
        }
    }
}