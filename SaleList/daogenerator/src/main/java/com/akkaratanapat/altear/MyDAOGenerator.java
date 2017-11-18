package com.akkaratanapat.altear;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyDAOGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.akkaratanapat.altear.daogenerator");

        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void addTables(final Schema schema) {
        addUserEntities(schema);
        // addPhonesEntities(schema);
    }

    private static Entity addUserEntities(final Schema schema) {
        Entity APIFailure = schema.addEntity("APIFailure");
        APIFailure.addIdProperty().primaryKey().autoincrement();
        APIFailure.addStringProperty("name");
        APIFailure.addStringProperty("url");
        APIFailure.addStringProperty("param");

        return APIFailure;
    }

}
