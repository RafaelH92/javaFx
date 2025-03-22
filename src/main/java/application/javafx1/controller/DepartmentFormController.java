package application.javafx1.controller;

import application.javafx1.guiListeners.DataChangeListener;
import application.javafx1.guiUtil.Alerts;
import application.javafx1.guiUtil.Constraints;
import application.javafx1.guiUtil.Utils;
import application.javafx1.modelEntities.Department;
import application.javafx1.modelExceptions.ValidationException;
import application.javafx1.modelServices.DepartmentService;
import db.DbException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {

    /* Dependencia para o Department */
    private Department entity;

    /* Dependencia do department service */
    private DepartmentService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    /* Metodo set para a entidade Department */
    public void setDepartment(Department entity){
        this.entity = entity;
    }

    /* Metodo set para a dependencia do Departmet service */
    public void setDepartmentService(DepartmentService service){
        this.service = service;
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

    private Department getFormData() {
        Department obj = new Department();

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

    /* Iniciar componentes da tela de cadastro */
    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    /* Seta os dados do objeto Department nos campos do dialog */
    public void updateFormData(){
        if (entity == null){
            throw new IllegalStateException("Entity  was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }

    /* Seta os erros no campo do formulario */
    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        if (fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        };
    }
}
