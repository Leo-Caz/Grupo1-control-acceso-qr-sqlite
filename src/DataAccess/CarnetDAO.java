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

import DataAccess.DTO.CarnetDTO;
import DataAccess.Helpers.DataHelperSQLite;
import Infrastructure.Config.BNAppException;

public class CarnetDAO extends DataHelperSQLite implements IDAO<CarnetDTO> {

    @Override
    public boolean create(CarnetDTO entity) throws BNAppException {
        String query = "INSERT INTO Carnet (IdUsuario, CodigoQR) VALUES (?, ?)";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, entity.getIdUsuario());
            pstmt.setString(2, entity.getCodigoQR());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "create()");
        }
    }

    @Override
    public List<CarnetDTO> readAll() throws BNAppException {
        List<CarnetDTO> lst = new ArrayList<>();
        String query = "SELECT IdCarnet, IdUsuario, CodigoQR, Estado, FechaCreacion, FechaModificacion " +
                       "FROM Carnet WHERE Estado = 'A'";

        try {
            Connection conn = openConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                CarnetDTO c = new CarnetDTO(
                    rs.getInt("IdCarnet"),
                    rs.getInt("IdUsuario"),
                    rs.getString("CodigoQR"),
                    rs.getString("Estado"),
                    rs.getString("FechaCreacion"),
                    rs.getString("FechaModificacion")
                );
                lst.add(c);
            }
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "readAll()");
        }
        return lst;
    }

    @Override
    public CarnetDTO readById(Integer id) throws BNAppException {
        CarnetDTO c = null;
        String query = "SELECT IdCarnet, IdUsuario, CodigoQR, Estado, FechaCreacion, FechaModificacion " +
                       "FROM Carnet WHERE Estado = 'A' AND IdCarnet = ?";

        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                c = new CarnetDTO(
                    rs.getInt("IdCarnet"),
                    rs.getInt("IdUsuario"),
                    rs.getString("CodigoQR"),
                    rs.getString("Estado"),
                    rs.getString("FechaCreacion"),
                    rs.getString("FechaModificacion")
                );
            }
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "readById()");
        }
        return c;
    }

    @Override
    public boolean update(CarnetDTO entity) throws BNAppException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String query = "UPDATE Carnet SET IdUsuario = ?, CodigoQR = ?, FechaModificacion = ? " +
                       "WHERE IdCarnet = ?";

        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, entity.getIdUsuario());
            pstmt.setString(2, entity.getCodigoQR());
            pstmt.setString(3, dtf.format(now));
            pstmt.setInt(4, entity.getIdCarnet());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "update()");
        }
    }

    @Override
    public boolean delete(int id) throws BNAppException {
        String query = "UPDATE Carnet SET Estado = ? WHERE IdCarnet = ?";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "X");
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "delete()");
        }
    }

    public CarnetDTO readByCodigoQR(String codigoQR) throws BNAppException {
        CarnetDTO c = null;
        String query = "SELECT IdCarnet, IdUsuario, CodigoQR, Estado, FechaCreacion, FechaModificacion " +
                       "FROM Carnet WHERE Estado = 'A' AND CodigoQR = ?";

        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, codigoQR);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                c = new CarnetDTO(
                    rs.getInt("IdCarnet"),
                    rs.getInt("IdUsuario"),
                    rs.getString("CodigoQR"),
                    rs.getString("Estado"),
                    rs.getString("FechaCreacion"),
                    rs.getString("FechaModificacion")
                );
            }
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "readByCodigoQR()");
        }
        return c;
    }

    public CarnetDTO readByUsuarioId(Integer idUsuario) throws BNAppException {
        CarnetDTO c = null;
        String query = "SELECT IdCarnet, IdUsuario, CodigoQR, Estado, FechaCreacion, FechaModificacion " +
                       "FROM Carnet WHERE Estado = 'A' AND IdUsuario = ?";

        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                c = new CarnetDTO(
                    rs.getInt("IdCarnet"),
                    rs.getInt("IdUsuario"),
                    rs.getString("CodigoQR"),
                    rs.getString("Estado"),
                    rs.getString("FechaCreacion"),
                    rs.getString("FechaModificacion")
                );
            }
        } catch (SQLException e) {
            throw new BNAppException(e, getClass().getName(), "readByUsuarioId()");
        }
        return c;
    }
}