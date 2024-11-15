package application.javafx1.modelDaoImp;

import application.javafx1.modelDao.SellerDao;
import application.javafx1.modelEntities.Department;
import application.javafx1.modelEntities.Seller;
import db.DB;
import db.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

        PreparedStatement st = null;

        try {

            /* Prepara a query */
            st = conn.prepareStatement("INSERT INTO seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS); /* Retorna a chave criada */

            /* Modela a query com os parametros informados pelo usuario*/
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            /* Executa o update */
            int rowsAffected = st.executeUpdate();

            /* Se o resultado for maior que zero e porque foi adicionado um novo registro */
            if (rowsAffected > 0){

                /* Obtem o resultado da chave inserida e seta no obj passado de parametro */
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else{
                throw new DbException("Unexpectec error! No rows affected");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStetement(st);
            /* Fechar a conexao somente na classe principal */
        }

    }

    @Override
    public void update(Seller obj) {

        PreparedStatement st = null;

        try {

            /* Prepara a query */
            st = conn.prepareStatement("UPDATE seller " +
                    "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                    "WHERE Id = ?");

            /* Modela a query com os parametros informados pelo usuario*/
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6,obj.getId());

            /* Executa o update */
            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStetement(st);
            /* Fechar a conexao somente na classe principal */
        }

    }

    @Override
    public void deleteById(Integer id) {

        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("DELETE FROM seller " +
                    "WHERE Id = ?");
            st.setInt(1, id);

            int rows = st.executeUpdate();

            if (rows == 0){
                throw new DbException("ID not found!");
            }

        } catch (Exception e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStetement(st);
            /* Fechar a conexao somente na classe principal */
        }

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
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            /* Prepara a query */
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "ORDER BY Name");

            /* Executa a query e atribuiu o resultado para uma variavel do tipo resultset */
            rs = st.executeQuery();

            /* Declaracao de uma lista pois a query pode ter varios linhas de retorno */
            List<Seller> list = new ArrayList<>();

            /* Declaracao de um Map para realizar um mapeamento de department ( Solucao elegante ) */
            Map<Integer, Department> map = new HashMap<>();

            /* while pois pois a query pode ter mais de uma linha de retorno */
            while (rs.next()){

                Department dep = map.get(rs.getInt("DepartmentId"));

                /* Verifica se o department ja foi instanciado anteriormente */
                if (dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                /* Instancia o objeto Seller relacionado ao objeto department*/
                Seller obj = instantiateSeller(rs, dep);

                list.add(obj);

            }

            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStetement(st);
            DB.closeResultSet(rs);
            /* Fechar a conexao somente na classe principal */
        }

    }

    @Override
    public List<Seller> findByDepartment(Department department) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            /* Prepara a query */
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department "+
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE DepartmentId = ? " +
                            "ORDER BY Name");

            /* Modela a query com os parametros informados pelo usuario */
            st.setInt(1, department.getId());

            /* Executa a query e atribuiu o resultado para uma variavel do tipo resultset */
            rs = st.executeQuery();

            /* Declaracao de uma lista pois a query pode ter varios linhas de retorno */
            List<Seller> list = new ArrayList<>();

            /* Declaracao de um Map para realizar um mapeamento de department ( Solucao elegante ) */
            Map<Integer, Department> map = new HashMap<>();

            /* while pois pois a query pode ter mais de uma linha de retorno */
            while (rs.next()){

                Department dep = map.get(rs.getInt("DepartmentId"));

                /* Verifica se o department ja foi instanciado anteriormente */
                if (dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                /* Instancia o objeto Seller relacionado ao objeto department*/
                Seller obj = instantiateSeller(rs, dep);

                list.add(obj);

            }

            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStetement(st);
            DB.closeResultSet(rs);
            /* Fechar a conexao somente na classe principal */
        }

    }
}
