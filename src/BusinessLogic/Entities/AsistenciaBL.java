package BusinessLogic.Entities;

import java.nio.file.Path;

import DataAccess.CarnetDAO;
import DataAccess.DTO.CarnetDTO;
import DataAccess.DTO.UsuarioDTO;
import DataAccess.RegistroDAO;
import DataAccess.Reports.AsistenciaCsvExporter;
import DataAccess.UsuarioDAO;

public class AsistenciaBL {

    private final UsuarioDAO usuarioDAO;
    private final CarnetDAO carnetDAO;
    private final RegistroDAO registroDAO;

    public AsistenciaBL() {
        // La BL es dueña de los DAOs
        this.usuarioDAO = new UsuarioDAO();
        this.carnetDAO = new CarnetDAO();
        this.registroDAO = new RegistroDAO();
    }

    /**
     * Procesa el intento de acceso.
     * @param qrCode El código leído.
     * @return UsuarioDTO si el acceso es concedido.
     * @throws Exception con el mensaje de error si el acceso es denegado.
     */
    public UsuarioDTO registrarAcceso(String qrCode) throws Exception {
        // 1. Buscar Carnet
        CarnetDTO carnet = carnetDAO.readByCodigoQR(qrCode);

        if (carnet == null) {
            throw new Exception("QR NO REGISTRADO");
        }
        
        if (!"A".equals(carnet.getEstado())) {
            throw new Exception("CARNET INACTIVO");
        }

        // 2. Buscar Usuario asociado
        UsuarioDTO usuario = usuarioDAO.readById(carnet.getIdUsuario());

        if (usuario == null) {
            throw new Exception("USUARIO NO EXISTE");
        }

        if (!"A".equals(usuario.getEstado())) {
            throw new Exception("USUARIO INACTIVO");
        }

        // 3. Si todo está bien, registramos en la BD
        registroDAO.createByQr(qrCode);

        // Retornamos el usuario para que el controlador lo muestre
        return usuario;
    }

    public void generarReporteCsv(Path path) throws Exception {
        AsistenciaCsvExporter exporter = new AsistenciaCsvExporter();
        exporter.export(path);
    }
}