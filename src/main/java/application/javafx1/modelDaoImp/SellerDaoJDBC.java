package application.javafx1.modelDaoImp;

import application.javafx1.modelDao.SellerDao;
import application.javafx1.modelEntities.Department;
import application.javafx1.modelEntities.Seller;
import db.DB;
import db.DbException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            /* Prepara a query */
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE seller.Id = ?");

            /* Modela a query com os parametros informados pelo usuario*/
            st.setInt(1, id);

            /* Executa a query e atribuiu o resultado para uma variavel do tipo resultset*/
            rs = st.executeQuery();

            /* Verifica se a 1 posicao possui resultado e converte o a tabela retornada do rs para objeto */
            if (rs.next()){

                /* Instancia o objeto department*/
                Department dep = instantiateDepartment(rs);

                /* Instancia o objeto Seller relacionado ao objeto department*/
                Seller obj = instantiateSeller(rs, dep);

                return obj;
            }

            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStetement(st);
            DB.closeResultSet(rs);
            /* Fechar a conexao somente na classe principal */
        }

    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {

        Seller obj = new Seller();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        obj.setDepartment(dep);
        return obj;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {

        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }
}