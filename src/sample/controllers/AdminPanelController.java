package sample.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.DBHelper;
import sample.model.User;
import sample.model.UserType;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminPanelController implements Initializable,PanelController {
    private DBHelper dbHelper;
    private User user;
    @FXML
    Label labelProfile;
    @FXML
    ListView<User> usersList;

    public void setUser(User user) {
        this.user = user;
        labelProfile.setText(user.username +" : "+user.type.toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHelper = DBHelper.getInstance();
        List<User> users = dbHelper.getAllUsers();
        usersList.setItems(FXCollections.observableArrayList(users));
    }

    public void btnAddUserPressed(){
        openUserEditor(null);
    }

    public void btnAlterUserPressed(){
        User selectedUser = usersList.getSelectionModel().getSelectedItem();
        openUserEditor(selectedUser);
    }
    public void btnDeleteUserPressed(){
        User selectedUser = usersList.getSelectionModel().getSelectedItem();
        int selectedItemIndex = usersList.getSelectionModel().getSelectedIndex();
        dbHelper.dropUser(selectedUser);
        usersList.getItems().remove(selectedItemIndex);
    }
    private void openUserEditor(User selectedUser){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../layout/user.fxml"));
            Parent root = loader.load();
            UserController controller = loader.getController();
            controller.setConfirmCallback(user -> {
                if (selectedUser!=null&&user.id==selectedUser.id){
                    int indexToInsert = usersList.getSelectionModel().getSelectedIndex();
                    usersList.getItems().remove(indexToInsert);
                    usersList.getItems().add(indexToInsert,user);
                }
                else usersList.getItems().add(user);
            });
            controller.setUser(selectedUser);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOpacity(1);
            stage.setScene(new Scene(root, 310, 180));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
