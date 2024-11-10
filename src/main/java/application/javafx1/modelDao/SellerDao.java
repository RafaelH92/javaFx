package application.javafx1.modelDao;

import application.javafx1.modelEntities.Seller;

import java.util.List;

public interface SellerDao {

    void insert (Seller obj);
    void update (Seller obj);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();

}