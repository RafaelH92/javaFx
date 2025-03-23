package application.javafx1.modelServices;

import application.javafx1.modelDao.DaoFactory;
import application.javafx1.modelDao.DepartmentDao;
import application.javafx1.modelEntities.Department;

import java.util.List;

public class DepartmentService {

    private DepartmentDao dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll(){

        return dao.findAll();

    }

    public void saveOrUpdate(Department obj){
        if (obj.getId() == null){ /* Se o Id do Departement for nulo sera criado um novo registro no banco de dados */
            dao.insert(obj);
        }
        else{ /* Caso contrario sera realizado a alteracao */
            dao.update(obj);
        }
    }

    public void remove(Department obj){
        dao.deleteById(obj.getId()); /* Deleta o registro */
    }
}
