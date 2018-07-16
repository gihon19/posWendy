package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.AbstractJasperReports;
import modelo.Conexion;
import modelo.Empleado;
import modelo.SalidaCaja;
import modelo.dao.DetalleFacturaDao;
import modelo.dao.KardexDao;
import modelo.dao.SalidaCajaDao;
import view.ViewCrearArticulo;
import view.ViewFiltroSalidas;
import view.ViewListaSalidas;

public class CtlSalidasListas implements ActionListener, MouseListener {
	private ViewListaSalidas view;
	private Conexion conexion;
	
	private SalidaCajaDao myDao;
	private SalidaCaja mySalida;
	
	private int filaPulsada;
	
	
	public CtlSalidasListas(ViewListaSalidas v,Conexion conn){
		view=v;
		conexion=conn;
		view.concetarControlador(this);
		
		mySalida=new SalidaCaja();
		
		myDao=new SalidaCajaDao(conexion);
		
		
		
		cargarTabla(myDao.todos(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
		
		view.setVisible(true);
		
	}
	
	private void cargarTabla(List<SalidaCaja> salidas) {
		// TODO Auto-generated method stub
		view.getModelo().limpiar();
		if(salidas!=null){
		for(int x=0;x<salidas.size();x++)
			view.getModelo().agregar(salidas.get(x));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		//se recoge el comando programado en el boton que se hizo click
				String comando=e.getActionCommand();
				

				
				
				switch(comando){
				case "BUSCAR":
					//si se seleciono el boton ID
					if(this.view.getRdbtnId().isSelected()){
						
						
						this.mySalida=myDao.bucarPorId(Integer.parseInt(view.getTxtBuscar().getText()));
						if(mySalida!=null){
							view.getModelo().limpiar();
							view.getModelo().agregar(mySalida);
						}
						//myArticulo=myArticuloDao.buscarArticulo(Integer.parseInt(this.view.getTxtBuscar().getText()));
				/*		myArticulo=myArticuloDao.buscarArticuloBarraCod(this.view.getTxtBuscar().getText());
						if(myArticulo!=null){												
							this.view.getModelo().limpiarArticulos();
							this.view.getModelo().agregarArticulo(myArticulo);
						}else{
							JOptionPane.showMessageDialog(view, "No se encuentro el articulo");
						}*/
					} 
					
					if(this.view.getRdbtnEmpleado().isSelected()){
						cargarTabla(myDao.buscarPorEmpleado(this.view.getTxtBuscar().getText()));
					}
					
				/*	if(this.view.getRdbtnArticulo().isSelected()){ //si esta selecionado la busqueda por nombre	
						
						cargarTabla(myDao.todos(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				        
						}
					if(this.view.getRdbtnMarca().isSelected()){  
						cargarTabla(myArticuloDao.buscarArticuloMarca(this.view.getTxtBuscar().getText()));
						}*/
					
					if(this.view.getRdbtnTodos().isSelected()){  
						cargarTabla(myDao.todos(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				//		this.view.getTxtBuscar().setText("");
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
				case "ESCRIBIR":
					view.setTamanioVentana(1);
					break;
				case "TODOS":
					
					view.getModelo().limpiar();
					view.getTxtBuscar().setText("");
					view.getTxtBuscar().setRequestFocusEnabled(true);
					cargarTabla(myDao.todos(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				break;
				case "REPORTE":
						ViewFiltroSalidas vFiltroSalidas=new ViewFiltroSalidas(view);
						CtlFiltroRepSalidas cFiltroSalidas=new CtlFiltroRepSalidas(vFiltroSalidas,conexion);
						
					break;
					
		
							
						
			}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		//Recoger qu� fila se ha pulsadao en la tabla
        filaPulsada = this.view.getTabla().getSelectedRow();
        
      //si fue doble click mostrar modificar
    	if (e.getClickCount() == 2) {
    		
    		
    		this.mySalida=view.getModelo().getSalida(filaPulsada);
    		try {
				
    			//AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Cierre_Caja_Saint_Paul.jasper",1 );
				AbstractJasperReports.createReportSalidaCaja(conexion.getPoolConexion().getConnection(), mySalida.getCodigoSalida());
				
				//AbstractJasperReports.Imprimir2();
				//JOptionPane.showMessageDialog(view, "Se realizo el corte correctamente.");
				
				//view.setVisible(false);
				//AbstractJasperReports.imprimierFactura();
				AbstractJasperReports.showViewer(view);
				//AbstractJasperReports.imprimierFactura();
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
