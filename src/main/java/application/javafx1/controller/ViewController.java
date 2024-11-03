package application.javafx1.controller;

import application.javafx1.guiUtil.Alerts;
import application.javafx1.modelEntities.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    @FXML
    private Button btAll;

    @FXML
    public void onBtAllAction(){

        for(Person person : comboBoxPerson.getItems()){

            System.out.println(person);

        }

    }

    @FXML
    private ComboBox<Person> comboBoxPerson;

    private ObservableList<Person> obsList;

        // Alerts.showAlert("Titulo", "Cabeçalho", "Parabens você clicou em um botão", Alert.AlertType.INFORMATION);

    public void onComboBoxPersonAction(){

        Person person = comboBoxPerson.getSelectionModel().getSelectedItem();

        Alerts.showAlert("Pessoa", null, person.toString(), Alert.AlertType.INFORMATION);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Person> list = new ArrayList<>();
        list.add(new Person(1, "Rafa","rafael.souza@cocamar.com.br"));
        list.add(new Person(2, "Fer","fer@email.com.br"));
        list.add(new Person(3, "Bianca","bianca@cocamar.com.br"));

        obsList = FXCollections.observableArrayList(list);
        comboBoxPerson.setItems(obsList);

        Callback<ListView<Person>, ListCell<Person>> factory = lv -> new ListCell<Person>() {
            @Override
            protected void updateItem(Person item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };

        comboBoxPerson.setCellFactory(factory);
        comboBoxPerson.setButtonCell(factory.call(null));

    }
}
