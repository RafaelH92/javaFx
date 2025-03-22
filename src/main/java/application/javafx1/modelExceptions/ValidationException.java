package application.javafx1.modelExceptions;

import java.util.HashMap;
import java.util.Map;

/* Excecao personalizada que carrega uma colecao contendo os erros possiveis do formulario */

public class ValidationException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private Map<String, String> errors = new HashMap<>();

    public ValidationException(String msg){
        super(msg);
    }

    public Map<String, String> getErrors(){
        return errors;
    }

    public void addError(String fildname, String errorMessage){
        errors.put(fildname, errorMessage);
    }
}
