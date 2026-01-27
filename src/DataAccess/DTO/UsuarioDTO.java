package DataAccess.DTO;

public class UsuarioDTO {

    private Integer idUsuario;
    private Integer idCatalogoTipoUsuario;
    private Integer idCatalogoSexo;
    private Integer idCatalogoEstadoCivil;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String cedula;
    private String foto;
    private String estado;
    private String fechaCreacion;
    private String fechaModificacion;

    private String nombreRol;
    private String nombreSexo;
    private String nombreEstadoCivil;

    public UsuarioDTO(
            Integer idUsuario,
            Integer idCatalogoTipoUsuario,
            Integer idCatalogoSexo,
            Integer idCatalogoEstadoCivil,
            String primerNombre,
            String segundoNombre,
            String primerApellido,
            String segundoApellido,
            String cedula,
            String foto,
            String estado,
            String fechaCreacion,
            String fechaModificacion,
            String nombreRol,
            String nombreSexo,
            String nombreEstadoCivil
    ) {
        this.idUsuario = idUsuario;
        this.idCatalogoTipoUsuario = idCatalogoTipoUsuario;
        this.idCatalogoSexo = idCatalogoSexo;
        this.idCatalogoEstadoCivil = idCatalogoEstadoCivil;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.cedula = cedula;
        this.foto = foto;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.nombreRol = nombreRol;
        this.nombreSexo = nombreSexo;
        this.nombreEstadoCivil = nombreEstadoCivil;
    }

    public UsuarioDTO() {
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdCatalogoTipoUsuario() {
        return idCatalogoTipoUsuario;
    }

    public void setIdCatalogoTipoUsuario(Integer idCatalogoTipoUsuario) {
        this.idCatalogoTipoUsuario = idCatalogoTipoUsuario;
    }

    public Integer getIdCatalogoSexo() {
        return idCatalogoSexo;
    }

    public void setIdCatalogoSexo(Integer idCatalogoSexo) {
        this.idCatalogoSexo = idCatalogoSexo;
    }

    public Integer getIdCatalogoEstadoCivil() {
        return idCatalogoEstadoCivil;
    }

    public void setIdCatalogoEstadoCivil(Integer idCatalogoEstadoCivil) {
        this.idCatalogoEstadoCivil = idCatalogoEstadoCivil;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getNombreSexo() {
        return nombreSexo;
    }

    public void setNombreSexo(String nombreSexo) {
        this.nombreSexo = nombreSexo;
    }

    public String getNombreEstadoCivil() {
        return nombreEstadoCivil;
    }

    public void setNombreEstadoCivil(String nombreEstadoCivil) {
        this.nombreEstadoCivil = nombreEstadoCivil;
    }

}
