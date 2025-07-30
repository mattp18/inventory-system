module com.code4joe.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.slf4j;

    opens com.code4joe.inventorysystem.addItem to javafx.fxml;

    opens com.code4joe.inventorysystem.itemDetail to javafx.fxml;

    opens com.code4joe.inventorysystem to javafx.fxml;
    exports com.code4joe.inventorysystem;
}