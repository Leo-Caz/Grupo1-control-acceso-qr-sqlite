package BusinessLogic.Entities;

import java.util.List;

import DataAccess.DTO.UsuarioDTO;
import DataAccess.UsuarioDAO;

public class UsuarioBL {
    private UsuarioDAO uDAO = new UsuarioDAO();

    public UsuarioBL() {}

    public List<UsuarioDTO> getAll() throws Exception {
        List<UsuarioDTO> lst = uDAO.readAll();

        for (UsuarioDTO dto : lst) {
            dto.setPrimerNombre(dto.getPrimerNombre().toUpperCase());
            dto.setPrimerApellido(dto.getPrimerApellido().toUpperCase());
            
            // Si hay segundos nombres/apellidos, también los subimos
            if(dto.getSegundoNombre() != null) 
                dto.setSegundoNombre(dto.getSegundoNombre().toUpperCase());
            if(dto.getSegundoApellido() != null) 
                dto.setSegundoApellido(dto.getSegundoApellido().toUpperCase());
        }
        return lst;
    }

    public UsuarioDTO getById(int idUsuario) throws Exception {
        return uDAO.readById(idUsuario);
    }

    public boolean create(UsuarioDTO dto) throws Exception {
        if (dto.getCedula().length() < 10) {
             throw new Exception("La cédula debe tener al menos 10 caracteres.");
        }
        return uDAO.create(dto);
    }

    public boolean update(UsuarioDTO dto) throws Exception {
        return uDAO.update(dto);
    }

    public boolean delete(int idUsuario) throws Exception {
        return uDAO.delete(idUsuario);
    }
    
    /**
     * Método Extra: Buscar por Cédula.
     * Útil para evitar duplicados antes de guardar o para el Login.
     */
    public UsuarioDTO getByCedula(String cedula) throws Exception {
        return uDAO.readByCedula(cedula);
    }
}
