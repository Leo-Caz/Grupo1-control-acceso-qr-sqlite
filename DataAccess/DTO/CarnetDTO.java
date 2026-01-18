package DataAccess.DTO;

public class CarnetDTO {
    private Integer IdCarnet        ;
    private Integer IdUsuario       ;
    private String CodigoQR         ;
    private String Estado           ;
    private String FechaCreacion    ;
    private String FechaModificacion;

    public CarnetDTO(Integer idCarnet, Integer idUsuario, String codigoQR, String estado, String fechaCreacion,
            String fechaModificacion) {
        IdCarnet = idCarnet;
        IdUsuario = idUsuario;
        CodigoQR = codigoQR;
        Estado = estado;
        FechaCreacion = fechaCreacion;
        FechaModificacion = fechaModificacion;
    }

    public CarnetDTO() {
    }

    public Integer getIdCarnet() {
        return IdCarnet;
    }

    public void setIdCarnet(Integer IdCarnet) {
        this.IdCarnet = IdCarnet;
    }

    public Integer getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(Integer IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getCodigoQR() {
        return CodigoQR;
    }

    public void setCodigoQR(String CodigoQR) {
        this.CodigoQR = CodigoQR;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String FechaCreacion) {
        this.FechaCreacion = FechaCreacion;
    }

    public String getFechaModificacion() {
        return FechaModificacion;
    }

    public void setFechaModificacion(String FechaModificacion) {
        this.FechaModificacion = FechaModificacion;
    }
}
