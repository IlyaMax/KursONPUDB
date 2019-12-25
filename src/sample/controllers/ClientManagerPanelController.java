package sample.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import sample.DBHelper;
import sample.model.Activity;
import sample.model.Client;
import sample.model.Employee;
import sample.model.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientManagerPanelController implements PanelController, Initializable {
    private User user;
    private DBHelper dbHelper;
    @FXML
    private Label labelProfile;
    @FXML
    private TableView<Client> table1;
    @FXML
    private TableView<Activity> table2;
    @FXML
    private TableView<ImmutablePair<Client,Integer>> table3;
    @FXML
    private TableColumn<Client,Integer> idTable1;
    @FXML
    private TableColumn<Client,String> nameTable1;
    @FXML
    private TableColumn<Client,String> phoneNumberTable1;
    @FXML
    private TableColumn<Activity,Integer> clientIdTable2;
    @FXML
    private TableColumn<Activity,Integer> orderIdTable2;
    @FXML
    private TableColumn<Activity,String> nameTable2;
    @FXML
    private TableColumn<ImmutablePair<Client,Integer>,Integer> idTable3;
    @FXML
    private TableColumn<ImmutablePair<Client,Integer>,String> nameTable3;
    @FXML
    private TableColumn<ImmutablePair<Client,Integer>,String> phoneNumberTable3;
    @FXML
    private TableColumn<ImmutablePair<Client,Integer>,Integer> orderNumberTable3;
    @FXML
    private TableColumn<ImmutablePair<Client,Integer>,String> infoTable3;

    public void btnQuery1Clicked(){
        List<Client> data = dbHelper.getQueryClientManager1();
        table1.setItems(FXCollections.observableArrayList(data));
    }
    public void btnQuery2Clicked(){
        List<Activity> data = dbHelper.getQueryClientManager2();
        table2.setItems(FXCollections.observableArrayList(data));
    }
    public void btnQuery3Clicked(){
        List<ImmutablePair<Client,Integer>> data = dbHelper.getQueryClientManager3();
        table3.setItems(FXCollections.observableArrayList(data));
    }
    public void btnUpdateData3(){
        dbHelper.updateDataClientManager();
    }
    public void printTables(){
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob printerJob = PrinterJob.createPrinterJob();


        //set layout to A4 and landscape
        PageLayout pageLayout = printer.createPageLayout(Paper.A4,
                PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);

        printerJob.getJobSettings().setPageLayout(pageLayout);
        if(printerJob.showPrintDialog(labelProfile.getScene().getWindow())
                && printerJob.printPage(table1)
                && printerJob.printPage(table2)
                && printerJob.printPage(table3)) {
            printerJob.endJob();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHelper = DBHelper.getInstance();

        idTable1.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTable1.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNumberTable1.setCellValueFactory(new PropertyValueFactory<>("phone"));

        clientIdTable2.setCellValueFactory(param -> (new SimpleIntegerProperty(param.getValue().getOrder().getClient().getId()).asObject()));
        orderIdTable2.setCellValueFactory(param -> (new SimpleIntegerProperty(param.getValue().getOrder().getId()).asObject()));
        nameTable2.setCellValueFactory(new PropertyValueFactory<>("name"));

        idTable3.setCellValueFactory(param -> (new SimpleIntegerProperty(param.getValue().getLeft().getId()).asObject()));
        nameTable3.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLeft().getName()));
        phoneNumberTable3.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLeft().getPhone()));
        orderNumberTable3.setCellValueFactory(new PropertyValueFactory<>("right"));
        infoTable3.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLeft().getInfo()));
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        labelProfile.setText(user.username +" : "+user.type.toString());
    }
}
