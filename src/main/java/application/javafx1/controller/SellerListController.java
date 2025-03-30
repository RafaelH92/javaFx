package application.javafx1.controller;

import application.javafx1.Main;
import application.javafx1.guiListeners.DataChangeListener;
import application.javafx1.guiUtil.Alerts;
import application.javafx1.guiUtil.Utils;
import application.javafx1.modelEntities.Seller;
import application.javafx1.modelServices.SellerService;
import db.DbException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerListController implements Initializable, DataChangeListener {

    private SellerService service;

    @FXML
    private TableView<Seller> tableViewSeller;

    @FXML
    private TableColumn<Seller, Integer> tableColumnId;

    @FXML
    private TableColumn<Seller, String> tableColumnName;

    @FXML
    private TableColumn<Seller, String> tableColumnEmail;

    @FXML
    private TableColumn<Seller, Date> tableColumnBirthDate;

    @FXML
    private TableColumn<Seller, Double> tableColumnBaseSalary;

    @FXML
    private TableColumn<Seller, Seller> tableColumnEDIT;

    @FXML
    private TableColumn<Seller, Seller> tableColumnREMOVE;

    @FXML
    private Button btNew;

    private ObservableList<Seller> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event){

        Stage parentStage = Utils.currentStage(event);
        /* Acao de cadastrar obj Seller sera passado nulo */
        Seller obj = new Seller();
        createDialogForm(obj,"/application/javafx1/gui/SellerForm.fxml", parentStage);

    }

    public void setSellerService(SellerService service){

        this.service = service;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeNodes();

    }

    private void initializeNodes() {

        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy"); /* Formata a data em dia/mes/ano */
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(tableColumnBaseSalary, 2); /* Formata o numeral com 2 casas decimais  */

        /*Obtem referencia da janela principal e seta a altura no Table View */
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView(){

        if(service == null){

            throw new IllegalStateException("Servide was null");

        }

        List<Seller> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewSeller.setItems(obsList);
        initEditButtons();
        initRemoveButtons();

    }

    private void createDialogForm(Seller obj, String absoluteName, Stage parentStage){

//        try {
//
//            FXMLLoader loader =  new FXMLLoader(getClass().getResource(absoluteName));
//            Pane pane = loader.load();
//
//            /* Obter refencia para o controlador, passando o objeto para carregar no dialog */
//            SellerFormController controller = loader.getController();
//            controller.setSeller(obj);
//            controller.setSellerService(new SellerService());
//            controller.subscribeDataChangeListener(this); /* Inscrever para o evento */
//            controller.updateFormData();
//
//            /* Instaciar e configurar o novo stage */
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("Enter Seller data");
//            dialogStage.setScene(new Scene(pane));
//            dialogStage.setResizable(false);
//            dialogStage.initOwner(parentStage);
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.showAndWait();
//
//        } catch (Exception e) {
//            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
//        }
    }

    @Override
    public void onDataChanged() {
        updateTableView(); /* Atualiza a lista par mostrar na grid quando o evento for disparado */
    }

    /* Adicina uma coluna na grid com um botao para realizar a edicao do registro */
    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("edit");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/application/javafx1/gui/SellerForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    /* Adicina uma coluna na grid com um botao para realizar a delecao do registro */
    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("remove");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Seller obj) {
        /* O resultado da confirmacao do usuario sera atribuido para uma variavel do tipo Optional<ButtonType> */
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

        /* Obtem o conteudo da variavel Optional pois e gerado um objeto da mesma e valida se o usuario clicou no botao OK do dialog de confirmacao */
        if (result.get() == ButtonType.OK){
            if (service == null){
                throw new IllegalStateException("Service was null");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbException e) {
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
