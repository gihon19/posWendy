package controlador;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.AbstractJasperReports;
import modelo.Articulo;
import modelo.Conexion;
import modelo.Factura;
import modelo.dao.CotizacionDao;
import modelo.dao.FacturaDao;
import view.ViewFacturar;
import view.ViewListaFactura;

public class CtlFacturaLista implements ActionListener, MouseListener {
	
	private Conexion conexion=null;
	private ViewListaFactura view=null;
	private CotizacionDao myFacturaDao=null;
	private int filaPulsada;
	private Factura myFactura;
	private boolean resultado;
	
	public CtlFacturaLista(){
		
	}

	public CtlFacturaLista(ViewListaFactura v, Conexion conn) {
		// TODO Auto-generated constructor stub
		view=v;
		view.conectarControlador(this);
		conexion=conn;
		myFacturaDao=new CotizacionDao(conexion);
		cargarTabla(myFacturaDao.getCotizaciones());
		//setMyFactura(new Factura());
		
		//view.setVisible(true);
	}
	
	public void cargarTabla(List<Factura> facturas){
		//JOptionPane.showMessageDialog(view, " "+facturas.size());
		this.view.getModelo().limpiarFacturas();
		
		if(facturas!=null){
			for(int c=0;c<facturas.size();c++){
				this.view.getModelo().agregarFactura(facturas.get(c));
				
			}
		}else{
			JOptionPane.showMessageDialog(view, "No se encontro ninguna cotizacion. Escriba otra busqueda", "Error al buscar", JOptionPane.ERROR_MESSAGE);
			view.getTxtBuscar().requestFocusInWindow();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		filaPulsada=this.view.getTablaFacturas().getSelectedRow();
		
		 //si seleccion una fila
        if(filaPulsada>=0){
        	
        	
        	//se consigue el proveedore de la fila seleccionada
        	setMyFactura(this.view.getModelo().getFactura(filaPulsada));
        	
        	//si fue doble click mostrar modificar
        	if (e.getClickCount() == 2) {
        		
        		
        		setResultado(true);
    			//myArticuloDao.desconectarBD();
    			this.view.setVisible(false);
    			//JOptionPane.showMessageDialog(null,myMarca);
    			this.view.dispose();
        		
        		/*	ViewFacturar viewFacturar=new ViewFacturar(this.view);
        		CtlFacturar ctlFacturar=new CtlFacturar(viewFacturar,conexion);
        		
        		//viewFacturar.get.getBtnPendientes().setEnabled(false);
        		
        		//viewFacturar.getBtnCobrar()
        		
        		//si se cobro la factura se debe eleminiar el temp por eso se guarda el id
        		int idFactura=myFactura.getIdFactura();
        		
        		//se llama al controlador de la factura para que la muestre 
        		myFactura=ctlFacturar.actualizarFactura(myFactura);
        		
        		
        	//si la factura se cobro se regresara null sino modificamos la factura en la lista
        		if(myFactura==null){
        			this.view.getModelo().eliminarFactura(filaPulsada);
        			myFacturaDao.EliminarTemp(idFactura);
        		}else{
        			this.view.getModelo().cambiarArticulo(filaPulsada, myFactura);
        			this.view.getTablaFacturas().getSelectionModel().setSelectionInterval(filaPulsada,filaPulsada);//se seleciona lo cambiado
        		}
        		viewFacturar.dispose();
        		ctlFacturar=null;
        		*/
        	}//fin del if del doble click
        	else{//si solo seleccion la fila se guarda el id de proveedor para accion de eliminar
        		
        		this.view.getBtnEliminar().setEnabled(true);
        		//this.view.getBtnCobrar().setEnabled(true);
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
/*	
private void cobrar(){
		
		
		boolean resul=myFacturaDao.registrarFactura(myFactura);
				
		if(resul){
			
			int idFacTemp=myFactura.getIdFactura();
			myFactura.setIdFactura(myFacturaDao.getIdFacturaGuardada());
			
				try {
					//AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Factura_Saint_Paul.jasper",myFactura.getIdFactura() );
					AbstractJasperReports.createReport(conexion.getPoolConexion().getConnection(), 1, myFactura.getIdFactura());
					//this.view.setModal(false);
					AbstractJasperReports.imprimierFactura();
					//this.view.setModal(true);
					//JOptionPane.showMessageDialog(view, "El id factura:"+myFactura.getIdFactura());
					myFacturaDao.EliminarTemp(idFacTemp);
					
					
					
					this.view.getModelo().eliminarFactura(this.filaPulsada);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//AbstractJasperReports.showViewer();
				//AbstractJasperReports.imprimierFactura();
				
			
		}else{
			
		}
		
	}
*/
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		String comando=e.getActionCommand();
		
		switch(comando){
		
		
		case "BUSCAR":
			//si se seleciono el boton ID
			if(this.view.getRdbtnId().isSelected()){ 
				
	
				myFactura=myFacturaDao.cotizacionPorId(Integer.parseInt(this.view.getTxtBuscar().getText()));
				if(myFactura!=null){
					resultado=true;
					this.view.getModelo().limpiarFacturas();
					this.view.getModelo().agregarFactura(myFactura);//.agregarArticulo(myArticulo);
					//view.setVisible(false);
				}else{
					JOptionPane.showMessageDialog(view, "No se encontro ninguna cotizacion. Escriba otra busqueda", "Error al buscar", JOptionPane.ERROR_MESSAGE);
					this.view.getTxtBuscar().setText("");
				}
			} 
			
			if(this.view.getRdbtnCliente().isSelected()){ //si esta selecionado la busqueda por nombre	
				
				cargarTabla(myFacturaDao.cotizacionesDelCliente(view.getTxtBuscar().getText()));
				/*
				//cargarTabla(myArticuloDao.buscarArticulo(this.view.getTxtBuscar().getText()));
				//JOptionPane.showMessageDialog(view, myArticulo);
				if(this.filaPulsada>=0 && myArticulo!=null){
					resultado=true;
				}
		        view.setVisible(false);*/
				}
			if(this.view.getRdbtnFecha().isSelected()){ 
				
				//JOptionPane.showMessageDialog(view, view.getTxtBuscarFecha().getText());
				
				cargarTabla(myFacturaDao.cotizacionesPorFecha(view.getTxtBuscarFecha().getText()));
				
				/*cargarTabla(myArticuloDao.buscarArticuloMarca(this.view.getTxtBuscar().getText()));
					if(myArticulo!=null){
						view.setVisible(false);
					}else{
						this.view.getTxtBuscar().setText("");
					}*/
				}
			
			if(this.view.getRdbtnTodos().isSelected()){  
				cargarTabla(myFacturaDao.getCotizaciones());
				/*cargarTabla(myArticuloDao.todoArticulos());
				this.view.getTxtBuscar().setText("");*/
				}
			break;
		case "INSERTAR":
			
			ViewFacturar vistaFacturar=new ViewFacturar(this.view);
			vistaFacturar.pack();
			CtlFacturar ctlFacturar=new CtlFacturar(vistaFacturar,conexion );
				
			vistaFacturar.getTxtBuscar().requestFocusInWindow();
			
			//myFac=ctlFacturar.getAccion();
			boolean resul=ctlFacturar.getAccion();
			//JOptionPane.showMessageDialog(view, myFac);
			
			if(resul){
				Factura myFac=new Factura();
				myFac=ctlFacturar.getFactura();
				this.view.getModelo().agregarFactura(myFac);
			}
			
			vistaFacturar.dispose();
			vistaFacturar=null;
			ctlFacturar=null;
			break;
		case "COBRAR":
		/*		cobrar();
				//this.view.getBtnEliminar().setEnabled(true);
        		this.view.getBtnCobrar().setEnabled(false);
        		this.view.getBtnEliminar().setEnabled(false);*/
			break;
		case "ELIMINAR":
			/*this.myFacturaDao.EliminarTemp(this.myFactura.getIdFactura());
			this.view.getModelo().eliminarFactura(this.filaPulsada);
			this.view.getBtnCobrar().setEnabled(false);
    		this.view.getBtnEliminar().setEnabled(false);*/
		break;
		}

	}

	public boolean isResultado() {
		return resultado;
	}

	public void setResultado(boolean resultado) {
		this.resultado = resultado;
	}

	public Factura getMyFactura() {
		return myFactura;
	}

	public void setMyFactura(Factura myFactura) {
		this.myFactura = myFactura;
	}
	
public boolean buscarCotizaciones(Window v){
		
		//this.myArticuloDao.cargarInstrucciones();
		//cargarTabla(myArticuloDao.todoArticulos());
		myFactura=null;
		this.view.getRdbtnCliente().setSelected(true);
		this.view.getBtnEliminar().setEnabled(false);
		this.view.getBtnAgregar().setEnabled(false);
		this.view.setLocationRelativeTo(v);
		this.view.setModal(true);
		view.getTxtBuscar().requestFocusInWindow();
		
		//view.setFocusable(true);
		this.view.setVisible(true);
		//
		return resultado;
	}

}
