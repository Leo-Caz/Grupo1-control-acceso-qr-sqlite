package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import DataAccess.DTO.CatalogoDTO;
import DataAccess.Helpers.DataHelperSQLite;
import Infrastructure.Config.BNAppException;

public class CatalogoDAO extends DataHelperSQLite implements IDAO<CatalogoDTO> {

    @Override
    public boolean create(CatalogoDTO entity) throws BNAppException {
        String query = "INSERT INTO Catalogo (IdCatalogoTipo, Nombre, Descripcion) VALUES (?, ?, ?)";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, entity.getIdCatalogoTipo());
            pstmt.setString(2, entity.getNombre());
            pstmt.setString(3, entity.getDescripcion());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "create()");
        }
    }

    @Override
    public List<CatalogoDTO> readAll() throws BNAppException {
        List<CatalogoDTO> lst = new ArrayList<>();
        String query = " SELECT IdCatalogo, IdCatalogoTipo, Nombre, Descripcion, Estado, FechaCreacion, FechaModificacion "
                     + " FROM Catalogo "
                     + " WHERE Estado = 'A' ";
        try {
            Connection conn = openConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                lst.add(new CatalogoDTO(
                    rs.getInt("IdCatalogo"),
                    rs.getInt("IdCatalogoTipo"),
                    rs.getString("Nombre"),
                    rs.getString("Descripcion"),
                    rs.getString("Estado"),
                    rs.getString("FechaCreacion"),
                    rs.getString("FechaModificacion")
                ));
            }
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "readAll()");
        }
        return lst;
    }

    /**
     * MÉTODO CRÍTICO PARA LA INTERFAZ GRÁFICA.
     * Permite obtener listas filtradas (Solo Sexos, Solo Roles, etc.)
     * @param idCatalogoTipo El ID del tipo de lista que quieres (ej: 1=Roles, 2=Sexo)
     */
    public List<CatalogoDTO> readByType(int idCatalogoTipo) throws BNAppException {
        List<CatalogoDTO> lst = new ArrayList<>();
        String query = " SELECT IdCatalogo, IdCatalogoTipo, Nombre, Descripcion, Estado, FechaCreacion, FechaModificacion "
                     + " FROM Catalogo "
                     + " WHERE Estado = 'A' AND IdCatalogoTipo = ? ";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idCatalogoTipo);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lst.add(new CatalogoDTO(
                    rs.getInt("IdCatalogo"),
                    rs.getInt("IdCatalogoTipo"),
                    rs.getString("Nombre"),
                    rs.getString("Descripcion"),
                    rs.getString("Estado"),
                    rs.getString("FechaCreacion"),
                    rs.getString("FechaModificacion")
                ));
            }
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "readByType()");
        }
        return lst;
    }

    @Override
    public boolean update(CatalogoDTO entity) throws BNAppException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String query = " UPDATE Catalogo SET IdCatalogoTipo = ?, Nombre = ?, Descripcion = ?, FechaModificacion = ? "
                     + " WHERE IdCatalogo = ?";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, entity.getIdCatalogoTipo());
            pstmt.setString(2, entity.getNombre());
            pstmt.setString(3, entity.getDescripcion());
            pstmt.setString(4, dtf.format(now));
            pstmt.setInt(5, entity.getIdCatalogo());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "update()");
        }
    }

    @Override
    public boolean delete(int id) throws BNAppException {
        String query = " UPDATE Catalogo SET Estado = ? WHERE IdCatalogo = ? ";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "X"); // Borrado lógico
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "delete()");
        }
    }

    @Override
    public CatalogoDTO readById(Integer id) throws BNAppException {
        CatalogoDTO s = null; // Corregido para retornar null si no existe
        String query = " SELECT IdCatalogo, IdCatalogoTipo, Nombre, Descripcion, Estado, FechaCreacion, FechaModificacion "
                     + " FROM Catalogo "
                     + " WHERE Estado = 'A' AND IdCatalogo = ? ";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                s = new CatalogoDTO(
                    rs.getInt("IdCatalogo"),
                    rs.getInt("IdCatalogoTipo"),
                    rs.getString("Nombre"),
                    rs.getString("Descripcion"),
                    rs.getString("Estado"),
                    rs.getString("FechaCreacion"),
                    rs.getString("FechaModificacion")
                );
            }
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "readById()");
        }
        return s;
    }
    
    public Integer getRowCount() throws BNAppException {
        String query = " SELECT COUNT(*) FROM Catalogo WHERE Estado = 'A' ";
        try {
            Connection conn = openConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "getRowCount()");
        }
        return 0;
    }
}