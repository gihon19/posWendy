package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.Conexion;
import modelo.CuentasBancos;
import modelo.dao.CuentasBancosDao;
import view.ViewCrearCuentaBanco;
import view.ViewListaCuentaBancos;

public class CtlCuentasBancosLista implements ActionListener, MouseListener {
	
	private Conexion conexion=null;
	private ViewListaCuentaBancos view=null;
	private CuentasBancos myCuenta=null;
	private CuentasBancosDao myDao=null;
	private int filaPulsada=-1;
	
	public CtlCuentasBancosLista(ViewListaCuentaBancos v,Conexion conn){
		view=v;
		view.conectarCtl(this);
		conexion=conn;
		myDao=new CuentasBancosDao(conexion);
		cargarTabla(myDao.getCuentas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
		
	}
	
	public void cargarTabla(List<CuentasBancos> cuentas){
		//JOptionPane.showMessageDialog(view, " "+facturas.size());
		this.view.getModelo().limpiar();
		
		if(cuentas!=null){
			for(int c=0;c<cuentas.size();c++){
				this.view.getModelo().agregar(cuentas.get(c));
				
			}
		}else{
			JOptionPane.showMessageDialog(view, "No se encontro ninguna cotizacion. Escriba otra busqueda", "Error al buscar", JOptionPane.ERROR_MESSAGE);
			view.getTxtBuscar().requestFocusInWindow();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		filaPulsada=this.view.getTabla().getSelectedRow();
		
		 //si seleccion una fila
       if(filaPulsada>=0){
       	
       	
       	//se consigue la cuenta de la fila seleccionada
       this.myCuenta=view.getModelo().getCuenta(filaPulsada);
       	
       	//si fue doble click mostrar modificar
       	if (e.getClickCount() == 2) {
       		
       		ViewCrearCuentaBanco vCuenta=new ViewCrearCuentaBanco(view);
			CtlCuentaBanco cCuenta=new CtlCuentaBanco(vCuenta, conexion);
			
			boolean resultado=cCuenta.actualizarCuenta(myCuenta);
			if(resultado){
				cargarTabla(myDao.getCuentas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			}
       		
       		
       	}//fin del if del doble click
       	
       	
       }
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando=e.getActionCommand();
		
		switch(comando){
		
		case "ESCRIBIR":
			view.setTamanioVentana(1);
			break;
		
			case "INSERTAR":
				ViewCrearCuentaBanco vCuenta=new ViewCrearCuentaBanco(view);
				CtlCuentaBanco cCuenta=new CtlCuentaBanco(vCuenta, conexion);
				
				boolean resultado=cCuenta.agregarCuenta();
				if(resultado){
					cargarTabla(myDao.getCuentas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				}
				break;
		
		}
		
		
	}

}
