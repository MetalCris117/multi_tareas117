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

    opens mx.edu.uacm.is.slt.ds.multi_tareas117 to javafx.fxml;

    opens mx.edu.uacm.is.slt.ds.multi_tareas117.controller to javafx.fxml;

    exports mx.edu.uacm.is.slt.ds.multi_tareas117;
    exports mx.edu.uacm.is.slt.ds.multi_tareas117.controller;

}