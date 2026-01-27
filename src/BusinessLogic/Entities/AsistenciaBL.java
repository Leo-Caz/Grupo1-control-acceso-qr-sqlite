package BusinessLogic.Entities;

import java.nio.file.Path;

import DataAccess.CarnetDAO;
import DataAccess.DTO.CarnetDTO;
import DataAccess.DTO.UsuarioDTO;
import DataAccess.RegistroDAO;
import DataAccess.Reports.AsistenciaCsvExporter;
import DataAccess.UsuarioDAO;
import Infrastructure.Config.BNAppException; 

public class AsistenciaBL {

    private final UsuarioDAO usuarioDAO;
    private final CarnetDAO carnetDAO;
    private final RegistroDAO registroDAO;

    public AsistenciaBL() {
        this.usuarioDAO = new UsuarioDAO();
        this.carnetDAO = new CarnetDAO();
        this.registroDAO = new RegistroDAO();
    }

    /**
     * Procesa el intento de acceso validando carnet y usuario.
     * @param qrCode El código leído por la cámara.
     * @return UsuarioDTO si el acceso es concedido.
     * @throws BNAppException si el acceso es denegado o ocurre un error de sistema.
     */
    public UsuarioDTO registrarAcceso(String qrCode) throws BNAppException {
        try {
            // 1. Buscar Carnet
            CarnetDTO carnet = carnetDAO.readByCodigoQR(qrCode);

            if (carnet == null) {
                throw new BNAppException("QR NO REGISTRADO"); 
            }
            
            if (!"A".equals(carnet.getEstado())) {
                throw new BNAppException("CARNET INACTIVO"); 
            }

            // 2. Buscar Usuario asociado
            UsuarioDTO usuario = usuarioDAO.readById(carnet.getIdUsuario());

            if (usuario == null) {
                throw new BNAppException("USUARIO NO EXISTE"); 
            }

            if (!"A".equals(usuario.getEstado())) {
                throw new BNAppException("USUARIO INACTIVO"); 
            }

            // 3. Si todo está bien, registramos la asistencia en la BD
            registroDAO.createByQr(qrCode);

            // Retornamos el usuario para que el controlador lo muestre en la UI
            return usuario;
        } catch (BNAppException e) {
            // Relanzamos excepciones de negocio o las que ya vienen de los DAO
            throw e;
        } catch (Exception e) {
            // Captura errores inesperados de sistema y los registra en el Log
            throw new BNAppException(e, getClass().getName(), "registrarAcceso()");
        }
    }

    /**
     * Genera el reporte de asistencia en formato CSV.
     * @param path Ruta donde se guardará el archivo.
     * @throws BNAppException si ocurre un error en la exportación.
     */
    public void generarReporteCsv(Path path) throws BNAppException {
        try {
            AsistenciaCsvExporter exporter = new AsistenciaCsvExporter();
            exporter.export(path);
        } catch (Exception e) {
            // Registra fallos técnicos durante la generación del reporte
            throw new BNAppException(e, getClass().getName(), "generarReporteCsv()");
        }
    }
}