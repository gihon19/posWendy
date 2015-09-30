package view.botones;

import java.awt.Dimension;

import javax.swing.JButton;

public class BotonesApp extends JButton {
	private static final int ancho=128;
	private static final int alto=45;
	private static Dimension dim=new Dimension(ancho,alto);
	
	public BotonesApp(){
		this.setSize(ancho, alto);
		//this.setPreferredSize(dim);
		
	}
	public BotonesApp(String titulo){
		super(titulo);
		this.setSize(ancho, alto);
		//this.setPreferredSize(dim);
		
	}
	

}
