package BussinessLogic.Entities;

import java.util.List;

import DataAccess.CatalogoDAO;
import DataAccess.DTO.CatalogoDTO;

public class CatalogoBL {
    private final CatalogoDAO catDAO = new CatalogoDAO();

    public CatalogoBL() {}

    /**
     * En lugar de getAll(), se usa getByTipo(int idTipo)
     * para obtener listas filtradas (Roles, Sexos, Estado Civil, Raza, etc.).
     */
    public List<CatalogoDTO> getByTipo(int idTipo) throws Exception {
        List<CatalogoDTO> lst = catDAO.readByType(idTipo);
        for (CatalogoDTO dto : lst) {
            if (dto.getNombre() != null) {
                dto.setNombre(dto.getNombre().toUpperCase());
            }
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