//  ©──❰ 💀 © 2K25 PatMic ❱──© 

package DataAccess.Helpers;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DataHelperSQLiteDAO<T> implements IDAO<T> {
    protected final Class<T>DTOClass;
    protected final String  tableName;
    protected final String  tablePK;

    private static final String DBPath = "jdbc:sqlite:db//Usuario.sqlite";
    private static Connection conn = null;

    protected static synchronized Connection openConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DBPath);
        }
        return conn;
    }

    protected static void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    protected String getDataTimeNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 
     * @param DTOClass  : Nombre de la clase DTO       
     * @param tableName : Nombre de la tabla
     * @param tablePK   : Nombre del PK de la tabla
     * @throws Exception: Error que no se puede asociar la clase a tabla
     */
    public DataHelperSQLiteDAO(Class<T> DTOClass, String tableName, String tablePK) throws Exception {
        openConnection();
        this.DTOClass  = DTOClass;
        this.tableName = tableName;
        this.tablePK   = tablePK;
    }

    @Override
    public boolean create(T entity) throws Exception {
        Field[] fields = DTOClass.getDeclaredFields();
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            // Excluir PK y campos por defecto y auditoria
            if (!name.equalsIgnoreCase(tablePK)
                && !name.equalsIgnoreCase("Estado")
                && !name.equalsIgnoreCase("FechaCreacion")
                && !name.equalsIgnoreCase("FechaModifica")) {
                columns.append(name).append(",");
                placeholders.append("?,");
            }
        }

        // Eliminar la última coma
        String cols = columns.substring(0, columns.length() - 1);
        String vals = placeholders.substring(0, placeholders.length() - 1);

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, cols, vals);

        try (PreparedStatement stmt = openConnection().prepareStatement(sql)) {
            int index = 1;
            for (Field field : fields) {
                String name = field.getName();
                if (!name.equalsIgnoreCase(tablePK)
                    && !name.equalsIgnoreCase("Estado")
                    && !name.equalsIgnoreCase("FechaCreacion")
                    && !name.equalsIgnoreCase("FechaModifica")) 
                        stmt.setObject(index++, field.get(entity));
            }
            return (stmt.executeUpdate() > 0);
        } 
    }

    @Override
    public boolean update(T entity) throws Exception {
        Field[] fields = DTOClass.getDeclaredFields();
        StringBuilder updates = new StringBuilder();
        Object pkValue = null;

        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            if (!name.equalsIgnoreCase(tablePK)) {
                updates.append(name).append(" = ?,");
            } else {
                pkValue = field.get(entity);
            }
        }

        updates.append("FechaModifica = ?");

        String sql = String.format("UPDATE %s SET %s WHERE %s = ?", tableName, updates, tablePK);

        try (PreparedStatement stmt = openConnection().prepareStatement(sql)) {
            int index = 1;
            for (Field field : fields) {
                if (!field.getName().equalsIgnoreCase(tablePK)) {
                    stmt.setObject(index++, field.get(entity));
                }
            }
            stmt.setString(index++, getDataTimeNow());
            stmt.setObject(index, pkValue);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        String sql = String.format("UPDATE %s SET Estado = ?, FechaModifica = ? WHERE %s = ?", tableName, tablePK);
        try (PreparedStatement stmt = openConnection().prepareStatement(sql)) {
            stmt.setString(1, "X");
            stmt.setString(2, getDataTimeNow());
            stmt.setInt   (3, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public T readBy(Integer id) throws Exception {
        String sql = String.format("SELECT * FROM %s WHERE %s = ? AND Estado = 'A'", tableName, tablePK);
        try (PreparedStatement stmt = openConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToEntity(rs) : null;
            }
        }
    }

    @Override
    public List<T> readAll() throws Exception {
        List<T> list = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s WHERE Estado = 'A'", tableName);
        try (PreparedStatement stmt = openConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        }
        return list;
    }

    @Override
    public Integer getMaxReg() throws Exception {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE Estado = 'A'", tableName);
        try (PreparedStatement stmt = openConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    protected T mapResultSetToEntity(ResultSet rs) throws Exception {
        T instance = DTOClass.getDeclaredConstructor().newInstance();
        ResultSetMetaData meta = rs.getMetaData();

        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String col = meta.getColumnName(i);
            Object val = rs.getObject(i);
            try {
                Field field = DTOClass.getDeclaredField(col);
                field.setAccessible(true);
                field.set(instance, val);
            } catch (NoSuchFieldException ignored) {}
        }
        return instance;
    }
}