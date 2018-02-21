package view.botones;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class BotonCrearCotizaciones extends BotonesApp {

	public BotonCrearCotizaciones() {
		// TODO Auto-generated constructor stub
		
		super("Cotizaciones");
		
		this.setIcon(new ImageIcon(BotonCierreCaja.class.getResource("/view/recursos/cotizacion.png")));
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
	}

	public BotonCrearCotizaciones(String titulo) {
		super(titulo);
		// TODO Auto-generated constructor stub
	}

}
