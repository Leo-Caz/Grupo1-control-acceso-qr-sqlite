package DataAccess;

import java.util.List;

public interface IDAO<T> {
    boolean create(T entity) throws Exception;
    List<T> readAll() throws Exception;
    boolean update(T entity) throws Exception;
    boolean delete(int id) throws Exception;
    T readById(Integer id) throws Exception;
}