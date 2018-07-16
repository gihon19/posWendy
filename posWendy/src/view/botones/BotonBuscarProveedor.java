package view.botones;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class BotonBuscarProveedor extends BotonesApp{
	private ImageIcon imgGuardar;
	
	public BotonBuscarProveedor(){
		super("F3 Proveedor");
		
		/*imgGuardar=new ImageIcon(BotonCancelar.class.getResource("/view/recursos/clientes_2.png"));
		
		 Image image = imgGuardar.getImage();
		    // reduce by 50%
		 image = image.getScaledInstance(image.getWidth(null)/4, image.getHeight(null)/4, Image.SCALE_SMOOTH);
		 imgGuardar.setImage(image);
	
		this.setIcon(imgGuardar);*/
		
		this.setIcon(new ImageIcon(BotonBuscarClientes.class.getResource("/view/recursos/proveedor.png")));
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
	}

}
