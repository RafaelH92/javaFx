package application.javafx1.controller;

import application.javafx1.Main;
import application.javafx1.guiUtil.Alerts;
import application.javafx1.guiUtil.Utils;
import application.javafx1.modelServices.DepartmentService;
import application.javafx1.modelEntities.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
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
    public void onBtNewAction(ActionEvent event){

        Stage parentStage = Utils.currentStage(event);
        /* Acao de cadastrar obj Department sera passado nulo */
        Department obj = new Department();
        createDialogForm(obj,"/application/javafx1/gui/DepartmentForm.fxml", parentStage);

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

    private void createDialogForm(Department obj, String absoluteName, Stage parentStage){

        try {

            FXMLLoader loader =  new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            /* Obter refencia para o controlador, passando o objeto para carregar no dialog */
            DepartmentFormController controller = loader.getController();
            controller.setDepartment(obj);
            controller.setDepartmentService(new DepartmentService());
            controller.updateFormData();

            /* Instaciar e configurar o novo stage */
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Department data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (Exception e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
