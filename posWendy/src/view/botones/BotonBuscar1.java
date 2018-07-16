package view.botones;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class BotonBuscar1  extends BotonesApp {
	
private ImageIcon imgGuardar;
	
	
public BotonBuscar1(){
		super("F1 Buscar");
		
	
		this.setIcon(new ImageIcon(BotonBuscar1.class.getResource("/view/recursos/buscar.png")));
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setPreferredSize(new Dimension(128,200));
		
			
		
	}
	

}
