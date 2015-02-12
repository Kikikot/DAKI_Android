package objetos;

public class Notificacion {
	
	public static final int ESTADO_NUEVA = 1;
	public static final int ESTADO_LEYENDO = 2;
	public static final int ESTADO_LEIDA = 3;
	
	private String id;
	private String usuario;
	private String mensaje;
	private String estado;
	private String asunto;
	
	public Notificacion(){
	}
	
	public Notificacion(String id, String usuario, String mensaje, String estado, String asunto) {
		this.id = id;
		this.usuario = usuario;
		this.mensaje = mensaje;
		this.estado = estado;
		this.asunto = asunto;
	}
	
	public String getId() {
		return id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getEstado() {
		return Integer.parseInt(estado);
	}

	public void setEstado(int estado) {
		this.estado = estado+"";
	}

}
