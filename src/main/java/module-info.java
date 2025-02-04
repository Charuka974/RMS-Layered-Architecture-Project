module RMS.Project {
    requires com.jfoenix;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires lombok;
    requires java.mail;
    requires jdk.compiler;
    requires net.sf.jasperreports.core;
    requires jbcrypt;



    opens org.gourmetDelight.controller to javafx.fxml;
    opens org.gourmetDelight.dto to javafx.base;
    opens org.gourmetDelight.entity to javafx.base;
    exports org.gourmetDelight;
    exports org.gourmetDelight.controller;
    exports org.gourmetDelight.dao.custom.impl.home;




    exports org.gourmetDelight.dao;
    exports org.gourmetDelight.controller.login;
    opens org.gourmetDelight.controller.login to javafx.fxml;
    exports org.gourmetDelight.controller.home;
    opens org.gourmetDelight.controller.home to javafx.fxml;
    exports org.gourmetDelight.controller.inventory;
    opens org.gourmetDelight.controller.inventory to javafx.fxml;
    exports org.gourmetDelight.controller.employee;
    opens org.gourmetDelight.controller.employee to javafx.fxml;
    exports org.gourmetDelight.controller.orders;
    opens org.gourmetDelight.controller.orders to javafx.fxml;
    exports org.gourmetDelight.dao.custom.impl.employee;
    opens org.gourmetDelight.dto.employee to javafx.base;
    opens org.gourmetDelight.dto.inventory to javafx.base;
    opens org.gourmetDelight.dto.menuItems to javafx.base;
    exports org.gourmetDelight.dao.custom.impl.menuItems;
    opens org.gourmetDelight.dto.tm to javafx.base;
    opens org.gourmetDelight.dto.reservations to javafx.base;
    exports org.gourmetDelight.controller.reservations;
    opens org.gourmetDelight.controller.reservations to javafx.fxml;
    opens org.gourmetDelight.dto.custom to javafx.base;


}