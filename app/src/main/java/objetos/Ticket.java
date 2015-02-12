package objetos;

public class Ticket {
	
	private int id;
	private String numero;
	private String usuario;
	private String patrocinador;
	private String sorteo;
	private String fechaCompra;
	private String valor;
	
	public Ticket(){
	}
	
	public Ticket(String numero, String usuario, String patrocinador, String sorteo,
				  String fechaCompra, String valor) {
		this.numero = numero;
		this.usuario = usuario;
		this.patrocinador = patrocinador;
		this.sorteo = sorteo;
		this.fechaCompra = fechaCompra;
		this.valor = valor;
	}

	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getPatrocinador() {
		return patrocinador;
	}
	
	public void setPatrocinador(String patrocinador) {
		this.patrocinador = patrocinador;
	}
	
	public String getSorteo() {
		return sorteo;
	}
	
	public void setSorteo(String sorteo) {
		this.sorteo = sorteo;
	}
	
	public String getFechaCompra() {
		return fechaCompra;
	}
	
	public void setFechaCompra(String fechaCompra) {
		this.fechaCompra = fechaCompra;
	}
	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getResumen() {
		return numero+" - "+patrocinador+" - "+fechaCompra+" - "+valor;
	}
}
