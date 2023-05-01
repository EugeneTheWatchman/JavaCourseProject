package ru.croc.Project;

import ru.croc.Project.db.tests.Test;
import ru.croc.Project.db.tests.TestDao;
import ru.croc.Project.db.usersTests.UsersTests;
import ru.croc.Project.db.usersTests.UsersTestsDao;
import ru.croc.Project.db.variants.Variant;

import java.sql.*;
import java.util.*;

public final class LeartIt {

    private static Connection connection;
    private static UserProfile profile;
    private static ArrayList<Test> availableTests = new ArrayList<>();

    public static void main(Connection connection) {
        LeartIt.connection = connection;

        System.out.println("Введите логин:");
        Scanner scan = new Scanner(System.in);
        String login = scan.nextLine();

        profile = UserProfile.logIn(login, connection);

        boolean onGoing = true;
        while (onGoing) {
            String[] options;
            if (profile.getUser().isAdmin()) {
                options = new String[]{"Запустить тест", "Сохранить прогресс и выйти",  "Вывести список тестов", "Создать тест", "Удалить тест", "Добавить вариант тесту"};
                System.out.println("Выберите опцию:");
                for (int i = 0; i < options.length; i++) {
                    System.out.println(i + 1 + ") " + options[i]);
                }
            } else {
                options = new String[]{"Запустить тест", "Сохранить прогресс и выйти"};
                for (int i = 0; i < options.length; i++) {
                    System.out.println(i + 1 + ") " + options[i]);
                }
            }

            int selectedOption = scan.nextInt();
            if (selectedOption < 1 || selectedOption > options.length) {
                System.out.println("Нет такой опции!");
                continue;
            }

            switch (selectedOption) {
                case 1:
                    InsertWordTest test = chooseTest();

                    boolean testResult = runTest(test);
                    profile.addTest(test.getTest().getId(), testResult);
                    break;
                case 2:
                    profile.commitToDB(connection);
                    onGoing = false;
                    break;
            }
        }
    }

    private static InsertWordTest chooseTest() {
        if (availableTests.size() < 1) {
            getAvailableTestsIds();
        }

        InsertWordTest availableTest = InsertWordTest.readFromDB(availableTests.get(0).getId(), connection);
        availableTests.remove(0);
        return availableTest;
    }

    private static ArrayList<Test> getAvailableTestsIds() {
        try {
            TestDao testDao = new TestDao(connection);
            Collection<Test> tests = testDao.readAll();

            // нахождение всех тестов за исключением тех, которые успешно пройдены у конкретного пользователя
            for (Test test : tests) {
                boolean available = true;
                for (UsersTests usersTests : profile.getUsersTestsCollection()) {
                    if (test.getId() == usersTests.getTestId() && usersTests.isPassed()) {
                        available = false;
                    }
                }
                if (available) { availableTests.add(test); }
            }
            return availableTests;
        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

    }

    // для других тестов переопределяются другие типы, т.к. заранее не знаю их структру
    private static boolean runTest(InsertWordTest test) {
        if (test.getVariants().size() < 1) { return false; }

        System.out.println(test.getTest().getDescription());
        System.out.println(test.getTest().getText());
        System.out.println("Variants:");

        ArrayList<String> variantsToInsert = new ArrayList<>();
        for (Variant variant : test.getVariants()) {
            variantsToInsert.add(variant.getText());
        }

        List<String> shuffledVariants = new ArrayList<>(variantsToInsert);
        Collections.shuffle(shuffledVariants);

        for (int i = 0; i < shuffledVariants.size(); i++) {
            System.out.println(i + 1 + ")" + shuffledVariants.get(i));

        }
        System.out.print("Enter a right variant number: ");
        Scanner scanner = new Scanner(System.in);
        int rightVariant = scanner.nextInt() - 1;

        while (rightVariant < 0 || rightVariant > shuffledVariants.size() - 1) {
            System.out.println("Нет такого варианта ответа");
            rightVariant = scanner.nextInt() - 1;
        }
        if (shuffledVariants.get(rightVariant).equals(variantsToInsert.get(0))) {
            System.out.println("Выбран правильный вариант ответа");
            return true;
        } else {
            System.out.println("Выбран неправильный вариант ответа");
            return false;
        }
    }

}
