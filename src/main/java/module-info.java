module com.justiceasare.gtplibrabry {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.justiceasare.gtplibrabry.controller to javafx.fxml;
    opens com.justiceasare.gtplibrabry.model to javafx.base;
    opens com.justiceasare.gtplibrabry.dao;
    opens com.justiceasare.gtplibrabry.util;

    exports com.justiceasare.gtplibrabry.app;
    exports com.justiceasare.gtplibrabry.controller;
    exports com.justiceasare.gtplibrabry.model;
    exports com.justiceasare.gtplibrabry.dao;
    exports com.justiceasare.gtplibrabry.util;
}