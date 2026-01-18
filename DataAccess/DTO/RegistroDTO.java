package DataAccess.DTO;

public class RegistroDTO {
    private Integer idRegistro;
    private Integer idUsuario;
    private Integer idCarnet;
    private String fechaEntrada; // datetime('now','localtime')

    private String estado;

    public RegistroDTO() {}

    public RegistroDTO(Integer idRegistro, Integer idUsuario, Integer idCarnet,
                       String fechaEntrada, String estado) {
        this.idRegistro = idRegistro;
        this.idUsuario = idUsuario;
        this.idCarnet = idCarnet;
        this.fechaEntrada = fechaEntrada;
        this.estado = estado;
    }

    public Integer getIdRegistro() { return idRegistro; }
    public void setIdRegistro(Integer idRegistro) { this.idRegistro = idRegistro; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdCarnet() { return idCarnet; }
    public void setIdCarnet(Integer idCarnet) { this.idCarnet = idCarnet; }

    public String getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(String fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
