package controlador;

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
import modelo.Conexion;
import modelo.Factura;
import modelo.dao.DetalleFacturaDao;
import modelo.dao.DevolucionesDao;
import modelo.dao.FacturaDao;
import modelo.dao.UsuarioDao;
import view.ViewFacturaDevolucion;
import view.ViewFacturas;

public class CtlFacturas implements ActionListener, MouseListener, ChangeListener {
	private ViewFacturas view;
	
	private FacturaDao myFacturaDao=null;
	private Conexion conexion=null;
	private Factura myFactura;
	private UsuarioDao myUsuarioDao=null;
	private DetalleFacturaDao detallesDao=null;
	private DevolucionesDao devolucionDao=null;
	
	
	//fila selecciona enla lista
	private int filaPulsada=-1;
	
	public CtlFacturas(ViewFacturas v,Conexion conn) {
		
		view =v;
		view.conectarControlador(this);
		conexion=conn;
		myFacturaDao=new FacturaDao(conexion);
		cargarTabla(myFacturaDao.todasfacturas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
		myFactura=new Factura();
		myUsuarioDao=new UsuarioDao(conexion);
		detallesDao=new DetalleFacturaDao(conexion);
		devolucionDao=new DevolucionesDao(conexion);
		view.setVisible(true);
	}
	
	public void cargarTabla(List<Factura> facturas){
		//JOptionPane.showMessageDialog(view, " "+facturas.size());
		this.view.getModelo().limpiarFacturas();
		
		if(facturas!=null){
			for(int c=0;c<facturas.size();c++){
				this.view.getModelo().agregarFactura(facturas.get(c));
				
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		//Recoger qu� fila se ha pulsadao en la tabla
        filaPulsada = this.view.getTabla().getSelectedRow();
        //JOptionPane.showMessageDialog(view, "click en la tabla"+filaPulsada);
        
        //si seleccion una fila
        if(filaPulsada>=0){
        	
        	//Se recoge el id de la fila marcada
            //int idFactura= (int)this.view.getModelo().getValueAt(filaPulsada, 0);
            
           // this.view.getBtnEliminar().setEnabled(true);
           // this.view.getBtnAgregar().setEnabled(true);
            //this.view.getBtnImprimir().setEnabled(true);
            this.myFactura=this.view.getModelo().getFactura(filaPulsada);
            //se consigue el proveedore de la fila seleccionada
            //myArticulo=this.view.getModelo().getArticulo(filaPulsada);
        
            
        	//si fue doble click mostrar modificar
        	if (e.getClickCount() == 2) {
        		
        		
        		
        		try {
    				
    				//AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Factura_Saint_Paul_Reimpresion.jasper",myFactura.getIdFactura() );
        			AbstractJasperReports.createReport(conexion.getPoolConexion().getConnection(), 3, myFactura.getIdFactura());
        			AbstractJasperReports.showViewer(this.view);
    				//AbstractJasperReports.imprimierFactura();
    				this.view.getBtnImprimir().setEnabled(false);
    				myFactura=null;
    			} catch (SQLException ee) {
    				// TODO Auto-generated catch block
    				ee.printStackTrace();
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
		
		switch (comando){
		case "ESCRIBIR":
			view.setTamanioVentana(1);
			break;

		case "BUSCAR":
			
			//si la busqueda es por id
			if(this.view.getRdbtnId().isSelected()){
				//JOptionPane.showMessageDialog(view, "No se encuentro la factura");
				myFactura=myFacturaDao.facturasPorId(Integer.parseInt(this.view.getTxtBuscar().getText()));
				if(myFactura!=null){												
					this.view.getModelo().limpiarFacturas();
					this.view.getModelo().agregarFactura(myFactura); 
				}else{
					JOptionPane.showMessageDialog(view, "No se encuentro la factura");
				}
				
			}
			//si la busqueda es por fecha
			if(this.view.getRdbtnFecha().isSelected()){ 
				
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date1 = sdf.format(this.view.getDcFecha1().getDate());
				String date2 = sdf.format(this.view.getDcFecha2().getDate());
				
				//JOptionPane.showMessageDialog(view, date1+" al  "+date2);
				cargarTabla(myFacturaDao.facturasPorFechas(date1,date2));
				
		
				}
			
			
			//si la busqueda son tadas
			if(this.view.getRdbtnTodos().isSelected()){  
				cargarTabla(myFacturaDao.todasfacturas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				this.view.getTxtBuscar().setText("");
				}
			if(view.getRdbtnCliente().isSelected()){
				
				if(view.getTxtBuscar().getText().length()!=0)
					cargarTabla(myFacturaDao.porNombreCliente(view.getTxtBuscar().getText()));
				else{
					JOptionPane.showMessageDialog(view, "Debe escribir algo en la busqueda","Error en busqueda",JOptionPane.ERROR_MESSAGE);
					view.getTxtBuscar().requestFocusInWindow();
				}
			}
			break;
		case "ANULARFACTURA":
			
			//se verifica que se haya selecciona una fila
			if(verificarSelecion()){
					//se verifica si la factura ya esta agregada al kardex
					if (myFactura.getAgregadoAkardex()==0){
						
							int resul=JOptionPane.showConfirmDialog(view, "Desea anular la factura no "+myFactura.getIdFactura()+"?");
							//sin confirmo la anulacion
							if(resul==0){
								JPasswordField pf = new JPasswordField();
								int action = JOptionPane.showConfirmDialog(view, pf,"Escriba el password de admin",JOptionPane.OK_CANCEL_OPTION);
								//String pwd=JOptionPane.showInputDialog(view, "Escriba la contrase�a admin", "Seguridad", JOptionPane.INFORMATION_MESSAGE);
								if(action < 0){
									
									
								}else{
									String pwd=new String(pf.getPassword());
									//comprabacion del permiso administrativo
									if(this.myUsuarioDao.comprobarAdmin(pwd)){
										myFactura.setDetalles(detallesDao.getDetallesFactura(myFactura.getIdFactura()));
										//se anula la factura en la bd
										if(myFacturaDao.anularFactura(myFactura)){
											myFactura.setEstado("NULA");
											
											this.view.getModelo().limpiarFacturas();
											this.view.getModelo().agregarFactura(myFactura); 
											
											//se registrar los detalles de la factura anulada como una devolucion
											
											//se recorre los detalles de la factura
											for(int x=0;x<myFactura.getDetalles().size();x++){
												
													//se registra la devolucion
													boolean resu=this.devolucionDao.agregarDetalle(myFactura.getDetalles().get(x), myFactura.getIdFactura());
													try {
														/*this.view.setVisible(false);
														this.view.dispose();*/
														//AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Factura_Saint_Paul.jasper",myFactura.getIdFactura() );
														AbstractJasperReports.createReport(conexion.getPoolConexion().getConnection(), 7,myFactura.getIdFactura());
														//AbstractJasperReports.showViewer(view);
														AbstractJasperReports.showViewer(view);
														
														//myFactura.
													} catch (SQLException ee) {
														// TODO Auto-generated catch block
														ee.printStackTrace();
													}
				
											}
									
										}
										//JOptionPane.showMessageDialog(view, "Usuario Valido");
									}else{
										JOptionPane.showMessageDialog(view, "Usuario Invalido");
									}
								}
								
							}
					}
			}
			break;
			
		case "IMPRIMIR":
			if(verificarSelecion()){
				try {
					
					//AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Factura_Saint_Paul_Reimpresion.jasper",myFactura.getIdFactura() );
	    			AbstractJasperReports.createReport(conexion.getPoolConexion().getConnection(), 3, myFactura.getIdFactura());
	    			AbstractJasperReports.showViewer(this.view);
					//AbstractJasperReports.imprimierFactura();
					//this.view.getBtnImprimir().setEnabled(false);
					myFactura=null;
				} catch (SQLException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}
			}
			break;
			
		case "DEVOLUCION":
			if(verificarSelecion()){
				myFactura.setDetalles(detallesDao.getDetallesFactura(myFactura.getIdFactura()));
				ViewFacturaDevolucion viewDevolucion=new ViewFacturaDevolucion(view);
				CtlDevoluciones ctlDevolucion=new CtlDevoluciones(viewDevolucion,conexion);
				ctlDevolucion.actualizarFactura(myFactura);
				viewDevolucion.dispose();
				viewDevolucion=null;
				ctlDevolucion=null;
			}
			
			break;
			
			
		case "NEXT":
			view.getModelo().netPag();
			cargarTabla(myFacturaDao.todasfacturas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			
			view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
			break;
		case "LAST":
			view.getModelo().lastPag();
			cargarTabla(myFacturaDao.todasfacturas(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			
			view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
			break;
		}//fin del witch

	}
	
	public boolean verificarSelecion(){
		//fsdf
		boolean resul=false;
		
		if(view.getTabla().getSelectedRow()>=0){
			this.filaPulsada=view.getTabla().getSelectedRow();
			resul=true;
		}else{
			JOptionPane.showMessageDialog(view,"No seleccion una fila. Debe Selecionar una fila primero","Error",JOptionPane.ERROR_MESSAGE);
		}
		return resul;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub facturasPorId
		
	}

}
