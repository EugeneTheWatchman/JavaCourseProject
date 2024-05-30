package ru.croc.Project;

import ru.croc.Project.db.tests.Test;
import ru.croc.Project.db.tests.TestDao;
import ru.croc.Project.db.variants.Variant;
import ru.croc.Project.db.variants.VariantDao;

import java.sql.*;
import java.util.*;


public class InsertWordTest {

    private Test test;
    private Collection<Variant> variants = new ArrayList<>();

    public InsertWordTest(String description, String sentence, String[] variantsToInsert) {
        Test test = new Test(-1, description, sentence);
        for (int i = 0; i < variantsToInsert.length; i++) {
            variants.add(new Variant(-1, -1, variantsToInsert[i], i == 0));
            // первый вариант всегда правильный
        }
    }

    private InsertWordTest(Test test, Collection<Variant> variants) {
        this.test = test;
        this.variants = variants;
    }

    public static InsertWordTest exampleTest(String pathToFile) {

        String description = "Insert a right variant of verb \"be\"";
        String sentence = "My favorite politician and actor %s Donald Trump";
        String[] variantsToInsert = new String[] {"is", "are", "were", "was"};

        return new InsertWordTest(description, sentence, variantsToInsert);
    }

    public void commitToDB(Connection connection) {
        try {
            // create test and get it's ID
            TestDao testDao = new TestDao(connection);
            testDao.create(this.test);
            if (this.test.getId() == -1) {
                Test temp = testDao.read(this.test.getText()); // temp теоретически может быть null
                this.test.setId(temp.getId());
            }

            // create variants
            VariantDao variantDao = new VariantDao(connection);
            for (Variant variant : variants) {
                variant.setTestId(this.test.getId());
                variantDao.create(variant);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static InsertWordTest readFromDB(int testId, Connection connection) {

        try {
            TestDao testDao = new TestDao(connection);
            Test test = testDao.read(testId);
            if (test == null) { return null; }

            VariantDao variantDao = new VariantDao(connection);
            Collection<Variant> variants = variantDao.read(test.getId());

            return new InsertWordTest(test, variants);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static InsertWordTest readVariantsFromDB(Test test, Connection connection) {

        try {
            if (test == null) { return null; }

            VariantDao variantDao = new VariantDao(connection);
            Collection<Variant> variants = variantDao.read(test.getId());

            return new InsertWordTest(test, variants);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Test getTest() {
        return test;
    }

    public Collection<Variant> getVariants() {
        return variants;
    }
}
