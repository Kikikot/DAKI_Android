package objetos;

import java.net.MalformedURLException;
import java.net.URL;

public class PrevisualizacionSorteo /* extends JPanelFondo */{
	
	private String id;
	private String d;
	private String v;
	private String t;
	
/*	private JLabel distancia;
	private JLabel valor;
	private JLabel tickets;
*/
	public PrevisualizacionSorteo(URL img, String id, String distancia, String valor, String tickets, boolean ojeado){
		//pre[0], pre[3], pre[1], pre[2]
		super(/*img*/);
		init(id, distancia, valor, tickets, ojeado);
	}

	public PrevisualizacionSorteo(String id, String distancia, String valor, String tickets, boolean ojeado){
		super();
		init(id, distancia, valor, tickets, ojeado);
	}
	
	private void init(String id, String distancia, String valor, String tickets, boolean ojeado) {
		
		this.id = id;
/*
		Color color = new Color(0xBB00BB);
		if (ojeado){
			color = new Color(0x00FF00);
		}
		
		AbstractBorder borde = new BordeRedondeado(color, 1, 5, new Color(0xffffff));
		setBorder(borde);
		
		this.setLayout(new BorderLayout(0, 0));
		
		JPanelFondo panel = new JPanelFondo();
		this.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
*/
		d = distancia;
/*		this.distancia = new JLabel(distancia+" Km");
		this.distancia.setBackground(Color.WHITE);
		this.distancia.setForeground(color);
		this.distancia.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(this.distancia, BorderLayout.WEST);
*/
		v = valor;
/*		this.valor = new JLabel(valor+" �");
		this.valor.setBackground(Color.WHITE);
		this.valor.setForeground(color);
		this.valor.setFont(new Font("Verdana", Font.BOLD, 10));
		panel.add(this.valor, BorderLayout.EAST);
		
		JPanelFondo panel_1 = new JPanelFondo();
		this.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
*/
		t = tickets;
/*		this.tickets = new JLabel(tickets+" tickets");
		this.tickets.setBackground(Color.WHITE);
		this.tickets.setForeground(color);
		this.tickets.setFont(new Font("Verdana", Font.BOLD, 10));
		panel_1.add(this.tickets, BorderLayout.WEST);
*/	}

	public String getIdSorteo(){
		return id;
	}

	public String getDistancia() {
		return d;
	}

	public String getValor() {
		return v;
	}

	public String getTickets() {
		return t;
	}
}
