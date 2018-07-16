package controlador;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modelo.AbstractJasperReports;
import modelo.Articulo;
import modelo.Conexion;
import modelo.Requisicion;
import modelo.dao.RequisicionDao;
import modelo.dao.UsuarioDao;
import view.ViewListaRequisiciones;
import view.ViewRequisicion;
import view.tablemodel.TableModeloArticulo;

public class CtlRequisicionesLista implements ActionListener, MouseListener, ChangeListener {
	
	private ViewListaRequisiciones view=null;
	private Conexion conexion=null;
	private RequisicionDao myRequiDao=null;
	private Requisicion myRequi=null;
	//fila selecciona enla lista
	private int filaPulsada;
	
	private UsuarioDao myUsuarioDao=null;

	public CtlRequisicionesLista(ViewListaRequisiciones v, Conexion conn) {
		view=v;
		conexion=conn;
		myRequiDao=new RequisicionDao(conexion);
		myRequi=new Requisicion();
		myUsuarioDao=new UsuarioDao(conexion);
		
		cargarTabla(myRequiDao.todas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
		view.conectarCtl(this);
		view.setVisible(true);
	}
	
	public void cargarTabla(List<Requisicion> requisiciones){
		//JOptionPane.showMessageDialog(view, articulos);
		this.view.getModelo().limpiarArticulos();
		
		if(requisiciones!=null){
			
			for(int c=0;c<requisiciones.size();c++){
				this.view.getModelo().addRequisicion(requisiciones.get(c));
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		//Recoger qu� fila se ha pulsadao en la tabla
        filaPulsada = this.view.getTabla().getSelectedRow();
        
        //si seleccion una fila
        if(filaPulsada>=0){
        	
        	//Se recoge el id de la fila marcada
            //int idFactura= (int)this.view.getModelo().getValueAt(filaPulsada, 0);
            
            this.view.getBtnEliminar().setEnabled(true);
            this.view.getBtnImprimir().setEnabled(true);
            this.myRequi=this.view.getModelo().getRequisicion(filaPulsada);
            //se consigue el proveedore de la fila seleccionada
            //myArticulo=this.view.getModelo().getArticulo(filaPulsada);
        
            
        	//si fue doble click mostrar modificar
        	if (e.getClickCount() == 2) {
        		
        		
        		
        		try {
    				
    				//AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Factura_Saint_Paul_Reimpresion.jasper",myFactura.getIdFactura() );
        			AbstractJasperReports.createReport(conexion.getPoolConexion().getConnection(), 3, myRequi.getNoRequisicion());
        			AbstractJasperReports.showViewer(this.view);
    				//AbstractJasperReports.imprimierFactura();
    				this.view.getBtnImprimir().setEnabled(false);
    				myRequi=null;
    			} catch (SQLException ee) {
    				// TODO Auto-generated catch block
    				ee.printStackTrace();
    			}
        	
        		/*ViewFacturar viewFacturar=new ViewFacturar(this.view);
        		CtlFacturar ctlFacturar=new CtlFacturar(viewFacturar,conexion);
        		
        		//si se cobro la factura se debe eleminiar el temp por eso se guarda el id
        		int idFactura=myFactura.getIdFactura();
        		
        		//se llama al controlador de la factura para que la muestre 
        		ctlFacturar.viewFactura(myFactura);//actualizarFactura(myFactura);
        		
        		//si la factura se cobro se regresara null sino modificamos la factura en la lista
        		if(myFactura==null){
        			this.view.getModelo().eliminarFactura(filaPulsada);
        			myFacturaDao.EliminarTemp(idFactura);
        		}else{
        			this.view.getModelo().cambiarArticulo(filaPulsada, myFactura);
        			this.view.getTablaFacturas().getSelectionModel().setSelectionInterval(filaPulsada,filaPulsada);//se seleciona lo cambiado
        		}
        		viewFacturar.dispose();
        		ctlFacturar=null;*/
        		
	        	
			
				
				
				
	        }//fin del if del doble click
        	else{//si solo seleccion la fila se guarda el id de proveedor para accion de eliminar
        		
        		this.view.getBtnEliminar().setEnabled(true);
        		
        		
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando=e.getActionCommand();
		
		switch (comando){
		
		case "INSERTAR":
			ViewRequisicion viewRequi=new ViewRequisicion(view);
			CtlRequisicion ctlRequi=new CtlRequisicion(viewRequi,conexion);
			
			
			boolean resuldoGuarda=ctlRequi.agregarRequisicion();
			
			if(resuldoGuarda){
				view.getModelo().setPaginacion();
				
				cargarTabla(myRequiDao.todas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				/*
				this.view.getModelo().addRequisicion(ctlRequi.getRequisicionGuardado());
				
				/*<<<<<<<<<<<<<<<selecionar la ultima fila creada>>>>>>>>>>>>>>>
				int row =  this.view.getTabla().getRowCount () - 1;
				Rectangle rect = this.view.getTabla().getCellRect(row, 0, true);
				this.view.getTabla().scrollRectToVisible(rect);
				this.view.getTabla().clearSelection();
				this.view.getTabla().setRowSelectionInterval(row, row);
				TableModeloArticulo modelo = (TableModeloArticulo)this.view.getTabla().getModel();
				modelo.fireTableDataChanged();*/
			}
			break;
		case "FECHA":
			
			break;
			
		case "TODAS":
			
			
			break;
		case "ID":
			
			
			break;
		case "BUSCAR":
			//JOptionPane.showMessageDialog(view, "Click en buscar");
			//si la busqueda es por id
			if(this.view.getRdbtnId().isSelected()){
				myRequi=myRequiDao.requiPorId(Integer.parseInt(this.view.getTxtBuscar().getText()));
				if(myRequi!=null){												
					this.view.getModelo().limpiarArticulos();
					this.view.getModelo().addRequisicion(myRequi);
				}else{
					JOptionPane.showMessageDialog(view, "No se encuentro la factura");
				}
				
			}
			//si la busqueda es por fecha
			if(this.view.getRdbtnFecha().isSelected()){  
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date1 = sdf.format(this.view.getDcFecha1().getDate());
				String date2 = sdf.format(this.view.getDcFecha2().getDate());
				cargarTabla(myRequiDao.requiPorFechas(date1,date2));
				//this.view.getTxtBuscar1().setText("");
				//this.view.getTxtBuscar2().setText("");
				}
			
			
			//si la busqueda son tadas
			if(this.view.getRdbtnTodos().isSelected()){  
				cargarTabla(myRequiDao.todas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				this.view.getTxtBuscar().setText("");
				}
			break;
		case "ANULARFACTURA":
			
			//se verifica si la factura ya esta agregada al kardex
			if (myRequi.getAgregadoAkardex()==0){
				
					int resul=JOptionPane.showConfirmDialog(view, "�Desea anular la requisicion no. "+myRequi.getNoRequisicion()+"?");
					//sin confirmo la anulacion
					if(resul==0){
						JPasswordField pf = new JPasswordField();
						int action = JOptionPane.showConfirmDialog(view, pf,"Escriba la contrase�a admin",JOptionPane.OK_CANCEL_OPTION);
						//String pwd=JOptionPane.showInputDialog(view, "Escriba la contrase�a admin", "Seguridad", JOptionPane.INFORMATION_MESSAGE);
						if(action < 0){
							
							
						}else{
							String pwd=new String(pf.getPassword());
							//comprabacion del permiso administrativo
							if(this.myUsuarioDao.comprobarAdmin(pwd)){
								//se anula la factura en la bd
								if(myRequiDao.anularRequi(myRequi))
									myRequi.setEstado("NULA");
								//JOptionPane.showMessageDialog(view, "Usuario Valido");
							}else{
								JOptionPane.showMessageDialog(view, "Usuario Invalido");
							}
						}
						
					}
			}else{
				JOptionPane.showMessageDialog(view, "No se puede anular la compra porque ya esta en el Kardex!!!");
				this.view.getBtnEliminar().setEnabled(false);
			}
			break;
			
		case "IMPRIMIR":
			try {
				//this.view.setVisible(false);
				//this.view.dispose();
				AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Factura_Saint_Paul_Reimpresion.jasper",myRequi.getNoRequisicion() );
				//AbstractJasperReports.showViewer();
				AbstractJasperReports.showViewer(view);
				this.view.getBtnImprimir().setEnabled(false);
				myRequi=null;
			} catch (SQLException ee) {
				// TODO Auto-generated catch block
				ee.printStackTrace();
			}
			break;
			
		case "NEXT":
			view.getModelo().netPag();
			cargarTabla(myRequiDao.todas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			
			view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
			break;
		case "LAST":
			view.getModelo().lastPag();
			cargarTabla(myRequiDao.todas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			
			view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
			break;
		}

	}

}
