package view.botones;

import java.awt.Image;

import javax.swing.ImageIcon;

public class BotonImprimirSmall extends BotonesApp  {
	private ImageIcon imgGuardar;
	
	
	
	public BotonImprimirSmall(){
		//super("F3 Cobrar");
		
		
		
		setIcon(new ImageIcon(BotonAgregar.class.getResource("/view/recursos/imprimir3.png"))); // NOI18N
		//this.setSize(200, 100);.
		setToolTipText("Imprimir");
		
		
	}
	

}
