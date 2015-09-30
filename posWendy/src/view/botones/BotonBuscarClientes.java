package view.botones;

import java.awt.Image;

import javax.swing.ImageIcon;

public class BotonBuscarClientes extends BotonesApp  {
	
	private ImageIcon imgGuardar;
	
	
	public BotonBuscarClientes(){
			super("F2 Clientes");
			
			imgGuardar=new ImageIcon(BotonCancelar.class.getResource("/view/recursos/busar_clientes.png"));
			
			 Image image = imgGuardar.getImage();
			    // reduce by 50%
			 image = image.getScaledInstance(image.getWidth(null)/4, image.getHeight(null)/4, Image.SCALE_SMOOTH);
			 imgGuardar.setImage(image);
		
			this.setIcon(imgGuardar);
				
			
		}

}
