package application.javafx1.guiUtil;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

    public static Stage currentStage(ActionEvent event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow(); /* Obtem a janela do botão clicado */
    }

    /* Metodo para converter para inteiro */
    public static Integer tryParceToInt(String str){
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
