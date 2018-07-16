package modelo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.CierreCaja;
import modelo.Cliente;
import modelo.Conexion;
import modelo.CuentasBancos;
import modelo.Factura;
import modelo.Proveedor;
import modelo.ReciboPago;
import modelo.ReciboPagoProveedor;

public class ReciboPagoDao {
	
	private Conexion conexion;
	private PreparedStatement insertar=null;
	private PreparedStatement insertar2=null;
	private PreparedStatement todos=null;
	private PreparedStatement buscar=null;
	private PreparedStatement actualizar=null;
	private PreparedStatement eliminar=null;
	private CuentaPorCobrarDao myCuentaCobrarDao=null;
	private CuentaPorPagarDao myCuentaPagarDao=null;
	private ClienteDao myClienteDao=null;
	private ProveedorDao proveedorDao=null;
	public int idUltimoRecibo=0;
	private PreparedStatement saldoCliente=null;

	public ReciboPagoDao(Conexion conn) {
		conexion=conn;
		myClienteDao=new ClienteDao(conexion);
		myCuentaCobrarDao=new CuentaPorCobrarDao(conexion);
		myCuentaPagarDao=new CuentaPorPagarDao(conexion);
		proveedorDao= new ProveedorDao(conexion);
	}
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para agreagar Articulo>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public boolean registrar(ReciboPago myRecibo)
	{
		//JOptionPane.showConfirmDialog(null, myCliente);
		int resultado=0;
		ResultSet rs=null;
		Connection con = null;
		
		try 
		{
			con = conexion.getPoolConexion().getConnection();
			
			myRecibo.setCliente(this.myClienteDao.buscarCliente(myRecibo.getCliente().getId()));
			
			//se establece los saldo en 0
			myRecibo.setSaldos0();
			
			//el salado anterio
			myRecibo.setSaldoAnterior(myClienteDao.getSaldoCliente(myRecibo.getCliente().getId()));
			
			//el saldo actural
			myRecibo.setSaldo(myRecibo.getSaldoAnterior().subtract(myRecibo.getTotal()));
			
			//insertar=con.prepareStatement( "INSERT INTO recibo_pago(fecha,codigo_cliente,total_letras,total,concepto,usuario) VALUES (now(),?,?,?,?,?)");
			insertar=con.prepareStatement( "INSERT INTO recibo_pago(fecha,codigo_cliente,total_letras,total,concepto,usuario,saldo_anterio,saldo) VALUES (now(),?,?,?,?,?,?,?)");
			
			insertar.setInt(1, myRecibo.getCliente().getId());
			insertar.setString(2, myRecibo.getTotalLetras());
			insertar.setBigDecimal(3, myRecibo.getTotal());
			insertar.setString(4, myRecibo.getConcepto());
			insertar.setString(5, conexion.getUsuarioLogin().getUser());
			insertar.setBigDecimal(6, myRecibo.getSaldoAnterior().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			insertar.setBigDecimal(7, myRecibo.getSaldo().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						
			resultado=insertar.executeUpdate();
			
			rs=insertar.getGeneratedKeys(); //obtengo las ultimas llaves generadas
			while(rs.next()){
				//this.setIdClienteRegistrado(rs.getInt(1));
				myRecibo.setNoRecibo(rs.getInt(1));
				this.idUltimoRecibo=rs.getInt(1);
			}
			
			//se establece en el concepto en numero de recibo con que se pago
			String concepto=myRecibo.getConcepto();
			concepto=concepto+" con recibo no. "+myRecibo.getNoRecibo();
			myRecibo.setConcepto(concepto);
			
			this.myCuentaCobrarDao.reguistrarDebito(myRecibo);
			
			
			for(int x=0;x<myRecibo.getFacturas().size();x++){
				this.registrarFacturasPagadas(myRecibo.getFacturas().get(x).getIdFactura(), myRecibo.getNoRecibo());
			}
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			//conexion.desconectar();
            return false;
		}
		finally
		{
			try{
				if(rs!=null)rs.close();
				 if(insertar != null)insertar.close();
	              if(con != null) con.close();
			} // fin de try
			catch ( SQLException excepcionSql )
			{
				excepcionSql.printStackTrace();
				//conexion.desconectar();
			} // fin de catch
		} // fin de finally
	}
	
	public boolean registrarProveedor(ReciboPagoProveedor myReciboPago)
	{
		//JOptionPane.showMessageDialog(null, myReciboPago.toString());
		int resultado=0;
		ResultSet rs=null;
		Connection con = null;
		
		try 
		{
			con = conexion.getPoolConexion().getConnection();
			
			//ClienteDao clienteDao= new ClienteDao(conexion);
			
			
			//se establece los saldo en 0
			myReciboPago.setSaldos0();
			
			//el salado anterio
			myReciboPago.setSaldoAnterior(myReciboPago.getProveedor().getSaldo());
			
			//el saldo actural
			myReciboPago.setSaldo(myReciboPago.getSaldoAnterior().subtract(myReciboPago.getTotal()));
			
			//insertar=con.prepareStatement( "INSERT INTO recibo_pago(fecha,codigo_cliente,total_letras,total,concepto,usuario) VALUES (now(),?,?,?,?,?)");
			insertar=con.prepareStatement( "INSERT INTO recibo_pago_proveedores(fecha,codigo_proveedor,total_letras,total,concepto,usuario,saldo_anterio,saldo,codigo_tipo_pago) VALUES (now(),?,?,?,?,?,?,?,?)");
			
			
			insertar.setInt(1, myReciboPago.getProveedor().getId());
			insertar.setString(2, myReciboPago.getTotalLetras());
			insertar.setBigDecimal(3, myReciboPago.getTotal());
			insertar.setString(4, myReciboPago.getConcepto());
			insertar.setString(5, conexion.getUsuarioLogin().getUser());
			insertar.setBigDecimal(6, myReciboPago.getSaldoAnterior().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			insertar.setBigDecimal(7, myReciboPago.getSaldo().setScale(2, BigDecimal.ROUND_HALF_EVEN));
			insertar.setInt(8, myReciboPago.getFormaPago().getId());
						
			resultado=insertar.executeUpdate();
			
			rs=insertar.getGeneratedKeys(); //obtengo las ultimas llaves generadas
			while(rs.next()){
				//this.setIdClienteRegistrado(rs.getInt(1));
				myReciboPago.setNoRecibo(rs.getInt(1));
				this.idUltimoRecibo=rs.getInt(1);
			}
			
			//se establece en el concepto en numero de recibo con que se pago
			String concepto=myReciboPago.getConcepto();
			concepto=concepto+" con recibo no. "+myReciboPago.getNoRecibo();
			myReciboPago.setConcepto(concepto);
			
			//se registra el pago en la la tabla cuentas por pagar
			this.myCuentaPagarDao.reguistrarDebito(myReciboPago);//.reguistrarDebito(myRecibo);
			
	
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			//conexion.desconectar();
            return false;
		}
		finally
		{
			try{
				if(rs!=null)rs.close();
				 if(insertar != null)insertar.close();
	              if(con != null) con.close();
			} // fin de try
			catch ( SQLException excepcionSql )
			{
				excepcionSql.printStackTrace();
				//conexion.desconectar();
			} // fin de catch
		} // fin de finally
	}
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para agreagar Articulo>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public boolean registrarFacturasPagadas(int idFactura, int idRecibo)
	{
		//JOptionPane.showConfirmDialog(null, myCliente);
		int resultado=0;
		//ResultSet rs=null;
		Connection con = null;
		
		try 
		{
			con = conexion.getPoolConexion().getConnection();
			
			insertar2=con.prepareStatement( "INSERT INTO detalle_pago(no_recibo_pago,no_factura_pagada) VALUES (?,?)");
			
			insertar2.setInt(1,idRecibo );
			insertar2.setInt(2,idFactura);
			
						
			resultado=insertar2.executeUpdate();
			
					
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			//conexion.desconectar();
            return false;
		}
		finally
		{
			try{
				//if(rs!=null)rs.close();
				 if(insertar2 != null)insertar2.close();
	              if(con != null) con.close();
			} // fin de try
			catch ( SQLException excepcionSql )
			{
				excepcionSql.printStackTrace();
				//conexion.desconectar();
			} // fin de catch
		} // fin de finally
	}
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para seleccionar todos los articulos>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public List<ReciboPago> todosRecibo(int limInf,int limSupe){
		
		//se crear un referencia al pool de conexiones
		//DataSource ds = DBCPDataSourceFactory.getDataSource("mysql");
		
		
        Connection con = null;
        
    	String sql="select DATE_FORMAT(fecha, '%d/%m/%Y') as fecha,no_recibo,codigo_cliente,total_letras,total,concepto,usuario,codigo_cliente,nombre_cliente from v_recibo_pago_cuenta ORDER BY no_recibo DESC  LIMIT ?,?;";
        //Statement stmt = null;
       	List<ReciboPago> pagos=new ArrayList<ReciboPago>();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			todos = con.prepareStatement(sql);
			todos.setInt(1, limInf);
			todos.setInt(2, limSupe);
			
			res = todos.executeQuery();
			while(res.next()){
				ReciboPago un=new ReciboPago();
				existe=true;
				un.setFecha(res.getString("fecha"));
				un.setConcepto(res.getString("concepto"));
				un.setNoRecibo(res.getInt("no_recibo"));
				un.setTotal(res.getBigDecimal("total"));
				
				Cliente unCliente=new Cliente();//myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
				
				unCliente.setId(res.getInt("codigo_cliente"));
				unCliente.setNombre(res.getString("nombre_cliente"));
				
				un.setCliente(unCliente);
				
				
				pagos.add(un);
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(todos != null)todos.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return pagos;
			}
			else return null;
		
	}
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para seleccionar todos los articulos>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public ReciboPago reciboPorNo(int noRecibo){
		
		//se crear un referencia al pool de conexiones
		//DataSource ds = DBCPDataSourceFactory.getDataSource("mysql");
		
		
        Connection con = null;
        
    	String sql="select DATE_FORMAT(recibo_pago.fecha, '%d/%m/%Y') as fecha,"
    			+ " no_recibo, "
    			+ "codigo_cliente, "
    			+ "total_letras, "
    			+ "total, "
    			+ " concepto,"
    			+ " usuario "
    			+ " from "
    			+ " recibo_pago "
    			+ " WHERE "
    			+ " recibo_pago.no_recibo =? "
    			+ " ORDER BY recibo_pago.no_recibo DESC";
        //Statement stmt = null;
       	ReciboPago un=new ReciboPago();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			todos = con.prepareStatement(sql);
			
			todos.setInt(1, noRecibo);
			
			res = todos.executeQuery();
			while(res.next()){
				
				existe=true;
				un.setFecha(res.getString("fecha"));
				un.setConcepto(res.getString("concepto"));
				un.setNoRecibo(res.getInt("no_recibo"));
				un.setTotal(res.getBigDecimal("total"));
				
				Cliente unCliente=myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
				
				un.setCliente(unCliente);
				
				
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(todos != null)todos.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return un;
			}
			else return null;
		
	}
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para seleccionar los recibo por fecha>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public List<ReciboPago>  reciboPorFecha(String fecha1, String fecha2){//se crear un referencia al pool de conexiones
		//DataSource ds = DBCPDataSourceFactory.getDataSource("mysql");
		
		
        Connection con = null;
        
        String sql="select DATE_FORMAT(recibo_pago.fecha, '%d/%m/%Y') as fecha2,"
    			+ " no_recibo,"
    			+ "fecha as fecha, "
    			+ "codigo_cliente, "
    			+ "total_letras, "
    			+ "total, "
    			+ " concepto,"
    			+ " usuario "
    			+ " from "
    			+ " recibo_pago "
    			+ " where fecha BETWEEN ? and ? "
    			+ " ORDER BY recibo_pago.no_recibo DESC";
        //Statement stmt = null;
       	List<ReciboPago> pagos=new ArrayList<ReciboPago>();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			
			todos = con.prepareStatement(sql);
			todos.setString(1, fecha1);
			todos.setString(2, fecha2);
			res = todos.executeQuery();
			while(res.next()){
				ReciboPago un=new ReciboPago();
				existe=true;
				un.setFecha(res.getString("fecha2"));
				un.setConcepto(res.getString("concepto"));
				un.setNoRecibo(res.getInt("no_recibo"));
				un.setTotal(res.getBigDecimal("total"));
				
				Cliente unCliente=myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
				
				un.setCliente(unCliente);
				
				
				pagos.add(un);
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(todos != null)todos.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return pagos;
			}
			else return null;
		}
	
	public void calcularTotalPagosCierre(CierreCaja unaCierre) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		
		
		Connection con = null;
		ResultSet res=null;
		
		//se consiguie el ultima pago que realizo el usuario
		ReciboPagoProveedor ultimo=this.getPagoUltimoUser();
		//se establece la ultima pago del usuaurio
		unaCierre.setNoPagoFinal(ultimo.getNoRecibo());
		
		
		//JOptionPane.showMessageDialog(null,"aqui "+unaCierre.toString());
		
		
		String sql="SELECT	ifnull(sum(recibo_pago_proveedores.total), 0) AS cantidad FROM recibo_pago_proveedores WHERE recibo_pago_proveedores.no_recibo >= ? AND recibo_pago_proveedores.no_recibo <= ?  AND recibo_pago_proveedores.usuario =?";
		try {
			con = conexion.getPoolConexion().getConnection();
			
			insertar = con.prepareStatement(sql);
			
			insertar.setInt(1, unaCierre.getNoPagoInicial());
			insertar.setInt(2, unaCierre.getNoPagoFinal());
			insertar.setString(3, unaCierre.getUsuario());
			
			
			//seleccionarCierre.setString(1, conexion.getUsuarioLogin().getUser());
			res = insertar.executeQuery();
			while(res.next()){
				
				
				
				//si existe alguna salida se suma y se establecen en el cierre
				unaCierre.setTotalPago(res.getBigDecimal("cantidad"));
	
			
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(insertar != null)insertar.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
	
		
	
		
	}
	
	public void calcularTotalCierre(CierreCaja unaCierre) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		
		
		Connection con = null;
		ResultSet res=null;
		
		//se consiguie el ultima salida que realizo el usuario
		ReciboPago ultimo=this.getCobroUltimoUser();
		//se establece la ultima salida del usuaurio
		unaCierre.setNoCobroFinal(ultimo.getNoRecibo());
		
		
		//JOptionPane.showMessageDialog(null,"aqui "+unaCierre.toString());
		
		
		String sql="SELECT	ifnull(sum(recibo_pago.total), 0) AS cantidad FROM recibo_pago WHERE recibo_pago.no_recibo >= ? AND recibo_pago.no_recibo <= ?  AND recibo_pago.usuario =?";
		try {
			con = conexion.getPoolConexion().getConnection();
			
			insertar = con.prepareStatement(sql);
			
			insertar.setInt(1, unaCierre.getNoCobroInicial());
			insertar.setInt(2, unaCierre.getNoCobroFinal());
			insertar.setString(3, unaCierre.getUsuario());
			
			
			//seleccionarCierre.setString(1, conexion.getUsuarioLogin().getUser());
			res = insertar.executeQuery();
			while(res.next()){
				
				
				
				//si existe alguna salida se suma y se establecen en el cierre
				unaCierre.setTotalCobro(res.getBigDecimal("cantidad"));
	
			
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(insertar != null)insertar.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
	
		
	
		
	}

	private ReciboPago getCobroUltimoUser() {
		
		//se crear un referencia al pool de conexiones
		//DataSource ds = DBCPDataSourceFactory.getDataSource("mysql");
		
		
        Connection con = null;
        
    	//String sql="select * from cierre where usuario = ?";
    	
    	String sql2="SELECT * FROM v_recibo_pago_cuenta WHERE usuario=? ORDER BY no_recibo DESC LIMIT 1";
    	//dfsafa
        //Statement stmt = null;
    	ReciboPago unRecibo=new ReciboPago();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			insertar = con.prepareStatement(sql2);
			
			insertar.setString(1, conexion.getUsuarioLogin().getUser());
			res = insertar.executeQuery();
			while(res.next()){
				
				existe=true;
				
				unRecibo.setFecha(res.getString("fecha"));
				unRecibo.setConcepto(res.getString("concepto"));
				unRecibo.setNoRecibo(res.getInt("no_recibo"));
				unRecibo.setTotal(res.getBigDecimal("total"));
				unRecibo.setSaldoAnterior(res.getBigDecimal("saldo_anterio"));
				unRecibo.setSaldo(res.getBigDecimal("saldo"));
				
				Cliente unCliente=new Cliente();//myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
				
				unCliente.setId(res.getInt("codigo_cliente"));
				unCliente.setNombre(res.getString("nombre_cliente"));
				
				unRecibo.setCliente(unCliente);
				
				//unRecibo.setCliente(unCliente);
			
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(insertar != null)insertar.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		return unRecibo;
			/*if (existe) {
				return unaCierre;
			}
			else return null;*/
		
	}
	private ReciboPagoProveedor getPagoUltimoUser() {
		
		//se crear un referencia al pool de conexiones
		//DataSource ds = DBCPDataSourceFactory.getDataSource("mysql");
		
		
        Connection con = null;
        
    	//String sql="select * from cierre where usuario = ?";
    	
    	String sql2="SELECT * FROM v_recibo_pago_proveedor WHERE usuario=? ORDER BY no_recibo DESC LIMIT 1";
    	//dfsafa
        //Statement stmt = null;
    	ReciboPagoProveedor unRecibo=new ReciboPagoProveedor();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			insertar = con.prepareStatement(sql2);
			
			insertar.setString(1, conexion.getUsuarioLogin().getUser());
			res = insertar.executeQuery();
			while(res.next()){
				
				existe=true;
				
				unRecibo.setFecha(res.getString("fecha"));
				unRecibo.setConcepto(res.getString("concepto"));
				unRecibo.setNoRecibo(res.getInt("no_recibo"));
				unRecibo.setTotal(res.getBigDecimal("total"));
				unRecibo.setSaldoAnterior(res.getBigDecimal("saldo_anterio"));
				unRecibo.setSaldo(res.getBigDecimal("saldo"));
				
				Proveedor unProveedor=new Proveedor();//myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
				
				unProveedor.setId(res.getInt("codigo_proveedor"));
				unProveedor.setNombre(res.getString("nombre_proveedor"));
				
				unRecibo.setProveedor(unProveedor);
				
				//unRecibo.setCliente(unCliente);
			
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(insertar != null)insertar.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		return unRecibo;
			/*if (existe) {
				return unaCierre;
			}
			else return null;*/
		
	}

	public List<ReciboPagoProveedor> todosReciboProveedor(int limiteInferior, int limiteSuperior) {
		// TODO Auto-generated method stub

        Connection con = null;
        
    	String sql="select *, DATE_FORMAT(fecha, '%d/%m/%Y') as fecha2 from v_recibo_pago_proveedor ORDER BY no_recibo DESC  LIMIT ?,?;";
        //Statement stmt = null;
       	List<ReciboPagoProveedor> pagos=new ArrayList<ReciboPagoProveedor>();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			todos = con.prepareStatement(sql);
			todos.setInt(1, limiteInferior);
			todos.setInt(2, limiteSuperior);
			
			res = todos.executeQuery();
			while(res.next()){
				ReciboPagoProveedor un=new ReciboPagoProveedor();
				existe=true;
				un.setFecha(res.getString("fecha2"));
				un.setConcepto(res.getString("concepto"));
				un.setNoRecibo(res.getInt("no_recibo"));
				un.setTotal(res.getBigDecimal("total"));
				
				Proveedor unProveedor=new Proveedor();//myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
				
				unProveedor.setId(res.getInt("codigo_proveedor"));
				unProveedor.setNombre(res.getString("nombre_proveedor"));
				
				un.setProveedor(unProveedor);
				
				CuentasBancos unCuenta=new CuentasBancos();
				unCuenta.setId(res.getInt("codigo_tipo_pago"));
				unCuenta.setNombre(res.getString("forma_pago"));
				unCuenta.setNoCuenta(res.getString("no_cuenta"));
				unCuenta.setTipoCuenta(res.getString("tipo_cuenta"));
				un.setFormaPago(unCuenta);
				
				
				pagos.add(un);
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(todos != null)todos.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return pagos;
			}
			else return null;
	}

	public ReciboPagoProveedor reciboProveedorPorNo(int codigo) {
		// TODO Auto-generated method stub
		 Connection con = null;
	        
	    	String sql="select *, DATE_FORMAT(fecha, '%d/%m/%Y') as fecha2 from v_recibo_pago_proveedor where no_recibo=?";
	        //Statement stmt = null;
	    	ReciboPagoProveedor un=new ReciboPagoProveedor();
			
			ResultSet res=null;
			
			boolean existe=false;
			try {
				con = conexion.getPoolConexion().getConnection();
				
				todos = con.prepareStatement(sql);
				todos.setInt(1, codigo);
				
				
				res = todos.executeQuery();
				while(res.next()){
					existe=true;
					un.setFecha(res.getString("fecha2"));
					un.setConcepto(res.getString("concepto"));
					un.setNoRecibo(res.getInt("no_recibo"));
					un.setTotal(res.getBigDecimal("total"));
					
					Proveedor unProveedor=new Proveedor();//myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
					
					unProveedor.setId(res.getInt("codigo_proveedor"));
					unProveedor.setNombre(res.getString("nombre_proveedor"));
					
					un.setProveedor(unProveedor);
					
					CuentasBancos unCuenta=new CuentasBancos();
					unCuenta.setId(res.getInt("codigo_tipo_pago"));
					unCuenta.setNombre(res.getString("forma_pago"));
					unCuenta.setNoCuenta(res.getString("no_cuenta"));
					unCuenta.setTipoCuenta(res.getString("tipo_cuenta"));
					un.setFormaPago(unCuenta);
					
					

				 }
						
				} catch (SQLException e) {
					e.printStackTrace();
				}
			finally
			{
				try{
					
					if(res != null) res.close();
	                if(todos != null)todos.close();
	                if(con != null) con.close();
	                
					
					} // fin de try
					catch ( SQLException excepcionSql )
					{
						excepcionSql.printStackTrace();
						//conexion.desconectar();
					} // fin de catch
			} // fin de finally
			
			
				if (existe) {
					return un;
				}
				else return null;
	}

	public List<ReciboPagoProveedor> reciboProveedorPorFecha(String date1, String date2) {
		// TODO Auto-generated method stub
Connection con = null;
        
    	String sql="select *, DATE_FORMAT(fecha, '%d/%m/%Y') as fecha2 from v_recibo_pago_proveedor where fecha BETWEEN ? and ? ORDER BY no_recibo DESC;";
        //Statement stmt = null;
       	List<ReciboPagoProveedor> pagos=new ArrayList<ReciboPagoProveedor>();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			todos = con.prepareStatement(sql);
			todos.setString(1, date1);
			todos.setString(2, date2);
			
			res = todos.executeQuery();
			while(res.next()){
				ReciboPagoProveedor un=new ReciboPagoProveedor();
				existe=true;
				un.setFecha(res.getString("fecha2"));
				un.setConcepto(res.getString("concepto"));
				un.setNoRecibo(res.getInt("no_recibo"));
				un.setTotal(res.getBigDecimal("total"));
				
				Proveedor unProveedor=new Proveedor();//myClienteDao.buscarCliente(res.getInt("codigo_cliente"));
				
				unProveedor.setId(res.getInt("codigo_proveedor"));
				unProveedor.setNombre(res.getString("nombre_proveedor"));
				
				un.setProveedor(unProveedor);
				
				CuentasBancos unCuenta=new CuentasBancos();
				unCuenta.setId(res.getInt("codigo_tipo_pago"));
				unCuenta.setNombre(res.getString("forma_pago"));
				unCuenta.setNoCuenta(res.getString("no_cuenta"));
				unCuenta.setTipoCuenta(res.getString("tipo_cuenta"));
				un.setFormaPago(unCuenta);
				
				
				pagos.add(un);
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(todos != null)todos.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return pagos;
			}
			else return null;
	}

}
