package objetos;
/*
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import utiles.AKIPanel;
*/

public class Comentario{
	
	private String id;
	private String autor;
	private String fecha;
	private String idSorteo;
	private String comentario;
	
	public Comentario(String id, String autor, String fecha, String sorteo, String comentario) {
		this.id = id;
		this.autor = autor;
		this.fecha = fecha;
		this.idSorteo = sorteo;
		this.comentario = comentario;
	}

	public Comentario(String autor, String idSorteo, String comentario) {
		this.autor = autor;
		this.idSorteo = idSorteo;
		this.comentario = comentario;
	}

	public String getId(){
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getAutor() {
		return autor;
	}

	public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getSorteo() {
		return idSorteo;
	}

	public String getComentario() {
		return comentario;
	}
/*
	public JPanel getPanelComentario(int ancho){
		JPanel pContenedor = new JPanel();
		pContenedor.setBackground(Color.WHITE);
		pContenedor.setBounds(10, 124, ancho, 119);
		pContenedor.setLayout(null);
		
		AKIPanel pCabecera = new AKIPanel();
		pCabecera.setBounds(10, 5, pContenedor.getWidth()-50, 25);
		pContenedor.add(pCabecera);
		pCabecera.setLayout(null);
		
		JLabel lblInfo = new JLabel("@"+autor+" - "+fecha+" - C:"+id);
		lblInfo.setFont(new Font("Verdana", Font.PLAIN, 9));
		lblInfo.setBounds(5, 0, pCabecera.getWidth()-10, pCabecera.getHeight());
		pCabecera.add(lblInfo);
		
		AKIPanel pComent = new AKIPanel();
		pComent.setBounds(5, 20, pContenedor.getWidth()-10, 83);
		pContenedor.add(pComent);
		pComent.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JTextPane tComent = new JTextPane();
		tComent.setFont(new Font("Verdana", Font.PLAIN, 12));
		tComent.setEditable(false);
		pComent.add(tComent);
		tComent.setText(comentario);
		int altoFila = (int)tComent.getPreferredSize().getHeight()-4;
		int largoFila = pComent.getWidth()-10;
		if ((int)tComent.getPreferredSize().getWidth()<largoFila){
			tComent.setPreferredSize(new Dimension(largoFila, (int)tComent.getPreferredSize().getHeight()));
		}
		while ((int)tComent.getPreferredSize().getWidth()>largoFila){
			Dimension dim = tComent.getPreferredSize();
			int largo = Math.max(largoFila, (int)(dim.getWidth()-largoFila));
			tComent.setPreferredSize(new Dimension(largo, ((int)dim.getHeight())+altoFila));
		}
		pComent.setSize(largoFila+10, (int)tComent.getPreferredSize().getHeight()+15);
		
		pContenedor.setSize(339, (int) (pComent.getLocation().getY()+pComent.getSize().getHeight())+5);
		return pContenedor;
	}
	*/
}
