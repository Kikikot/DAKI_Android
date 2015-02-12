package objetos;

import java.util.Date;

public class Presorteo {
	
	// img, s.getDistancia(), s.getValor(), s.getTotalTickets()
	
	private String valor;
	private String urlImagen;
	private String distancia;
	private String totalTickets;
	
	public Presorteo(String valor, String urlImagen, String distancia, String totalTickets) {
		this.valor = valor;
		this.urlImagen = urlImagen;
		this.distancia = distancia;
		this.totalTickets = totalTickets;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}
	
	public String getDistancia() {
		return distancia;
	}

	public String getTotalTickets() {
		return totalTickets;
	}
	
}
