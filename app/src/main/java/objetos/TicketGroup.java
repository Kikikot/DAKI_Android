package objetos;

import java.util.ArrayList;

import objetos.Ticket;

public class TicketGroup {
	
	private String idSorteo;
	private ArrayList<Ticket> tickets;
	
	public TicketGroup(String idSorteo, ArrayList<Ticket> tickets){
		this.idSorteo = idSorteo;
		this.tickets = tickets;
	}
	
	public String getIdSorteo(){
		return idSorteo;
	}
	
	public ArrayList<Ticket> getTickes(){
		return tickets;
	}
	
/*	public AKIPanel getComoPanel(int ancho, boolean conBotonDeAcceso, MouseListener contenedor){
		
		AKIButton ver = new AKIButton("Ver "+idSorteo, AKIButton.TipoBoton.BotonSimple);
		
		AKIPanel panel = new AKIPanel();
		panel.setPreferredSize(new Dimension(ancho, 100));
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("IdSorteo");
		lblNewLabel.setBounds(10, 11, 85, 14);
		lblNewLabel.setForeground(Color.GRAY);
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel(idSorteo);
		lblNewLabel_1.setBounds(124, 11, 85, 14);
		lblNewLabel_1.setFont(new Font("Verdana", Font.BOLD, 14));
		panel.add(lblNewLabel_1);
		
		JPanelFondo panel_1 = new JPanelFondo();
		int largo = (int) ver.getPreferredSize().getWidth();
		panel_1.setBounds((int)panel.getPreferredSize().getWidth()-largo-1, 1, largo, (int) ver.getPreferredSize().getHeight());
		panel.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 0, -1));
		if (conBotonDeAcceso){
			ver.addMouseListener(contenedor);
			panel_1.add(ver, BorderLayout.CENTER);
		}
		
		JLabel lblTickets = new JLabel("Tickets:");
		lblTickets.setForeground(Color.GRAY);
		lblTickets.setFont(new Font("Verdana", Font.PLAIN, 14));
		lblTickets.setBounds(10, 36, 85, 14);
		panel.add(lblTickets);
		
		int y = 61;
		int h = 15;
		int w = (int)panel.getPreferredSize().getWidth()-20;
		int margen = 5;
		for(int i=0; i<tickets.size(); i++){
			JLabel temp = new JLabel(tickets.get(i).getResumen());
			temp.setBounds(10, y+margen*i+h*i, w, 15);
			temp.setFont(new Font("Verdana", Font.PLAIN, 14));
			panel.add(temp);
		}
		
		panel.setPreferredSize(new Dimension(ancho, y+tickets.size()*(h+margen)));
		
		return panel;
	}
*/
}
