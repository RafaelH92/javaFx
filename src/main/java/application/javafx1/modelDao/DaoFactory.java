package application.javafx1.modelDao;

import application.javafx1.modelDaoImp.SellerDaoJDBC;
import db.DB;

public class DaoFactory {

    /* Metodo que retorna um tipo da interface */
    public static SellerDao createSellerDao(){

        /* Internamente vai instanciar uma implementacao */
        return new SellerDaoJDBC(DB.getConnection());

    }
}