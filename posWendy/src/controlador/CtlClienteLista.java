package controlador;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.AbstractJasperReports;
import modelo.Articulo;
import modelo.Cliente;
import modelo.dao.ClienteDao;
import modelo.dao.CodBarraDao;
import modelo.Conexion;
import view.tablemodel.TmCategorias;
import view.ViewCrearArticulo;
import view.ViewCrearCliente;
import view.ViewListaClientes;

public class CtlClienteLista implements ActionListener, MouseListener {
	private ViewListaClientes view;
	private Conexion conexion=null;
	private ClienteDao clienteDao=null;
	private Cliente myCliente=null;
	
	//fila selecciona enla lista
		private int filaPulsada;
	
	public CtlClienteLista(ViewListaClientes v,Conexion conn){
		view=v;
		conexion=conn;
		view.conectarControlador(this);
		
		clienteDao=new ClienteDao(conexion);
		cargarTabla(clienteDao.todoClientes(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
		view.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando=e.getActionCommand();
		switch (comando){
		case "ESCRIBIR":
			view.setTamanioVentana(1);
			break;
		case "BUSCAR":
			//si se seleciono el boton ID
			if(this.view.getRdbtnId().isSelected()){  
				myCliente=clienteDao.buscarCliente(Integer.parseInt(this.view.getTxtBuscar().getText()));
				if(myCliente!=null){												
					this.view.getModelo().limpiarClientes();
					this.view.getModelo().agregarCliente(myCliente);
				}else{
					JOptionPane.showMessageDialog(view, "No se encuentro el articulo");
				}
			} 
			
			if(this.view.getRdbtnNombre().isSelected()){ //si esta selecionado la busqueda por nombre	
				
				cargarTabla(clienteDao.buscarCliente(this.view.getTxtBuscar().getText()));
		        
				}
			if(this.view.getRdbtnRtn().isSelected()){  
				cargarTabla(clienteDao.buscarClienteRtn(this.view.getTxtBuscar().getText()));
				}
			
			if(this.view.getRdbtnTodos().isSelected()){
				view.getModelo().setPaginacion();
				cargarTabla(clienteDao.todoClientes(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				this.view.getTxtBuscar().setText("");
				}
			break;
		case "NUEVO":
			ViewCrearCliente viewNewCliente=new ViewCrearCliente();
			CtlCliente ctlCliente=new CtlCliente(viewNewCliente,conexion);
			
			boolean resuldoGuarda=ctlCliente.agregarCliente();
			if(resuldoGuarda){
				view.getModelo().setPaginacion();
				cargarTabla(clienteDao.todoClientes(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				/*
				this.view.getModelo().agregarCliente(ctlCliente.getClienteGuardado());
				
				/*<<<<<<<<<<<<<<<selecionar la ultima fila creada>>>>>>>>>>>>>>>
				int row =  this.view.getTabla().getRowCount () - 1;
				Rectangle rect = this.view.getTabla().getCellRect(row, 0, true);
				this.view.getTabla().scrollRectToVisible(rect);
				this.view.getTabla().clearSelection();
				this.view.getTabla().setRowSelectionInterval(row, row);
				TablaModeloMarca modelo = (TablaModeloMarca)this.view.getTabla().getModel();
				modelo.fireTableDataChanged();*/
			}
			view=null;
			ctlCliente=null;
			break;
			
		case "NEXT":
			view.getModelo().netPag();
			cargarTabla(clienteDao.todoClientes(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			
			view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
			break;
		case "LAST":
			view.getModelo().lastPag();
			cargarTabla(clienteDao.todoClientes(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			
			view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
			break;
		case "CUENTASCLIENTES":
			try {
    			AbstractJasperReports.createReportSaltosClientes(conexion.getPoolConexion().getConnection());
    			AbstractJasperReports.showViewer(this.view);
    			
    		}catch (SQLException ee) {
				// TODO Auto-generated catch block
				ee.printStackTrace();
			}
			
			break;
		case "CUENTACLIENTE":
			
			
            
          //se consigue el proveedore de la fila seleccionada
            myCliente=this.view.getModelo().getCliente(filaPulsada);
            
            
            try {
				AbstractJasperReports.createReportCuentaCliente(conexion.getPoolConexion().getConnection(),myCliente.getId());
				AbstractJasperReports.showViewer(this.view);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
			break;
		}
	}
	public void cargarTabla(List<Cliente> clientes){
		//JOptionPane.showMessageDialog(view, articulos);
		this.view.getModelo().limpiarClientes();
		if(clientes!=null){
			for(int c=0;c<clientes.size();c++){
				this.view.getModelo().agregarCliente(clientes.get(c));
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		//Recoger qu� fila se ha pulsadao en la tabla
        filaPulsada = this.view.getTabla().getSelectedRow();
        
        //si seleccion una fila
        if(filaPulsada>=0){
        	
        	//Se recoge el id de la fila marcada
            int identificador= (int)this.view.getModelo().getValueAt(filaPulsada, 0);
            
          //se consigue el proveedore de la fila seleccionada
            myCliente=this.view.getModelo().getCliente(filaPulsada);
        
            
        	//si fue doble click mostrar modificar
        	if (e.getClickCount() == 2) {
        		
        		myCliente=this.view.getModelo().getCliente(filaPulsada);
        		//myArticulo=this.view.getModelo().getArticulo(filaPulsada);//se consigue el Marca de la fila seleccionada
	           
	        	//crea la ventana para ingresar un nuevo proveedor
				ViewCrearCliente viewCliente= new ViewCrearCliente();
				
				//se crea el controlador de la ventana y se le pasa la view
				CtlCliente ctlActulizarCliente=new CtlCliente(viewCliente,conexion);
				
				
						
				
				//se llama del metodo actualizar marca para que se muestre la ventanda y procesa la modificacion
				boolean resultado=ctlActulizarCliente.actualizarCliente(myCliente);
				
				//se proceso el resultado de modificar la marca
				if(resultado){
					this.view.getModelo().cambiarCliente(filaPulsada, ctlActulizarCliente.getClienteGuardado());//se cambia en la vista
					this.view.getModelo().fireTableDataChanged();//se refrescan los cambios
					this.view.getTabla().getSelectionModel().setSelectionInterval(filaPulsada,filaPulsada);//se seleciona lo cambiado
				}	
			
				ctlActulizarCliente=null;
				viewCliente=null;
				
	        }//fin del if del doble click
        	else{//si solo seleccion la fila se guarda el id de proveedor para accion de eliminar
        		
        		//this.view.getBtnEliminar().setEnabled(true);
        		/*idProveedor=identificador;
        		filaTabla=filaPulsada;*/
        		
        	}
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

}
