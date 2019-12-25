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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import sample.DBHelper;
import sample.model.Employee;
import sample.model.RetailPurchase;
import sample.model.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeManagerPanelController implements Initializable,PanelController {
    private User user;
    private DBHelper dbHelper;

    @FXML
    private TextField tf1;
    @FXML
    private TableView<Employee> table1;
    @FXML
    private TableView<Employee> table2;
    @FXML
    private TableView<Triple<Employee,String,String>> table3;
    @FXML
    private TableColumn<Employee,Integer> idTable1;
    @FXML
    private TableColumn<Employee,String> nameTable1;
    @FXML
    private TableColumn<Employee,Integer> idTable2;
    @FXML
    private TableColumn<Employee,String> nameTable2;
    @FXML
    private TableColumn<Employee,Integer> salaryTable2;
    @FXML
    private TableColumn<Employee,String> positionTable2;
    @FXML
    private TableColumn<ImmutableTriple<Employee,String,String>,Integer> idTable3;
    @FXML
    private TableColumn<ImmutableTriple<Employee,String,String>,String> nameTable3;
    @FXML
    private TableColumn<ImmutableTriple<Employee,String,String>,Integer> salaryTable3;
    @FXML
    private TableColumn<ImmutableTriple<Employee,String,String>,String> positionTable3;
    @FXML
    private TableColumn<ImmutableTriple<Employee,String,String>,String> commentTable3;
    @FXML
    private TableColumn<ImmutableTriple<Employee,String,String>,String> ordersTable3;
    @FXML
    private Label labelProfile;

    public void btnQuery1Executed(){
        List<Employee> data = dbHelper.getQueryEmployeeManager1(Integer.valueOf(tf1.getText()));
        table1.setItems(FXCollections.observableArrayList(data));
    }
    public void btnQuery2Executed(){
        List<Employee> data = dbHelper.getQueryEmployeeManager2();
        table2.setItems(FXCollections.observableArrayList(data));
    }
    public void btnQuery3Executed(){
        List<ImmutableTriple<Employee,String,String>> data = dbHelper.getQueryEmployeeManager3();
        table3.setItems(FXCollections.observableArrayList(data));
    }
    public void btnUpdateData3(){
        dbHelper.updateDataEmployeeManager();
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

        idTable2.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTable2.setCellValueFactory(new PropertyValueFactory<>("name"));
        salaryTable2.setCellValueFactory(new PropertyValueFactory<>("salary"));
        positionTable2.setCellValueFactory(new PropertyValueFactory<>("position"));

        commentTable3.setCellValueFactory(new PropertyValueFactory<>("right"));
        ordersTable3.setCellValueFactory(new PropertyValueFactory<>("middle"));
        idTable3.setCellValueFactory(param -> (new SimpleIntegerProperty(param.getValue().getLeft().getId()).asObject()));
        nameTable3.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLeft().getName()));
        salaryTable3.setCellValueFactory(param -> (new SimpleIntegerProperty(param.getValue().getLeft().getSalary()).asObject()));
        positionTable3.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLeft().getPosition()));
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        labelProfile.setText(user.username +" : "+user.type.toString());
    }
}
