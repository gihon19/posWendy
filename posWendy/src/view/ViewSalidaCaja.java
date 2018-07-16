package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JDialog;

import view.botones.BotonCancelar;
import view.botones.BotonGuardar;
import view.rendes.PanelPadre;
import javax.swing.JLabel;
import javax.swing.JTextField;

import controlador.CtlSalidaCaja;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class ViewSalidaCaja extends JDialog {
	private JTextField txtCantidad;
	private BotonGuardar btnGuardar;
	private BotonCancelar btnCancelar;
	private JTextArea txtrConcepto;
	private JTextField txtEmpleado;

	public ViewSalidaCaja(Window view) {
		// TODO Auto-generated constructor stub
		this.setTitle("Salida de efectivo");
		this.setLocationRelativeTo(view);
		this.setModal(true);
		
		getContentPane().setBackground(PanelPadre.color1);
		getContentPane().setLayout(null);
		
		JLabel lblCantida = new JLabel("Cantidad");
		lblCantida.setBounds(16, 6, 109, 16);
		getContentPane().add(lblCantida);
		
		txtCantidad = new JTextField();
		txtCantidad.setBounds(16, 25, 402, 40);
		getContentPane().add(txtCantidad);
		txtCantidad.setColumns(10);
		
		JLabel lblConcepto = new JLabel("Concepto");
		lblConcepto.setBounds(16, 68, 61, 16);
		getContentPane().add(lblConcepto);
		
		btnGuardar = new BotonGuardar();
		btnGuardar.setLocation(56, 274);
		//btnGuardar.setBounds(16, 219, 117, 29);
		getContentPane().add(btnGuardar);
		
		btnCancelar = new BotonCancelar();
		//btnCancelar.setBounds(241, 219, 117, 29);
		btnCancelar.setLocation(246, 274);
		getContentPane().add(btnCancelar);
		
		txtrConcepto = new JTextArea();
		txtrConcepto.setBounds(16, 97, 396, 91);
		getContentPane().add(txtrConcepto);
		
		JLabel lblEntregadoA = new JLabel("F1 - Entregado a");
		lblEntregadoA.setBounds(16, 200, 109, 16);
		getContentPane().add(lblEntregadoA);
		
		txtEmpleado = new JTextField();
		txtEmpleado.setEditable(false);
		txtEmpleado.setBounds(16, 228, 396, 40);
		getContentPane().add(txtEmpleado);
		txtEmpleado.setColumns(10);
		
		this.setSize(435, 397);
		
		setResizable(false);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
	}
	
	public JTextField getTxtCantidad(){
		return txtCantidad;
	}
	public JTextArea getTxtConcepto(){
		return txtrConcepto;
	}
	public JTextField getTxtEmpleado(){
		return txtEmpleado;
	}
	
	public void conectarControlador(CtlSalidaCaja c){
		btnGuardar.setActionCommand("GUARDAR");
		btnGuardar.addActionListener(c);
		
		btnCancelar.setActionCommand("CANCELAR");
		btnCancelar.addActionListener(c);
		
		
		btnGuardar.addKeyListener(c);
		btnCancelar.addKeyListener(c);
		txtEmpleado.addKeyListener(c);
		txtrConcepto.addKeyListener(c);
		txtCantidad.addKeyListener(c);
		
		
	}
}
