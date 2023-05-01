package ru.croc.Project;

import ru.croc.Project.db.tests.Test;
import ru.croc.Project.db.tests.TestDao;
import ru.croc.Project.db.usersTests.UsersTests;
import ru.croc.Project.db.variants.Variant;
import ru.croc.Project.db.variants.VariantDao;

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
        if (login.length() > 30) {
            System.out.println("Логин слишком длинный");
        }
        profile = UserProfile.logIn(login, connection);

        boolean onGoing = true;
        while (onGoing) {
            String[] options;
            if (profile.getUser().isAdmin()) {
                options = new String[]{"Запустить тест",  "Показать пройденные тесты", "Сохранить прогресс и выйти",  "Вывести список тестов", "Создать тест", "Удалить тест", "Добавить вариант тесту", "Удалить вариант"};
                System.out.println("Выберите опцию:");
                for (int i = 0; i < options.length; i++) {
                    System.out.println(i + 1 + ") " + options[i]);
                }
            } else {
                options = new String[]{"Запустить тест", "Показать пройденные тесты",  "Сохранить прогресс и выйти"};
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
                case 3:
                    profile.commitToDB(connection);
                    onGoing = false;
                    break;
                case 4:
                    showAllTests();
                    break;
                case 6:
                    System.out.print("Введие id теста на удаление: ");
                    int id = scan.nextInt();
                    deleteTest(id);
                    break;
                case 5:
                    int testId = createTest();
                    System.out.println("Добавьте 2 варианта для теста:");
                    addVariantToTest(testId);
                    addVariantToTest(testId);
                    break;
                case 7:
                    System.out.print("Введите id теста к которому хотите добавить вариант: ");
                    addVariantToTest(scan.nextInt());
                    break;
                case 8:
                    System.out.print("Введие id варианта на удаление: ");
                    int variantId = scan.nextInt();
                    try {
                        new VariantDao(connection).delete(variantId);
                    } catch (SQLException e) {throw new RuntimeException(e);}
                case 2:
                    showTests();

            }
        }
    }


    private static void showTests() {
        InsertWordTest test;
        int i = 0;
        for (UsersTests userTest : profile.getUsersTestsCollection()) {
            i++;
            boolean passed = userTest.isPassed();
            test = InsertWordTest.readFromDB(userTest.getTestId(), connection);

            String text = test.getTest().getText();

            System.out.print(i + ") " + text + " - " + (passed ? "правильный(-е) вариант(ы): " : "неудача\n"));
            for (Variant variant : test.getVariants()) {
                if (variant.isRight()) {
                    System.out.print(variant.getText() + ", ");
                }
            }
            System.out.println();
        }
    }

    private static void addVariantToTest(int testId) {
        Scanner scanner = new Scanner(System.in);

        Variant variant = new Variant();
        variant.setId(-1);
        variant.setTestId(testId);
        System.out.print("Введите текст варианта для теста: ");
        variant.setText(scanner.nextLine());

        System.out.print("Правильный ли это варианта ответа +/-: ");
        variant.setRight(scanner.nextLine() == "+");

        try {
            new VariantDao(connection).create(variant);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private static int createTest() {
        Test test = new Test();
        test.setId(-1);

        scanner = new Scanner(System.in);

        System.out.println("Введите тело теста: ");
        test.setText(scanner.nextLine());
        System.out.println("Введите описание теста (опционально): ");
        //test.setDescription(scanner.nextLine());

        try {
            TestDao testDao = new TestDao(connection);
            testDao.create(test);

            int id = testDao.read(test.getText()).getId();
            test.setId(id);
            return id;

        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    private static void deleteTest(int id) {
        try {
            new TestDao(connection).delete(id);

        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException();
        }

    }

    private static void showAllTests() {
        System.out.println("id) test text \t/\t test description");
        System.out.println("\t\tid) variant / is variant right");
        try {
            for (Test test : new TestDao(connection).readAll()) {
                System.out.println(test.getId() + ") " + test.getText() + "\t\t\t" + test.getDescription());
                Collection<Variant> variants = InsertWordTest.readVariantsFromDB(test, connection).getVariants();

                for (Variant variant : variants) {
                    System.out.println("\t\t" + variant.getId() + ") " + variant.getText() + "\t\t" + (variant.isRight() ? "right" : ""));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static InsertWordTest chooseTest() {
        if (availableTests.size() < 1) {
            getAvailableTestsIds();
        }

        InsertWordTest availableTest = InsertWordTest.readVariantsFromDB(availableTests.get(0), connection);
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

        List<Variant> shuffledVariants = new ArrayList<>(test.getVariants());
        Collections.shuffle(shuffledVariants);

        for (int i = 0; i < shuffledVariants.size(); i++) {
            System.out.println(i + 1 + ")" + shuffledVariants.get(i).getText());

        }
        System.out.print("Enter a right variant number: ");
        Scanner scanner = new Scanner(System.in);
        int rightVariant = scanner.nextInt() - 1;

        while (rightVariant < 0 || rightVariant > shuffledVariants.size() - 1) {
            System.out.println("Нет такого варианта ответа");
            rightVariant = scanner.nextInt() - 1;
        }
        if (shuffledVariants.get(rightVariant).isRight()) {
            System.out.println("Выбран правильный вариант ответа");
            return true;
        } else {
            System.out.println("Выбран неправильный вариант ответа");
            return false;
        }
    }

}
