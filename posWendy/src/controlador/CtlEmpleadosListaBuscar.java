package controlador;

import java.awt.Rectangle;
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
import modelo.Empleado;
import modelo.dao.EmpleadoDao;
import view.ViewCrearEmpleado;
import view.ViewListaEmpleados;
import view.tablemodel.TmEmpleados;

public class CtlEmpleadosListaBuscar implements ActionListener,MouseListener, WindowListener,KeyListener {
	
	private ViewListaEmpleados view;
	private Conexion conexion;
	private EmpleadoDao myDao;
	private int filaPulsada;
	private Empleado myEmpleado;
	private boolean resultado=false;

	public CtlEmpleadosListaBuscar(ViewListaEmpleados v, Conexion conn) {
		// TODO Auto-generated constructor stub
		view=v;
		conexion=conn;
		
		myDao=new EmpleadoDao(conexion);
		
		view.conectarCtlBuscar(this);
		
		myEmpleado=new Empleado();
		
		cargarTabla(myDao.todos(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
	}

	public boolean buscar() {
		// TODO Auto-generated method stub
		view.getRdbtnNombre().setSelected(true);
		view.getTxtBuscar().requestFocusInWindow();
		view.setVisible(true);
		return resultado;
	}
	
	private void cargarTabla(List<Empleado> empleados) {
		// TODO Auto-generated method stub
		view.getModelo().limpiar();
		if(empleados!=null){
		for(int x=0;x<empleados.size();x++)
			view.getModelo().agregar(empleados.get(x));
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getComponent()==this.view.getTxtBuscar()&&view.getTxtBuscar().getText().trim().length()!=0){
			
			//si esta activado la busqueda por articulo
			if(this.view.getRdbtnNombre().isSelected()){
				
				this.filaPulsada=this.view.getTabla().getSelectedRow();
				
				if(e.getKeyCode()==KeyEvent.VK_DOWN){
					filaPulsada++;
					this.view.getTabla().setRowSelectionInterval(0	,filaPulsada);
					
					myEmpleado=view.getModelo().getEmpleado(filaPulsada);
					
					
				}else
					if(e.getKeyCode()==KeyEvent.VK_UP){
						
						filaPulsada--;
						this.view.getTabla().setRowSelectionInterval(0	, filaPulsada);
						myEmpleado=view.getModelo().getEmpleado(filaPulsada);
					}
				
				
				
				//this.view.getTablaArticulos().setRowSelectionInterval(0	, 0);
				
				//myArticulo=view.getModelo().getArticulo(0);
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
			resultado=false;
			myEmpleado=null;
	         view.setVisible(false);
	      }
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			
			if(filaPulsada>0){
				//Se recoge el id de la fila marcada
	            int identificador= (int)this.view.getModelo().getValueAt(filaPulsada, 0);
	            
	            //se consigue el proveedore de la fila seleccionada
	            myEmpleado=this.view.getModelo().getEmpleado(filaPulsada);// .getCliente(filaPulsada);
	            this.resultado=true;
	            
				//myArticulo=view.getModelo().getArticulo(filaPulsada-1);
				view.setVisible(false);
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
		
		this.myEmpleado=null;
		view.setVisible(false);
		
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
		filaPulsada = this.view.getTabla().getSelectedRow();
        //JOptionPane.showMessageDialog(view, filaPulsada);
		if (e.getClickCount() == 2){
			
			this.myEmpleado=this.view.getModelo().getEmpleado(filaPulsada);
			
			resultado=true;
			//myArticuloDao.desconectarBD();
			this.view.setVisible(false);
			//JOptionPane.showMessageDialog(null,myMarca);
			this.view.dispose();
		}
		
	}
	public Empleado getEmpleadoSelected(){
		return myEmpleado;
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

		// TODO Auto-generated method stub
		
		String comando=e.getActionCommand();
		switch(comando){
		
		case "ESCRIBIR":
			view.setTamanioVentana(1);
			break;
		
		case "BUSCAR":
			if(view.getRdbtnTodos().isSelected()){
				view.getModelo().setPaginacion();
				cargarTabla(myDao.todos(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				view.getTxtBuscar().setText("");
			}else
				if(view.getRdbtnNombre().isSelected()){
					cargarTabla(myDao.porNombre(view.getTxtBuscar().getText()));
				}else
					if(view.getRdbtnApellido().isSelected()){
						cargarTabla(myDao.porApellido(view.getTxtBuscar().getText()));
					}else
						if(view.getRdbtnId().isSelected()){
							myEmpleado=myDao.buscarPorId(Integer.parseInt(view.getTxtBuscar().getText()));
							if(myEmpleado!=null){
								view.getModelo().limpiar();
								view.getModelo().agregar(myEmpleado);
							}else{
								JOptionPane.showMessageDialog(view, "No se encontro el empleado.");
							}
						}
			
			break;
			
		case "NEXT":
			view.getModelo().netPag();
			cargarTabla(myDao.todos(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			
			view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
			break;
		case "LAST":
			view.getModelo().lastPag();
			cargarTabla(myDao.todos(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			
			view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
			break;
		}
		
	
		
	}

}
