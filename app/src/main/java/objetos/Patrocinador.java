package objetos;

import java.util.ArrayList;

public class Patrocinador {
	
	private String id;
	private String nombre;
	private String[] tipoComercio;
	private String nombreContacto;
	private String emailContacto;
	private String telfContacto;
	private String direccionContacto;
	private String distancia;
	private String imagen;
	
	
	
	public Patrocinador(String id, String nombre, String[] tipoComercio, String nombreContacto, String emailContacto,
			String telfContacto, String direccionContacto, String distancia, String imagen) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipoComercio = tipoComercio;
		this.nombreContacto = nombreContacto;
		this.emailContacto = emailContacto;
		this.telfContacto = telfContacto;
		this.direccionContacto = direccionContacto;
		this.distancia = distancia;
		this.imagen = imagen;
	}
	
	public String getId() {
		return id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String[] getTipoComercio() {
		return tipoComercio;
	}
	
	public void setTipoComercio(String[] tipoComercio) {
		this.tipoComercio = tipoComercio;
	}
	
	public String getNombreContacto() {
		return nombreContacto;
	}
	
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}
	
	public String getEmailContacto() {
		return emailContacto;
	}
	
	public void setEmailContacto(String emailContacto) {
		this.emailContacto = emailContacto;
	}
	
	public String getTelfContacto() {
		return telfContacto;
	}
	
	public void setTelfContacto(String telfContacto) {
		this.telfContacto = telfContacto;
	}
	
	public String getDireccionContacto() {
		return direccionContacto;
	}
	
	public void setDireccionContacto(String direccionContacto) {
		this.direccionContacto = direccionContacto;
	}
	
	public String getDistancia() {
		return distancia;
	}
	
	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getImagen() {
		return imagen;
	}
	
	public String toString(){
		return nombre + " (" + distancia + " Km)";
	}
}
