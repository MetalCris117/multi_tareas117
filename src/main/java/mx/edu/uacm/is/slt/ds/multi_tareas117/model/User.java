package mx.edu.uacm.is.slt.ds.multi_tareas117.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final StringProperty name;
    private final StringProperty status;
    private final StringProperty avatar;

    public User(String name, String status, String avatar) {
        this.name = new SimpleStringProperty(name);
        this.status = new SimpleStringProperty(status);
        this.avatar = new SimpleStringProperty(avatar);
    }

    // --- Getters ---
    public String getName() { return name.get(); }
    public String getStatus() { return status.get(); }
    public String getAvatar() { return avatar.get(); }

    public StringProperty nameProperty() { return name; }
    public StringProperty statusProperty() { return status; }
    public StringProperty avatarProperty() { return avatar; }
}