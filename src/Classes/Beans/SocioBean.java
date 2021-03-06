package Classes.Beans;

public class SocioBean implements java.io.Serializable {

    private int idUsuario;
    private String normbre;
    private String apellidoP;
    private String apellidoM;
    private String estado;
    private String municipio;
    private String calle;
    private String numero;
    private String telefono;
    private String usuario;
    private int Prestamos;
    private String Estatus;
    private String contraseña;

    public SocioBean() {
    }

//    Setters for class SocioBean
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String normbre) {
        this.normbre = normbre;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public void setEstatus(String usuario) {
        this.Estatus = usuario;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    
    public void setPrestamos(int contraseña) {
        this.Prestamos = contraseña;
    }

//    Getters for clas SocioBean
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return normbre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public String getEstado() {
        return estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getCalle() {
        return calle;
    }

    public String getNumero() {
        return numero;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getUsuario() {
        return usuario;
    }
    
    public String getEstatus() {
        return Estatus;
    }

    public String getContraseña() {
        return contraseña;
    }
    
    public int getPrestamos() {
        return Prestamos;
    }
}
