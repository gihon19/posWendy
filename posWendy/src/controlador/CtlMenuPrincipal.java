package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import modelo.AbstractJasperReports;
import modelo.dao.ArticuloDao;
import modelo.dao.CierreCajaDao;
import modelo.Conexion;
import view.ViewAgregarCompras;
import view.ViewCxCPagos;
import view.ViewFacturar;
import view.ViewFacturas;
import view.ViewFiltroComisiones;
import view.ViewFiltroReportDei;
import view.ViewListaArticulo;
import view.ViewListaCierresCaja;
import view.ViewListaClientes;
import view.ViewListaEmpleados;
import view.ViewListaCotizacion;
import view.ViewListaCuentaBancos;
import view.ViewListaFacturasCompra;
import view.ViewListaCategorias;
import view.ViewListaPagos;
import view.ViewListaPagosProveedores;
import view.ViewListaPrecioProgramar;
import view.ViewListaProveedor;
import view.ViewListaRequisiciones;
import view.ViewListaSalidas;
import view.ViewListaUsuarios;
import view.ViewMenuPrincipal;
import view.ViewPagoProveedor;
import view.ViewRequisicion;

public class CtlMenuPrincipal implements ActionListener {
	
	public ViewMenuPrincipal view;
	public Conexion conexion=null;
	private ArticuloDao myArticuloDao=null;
	
	
	
	public CtlMenuPrincipal(ViewMenuPrincipal view, Conexion conn){
		conexion=conn;
		this.view=view;
		myArticuloDao=new ArticuloDao(conexion);
		
		setAlertaExistencia();
		
		view.getLblUserName().setText(conexion.getUsuarioLogin().getUser());
		//view.setMaximumSize(maximumSize);
		
		
	}
	private void setAlertaExistencia() {
		// TODO Auto-generated method stub
		view.getBtnAlertaExistencia().setText(myArticuloDao.getAlertaExistencia()+" articulos con poca existencia");
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		String comando=e.getActionCommand();
		
		JDialog.setDefaultLookAndFeelDecorated(true);
		JFrame.setDefaultLookAndFeelDecorated(true);
		switch(comando){
		
		
		
		case "ALERTAEXISTENCIAS":
			try {
				//AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Cierre_Caja_Saint_Paul.jasper",1 );
				AbstractJasperReports.createReportAlertaExistencia(conexion.getPoolConexion().getConnection(), 4);
				
				//this.view.setModal(false);
				//AbstractJasperReports.imprimierFactura();
				AbstractJasperReports.showViewer(this.view);
				
				
			} catch (SQLException ee) {
				// TODO Auto-generated catch block
				ee.printStackTrace();
			}
			break;
	
		
			case "CUENTASBANCOS":
				ViewListaCuentaBancos vCuentasBancos=new ViewListaCuentaBancos(view);
				CtlCuentasBancosLista cCuentasBancos=new CtlCuentasBancosLista(vCuentasBancos,conexion);
				vCuentasBancos.setVisible(true);
				break;
		
				case "COTIZACIONES":
					ViewListaCotizacion vCotizaciones=new ViewListaCotizacion(view);
					CtlCotizacionLista cCotizaciones=new CtlCotizacionLista(vCotizaciones,conexion);
					vCotizaciones.setVisible(true);
					break;
		
		
			case "PAGOPROVEEDORES":
				ViewListaPagosProveedores vPagosProveedores= new ViewListaPagosProveedores(view);
				CtlPagosProveedoresLista cPagoProveedores=new CtlPagosProveedoresLista(vPagosProveedores,conexion);
				
				vPagosProveedores.dispose();
				cPagoProveedores=null;
				/*
					ViewPagoProveedor vPagoProveedores=new ViewPagoProveedor(view);
					CtlPagoProveedor cPagoProveedores=new CtlPagoProveedor(vPagoProveedores,conexion);
					vPagoProveedores.dispose();
					cPagoProveedores=null;*/
				break;
				
			case "REQUISICIONES":
				ViewListaRequisiciones viewRequiLista=new ViewListaRequisiciones(view);
				CtlRequisicionesLista ctlRequiLista=new CtlRequisicionesLista(viewRequiLista,conexion);
				viewRequiLista.dispose();
				ctlRequiLista=null;
			break;
	
			case "CERRARFACTURACION":
				try {
					//AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Cierre_Caja_Saint_Paul.jasper",1 );
					AbstractJasperReports.createReport(conexion.getPoolConexion().getConnection(), 4, 0);
					
					//this.view.setModal(false);
					//AbstractJasperReports.imprimierFactura();
					AbstractJasperReports.showViewer(this.view);
					
					CierreCajaDao cierre=new CierreCajaDao(conexion);
					
					if(cierre.registrarCierre()){
						JOptionPane.showMessageDialog(view, "Se creo el cierre de caja");
					}
					
				} catch (SQLException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}
				break;
			case "PROVEEDORES":
				ViewListaProveedor viewListaProveedor=new ViewListaProveedor(view);
				CtlProveedorLista ctlProveedor=new CtlProveedorLista(viewListaProveedor,conexion);
				viewListaProveedor.dispose();
				ctlProveedor=null;
				break;
			case "ARTICULOS":
				ViewListaArticulo viewListaArticulo=new ViewListaArticulo(view);
				CtlArticuloLista ctlArticulo =new CtlArticuloLista(viewListaArticulo,conexion);
				viewListaArticulo.dispose();
				ctlArticulo=null;
				
				break;
			case "AGREGARCOMPRAS":
				ViewAgregarCompras viewAgregarCompras= new ViewAgregarCompras(this.view);
				CtlAgregarCompras ctlAgregarCompra=new CtlAgregarCompras(viewAgregarCompras,conexion);
				
				
				viewAgregarCompras.dispose();
				viewAgregarCompras=null;
				ctlAgregarCompra=null;
				break;
			case "FACTURAR":
				
				/*ViewListaFactura vistaFacturars=new ViewListaFactura(this.view);
				CtlFacturaLista ctlFacturas=new CtlFacturaLista(vistaFacturars,conexion );
				vistaFacturars.dispose();
				ctlFacturas=null;*/
			
				ViewFacturar vistaFacturar=new ViewFacturar(this.view);
				vistaFacturar.pack();
				CtlFacturar ctlFacturar=new CtlFacturar(vistaFacturar,conexion );
				vistaFacturar.setVisible(true);
		
				
				break;
			case "CATEGORIAS":
				//se crea la lista de categorias
				ViewListaCategorias vlCategorias=new ViewListaCategorias(this.view);
				
				// se crea el control para la view lista marcas
				CtlCategoriaLista ctlListaCategorias =new CtlCategoriaLista(vlCategorias,conexion); 
				
				vlCategorias.dispose();
				ctlListaCategorias=null;
				
				break;
			case "CLIENTES":
				ViewListaClientes viewClientes=new ViewListaClientes(view);
				CtlClienteLista  ctlClientes=new CtlClienteLista(viewClientes,conexion);
				viewClientes.dispose();
				ctlClientes=null;
				break;
			case "BUSCARFACTURAS":
				
				ViewFacturas viewBuscarFacturas = new ViewFacturas(this.view);
				CtlFacturas cltBuscarFacturas= new CtlFacturas(viewBuscarFacturas,conexion);
				viewBuscarFacturas.dispose();
				cltBuscarFacturas=null;
				break;
				
			case "LISTAFACTURASCOMPRA":
				ViewListaFacturasCompra viewFacturasCompra=new ViewListaFacturasCompra(this.view);
				CtlFacturasCompra ctlFacturasCompra=new CtlFacturasCompra(viewFacturasCompra,conexion);
				viewFacturasCompra.dispose();
				viewFacturasCompra=null;
				ctlFacturasCompra=null;
				break;
				
			case "PAGOCLIENTES":
				ViewCxCPagos viewPagoClientes=new ViewCxCPagos(view);
				CtlFacturaPagos ctlPagoCleintes=new CtlFacturaPagos(viewPagoClientes,conexion);
				viewPagoClientes.dispose();
				viewPagoClientes=null;
				ctlPagoCleintes=null;
				break;
			case "LISTAPAGOS":
				
				ViewListaPagos viewListaPagos=new ViewListaPagos(view);
				CtlPagoLista ctlPagoLista =new CtlPagoLista(viewListaPagos, conexion);
				viewListaPagos.dispose();
				//viewListaPagos=null;
				//ctlPagoLista=null;
				break;
			case "PROGRAMARPRECIOS":
				ViewListaPrecioProgramar viewProgramarPrecio=new ViewListaPrecioProgramar(view);
				CtlProgramarPrecios ctlProgramarPrecio=new CtlProgramarPrecios(viewProgramarPrecio, conexion);
				viewProgramarPrecio.dispose();
				viewProgramarPrecio=null;
				ctlProgramarPrecio=null;
				break;
			case "R_DEI":
				ViewFiltroReportDei viewFiltroDei=new ViewFiltroReportDei(view);
				CtlFiltroRepDei ctlFiltroDei=new CtlFiltroRepDei(viewFiltroDei,conexion);
				break;
				
			case "USUARIOS":
				
				ViewListaUsuarios viewListaUsuarios=new ViewListaUsuarios(view);
				CtlUsuariosLista ctlUsuarios=new CtlUsuariosLista(viewListaUsuarios,conexion);
				viewListaUsuarios.dispose();
				viewListaUsuarios=null;
				ctlUsuarios=null;
				/*ViewCrearUsuario viewCrearUsuario=new ViewCrearUsuario(view);
				CtlUsuario ctlUsuario=new CtlUsuario(viewCrearUsuario, conexion);
				viewCrearUsuario.setVisible(true);*/
				
				break;
				
			case "INVENTARIO":
				try {
					//AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Cierre_Caja_Saint_Paul.jasper",1 );
					AbstractJasperReports.createReportInventario(conexion.getPoolConexion().getConnection(), conexion.getUsuarioLogin().getUser());
					
					//this.view.setModal(false);
					//AbstractJasperReports.imprimierFactura();
					AbstractJasperReports.showViewer(this.view);
					
					
				} catch (SQLException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}
				break;
				
				
			case "CIERRES_CAJA":
				
				ViewListaCierresCaja viewCierres=new ViewListaCierresCaja(view);
				CtlCierresCajaLista ctlCierres=new CtlCierresCajaLista(viewCierres,conexion);
				
				viewCierres.dispose();
				viewCierres=null;
				ctlCierres=null;
				break;
				
			case "EMPLEADOS":
				
				ViewListaEmpleados viewEmpleados=new ViewListaEmpleados(view);
				CtlEmpleadosLista ctlListaEmpleados=new CtlEmpleadosLista(viewEmpleados,conexion);
				
				viewEmpleados.dispose();
				viewEmpleados=null;
				ctlListaEmpleados=null;
				
				break;
			case "COMISIONES":
				ViewFiltroComisiones viewComisiones=new ViewFiltroComisiones(view);
				CtlFiltroRepComisiones ctlComisiones= new CtlFiltroRepComisiones(viewComisiones,conexion);
				viewComisiones.dispose();
				viewComisiones=null;
				ctlComisiones=null;
				
				break;
				
			case "SALIDASCAJAS":
				
				ViewListaSalidas viewSalidas=new ViewListaSalidas(view);
				CtlSalidasListas ctlSalidas=new CtlSalidasListas(viewSalidas,conexion);
				break;
		}
		
	}

}
