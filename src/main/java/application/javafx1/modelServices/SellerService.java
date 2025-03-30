package application.javafx1.modelServices;

import application.javafx1.modelDao.DaoFactory;
import application.javafx1.modelDao.SellerDao;
import application.javafx1.modelEntities.Seller;

import java.util.List;

public class SellerService {

    private SellerDao dao = DaoFactory.createSellerDao();

    public List<Seller> findAll(){

        return dao.findAll();

    }

    public void saveOrUpdate(Seller obj){
        if (obj.getId() == null){ /* Se o Id do Seller for nulo sera criado um novo registro no banco de dados */
            dao.insert(obj);
        }
        else{ /* Caso contrario sera realizado a alteracao */
            dao.update(obj);
        }
    }

    public void remove(Seller obj){
        dao.deleteById(obj.getId()); /* Deleta o registro */
    }
}
