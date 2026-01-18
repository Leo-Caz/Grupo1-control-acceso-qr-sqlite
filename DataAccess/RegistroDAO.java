package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DataAccess.DTO.RegistroDTO;
import DataAccess.Helpers.DataHelperSQLite;
import Framework.PatException;

public class RegistroDAO extends DataHelperSQLite implements IDAO<RegistroDTO> {

    @Override
    public boolean create(RegistroDTO entity) throws Exception {
        // FechaEntrada la deja por default (datetime now)
        String query = "INSERT INTO Registro (IdUsuario, IdCarnet) VALUES (?, ?)";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, entity.getIdUsuario());
            pstmt.setInt(2, entity.getIdCarnet());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "create()");
        }
    }

    @Override
    public List<RegistroDTO> readAll() throws Exception {
        List<RegistroDTO> lst = new ArrayList<>();
        String query = "SELECT IdRegistro, IdUsuario, IdCarnet, FechaEntrada, Estado " +
                       "FROM Registro WHERE Estado = 'A' ORDER BY FechaEntrada DESC";
        try {
            Connection conn = openConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                RegistroDTO r = new RegistroDTO(
                    rs.getInt("IdRegistro"),
                    rs.getInt("IdUsuario"),
                    rs.getInt("IdCarnet"),
                    rs.getString("FechaEntrada"),
                    rs.getString("Estado")
                );
                lst.add(r);
            }
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "readAll()");
        }
        return lst;
    }

    @Override
    public RegistroDTO readById(Integer id) throws Exception {
        RegistroDTO r = null;
        String query = "SELECT IdRegistro, IdUsuario, IdCarnet, FechaEntrada, Estado " +
                       "FROM Registro WHERE Estado = 'A' AND IdRegistro = ?";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                r = new RegistroDTO(
                    rs.getInt("IdRegistro"),
                    rs.getInt("IdUsuario"),
                    rs.getInt("IdCarnet"),
                    rs.getString("FechaEntrada"),
                    rs.getString("Estado")
                );
            }
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "readById()");
        }
        return r;
    }

    @Override
    public boolean update(RegistroDTO entity) throws Exception {
        // Normalmente no se actualiza registro de asistencia; lo dejo por consistencia.
        String query = "UPDATE Registro SET IdUsuario = ?, IdCarnet = ? WHERE IdRegistro = ?";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, entity.getIdUsuario());
            pstmt.setInt(2, entity.getIdCarnet());
            pstmt.setInt(3, entity.getIdRegistro());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "update()");
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String query = "UPDATE Registro SET Estado = 'X' WHERE IdRegistro = ?";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "delete()");
        }
    }

    /**
     * Método útil para el flujo del sistema: QR -> crea registro directo.
     * (Valida carnet activo, obtiene IdUsuario e IdCarnet y registra)
     */
    public boolean createByQr(String codigoQR) throws Exception {
        String query =
            "INSERT INTO Registro (IdUsuario, IdCarnet) " +
            "SELECT c.IdUsuario, c.IdCarnet " +
            "FROM Carnet c " +
            "JOIN Usuario u ON u.IdUsuario = c.IdUsuario " +
            "WHERE c.Estado = 'A' AND u.Estado = 'A' AND c.CodigoQR = ?";

        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, codigoQR);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "createByQr()");
        }
    }
}
