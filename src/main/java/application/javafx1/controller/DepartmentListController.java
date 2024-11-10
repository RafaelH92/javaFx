package application.javafx1.controller;

import application.javafx1.Main;
import application.javafx1.guiUtil.Alerts;
import application.javafx1.modelServices.DepartmentService;
import application.javafx1.modelEntities.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    private DepartmentService service;

    @FXML
    private TableView<Department> tableViewDepartment;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private TableColumn<Department, String> tableColumnName;

    @FXML
    private Button btNew;

    private ObservableList<Department> obsList;

    @FXML
    public void onBtNewAction(){

        Alerts.showAlert("titulo", null,"Click no botão new", Alert.AlertType.INFORMATION);

    }

    public void setDepartmentService(DepartmentService service){

        this.service = service;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeNodes();

    }

    private void initializeNodes() {

        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        /*Obtem referencia da janela principal e seta a altura no Table View */
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView(){

        if(service == null){

            throw new IllegalStateException("Servide was null");

        }

        List<Department> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewDepartment.setItems(obsList);

    }

}
