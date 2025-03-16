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
}
