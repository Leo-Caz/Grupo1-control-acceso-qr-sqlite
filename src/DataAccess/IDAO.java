package DataAccess;

import java.util.List;

import Infrastructure.Config.BNAppException;

public interface IDAO<T> {
    boolean create(T entity) throws BNAppException;
    List<T> readAll() throws BNAppException;
    boolean update(T entity) throws BNAppException;
    boolean delete(int id) throws BNAppException;
    T readById(Integer id) throws BNAppException;
}