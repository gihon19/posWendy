package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import modelo.Conexion;
import modelo.CuentasBancos;
import modelo.dao.CuentasBancosDao;
import view.ViewCrearCuentaBanco;

public class CtlCuentaBanco implements ActionListener {
	
	private Conexion conexion=null;
	private ViewCrearCuentaBanco view=null;
	private CuentasBancos myCuenta=null;
	

	private CuentasBancosDao myDao=null;


	private boolean resultaOperacion=false;
	
	public CtlCuentaBanco(ViewCrearCuentaBanco v,Conexion conn ){
		view=v;
		conexion=conn;
		myCuenta=new CuentasBancos();
		myDao=new CuentasBancosDao(conexion);
		view.conectarCtl(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
String comando=e.getActionCommand();
		
		switch(comando){
		case "GUARDAR":
			if(validar()){
				setCuenta();
				guardarCuenta();
			}
			break;
		case "CANCELAR":
			view.setVisible(false);
			break;
		case "ACTUALIZAR":
			
			if(validar()){
				setCuenta();
				actualizarCuenta();
			}
			
			break;
		}
		
	}

	private void actualizarCuenta() {
		// TODO Auto-generated method stub
		if(myDao.actualizar(myCuenta)){
			JOptionPane.showMessageDialog(view, "La Cuenta se actualizo correctamente.");
			this.resultaOperacion=true;
			view.setVisible(false);
		}else{
			JOptionPane.showMessageDialog(view, "Ocurrio un problema para actualizo la Cuenta");
		}
		
	}

	private void guardarCuenta() {
		// TODO Auto-generated method stub
		if(myDao.registrar(myCuenta)){
			JOptionPane.showMessageDialog(view, "La Cuenta se guardo correctamente.");
			this.resultaOperacion=true;
			view.setVisible(false);
		}else{
			JOptionPane.showMessageDialog(view, "Ocurrio un problema para guardar la Cuenta");
		}
	}

	private void setCuenta() {
		// TODO Auto-generated method stub
		myCuenta.setNoCuenta(view.getTxtNoCuenta().getText());
		myCuenta.setNombre(view.getTxtNombre().getText());
		
		int x=this.view.getCbTipoCuenta().getSelectedIndex();
		myCuenta.setIdTipoCuenta(x);
		
	}

	private boolean validar() {
		// TODO Auto-generated method stub
		boolean resul=false;
		if(view.getTxtNombre().getText().trim().length()==0){
			JOptionPane.showMessageDialog(view, "Debe rellenar todos los campos");
			view.getTxtNombre().requestFocusInWindow();
		}else
		if(view.getTxtNoCuenta().getText().trim().length()==0){
			JOptionPane.showMessageDialog(view, "Debe rellenar todos los campos");
			view.getTxtNoCuenta().requestFocusInWindow();
		}else
			resul=true;
		
		return resul;
	}
	
	public boolean agregarCuenta(){
		view.setVisible(true);
		return resultaOperacion;
		
	}
	/**
	 * @return the myCuenta
	 */
	public CuentasBancos getCuenta() {
		return myCuenta;
	}
	
	public boolean actualizarCuenta( CuentasBancos c){
		this.myCuenta=c;
		view.getBtnActualizar().setVisible(true);
		view.getBtnGuardar().setVisible(false);
		loadCuenta();
		view.setVisible(true);
		return resultaOperacion;
	}

	private void loadCuenta() {
		// TODO Auto-generated method stub
		view.getTxtNombre().setText(myCuenta.getNombre());
		view.getTxtNoCuenta().setText(myCuenta.getNoCuenta());
	
		view.getCbTipoCuenta().setSelectedIndex(myCuenta.getIdTipoCuenta());
		
		
	}

}
