@SuppressWarnings("module")
module mx.edu.uacm.is.slt.ds.multi_tareas117 {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires atlantafx.base;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens mx.edu.uacm.is.slt.ds.multi_tareas117 to javafx.fxml;

    opens mx.edu.uacm.is.slt.ds.multi_tareas117.controller to javafx.fxml;
    opens mx.edu.uacm.is.slt.ds.multi_tareas117.views.layout to javafx.fxml;
    opens mx.edu.uacm.is.slt.ds.multi_tareas117.db to javafx.fxml;
    opens mx.edu.uacm.is.slt.ds.multi_tareas117.DAO to javafx.fxml;

    exports mx.edu.uacm.is.slt.ds.multi_tareas117;
    exports mx.edu.uacm.is.slt.ds.multi_tareas117.controller;
    exports mx.edu.uacm.is.slt.ds.multi_tareas117.model;

    exports mx.edu.uacm.is.slt.ds.multi_tareas117.db;
    exports mx.edu.uacm.is.slt.ds.multi_tareas117.DAO;

}