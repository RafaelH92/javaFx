package application.javafx1.modelDaoImp;

import application.javafx1.modelDao.DepartmentDao;
import application.javafx1.modelEntities.Department;
import db.DB;
import db.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class
DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {

        PreparedStatement st = null;

        try {

            /* Prepara a query */
            st = conn.prepareStatement("INSERT INTO department " +
                    "(Name) " +
                    "VALUES " +
                    "(?)", Statement.RETURN_GENERATED_KEYS); /* Retorna a chave criada */

            /* Modela a query com os parametros informados pelo usuario*/
            st.setString(1, obj.getName());

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
    public void update(Department obj) {

        PreparedStatement st = null;

        try {

            /* Prepara a query */
            st = conn.prepareStatement("UPDATE department " +
                    "SET Name = ? " +
                    "WHERE Id = ?");

            /* Modela a query com os parametros informados pelo usuario*/
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());

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
            st = conn.prepareStatement("DELETE FROM department " +
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
    public Department findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            /* Prepara a query */
            st = conn.prepareStatement(
                    "SELECT * from department " +
                            "WHERE department.Id = ?");

            /* Modela a query com os parametros informados pelo usuario*/
            st.setInt(1, id);

            /* Executa a query e atribuiu o resultado para uma variavel do tipo resultset*/
            rs = st.executeQuery();

            /* Verifica se a 1 posicao possui resultado e converte o a tabela retornada do rs para objeto */
            if (rs.next()){

                /* Instancia o objeto department*/
                Department dep = instantiateDepartment(rs);

                return dep;
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

    @Override
    public List<Department> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            /* Prepara a query */
            st = conn.prepareStatement(
                    "SELECT * from department");

            /* Executa a query e atribuiu o resultado para uma variavel do tipo resultset */
            rs = st.executeQuery();

            List<Department> list = new ArrayList<>();

            /* while pois pois a query pode ter mais de uma linha de retorno */
            while (rs.next()){

                Department dep = instantiateDepartment(rs);

                list.add(dep);

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

    private Department instantiateDepartment(ResultSet rs) throws SQLException {

        Department dep = new Department();
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }
}
