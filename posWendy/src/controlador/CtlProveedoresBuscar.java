package controlador;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.Conexion;
import modelo.Proveedor;
import modelo.dao.ProveedorDao;
import view.ViewListaProveedor;

public class CtlProveedoresBuscar implements ActionListener ,MouseListener, WindowListener,KeyListener {
	
	private Conexion conexion=null;
	
	private Proveedor myProveedor=null;
	private ProveedorDao myDao=null;
	private ViewListaProveedor view;

	private boolean resultado=false;
	private int filaPulsada=-1;
	
	public CtlProveedoresBuscar(ViewListaProveedor v,Conexion conn){
		conexion=conn;
		view=v;
		view.conectarControladorBuscar(this);
		myDao=new ProveedorDao(conexion);
		cargarTabla(myDao.todoProveedor(view.getModelo().getLimiteInferior(), view.getModelo().getLimiteSuperior()));
		
		view.getRdbtnNombre().setSelected(true);
		view.getTxtBuscar().requestFocusInWindow();
		
		
	}
	
	public void cargarTabla(List<Proveedor> proveedores){
		view.getModelo().limpiarProveedores();
		
		if(proveedores!=null){
			for(int c=0;c<proveedores.size();c++){
				this.view.getModelo().agregarProveedor(proveedores.get(c));
			}
		}
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		//Recoger qu� fila se ha pulsadao en la tabla
        filaPulsada = this.view.getTabla().getSelectedRow();
		if (e.getClickCount() == 2){
			myProveedor=view.getModelo().getProveedor(filaPulsada);
			resultado=true;
			
			this.view.setVisible(false);
			this.view.dispose();
			
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
		
		
		
			case "NEXT":
				view.getModelo().netPag();
				cargarTabla(myDao.todoProveedor(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				
				view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
				break;
			case "LAST":
				view.getModelo().lastPag();
				cargarTabla(myDao.todoProveedor(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				
				view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
				break;
				
			case "BUSCAR":
				
				//si se seleciono el boton ID
				if(this.view.getRdbtnId().isSelected()){  
					myProveedor=myDao.buscarPro(Integer.parseInt(this.view.getTxtBuscar().getText()));
					if(myProveedor!=null){
						this.view.modelo.limpiarProveedores();
						this.view.modelo.agregarProveedor(myProveedor);
					}else{
						JOptionPane.showMessageDialog(view, "No se encuentro el proveedor");
					}
				} 
				
				if(this.view.getRdbtnNombre().isSelected()){ //si esta selecionado la busqueda por nombre	
					
					cargarTabla(myDao.buscarProveedorNombre(this.view.getTxtBuscar().getText()));
			        
					}
				if(this.view.getRdbtnDireccion().isSelected()){  
					cargarTabla(myDao.buscarProveedorDireccion(this.view.getTxtBuscar().getText()));
					}
				
				if(this.view.getRdbtnTodos().isSelected()){  
					cargarTabla(myDao.todoProveedor(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
					this.view.getTxtBuscar().setText("");
					}
				//if(myProveedorDao.buscarPro(Integer.parseInt(this.view.getTxtBuscar().getText())))
				//JOptionPane.showMessageDialog(view, "Apreto buscar");
				break;
			case "ESCRIBIR":
				view.setTamanioVentana(1);
				
				break;
			
		}
		
	}
	
	public boolean buscarProveedores(Window v){
		cargarTabla(myDao.todoProveedor(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
		
		this.view.setLocationRelativeTo(v);
		this.view.setModal(true);
		this.view.setVisible(true);
		return resultado;
		
	}

	/**
	 * @return the myProveedor
	 */
	public Proveedor getProveedor() {
		return myProveedor;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
			resultado=false;
			this.myProveedor=null;
	         view.setVisible(false);
	      }
		
	}


}
