package view;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import view.botones.BotonActualizar;
import view.botones.BotonCancelar;
import view.botones.BotonGuardar;
import controlador.CtlCliente;

public class ViewCrearCliente extends JDialog{
	private JTextField txtNombre;
	private JTextField txtDireccion;
	private JTextField txtTelefono;
	private JTextField txtMovil;
	private JTextField txtRtn;
	
	private BotonCancelar btnCancelar;
	private BotonActualizar btnActualizar;
	private BotonGuardar btnGuardar;
	
	public ViewCrearCliente() {
		setTitle("Crear Cliente");
		
		this.setSize(451,333);
		getContentPane().setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(21, 11, 60, 14);
		getContentPane().add(lblNombre);
		
		txtNombre = new JTextField();
		txtNombre.setBounds(91, 8, 311, 20);
		getContentPane().add(txtNombre);
		txtNombre.setColumns(10);
		
		JLabel lblDireccion = new JLabel("Direccion:");
		lblDireccion.setBounds(21, 51, 64, 14);
		getContentPane().add(lblDireccion);
		
		txtDireccion = new JTextField();
		txtDireccion.setBounds(91, 48, 311, 20);
		getContentPane().add(txtDireccion);
		txtDireccion.setColumns(10);
		
		JLabel lblTelefono = new JLabel("Telefono:");
		lblTelefono.setBounds(21, 94, 60, 14);
		getContentPane().add(lblTelefono);
		
		txtTelefono = new JTextField();
		txtTelefono.setBounds(91, 91, 311, 20);
		getContentPane().add(txtTelefono);
		txtTelefono.setColumns(10);
		
		JLabel lblMovil = new JLabel("Movil:");
		lblMovil.setBounds(21, 131, 64, 14);
		getContentPane().add(lblMovil);
		
		txtMovil = new JTextField();
		txtMovil.setBounds(91, 128, 311, 20);
		getContentPane().add(txtMovil);
		txtMovil.setColumns(10);
		
		JLabel lblRtn = new JLabel("RTN:");
		lblRtn.setBounds(21, 175, 60, 14);
		getContentPane().add(lblRtn);
		
		txtRtn = new JTextField();
		txtRtn.setBounds(91, 172, 311, 20);
		getContentPane().add(txtRtn);
		txtRtn.setColumns(10);
		
		// botones de accion
		btnCancelar = new BotonCancelar();
		btnCancelar.setLocation(280, 231);
		getContentPane().add(btnCancelar);
		
		btnGuardar = new BotonGuardar();	
		btnGuardar.setLocation(52, 231);
		getContentPane().add(btnGuardar);
		
		btnActualizar=new BotonActualizar();
		btnActualizar.setLocation(52, 231);
		btnActualizar.setVisible(false);
		getContentPane().add(btnActualizar);
		
		
	}
	public JTextField getTxtNombre(){
		return txtNombre;
	}
	public  JTextField getTxtDireccion(){
		return  txtDireccion;
	}
	public JTextField getTxtTelefono(){
		return txtTelefono;
	}
	public JTextField getTxtMovil(){
		return txtMovil;
	}
	public JTextField getTxtRtn(){
		return txtRtn;
	}
	public BotonActualizar getBtnActualizar(){
		return btnActualizar;
	}
	public BotonGuardar getBtnGuardar(){
		return btnGuardar;
	}
	public void conectarControlador(CtlCliente c){
		
		btnCancelar.addActionListener(c);
		btnCancelar.setActionCommand("CANCELAR");
		
		btnGuardar.addActionListener(c);
		btnGuardar.setActionCommand("GUARDAR");
		
		btnActualizar.addActionListener(c);
		btnActualizar.setActionCommand("ACTUALIZAR");
	}
	public void configActualizar() {
		// TODO Auto-generated method stub
		this.btnActualizar.setVisible(true);
		this.btnGuardar.setVisible(false);
		
	}
}
