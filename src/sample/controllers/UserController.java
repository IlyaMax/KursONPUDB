package sample.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import sample.DBHelper;
import sample.model.User;
import sample.model.UserType;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class UserController implements Initializable {
    private User user;
    private Consumer<User> confirmCallback;
    private DBHelper dbHelper;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField1;
    @FXML
    PasswordField passwordField2;
    @FXML
    ComboBox<UserType> comboBox;
    @FXML
    Label infoLabel;
    @FXML
    Label pwLabel1;
    @FXML
    Label pwLabel2;
    @FXML
    Button btnConfirm;
    public void setUser(User user){
        this.user = user;
        if (user!=null){
            usernameField.setText(user.username);
            comboBox.getSelectionModel().select(user.type);
            pwLabel1.setText("Old password:");
            pwLabel2.setText("New password:");
            btnConfirm.setText("Alter");
        }
    }
    public void setConfirmCallback(Consumer<User> callback){
        this.confirmCallback = callback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBox.setItems(FXCollections.observableArrayList(UserType.values()));
        dbHelper = DBHelper.getInstance();
    }
    public void btnConfirmClicked(){
        if (user==null){
            String username = usernameField.getText();
            String password = passwordField1.getText();
            String confirmPassword = passwordField2.getText();
            UserType type = comboBox.getSelectionModel().getSelectedItem();
            if (!password.equals(confirmPassword)){
                infoLabel.setVisible(true);
                return;
            }
            Integer id = dbHelper.addNewUser(username,DigestUtils.sha256Hex(password),type);
            user = new User(id,username,password,type);
            confirmCallback.accept(user);
        }else{
            String username = usernameField.getText();
            String oldPassword = passwordField1.getText();
            String newPassword = passwordField2.getText();
            UserType type = comboBox.getSelectionModel().getSelectedItem();
            if (!DigestUtils.sha256Hex(oldPassword).equals(user.passwordHex)){
                infoLabel.setText("Old password doesn't match");
                infoLabel.setVisible(true);
                return;
            }
            User newUser = new User(user.id,username,DigestUtils.sha256Hex(newPassword),type);
            Integer id = dbHelper.updateUser(newUser);
            confirmCallback.accept(newUser);
        }

        ((Stage)comboBox.getScene().getWindow()).close();
    }
}
