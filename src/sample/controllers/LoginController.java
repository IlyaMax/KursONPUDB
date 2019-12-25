package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.DBHelper;
import sample.model.User;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private void onButtonClicked(){
        DBHelper dbHelper = DBHelper.getInstance();
        User user = dbHelper.getUser(usernameField.getText(),passwordField.getText());
        if (user!=null){
            try {
                ((Stage)errorLabel.getScene().getWindow()).close();
                FXMLLoader loader = null;
                switch(user.type){
                    case ADMIN:
                        loader = new FXMLLoader(getClass().getResource("../layout/admin_panel.fxml"));
                        break;
                    case SALES_MANAGER:
                        loader = new FXMLLoader(getClass().getResource("../layout/sales_manager_panel.fxml"));
                        break;
                    case EMPLOYEE_MANAGER:
                        loader = new FXMLLoader(getClass().getResource("../layout/employee_manager_panel.fxml"));
                        break;
                    case CLIENT_MANAGER:
                        loader = new FXMLLoader(getClass().getResource("../layout/client_manager_panel.fxml"));
                        break;
                }
                Parent root = loader.load();
                PanelController controller = loader.getController();
                controller.setUser(user);
                Stage stage = new Stage();
                stage.initModality(Modality.NONE);
                stage.setOpacity(1);
                switch(user.type){
                    case ADMIN:
                        stage.setTitle("Admin panel");
                        break;
                    case SALES_MANAGER:
                        stage.setTitle("Sales manager panel");
                        break;
                    case EMPLOYEE_MANAGER:
                        stage.setTitle("Employee manager panel");
                        break;
                    case CLIENT_MANAGER:
                        stage.setTitle("Client manager panel");

                }

                stage.setMaximized(true);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            errorLabel.setText("User doesn't exist");
        }
    }
}
