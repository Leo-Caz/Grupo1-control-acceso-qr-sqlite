package BussinessLogic.Entities;

import java.util.List;
import DataAccess.CatalogoDAO; 
import DataAccess.DTO.CatalogoDTO; 

public class CatalogoBL {
    private CatalogoDAO catDAO = new CatalogoDAO(); 
    
    public CatalogoBL() {}
    /**
     * Método genérico.
     * En lugar de getAll(), usamos getByTipo(int idTipo)
     * Así la UI puede pedir: getByTipo(1) para Roles, getByTipo(2) para Sexos, etc.
     */
    public List<CatalogoDTO> getByTipo(int idTipo) throws Exception {
        List<CatalogoDTO> lst = catDAO.readByType(idTipo); 
        for (CatalogoDTO dto : lst) {
            dto.setNombre(dto.getNombre().toUpperCase());
        }
        return lst;
    }

    public CatalogoDTO getById(int idReg) throws Exception {
        return catDAO.readById(idReg);
    }

    public boolean create(CatalogoDTO dto) throws Exception {
        return catDAO.create(dto);
    }

    public boolean update(CatalogoDTO dto) throws Exception {
        return catDAO.update(dto);
    }

    public boolean delete(int idReg) throws Exception {
        return catDAO.delete(idReg);
    }
}
