package application.javafx1.modelServices;

import application.javafx1.modelEntities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    public List<Department> findAll(){

        List<Department> list = new ArrayList<>();

        list.add(new Department(1, "Fer"));
        list.add(new Department(2, "Flavia"));
        list.add(new Department(3, "Bianca"));
        list.add(new Department(4, "Dani"));

        return list;

    }
}
