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

import DataAccess.DTO.UsuarioDTO;
import DataAccess.Helpers.DataHelperSQLite;
import Framework.PatException;

public class UsuarioDAO extends DataHelperSQLite implements IDAO<UsuarioDTO> {

    @Override
    public boolean create(UsuarioDTO entity) throws Exception {
        String query = "INSERT INTO Usuario ("
                + "IdCatalogoTipoUsuario, IdCatalogoSexo, IdCatalogoEstadoCivil, "
                + "PrimerNombre, SegundoNombre, PrimerApellido, SegundoApellido, "
                + "Cedula, IdCatalogoRaza, Foto) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, entity.getIdCatalogoTipoUsuario());
            pstmt.setInt(2, entity.getIdCatalogoSexo());
            pstmt.setInt(3, entity.getIdCatalogoEstadoCivil());
            pstmt.setString(4, entity.getPrimerNombre());
            pstmt.setString(5, entity.getSegundoNombre());
            pstmt.setString(6, entity.getPrimerApellido());
            pstmt.setString(7, entity.getSegundoApellido());
            pstmt.setString(8, entity.getCedula());

            // CORREGIDO: Ahora pasamos el Integer directo
            pstmt.setInt(9, entity.getIdCatalogoRaza());

            pstmt.setString(10, entity.getFoto());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "create()");
        }
    }

    @Override
    public List<UsuarioDTO> readAll() throws Exception {
        List<UsuarioDTO> lst = new ArrayList<>();

        String query = " SELECT "
                + " u.IdUsuario, u.IdCatalogoTipoUsuario, u.IdCatalogoSexo, u.IdCatalogoEstadoCivil, u.IdCatalogoRaza, "
                + " u.PrimerNombre, u.SegundoNombre, u.PrimerApellido, u.SegundoApellido, "
                + " u.Cedula, u.Foto, u.Estado, u.FechaCreacion, u.FechaModificacion, "
                + " cRol.Nombre  AS RolNombre, "
                + " cSexo.Nombre AS SexoNombre, "
                + " cCivil.Nombre AS CivilNombre, "
                + " cRaza.Nombre AS RazaNombre "
                + " FROM Usuario u "
                + " JOIN Catalogo cRol  ON u.IdCatalogoTipoUsuario = cRol.IdCatalogo "
                + " JOIN Catalogo cSexo ON u.IdCatalogoSexo = cSexo.IdCatalogo "
                + " JOIN Catalogo cCivil ON u.IdCatalogoEstadoCivil = cCivil.IdCatalogo "
                + " JOIN Catalogo cRaza ON u.IdCatalogoRaza = cRaza.IdCatalogo "
                + " WHERE u.Estado = 'A' ";

        try {
            Connection conn = openConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                UsuarioDTO u = new UsuarioDTO(
                        rs.getInt("IdUsuario"),
                        rs.getInt("IdCatalogoTipoUsuario"),
                        rs.getInt("IdCatalogoSexo"),
                        rs.getInt("IdCatalogoEstadoCivil"),
                        rs.getString("PrimerNombre"),
                        rs.getString("SegundoNombre"),
                        rs.getString("PrimerApellido"),
                        rs.getString("SegundoApellido"),
                        rs.getString("Cedula"),
                        // CORREGIDO: Recuperamos directo como Int
                        rs.getInt("IdCatalogoRaza"),
                        rs.getString("Foto"),
                        rs.getString("Estado"),
                        rs.getString("FechaCreacion"),
                        rs.getString("FechaModificacion"),
                        rs.getString("RolNombre"),
                        rs.getString("SexoNombre"),
                        rs.getString("CivilNombre"),
                        rs.getString("RazaNombre")
                );
                lst.add(u);
            }
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "readAll()");
        }
        return lst;
    }

    @Override
    public UsuarioDTO readById(Integer id) throws Exception {
        UsuarioDTO u = null;
        String query = " SELECT "
                + " u.IdUsuario, u.IdCatalogoTipoUsuario, u.IdCatalogoSexo, u.IdCatalogoEstadoCivil, u.IdCatalogoRaza, "
                + " u.PrimerNombre, u.SegundoNombre, u.PrimerApellido, u.SegundoApellido, "
                + " u.Cedula, u.Foto, u.Estado, u.FechaCreacion, u.FechaModificacion, "
                + " cRol.Nombre AS RolNombre, "
                + " cSexo.Nombre AS SexoNombre, "
                + " cCivil.Nombre AS CivilNombre, "
                + " cRaza.Nombre AS RazaNombre "
                + " FROM Usuario u "
                + " JOIN Catalogo cRol  ON u.IdCatalogoTipoUsuario = cRol.IdCatalogo "
                + " JOIN Catalogo cSexo ON u.IdCatalogoSexo = cSexo.IdCatalogo "
                + " JOIN Catalogo cCivil ON u.IdCatalogoEstadoCivil = cCivil.IdCatalogo "
                + " JOIN Catalogo cRaza ON u.IdCatalogoRaza = cRaza.IdCatalogo "
                + " WHERE u.Estado = 'A' AND u.IdUsuario = ? ";

        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                u = new UsuarioDTO(
                        rs.getInt("IdUsuario"),
                        rs.getInt("IdCatalogoTipoUsuario"),
                        rs.getInt("IdCatalogoSexo"),
                        rs.getInt("IdCatalogoEstadoCivil"),
                        rs.getString("PrimerNombre"),
                        rs.getString("SegundoNombre"),
                        rs.getString("PrimerApellido"),
                        rs.getString("SegundoApellido"),
                        rs.getString("Cedula"),
                        rs.getInt("IdCatalogoRaza"),
                        rs.getString("Foto"),
                        rs.getString("Estado"),
                        rs.getString("FechaCreacion"),
                        rs.getString("FechaModificacion"),
                        rs.getString("RolNombre"),
                        rs.getString("SexoNombre"),
                        rs.getString("CivilNombre"),
                        rs.getString("RazaNombre")
                );
            }
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "readById()");
        }
        return u;
    }

    @Override
    public boolean update(UsuarioDTO entity) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String query = "UPDATE Usuario SET "
                + "IdCatalogoTipoUsuario = ?, IdCatalogoSexo = ?, IdCatalogoEstadoCivil = ?, "
                + "PrimerNombre = ?, SegundoNombre = ?, PrimerApellido = ?, SegundoApellido = ?, "
                + "Cedula = ?, IdCatalogoRaza = ?, Foto = ?, FechaModificacion = ? "
                + "WHERE IdUsuario = ?";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, entity.getIdCatalogoTipoUsuario());
            pstmt.setInt(2, entity.getIdCatalogoSexo());
            pstmt.setInt(3, entity.getIdCatalogoEstadoCivil());
            pstmt.setString(4, entity.getPrimerNombre());
            pstmt.setString(5, entity.getSegundoNombre());
            pstmt.setString(6, entity.getPrimerApellido());
            pstmt.setString(7, entity.getSegundoApellido());
            pstmt.setString(8, entity.getCedula());

            // CORREGIDO: Directo Int
            pstmt.setInt(9, entity.getIdCatalogoRaza());

            pstmt.setString(10, entity.getFoto());
            pstmt.setString(11, dtf.format(now));
            pstmt.setInt(12, entity.getIdUsuario());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "update()");
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String query = "UPDATE Usuario SET Estado = ? WHERE IdUsuario = ?";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "X");
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "delete()");
        }
    }

    public UsuarioDTO readByCedula(String cedula) throws Exception {
        UsuarioDTO u = null;
        // Agregamos los JOINs para que la UI reciba los nombres (Rol, Sexo, etc.)
        String query = " SELECT "
                + " u.IdUsuario, u.IdCatalogoTipoUsuario, u.IdCatalogoSexo, u.IdCatalogoEstadoCivil, u.IdCatalogoRaza, "
                + " u.PrimerNombre, u.SegundoNombre, u.PrimerApellido, u.SegundoApellido, "
                + " u.Cedula, u.Foto, u.Estado, u.FechaCreacion, u.FechaModificacion, "
                + " cRol.Nombre AS RolNombre, cSexo.Nombre AS SexoNombre, "
                + " cCivil.Nombre AS CivilNombre, cRaza.Nombre AS RazaNombre "
                + " FROM Usuario u "
                + " JOIN Catalogo cRol  ON u.IdCatalogoTipoUsuario = cRol.IdCatalogo "
                + " JOIN Catalogo cSexo ON u.IdCatalogoSexo = cSexo.IdCatalogo "
                + " JOIN Catalogo cCivil ON u.IdCatalogoEstadoCivil = cCivil.IdCatalogo "
                + " JOIN Catalogo cRaza ON u.IdCatalogoRaza = cRaza.IdCatalogo "
                + " WHERE u.Estado = 'A' AND u.Cedula = ? ";
        try {
            Connection conn = openConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cedula);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Usamos el constructor completo para que no falte ningún dato
                u = new UsuarioDTO(
                        rs.getInt("IdUsuario"),
                        rs.getInt("IdCatalogoTipoUsuario"),
                        rs.getInt("IdCatalogoSexo"),
                        rs.getInt("IdCatalogoEstadoCivil"),
                        rs.getString("PrimerNombre"),
                        rs.getString("SegundoNombre"),
                        rs.getString("PrimerApellido"),
                        rs.getString("SegundoApellido"),
                        rs.getString("Cedula"),
                        rs.getInt("IdCatalogoRaza"),
                        rs.getString("Foto"),
                        rs.getString("Estado"),
                        rs.getString("FechaCreacion"),
                        rs.getString("FechaModificacion"),
                        rs.getString("RolNombre"),
                        rs.getString("SexoNombre"),
                        rs.getString("CivilNombre"),
                        rs.getString("RazaNombre")
                );
            }
        } catch (SQLException e) {
            throw new PatException(e.getMessage(), getClass().getName(), "readByCedula()");
        }
        return u;
    }
}
