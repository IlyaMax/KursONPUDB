package sample.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import sample.DBHelper;
import sample.model.Reagent;
import sample.model.RetailPurchase;
import sample.model.User;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SalesManagerPanelController implements Initializable,PanelController {
    private DBHelper dbHelper;
    private User user;
    @FXML
    private Label labelProfile;
    @FXML
    private DatePicker datePicker1;
    @FXML
    private DatePicker datePicker2;
    @FXML
    private DatePicker datePicker3;
    @FXML
    private DatePicker datePicker4;
    @FXML
    private TableView<RetailPurchase> table1;
    @FXML
    private TableView<RetailPurchase> table2;
    @FXML
    private TableView<Pair<Reagent,Integer>> table5;
    @FXML
    private TableView<Pair<Reagent, String>> table6;
    @FXML
    private TableView<Pair<Reagent, String>> table7;
    @FXML
    private TableColumn<RetailPurchase,Integer> quantityTable1;
    @FXML
    private TableColumn<RetailPurchase,Integer> priceTable1;
    @FXML
    private TableColumn<RetailPurchase,String> reagentNameTable1;
    @FXML
    private TableColumn<RetailPurchase, String> dateTable1;
    @FXML
    private TableColumn<RetailPurchase,Integer> quantityTable2;
    @FXML
    private TableColumn<RetailPurchase,Integer> priceTable2;
    @FXML
    private TableColumn<RetailPurchase,String> reagentNameTable2;
    @FXML
    private TableColumn<RetailPurchase, String> dateTable2;
    @FXML
    private TableColumn<Pair<Reagent,Integer>,Integer> reagentIdTable5;
    @FXML
    private TableColumn<Pair<Reagent,Integer>,String> reagentNameTable5;
    @FXML
    private TableColumn<Pair<Reagent,Integer>,Integer> sumTable5;
    @FXML
    private TableColumn<Pair<Reagent, String>,Integer> reagentIdTable6;
    @FXML
    private TableColumn<Pair<Reagent,String>,String> reagentNameTable6;
    @FXML
    private TableColumn<Pair<Reagent,String>,String> commentTable6;
    @FXML
    private TableColumn<Pair<Reagent, String>,Integer> reagentIdTable7;
    @FXML
    private TableColumn<Pair<Reagent,String>,String> reagentNameTable7;
    @FXML
    private TableColumn<Pair<Reagent,String>,String> commentTable7;
    @FXML
    private Label labelResult1;
    @FXML
    private Label labelResult2;
    @FXML
    private TextField tfQuery5;


    public void btnExecute1Clicked(){
        Date date1 = Date.valueOf(datePicker1.getValue());
        Date date2 = Date.valueOf(datePicker2.getValue());
        List<RetailPurchase> result = dbHelper.getQuerySalesManager1(date1,date2);
        table1.setItems(FXCollections.observableArrayList(result));
    }
    public void btnExecute2Clicked(){
        Date date1 = Date.valueOf(datePicker3.getValue());
        Date date2 = Date.valueOf(datePicker4.getValue());
        List<RetailPurchase> result = dbHelper.getQuerySalesManager2(date1,date2);
        table2.setItems(FXCollections.observableArrayList(result));
    }
    public void btnExecute3Clicked(){
        Integer sum = dbHelper.getQuerySalesManager3();
        labelResult1.setText(String.format("За текущий месяц было заработано: %d грн.",(sum==null)?0:sum));
    }

    public void btnExecute4Clicked(){
        Integer sum = dbHelper.getQuerySalesManager4();
        labelResult2.setText(String.format("За текущий месяц было закуплено на: %d грн.",(sum==null)?0:sum));
    }
    public void btnExecute5Clicked(){
        List<Pair<Reagent,Integer>> result = dbHelper.getQuerySalesManager5(Integer.valueOf(tfQuery5.getText()));
        table5.setItems(FXCollections.observableArrayList(result));
    }
    public void btnExecute6Clicked(){
        List<Pair<Reagent,String>> result = dbHelper.getQuerySalesManager6();
        table6.setItems(FXCollections.observableArrayList(result));
    }
    public void btnExecute7Clicked(){
        List<Pair<Reagent,String>> result = dbHelper.getQuerySalesManager7();
        table7.setItems(FXCollections.observableArrayList(result));
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
                && printerJob.printPage(table5)
                && printerJob.printPage(table6)
                && printerJob.printPage(table7)) {
            printerJob.endJob();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHelper = DBHelper.getInstance();

        quantityTable1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceTable1.setCellValueFactory(new PropertyValueFactory<>("price"));
        reagentNameTable1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getReagent().getName()));
        dateTable1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDate().toString()));

        quantityTable2.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceTable2.setCellValueFactory(new PropertyValueFactory<>("price"));
        reagentNameTable2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getReagent().getName()));
        dateTable2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDate().toString()));

        reagentIdTable5.setCellValueFactory(param -> (new SimpleIntegerProperty(param.getValue().getKey().getId()).asObject()));
        reagentNameTable5.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey().getName()));
        sumTable5.setCellValueFactory(new PropertyValueFactory<>("value"));

        reagentIdTable6.setCellValueFactory(param -> (new SimpleIntegerProperty(param.getValue().getKey().getId()).asObject()));
        reagentNameTable6.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey().getName()));
        commentTable6.setCellValueFactory(new PropertyValueFactory<>("value"));

        reagentIdTable7.setCellValueFactory(param -> (new SimpleIntegerProperty(param.getValue().getKey().getId()).asObject()));
        reagentNameTable7.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey().getName()));
        commentTable7.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        labelProfile.setText(user.username +" : "+user.type.toString());
    }
}
