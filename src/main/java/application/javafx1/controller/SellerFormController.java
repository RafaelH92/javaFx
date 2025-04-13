package application.javafx1.controller;

import application.javafx1.guiListeners.DataChangeListener;
import application.javafx1.guiUtil.Alerts;
import application.javafx1.guiUtil.Constraints;
import application.javafx1.guiUtil.Utils;
import application.javafx1.modelEntities.Department;
import application.javafx1.modelEntities.Seller;
import application.javafx1.modelExceptions.ValidationException;
import application.javafx1.modelServices.DepartmentService;
import application.javafx1.modelServices.SellerService;
import db.DbException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    /* Dependencia para o Seller */
    private Seller entity;

    /* Dependencia do Seller service */
    private SellerService service;

    /*Dependencia para buscar do banco os departments */
    private DepartmentService departmentService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private TextField txtBaseSalary;

    @FXML
    private ComboBox<Department> comboBoxDepartment;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorBirthDate;

    @FXML
    private Label labelErrorBaseSalary;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;
    
    private ObservableList<Department> obsList;

    /* Metodo set para a entidade Seller */
    public void setSeller(Seller entity){
        this.entity = entity;
    }

    /* Metodo set dependencias  */
    public void setServices(SellerService service, DepartmentService departmentService){
        this.service = service;
        this.departmentService = departmentService;
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event){
        /* Validacoes */
        if (entity == null){
            throw new IllegalStateException("Entity was null");
        }
        if (service == null){
            throw new IllegalStateException("Service was null");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity); /* Salvar no banco de dados */
            notifyDataChangeListeners(); /* Notifica que foi salvo com sucesso */
            Utils.currentStage(event).close(); /* Obtem a referencia da janela atual (formulario) e fecha */

        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
        catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }


    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Seller getFormData() {
        Seller obj = new Seller();

        /*Instaciando a excecao de erro */
        ValidationException exception = new ValidationException("Validation error");

        obj.setId(Utils.tryParceToInt(txtId.getText()));

        if (txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("name", "Field can't be empty");
        }
        obj.setName(txtName.getText());

        /* Verificando se a colecao tem mais de um erro, se houver lanca a excecao */
        if(exception.getErrors().size() > 0){
            throw exception;
        }

        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event){
        Utils.currentStage(event).close(); /* Obtem a referencia da janela atual (formulario) e fecha */
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    /* Iniciar componentes da tela de cadastro e aplicar as restrições dos campos */
    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 30);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
        initializeComboBoxDepartment();
    }

    /* Seta os dados do objeto Seller nos campos do dialog */
    public void updateFormData(){
        if (entity == null){
            throw new IllegalStateException("Entity  was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US); /*Adicionar "." (ponto) e não a "," (Virgula) no double*/
        txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
        if (entity.getBirthDate() != null) { /*Validação para evitar de erro de conversao de um valor nulo*/
            dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault())); /*Pega a data que esta no banco de dados e converte para o formato da maquina que esta sendo executada*/
        }
        if (entity.getDepartment() == null){ /* Validação para quando for cadastro novo ou alteracao */
            comboBoxDepartment.getSelectionModel().selectFirst();
        }
        else {
            comboBoxDepartment.setValue(entity.getDepartment());
        }
    }
    
    /* Carrega a lista de Departments para preencher no select */
    public void loadAssociateObjects(){
        if (departmentService == null){
            throw new IllegalStateException("DepartmentService was null");
        }
        List<Department> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);
    }

    /* Seta os erros no campo do formulario */
    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        if (fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        };
    }

    /* Iniciar comboBox de department */
    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }
}
