package DataAccess.Reports;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DataAccess.Helpers.DataHelperSQLite;
import Framework.PatException;

public class AsistenciaCsvExporter extends DataHelperSQLite {

    public void export(Path outputCsv) throws Exception {
        String query =
            "SELECT " +
            "  r.FechaEntrada, " +
            "  u.Cedula, " +
            "  u.PrimerNombre || ' ' || IFNULL(u.SegundoNombre,'') || ' ' || u.PrimerApellido || ' ' || u.SegundoApellido AS NombreCompleto, " +
            "  cQR.CodigoQR, " +
            "  cRol.Nombre AS Rol, " +
            "  cSexo.Nombre AS Sexo, " +
            "  cCivil.Nombre AS EstadoCivil, " +
            "  cRaza.Nombre AS Raza " +
            "FROM Registro r " +
            "JOIN Usuario u ON u.IdUsuario = r.IdUsuario " +
            "JOIN Carnet cQR ON cQR.IdCarnet = r.IdCarnet " +
            "JOIN Catalogo cRol  ON u.IdCatalogoTipoUsuario = cRol.IdCatalogo " +
            "JOIN Catalogo cSexo ON u.IdCatalogoSexo = cSexo.IdCatalogo " +
            "JOIN Catalogo cCivil ON u.IdCatalogoEstadoCivil = cCivil.IdCatalogo " +
            "JOIN Catalogo cRaza ON u.IdCatalogoRaza = cRaza.IdCatalogo " +
            "WHERE r.Estado = 'A' " +
            "ORDER BY r.FechaEntrada ASC";

        try {
            Files.createDirectories(outputCsv.getParent());

            Connection conn = openConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            try (BufferedWriter bw = Files.newBufferedWriter(outputCsv, StandardCharsets.UTF_8)) {
                bw.write("fecha_entrada,cedula,nombre_completo,codigo_qr,rol,sexo,estado_civil,raza");
                bw.newLine();

                while (rs.next()) {
                    bw.write(csv(rs.getString(1)) + "," +
                             csv(rs.getString(2)) + "," +
                             csv(rs.getString(3)) + "," +
                             csv(rs.getString(4)) + "," +
                             csv(rs.getString(5)) + "," +
                             csv(rs.getString(6)) + "," +
                             csv(rs.getString(7)) + "," +
                             csv(rs.getString(8)));
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            throw new PatException(e.getMessage(), getClass().getName(), "export()");
        }
    }

    private static String csv(String s) {
        if (s == null) return "";
        boolean q = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String t = s.replace("\"", "\"\"");
        return q ? ("\"" + t + "\"") : t;
    }
}
