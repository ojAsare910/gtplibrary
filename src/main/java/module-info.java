module com.justiceasare.gtplibrabry {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.justiceasare.gtplibrary.controller to javafx.fxml;
    opens com.justiceasare.gtplibrary.app to javafx.fxml;
    opens com.justiceasare.gtplibrary.model to javafx.base;
    opens com.justiceasare.gtplibrary.dao;
    opens com.justiceasare.gtplibrary.util;

    exports com.justiceasare.gtplibrary.app;
    exports com.justiceasare.gtplibrary.controller;
    exports com.justiceasare.gtplibrary.model;
    exports com.justiceasare.gtplibrary.dao;
    exports com.justiceasare.gtplibrary.util;
}