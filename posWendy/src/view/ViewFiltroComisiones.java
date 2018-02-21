package view;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JDialog;
import com.toedter.calendar.JDateChooser;

import controlador.CtlFiltroRepComisiones;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;

public class ViewFiltroComisiones extends JDialog {
	
	private JDateChooser dCBuscar1;
	private JDateChooser dCBuscar2;
	private JButton btnBuscar;

	public ViewFiltroComisiones(Window view) {
		
		super(view,"Reporte Comisiones",Dialog.ModalityType.DOCUMENT_MODAL);
		setResizable(false);
		// TODO Auto-generated constructor stub
		
		this.setPreferredSize(new Dimension(212, 264));
		this.setSize(441, 197);
		// TODO Auto-generated constructor stub
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		getContentPane().setLayout(null);
		
		dCBuscar1 = new JDateChooser();
		dCBuscar1.setDateFormatString("dd-MM-yyyy");
		dCBuscar1.setSize(new Dimension(100, 20));
		dCBuscar1.setPreferredSize(new Dimension(160, 27));
		dCBuscar1.setBounds(44, 50, 160, 27);
		getContentPane().add(dCBuscar1);
		
		dCBuscar2 = new JDateChooser();
		dCBuscar2.setPreferredSize(new Dimension(160, 27));
		dCBuscar2.setBounds(270, 50, 160, 27);
		getContentPane().add(dCBuscar2);
		
		JLabel lblSelecioneRangoFecha = new JLabel("Selecione rango fecha");
		lblSelecioneRangoFecha.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblSelecioneRangoFecha.setBounds(19, 6, 194, 16);
		getContentPane().add(lblSelecioneRangoFecha);
		
		JLabel lblDe = new JLabel("De:");
		lblDe.setBounds(9, 55, 26, 16);
		getContentPane().add(lblDe);
		
		JLabel lblHasta = new JLabel("Hasta:");
		lblHasta.setBounds(213, 55, 48, 16);
		getContentPane().add(lblHasta);
		
		btnBuscar = new JButton("Ver Reporte");
		btnBuscar.setBounds(152, 98, 168, 49);
		getContentPane().add(btnBuscar);
	}
	
	public JDateChooser getBuscar1(){
		return this.dCBuscar1;
	}
	public JDateChooser getBuscar2(){
		return this.dCBuscar2;
	}
	public void conectarCtl(CtlFiltroRepComisiones c){
		btnBuscar.addActionListener( c);
		btnBuscar.setActionCommand("GENERAR");
	}
}
