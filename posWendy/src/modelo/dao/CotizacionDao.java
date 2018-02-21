package modelo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.Cliente;
import modelo.Conexion;
import modelo.Cotizacion;
import modelo.Factura;
import modelo.NumberToLetterConverter;

public class CotizacionDao {
	
	
	private PreparedStatement getFecha=null;
	private Connection conexionBD=null;
	private Conexion conexion;
	private PreparedStatement agregarFactura=null;
	private PreparedStatement seleccionarFacturasPendientes=null;
	private PreparedStatement seleccionarFacturas=null;
	private PreparedStatement elimiarTem = null;
	private PreparedStatement actualizarTem = null;
	private PreparedStatement actualizarFactura = null;
	
	private DetallesCotizacioDao detallesDao=null;
	private ClienteDao myClienteDao=null;
	private CuentaPorCobrarDao myCuentaCobrarDao=null;
	
	private Integer idFacturaGuardada=null;

	public CotizacionDao(Conexion conn) {
		// TODO Auto-generated constructor stub
		
		conexion =conn;
		detallesDao=new DetallesCotizacioDao(conexion);
		myClienteDao=new ClienteDao(conexion);
		myCuentaCobrarDao=new CuentaPorCobrarDao(conexion);
	}

	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para agreagar facturas>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public boolean registrarCotizacion(Factura myFactura){
		boolean resultado=false;
		ResultSet rs=null;
		
		Connection conn=null;
		//int idFactura=0;
		
		String sql= "INSERT INTO encabezado_cotizacion("
				+ "fecha,"
				+ "subtotal,"
				+ "impuesto,"
				+ "total,"
				+ "codigo_cliente,"
				+ "descuento,"
				+ "estado,"
				+ "observacion,"
				+ "isv18,"
				+ "usuario,"
				+ "total_letras,"
				+ "codigo,"
				+ "subtotal_excento,"
				+ "subtotal15,"
				+ "subtotal18,"
				+ "isvOtros)"
				+ " VALUES (now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try 
		{
			
			String nombreCliente=myFactura.getCliente().getNombre();//"Consumidor final";
			
			//si el cliente en escrito por el bombero
			if(myFactura.getCliente().getId()<0){
				myClienteDao.registrarCliente(myFactura.getCliente());
				myFactura.getCliente().setId(myClienteDao.getIdClienteRegistrado());
			}
			conn=conexion.getPoolConexion().getConnection();
			agregarFactura=conn.prepareStatement(sql);
			agregarFactura.setBigDecimal(1,myFactura.getSubTotal() );
			agregarFactura.setBigDecimal(2, myFactura.getTotalImpuesto());
			agregarFactura.setBigDecimal(3, myFactura.getTotal());
			agregarFactura.setInt(4, myFactura.getCliente().getId());
			agregarFactura.setBigDecimal(5, myFactura.getTotalDescuento());
			agregarFactura.setString(6, "ACT");
			agregarFactura.setString(7, myFactura.getObservacion());
			agregarFactura.setBigDecimal(8, myFactura.getTotalImpuesto18());
			agregarFactura.setString(9, conexion.getUsuarioLogin().getUser());
			agregarFactura.setString(10, NumberToLetterConverter.convertNumberToLetter(myFactura.getTotal().setScale(0, BigDecimal.ROUND_HALF_EVEN).doubleValue()));
			agregarFactura.setInt(11, myFactura.getCodigo());
			agregarFactura.setBigDecimal(12, myFactura.getSubTotalExcento());
			agregarFactura.setBigDecimal(13, myFactura.getSubTotal15());
			agregarFactura.setBigDecimal(14, myFactura.getSubTotal18());
			agregarFactura.setBigDecimal(15, myFactura.getTotalOtrosImpuesto1());
			
			
			//para relacionar el codigo de cai con cada factura
			//EmpresaDao empresa=new EmpresaDao(conexion);
			//agregarFactura.setInt(21, empresa.getIdCaiAct());
			
			
			
			
			agregarFactura.executeUpdate();//se guarda el encabezado de la factura
			rs=agregarFactura.getGeneratedKeys(); //obtengo las ultimas llaves generadas
			while(rs.next()){
				//idFactura=rs.getInt(1);
				idFacturaGuardada=rs.getInt(1);
				myFactura.setIdFactura(idFacturaGuardada);
				
			}
			
			//se guardan los detalles de la fatura
			for(int x=0;x<myFactura.getDetalles().size();x++){
				
				if(myFactura.getDetalles().get(x).getArticulo().getId()!=-1)
					detallesDao.agregarDetalle(myFactura.getDetalles().get(x), idFacturaGuardada);
			}
			resultado= true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
			//conexion.desconectar();
			resultado= false;
		}
		finally
		{
			try{
				if(rs != null) rs.close();
	            if(agregarFactura != null)agregarFactura.close();
	            if(conn != null) conn.close();
			} // fin de try
			catch ( SQLException excepcionSql )
			{
				excepcionSql.printStackTrace();
				//conexion.desconectar();
			} // fin de catch
		} // fin de finally
		
		
		
		
		return resultado;
	}

	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para seleccionar todos los articulos>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public List<Factura> getCotizaciones(){
		
		
	    Connection con = null;
		
		String sql="select * from v_encabezado_cotizacion";
	    //Statement stmt = null;
	   	List<Factura> facturas=new ArrayList<Factura>();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			seleccionarFacturasPendientes = con.prepareStatement(sql);
			//seleccionarFacturasPendientes.setString(1, conexion.getUsuarioLogin().getUser());
			res = seleccionarFacturasPendientes.executeQuery();
			
			while(res.next()){
				Factura unaFactura=new Factura();
				existe=true;
				unaFactura.setIdFactura(res.getInt("numero_cotizacion"));
				Cliente unCliente=new Cliente();//myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
				unCliente.setId(res.getInt("codigo_cliente"));
				unCliente.setNombre(res.getString("nombre_cliente"));
				unCliente.setRtn(res.getString("rtn"));
				
				unaFactura.setCliente(unCliente);
				
				unaFactura.setFecha(res.getString("fecha"));
				unaFactura.setSubTotal(res.getBigDecimal("subtotal"));
				unaFactura.setTotalImpuesto(res.getBigDecimal("impuesto"));
				unaFactura.setTotal(res.getBigDecimal("total"));
				//unaFactura.setEstado(res.getInt("estado_factura"));
				unaFactura.setTotalDescuento(res.getBigDecimal("descuento"));
				//unaFactura.setTipoFactura(res.getInt("tipo_factura"));
				unaFactura.setSubTotalExcento(res.getBigDecimal("subtotal_excento"));
				unaFactura.setSubTotal15(res.getBigDecimal("subtotal15"));
				unaFactura.setSubTotal18(res.getBigDecimal("subtotal18"));
				unaFactura.setTotalOtrosImpuesto(res.getBigDecimal("isvOtros"));
				
				unaFactura.setDetalles(detallesDao.detallesFacturaPendiente(unaFactura.getIdFactura()));
				
				
				facturas.add(unaFactura);
			 }
					
			} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error, no se conecto");
					System.out.println(e);
			}
		finally
		{
			try{
				
				if(res != null) res.close();
	            if(seleccionarFacturasPendientes != null)seleccionarFacturasPendientes.close();
	            if(con != null) con.close();
	            
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return facturas;
			}
			else return null;
		
	}

	public List<Factura> cotizacionesPorFecha(String fecha) {
		
		//se crear un referencia al pool de conexiones
		//DataSource ds = DBCPDataSourceFactory.getDataSource("mysql");
		
		
	    Connection con = null;
	    String sql="select * from v_encabezado_cotizacion where DATE_FORMAT(fecha1, '%d/%m/%Y')=?";
		/*String sql="SELECT "
				+ "encabezado_factura.numero_factura, "
				+ "DATE_FORMAT(encabezado_factura.fecha, '%d/%m/%Y') as fecha,"
				+ " encabezado_factura.subtotal, "
				+ "encabezado_factura.impuesto, "
				+ "encabezado_factura.total, "
				+ "encabezado_factura.codigo_cliente,"
				+ "encabezado_factura.codigo, "
				+ "encabezado_factura.estado_factura, "
				+ "encabezado_factura.isv18, "
				+ "encabezado_factura.tipo_factura, "
				+ "encabezado_factura.descuento,"
				+ "encabezado_factura.pago, "
				+ "encabezado_factura.usuario,"
				+ "encabezado_factura.estado_factura, "
				+ "encabezado_factura.agrega_kardex "
				+ "FROM encabezado_factura where DATE_FORMAT(encabezado_factura.fecha, '%d/%m/%Y')>=? and DATE_FORMAT(encabezado_factura.fecha, '%d/%m/%Y')<=?";
	    //Statement stmt = null;
*/	   	List<Factura> facturas=new ArrayList<Factura>();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			seleccionarFacturas = con.prepareStatement(sql);
			
			seleccionarFacturas.setString(1, fecha);
			//seleccionarFacturas.setString(2, fecha2);
			
			res = seleccionarFacturas.executeQuery();
			while(res.next()){
				Factura unaFactura=new Factura();
				existe=true;
				unaFactura.setIdFactura(res.getInt("numero_cotizacion"));
				Cliente unCliente=new Cliente();//myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
				unCliente.setId(res.getInt("codigo_cliente"));
				unCliente.setNombre(res.getString("nombre_cliente"));
				unCliente.setRtn(res.getString("rtn"));
				
				unaFactura.setCliente(unCliente);
				
				unaFactura.setFecha(res.getString("fecha"));
				unaFactura.setSubTotal(res.getBigDecimal("subtotal"));
				unaFactura.setTotalImpuesto(res.getBigDecimal("impuesto"));
				unaFactura.setTotal(res.getBigDecimal("total"));
				//unaFactura.setEstado(res.getInt("estado_factura"));
				unaFactura.setTotalDescuento(res.getBigDecimal("descuento"));
				//unaFactura.setTipoFactura(res.getInt("tipo_factura"));
				unaFactura.setSubTotalExcento(res.getBigDecimal("subtotal_excento"));
				unaFactura.setSubTotal15(res.getBigDecimal("subtotal15"));
				unaFactura.setSubTotal18(res.getBigDecimal("subtotal18"));
				unaFactura.setTotalOtrosImpuesto(res.getBigDecimal("isvOtros"));
				
				unaFactura.setDetalles(detallesDao.detallesFacturaPendiente(unaFactura.getIdFactura()));
				
				
				facturas.add(unaFactura);
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
	            if(seleccionarFacturas != null)seleccionarFacturas.close();
	            if(con != null) con.close();
	            
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return facturas;
			}
			else return null;
		
	}

	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para seleccionar las facturas por id>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public Factura cotizacionPorId(int id){
		
		//se crear un referencia al pool de conexiones
		//DataSource ds = DBCPDataSourceFactory.getDataSource("mysql");
		
		
	    Connection con = null;
	    
	    String sql="select * from v_encabezado_cotizacion where numero_cotizacion=?";
	    
		/*String sql="SELECT "
				+ "encabezado_factura.numero_factura, "
				+ "DATE_FORMAT(encabezado_factura.fecha, '%d/%m/%Y') as fecha,"
				+ " encabezado_factura.subtotal, "
				+ "encabezado_factura.impuesto, "
				+ "encabezado_factura.total, "
				+ "encabezado_factura.codigo_cliente,"
				+ "encabezado_factura.codigo, "
				+ "encabezado_factura.estado_factura, "
				+ "encabezado_factura.isv18, "
				+ "encabezado_factura.tipo_factura, "
				+ "encabezado_factura.descuento,"
				+ "encabezado_factura.pago, "
				+ "encabezado_factura.usuario,"
				+ "encabezado_factura.estado_factura, "
				+ "encabezado_factura.agrega_kardex "
				+ "FROM encabezado_factura where numero_factura=?";*/
	    //Statement stmt = null;
		Factura unaFactura=new Factura();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			seleccionarFacturas = con.prepareStatement(sql);
			seleccionarFacturas.setInt(1, id);
			
			res = seleccionarFacturas.executeQuery();
			while(res.next()){
				
				existe=true;
				unaFactura.setIdFactura(res.getInt("numero_cotizacion"));
				Cliente unCliente=new Cliente();//myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
				unCliente.setId(res.getInt("codigo_cliente"));
				unCliente.setNombre(res.getString("nombre_cliente"));
				unCliente.setRtn(res.getString("rtn"));
				
				unaFactura.setCliente(unCliente);
				
				unaFactura.setFecha(res.getString("fecha"));
				unaFactura.setSubTotal(res.getBigDecimal("subtotal"));
				unaFactura.setTotalImpuesto(res.getBigDecimal("impuesto"));
				unaFactura.setTotal(res.getBigDecimal("total"));
				//unaFactura.setEstado(res.getInt("estado_factura"));
				unaFactura.setTotalDescuento(res.getBigDecimal("descuento"));
				//unaFactura.setTipoFactura(res.getInt("tipo_factura"));
				unaFactura.setSubTotalExcento(res.getBigDecimal("subtotal_excento"));
				unaFactura.setSubTotal15(res.getBigDecimal("subtotal15"));
				unaFactura.setSubTotal18(res.getBigDecimal("subtotal18"));
				unaFactura.setTotalOtrosImpuesto(res.getBigDecimal("isvOtros"));
				
				unaFactura.setDetalles(detallesDao.detallesFacturaPendiente(unaFactura.getIdFactura()));
				
				
			
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
	            if(seleccionarFacturas != null)seleccionarFacturas.close();
	            if(con != null) con.close();
	            
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return unaFactura;
			}
			else return null;
		
	}

	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para Eliminar los proveedores>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public List<Factura> cotizacionesDelCliente(String myCliente) {
	    Connection con = null;
	    
	    String sql="select * from v_encabezado_cotizacion where nombre_cliente LIKE ?";
		/*String sql="SELECT "
				+ "encabezado_factura.numero_factura, "
				+ "DATE_FORMAT(encabezado_factura.fecha, '%d/%m/%Y') as fecha,"
				+ " encabezado_factura.subtotal, "
				+ "encabezado_factura.impuesto, "
				+ "encabezado_factura.total, "
				+ "encabezado_factura.codigo_cliente,"
				+ "encabezado_factura.codigo, "
				+ "encabezado_factura.estado_factura, "
				+ "encabezado_factura.isv18, "
				+ "encabezado_factura.tipo_factura, "
				+ "encabezado_factura.descuento,"
				+ "encabezado_factura.pago, "
				+ "encabezado_factura.usuario,"
				+ "encabezado_factura.estado_factura, "
				+ "encabezado_factura.agrega_kardex "
				+ " FROM encabezado_factura "
				+ "where encabezado_factura.codigo_cliente=? "
				+ " and "
				+ " encabezado_factura.estado_factura = 'ACT' "
				+ " and "
				+ " encabezado_factura.estado_pago = 0;";
	    //Statement stmt = null;
*/	   	List<Factura> facturas=new ArrayList<Factura>();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			seleccionarFacturas = con.prepareStatement(sql);
			
			//seleccionarFacturas.setInt(1, myCliente.getId());
			seleccionarFacturas.setString(1, "%" + myCliente + "%");
			
			res = seleccionarFacturas.executeQuery();
			while(res.next()){
				Factura unaFactura=new Factura();
				existe=true;
				unaFactura.setIdFactura(res.getInt("numero_cotizacion"));
				Cliente unCliente=new Cliente();//myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
				unCliente.setId(res.getInt("codigo_cliente"));
				unCliente.setNombre(res.getString("nombre_cliente"));
				unCliente.setRtn(res.getString("rtn"));
				
				unaFactura.setCliente(unCliente);
				
				unaFactura.setFecha(res.getString("fecha"));
				unaFactura.setSubTotal(res.getBigDecimal("subtotal"));
				unaFactura.setTotalImpuesto(res.getBigDecimal("impuesto"));
				unaFactura.setTotal(res.getBigDecimal("total"));
				//unaFactura.setEstado(res.getInt("estado_factura"));
				unaFactura.setTotalDescuento(res.getBigDecimal("descuento"));
				//unaFactura.setTipoFactura(res.getInt("tipo_factura"));
				unaFactura.setSubTotalExcento(res.getBigDecimal("subtotal_excento"));
				unaFactura.setSubTotal15(res.getBigDecimal("subtotal15"));
				unaFactura.setSubTotal18(res.getBigDecimal("subtotal18"));
				unaFactura.setTotalOtrosImpuesto(res.getBigDecimal("isvOtros"));
				
				unaFactura.setDetalles(detallesDao.detallesFacturaPendiente(unaFactura.getIdFactura()));
				
				
				facturas.add(unaFactura);
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
	            if(seleccionarFacturas != null)seleccionarFacturas.close();
	            if(con != null) con.close();
	            
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return facturas;
			}
			else return null;}

}
