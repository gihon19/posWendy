package modelo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.CierreCaja;
import modelo.Conexion;
import modelo.Factura;

public class CierreCajaDao {
	
	private Conexion conexion=null;
	private PreparedStatement seleccionarCierre=null;
	private PreparedStatement registrarCierre=null;
	public int idUltimoRequistro=0;
	private FacturaDao myFacturaDao;
	
	public CierreCajaDao(Conexion conn){
		//Class(Conexion);
		conexion =conn;
		myFacturaDao=new FacturaDao(conexion);
	}
	
	public boolean registrarCierre(){
		boolean resultado=false;
		 Connection con = null;
		 
		 ResultSet rs=null;
		 
		 //SE CONSIGUE EL ITEM PARA EL CIERRE DE CAJA
		 CierreCaja unCierre=this.getCierre();
		 String sql= "INSERT INTO cierre_caja("
					+ "fecha,"
					+ "factura_inicial,"
					+ "factura_final,"
					+ "efectivo,"
					+ "creditos,"
					+ "totalventa,"
					+ "tarjeta,"
					+ "usuario,"
					+ "isv15,"
					+ "isv18)"
					+ " VALUES (now(),?,?,?,?,?,?,?,?,?)";
			 if(unCierre!=null&&unCierre.getNoFacturaFinal()!=0){
				 try {
						con = conexion.getPoolConexion().getConnection();
						registrarCierre=
								con.prepareStatement(sql);
						
						registrarCierre.setInt(1,unCierre.getNoFacturaInicio() );
						registrarCierre.setInt(2,unCierre.getNoFacturaFinal() );
						registrarCierre.setBigDecimal(3, unCierre.getEfectivo());
						registrarCierre.setBigDecimal(4, unCierre.getCredito());
						registrarCierre.setBigDecimal(5, unCierre.getTotal());
						registrarCierre.setBigDecimal(6, unCierre.getTarjeta());
						registrarCierre.setString(7, unCierre.getUsuario());
						registrarCierre.setBigDecimal(8, unCierre.getIsv15());
						registrarCierre.setBigDecimal(9, unCierre.getIsv18());
						
						
						
						
						registrarCierre.executeUpdate();//se guarda el encabezado de la factura
						
						
						rs=registrarCierre.getGeneratedKeys(); //obtengo las ultimas llaves generadas
						while(rs.next()){
							this.idUltimoRequistro=rs.getInt(1);
							//this.setIdArticuloRegistrado(rs.getInt(1));
						}
						resultado=true;
						
				 }catch (SQLException e) {
						e.printStackTrace();
						resultado=false;
					}
				finally
				{
					try{
						
						if(rs != null) rs.close();
						if(registrarCierre != null)registrarCierre.close();
						if(con != null) con.close();
		             
						
						} // fin de try
						catch ( SQLException excepcionSql )
						{
							excepcionSql.printStackTrace();
							//conexion.desconectar();
						} // fin de catch
				} // fin de finally
		
		 }else{
			 resultado=false;
		 }
		
		return resultado;
	}
	public boolean actualizarCierre(BigDecimal total){
		
		
		boolean resultado=false;
		 Connection con = null;
		 
		 ResultSet rs=null;
		 
		 //SE calculan todos los datos para el actualizar el cierre
		 CierreCaja unCierre=this.getCalcularCierre();
		
		 
		 unCierre.setEfectivoCaja(total);
		 
		 
		 String sql= "update cierre_caja set "
		 		+ "fecha=now(),"
		 		+ "factura_final=?,"
		 		+ "efectivo=?,"
		 		+ "creditos=?,"
		 		+ "tarjeta=?,"
		 		+ "isv15=?,"
		 		+ "isv18=?,"
		 		+ "total_isv15=?,"
		 		+ "total_isv18=?,"
		 		+ "total_excento=?,"
		 		+ "estado=?,"
		 		+ "total_efectivo=?,"
		 		+ "totalventa=?,"
		 		+ "no_salida_final=?,"
		 		+ "total_salida=? ,"
		 		+ "no_cobro_final=?, "
		 		+ "total_cobro=?,"
		 		+ "efectivo_caja =?,"
		 		+ "no_pago_final=?,"
		 		+ "total_pago=? "
		 		+ "where "
		 		+ "idCierre=?";
		 		//+ "
		
			 if(unCierre!=null&&unCierre.getNoFacturaFinal()!=0){
				 
				
				 try {
					 	con = conexion.getPoolConexion().getConnection();
						registrarCierre=con.prepareStatement(sql);
						
						//se suma el efectivo inicial y la venta con efectivo
						BigDecimal t_efectivo=unCierre.getEfectivoInicial().add(unCierre.getEfectivo());
					 	unCierre.setTotalEfectivo(t_efectivo);
					 	
					 	//se consigue el total de las salidas asi como la ultima
					 	SalidaCajaDao salidaDao=new SalidaCajaDao(conexion);
					 	
					 	//se manda el cierre para que sea modificado con las salidas
					 	salidaDao.calcularTotal(unCierre);
					 
					 	
					 	//unCierre.setTotalSalida(salidaDao.calcularTotal1(unCierre));
					 	unCierre.setTotalEfectivo(unCierre.getTotalEfectivo().subtract(unCierre.getTotalSalida()));
					 	
					 	
					 	
					 	//se crea el objeto que para consultar los cobros
					 	ReciboPagoDao reciboDao=new ReciboPagoDao(conexion);
					 	
					 	//se manda el cierre para que sea modificado con las salidas y el total
					 	reciboDao.calcularTotalCierre(unCierre);
					 	//se calcula el total en efectivo sumando los cobros
					 	unCierre.setTotalEfectivo(unCierre.getTotalEfectivo().add(unCierre.getTotalCobro()));
					 	
					 	//se manda el cierre para que sea modificado con los pagos y el total
					 	reciboDao.calcularTotalPagosCierre(unCierre);
					 	//se calcula el total en efectivo restandoles los pagos
					 	unCierre.setTotalEfectivo(unCierre.getTotalEfectivo().subtract(unCierre.getTotalPago()));
					 	
						
						
						registrarCierre.setInt(1,unCierre.getNoFacturaFinal() );
						registrarCierre.setDouble(2,unCierre.getEfectivo().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
						registrarCierre.setBigDecimal(3, unCierre.getCredito().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						registrarCierre.setBigDecimal(4, unCierre.getTarjeta().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						
						registrarCierre.setBigDecimal(5, unCierre.getIsv15().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						
						registrarCierre.setBigDecimal(6, unCierre.getIsv18().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						
						registrarCierre.setBigDecimal(7, unCierre.getTotalIsv15().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						registrarCierre.setBigDecimal(8, unCierre.getTotalIsv18().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						
						registrarCierre.setBigDecimal(9, unCierre.getTotalExcento().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						
						registrarCierre.setInt(10, 0);
						registrarCierre.setBigDecimal(11, unCierre.getTotalEfectivo().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						registrarCierre.setBigDecimal(12, unCierre.getTotal().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						registrarCierre.setInt(13, unCierre.getNoSalidaFinal());
						
						registrarCierre.setBigDecimal(14, unCierre.getTotalSalida().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						
						registrarCierre.setInt(15, unCierre.getNoCobroFinal());
						
						registrarCierre.setBigDecimal(16, unCierre.getTotalCobro().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						
						registrarCierre.setBigDecimal(17, unCierre.getEfectivoCaja().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						
						registrarCierre.setInt(18, unCierre.getNoPagoFinal());
						
						registrarCierre.setBigDecimal(19, unCierre.getTotalPago().setScale(2, BigDecimal.ROUND_HALF_EVEN));
						
						registrarCierre.setInt(20, unCierre.getId());
						
						
						
						
						
						
						
						
						registrarCierre.executeUpdate();//se guarda el encabezado de la factura
						
						
						rs=registrarCierre.getGeneratedKeys(); //obtengo las ultimas llaves generadas
						while(rs.next()){
							this.idUltimoRequistro=rs.getInt(1);
							//this.setIdArticuloRegistrado(rs.getInt(1));
						}
						this.idUltimoRequistro=unCierre.getId();
						resultado=true;
						
				 }catch (SQLException e) {
						e.printStackTrace();
						resultado=false;
					}
				finally
				{
					try{
						
						if(rs != null) rs.close();
						if(registrarCierre != null)registrarCierre.close();
						if(con != null) con.close();
		             
						
						} // fin de try
						catch ( SQLException excepcionSql )
						{
							excepcionSql.printStackTrace();
							//conexion.desconectar();
						} // fin de catch
				} // fin de finally
		
		 }else{
			 
			 resultado=false;
			 
			 
		 }
		
		return resultado;
	}
	
	public boolean registrarCierre(CierreCaja unCierre){
		boolean resultado=false;
		 Connection con = null;
		 
		 ResultSet rs=null;
		 
		 
		 String sql= "INSERT INTO cierre_caja("
					+ "fecha,"
					+ "factura_inicial,"
					+ "factura_final,"
					+ "efectivo,"
					+ "creditos,"
					+ "totalventa,"
					+ "tarjeta,"
					+ "usuario,"
					+ "isv15,"
					+ "isv18,"
					+ "efectivo_inicial,"
					+ "no_salida_inicial,"
					+ "no_cobro_inicial)"
					+ " VALUES (now(),?,?,?,?,?,?,?,?,?,?,?,?)";
			 if(unCierre!=null){
				 try {
						con = conexion.getPoolConexion().getConnection();
						registrarCierre=
								con.prepareStatement(sql);
						
						registrarCierre.setInt(1,unCierre.getNoFacturaInicio() );
						registrarCierre.setInt(2,0 );
						registrarCierre.setBigDecimal(3, unCierre.getEfectivo());
						registrarCierre.setBigDecimal(4, unCierre.getCredito());
						registrarCierre.setBigDecimal(5, unCierre.getTotal());
						registrarCierre.setBigDecimal(6, unCierre.getTarjeta());
						registrarCierre.setString(7, unCierre.getUsuario());
						registrarCierre.setBigDecimal(8, unCierre.getIsv15());
						registrarCierre.setBigDecimal(9, unCierre.getIsv18());
						registrarCierre.setBigDecimal(10,unCierre.getEfectivoInicial());
						registrarCierre.setInt(11, unCierre.getNoSalidaInicial());
						registrarCierre.setInt(12, unCierre.getNoCobroInicial());
						
						
						
						
						registrarCierre.executeUpdate();//se guarda el encabezado de la factura
						
						
						rs=registrarCierre.getGeneratedKeys(); //obtengo las ultimas llaves generadas
						while(rs.next()){
							this.idUltimoRequistro=rs.getInt(1);
							//this.setIdArticuloRegistrado(rs.getInt(1));
						}
						resultado=true;
						
				 }catch (SQLException e) {
						e.printStackTrace();
						resultado=false;
					}
				finally
				{
					try{
						
						if(rs != null) rs.close();
						if(registrarCierre != null)registrarCierre.close();
						if(con != null) con.close();
		             
						
						} // fin de try
						catch ( SQLException excepcionSql )
						{
							excepcionSql.printStackTrace();
							//conexion.desconectar();
						} // fin de catch
				} // fin de finally
		
		 }else{
			 resultado=false;
		 }
		
		return resultado;
	}
	
	
	
	
	
public CierreCaja getCierreUltimoUser(){
		
		//se crear un referencia al pool de conexiones
		//DataSource ds = DBCPDataSourceFactory.getDataSource("mysql");
		
		
        Connection con = null;
        
    	//String sql="select * from cierre where usuario = ?";
    	
    	String sql2="SELECT * FROM cierre_caja WHERE cierre_caja.usuario = ? ORDER BY cierre_caja.idCierre DESC LIMIT 1";
        //Statement stmt = null;
    	CierreCaja unaCierre=new CierreCaja();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			seleccionarCierre = con.prepareStatement(sql2);
			
			seleccionarCierre.setString(1, conexion.getUsuarioLogin().getUser());
			res = seleccionarCierre.executeQuery();
			while(res.next()){
				
				existe=true;
				unaCierre.setId(res.getInt("idCierre"));
				unaCierre.setNoFacturaInicio(res.getInt("factura_inicial"));
				unaCierre.setNoFacturaFinal(res.getInt("factura_final"));
				unaCierre.setEfectivo(res.getBigDecimal("efectivo"));
				unaCierre.setCredito(res.getBigDecimal("creditos"));
				unaCierre.setTarjeta(res.getBigDecimal("tarjeta"));
				
				unaCierre.setIsv15(res.getBigDecimal("isv15"));
				unaCierre.setIsv18(res.getBigDecimal("isv18"));
				
				unaCierre.setTotal(res.getBigDecimal("totalventa"));
				unaCierre.setUsuario(res.getString("usuario"));//.setTotal(res.getBigDecimal("total"));
				unaCierre.setEstado(res.getBoolean("estado"));
				unaCierre.setEfectivoInicial(res.getBigDecimal("efectivo_inicial"));
				
				unaCierre.setNoSalidaFinal(res.getInt("no_salida_final"));
				unaCierre.setNoSalidaInicial(res.getInt("no_salida_inicial"));
				
				unaCierre.setNoCobroInicial(res.getInt("no_cobro_inicial"));
				unaCierre.setNoCobroFinal(res.getInt("no_cobro_final"));
				
				unaCierre.setNoPagoInicial(res.getInt("no_pago_inicial"));
				unaCierre.setNoPagoFinal(res.getInt("no_pago_final"));
				
				
			
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(seleccionarCierre != null)seleccionarCierre.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		return unaCierre;
			/*if (existe) {
				return unaCierre;
			}
			else return null;*/
		
	}
/**
 * @return the total en efectivo en un rago de facturas para un usuario
 * @param factInicial numero de factura inicial
 * @param factFinal numero de factura final
 */
	private BigDecimal getTotalEfectivo(int factInicial, int factFinal){
		
		BigDecimal total=null;
		
		//total=total.add(t);
		String sql="select sum(cobro_efectivo) AS total_efectivo from encabezado_factura where tipo_factura = 1 and estado_factura = 'ACT' and numero_factura >= ? and numero_factura <= ? and usuario =? ";
		Connection con = null;
        
        
		//String tot="";
		ResultSet res=null;
		
	//	String user=conexion.getUsuarioLogin().getUser();
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			seleccionarCierre= con.prepareStatement(sql);
			seleccionarCierre.setInt(1, factInicial);
			seleccionarCierre.setInt(2, factFinal);
			seleccionarCierre.setString(3, conexion.getUsuarioLogin().getUser());
			
			res = seleccionarCierre.executeQuery();
			
			while(res.next()){
				
				existe=true;
				//total=total.add(res.getBigDecimal("total_efectivo"));
				total=new BigDecimal(res.getDouble("total_efectivo"));
				//String dato=res.getString("total_efectivo");
				
				//JOptionPane.showMessageDialog(null, dato);
				
				
			
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(seleccionarCierre != null)seleccionarCierre.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return total;
			}
			else return null;
	}
	
	
	/**
	 * @return the total en tarjeta en un rago de facturas para un usuario
	 * @param factInicial numero de factura inicial
	 * @param factFinal numero de factura final
	 */
		private BigDecimal getTotalTarjeta(int factInicial, int factFinal){
			
			BigDecimal total=null;
			
			
			String sql="select "
	    			+ "			sum("
	    			+ "				`encabezado_factura`.`cobro_tarjeta` "
	    			+ "			) AS `total_tarjeta` "
	    			+ "		from "
	    			+ "			`encabezado_factura` "
	    			+ "		where "
	    			+ "					`encabezado_factura`.`tipo_factura` = 1 "
	    			+ "				and "
	    			+ "					`encabezado_factura`.`estado_factura` = 'ACT' "
	    			+ "				and "
	    			+ "					`encabezado_factura`.`numero_factura` >= ?"
	    			+ "				and "
	    			+ "					`encabezado_factura`.`numero_factura` <= ?"
	    			+ "				and "
	    			+ "					encabezado_factura.usuario =?";
			Connection con = null;
	        
	        
			
			ResultSet res=null;
			
			boolean existe=false;
			try {
				con = conexion.getPoolConexion().getConnection();
				
				this.seleccionarCierre= con.prepareStatement(sql);
				seleccionarCierre.setInt(1, factInicial);
				seleccionarCierre.setInt(2, factFinal);
				seleccionarCierre.setString(3, conexion.getUsuarioLogin().getUser());
				
				res = seleccionarCierre.executeQuery();
				while(res.next()){
					
					existe=true;
					total=new BigDecimal(res.getDouble("total_tarjeta"));
					
					
					
				
				 }
						
				} catch (SQLException e) {
					e.printStackTrace();
				}
			finally
			{
				try{
					
					if(res != null) res.close();
	                if(seleccionarCierre != null)seleccionarCierre.close();
	                if(con != null) con.close();
	                
					
					} // fin de try
					catch ( SQLException excepcionSql )
					{
						excepcionSql.printStackTrace();
						//conexion.desconectar();
					} // fin de catch
			} // fin de finally
			
			
				if (existe) {
					return total;
				}
				else return null;
		}
		
		/**
		 * @return the total en creditos en un rago de facturas para un usuario
		 * @param factInicial numero de factura inicial
		 * @param factFinal numero de factura final
		 */
			private BigDecimal getTotalCredito(int factInicial, int factFinal){
				
				BigDecimal total=null;
				
				String sql="select "
		    			+ "			sum("
		    			+ "				`encabezado_factura`.`total` "
		    			+ "			) AS `total_efectivo` "
		    			+ "		from "
		    			+ "			`encabezado_factura` "
		    			+ "		where "
		    			+ "					`encabezado_factura`.`tipo_factura` = 2 "
		    			+ "				and "
		    			+ "					`encabezado_factura`.`estado_factura` = 'ACT' "
		    			+ "				and "
		    			+ "					`encabezado_factura`.`numero_factura` >= ?"
		    			+ "				and "
		    			+ "					`encabezado_factura`.`numero_factura` <= ?"
		    			+ "				and "
		    			+ "					encabezado_factura.usuario =?";
				Connection con = null;
		        
		        
				
				ResultSet res=null;
				
				boolean existe=false;
				try {
					con = conexion.getPoolConexion().getConnection();
					
					this.seleccionarCierre= con.prepareStatement(sql);
					seleccionarCierre.setInt(1, factInicial);
					seleccionarCierre.setInt(2, factFinal);
					seleccionarCierre.setString(3, conexion.getUsuarioLogin().getUser());
					
					res = seleccionarCierre.executeQuery();
					while(res.next()){
						
						existe=true;
						total=new BigDecimal(res.getDouble("total_efectivo"));
						
						
						
					
					 }
							
					} catch (SQLException e) {
						e.printStackTrace();
					}
				finally
				{
					try{
						
						if(res != null) res.close();
		                if(seleccionarCierre != null)seleccionarCierre.close();
		                if(con != null) con.close();
		                
						
						} // fin de try
						catch ( SQLException excepcionSql )
						{
							excepcionSql.printStackTrace();
							//conexion.desconectar();
						} // fin de catch
				} // fin de finally
				
				
					if (existe) {
						return total;
					}
					else return null;
			}
	

	public CierreCaja getCierre(){
		
		//se crear un referencia al pool de conexiones
		//DataSource ds = DBCPDataSourceFactory.getDataSource("mysql");
		
		
        Connection con = null;
        //SE CONSIGUE EL ITEM PARA EL CIERRE DE CAJA
		 CierreCaja ultimoCierreUser=this.getCierreUltimoUser();
        
    	String sql="select * from cierre where usuario = ?";
    	
    	String sql2=""
    			+ " select "
    			+ " date_format(now(),'%d/%m/%Y %h:%i:%s') AS `fecha`, "
    			+ "	("
    			+ "		select "
    			+ "			`encabezado_factura`.`numero_factura` "
    			+ "		from "
    			+ "			`encabezado_factura` "
    			+ "		WHERE "
    			+ "			encabezado_factura.usuario ='"
    			+ 		conexion.getUsuarioLogin().getUser()+"'"
    			+ "		order by "
    			+ "			`encabezado_factura`.`numero_factura` desc "
    			+ "		limit 1"
    			+ "	) AS `factura_ultima`,"
    			+ "	("
    			+ "		select "
    			+ "			sum("
    			+ "				`encabezado_factura`.`total` "
    			+ "			) AS `total_efectivo` "
    			+ "		from "
    			+ "			`encabezado_factura` "
    			+ "		where "
    			+ "			("
    			+ "				("
    			+ "					`encabezado_factura`.`tipo_factura` = 1"
    			+ "				) "
    			+ "				and "
    			+ "				("
    			+ "					`encabezado_factura`.`estado_factura` = 'ACT'"
    			+ "				) "
    			+ "				and "
    			+ "				("
    			+ "					`encabezado_factura`.`numero_factura` > "+ultimoCierreUser.getNoFacturaFinal()
    			+ "				) "
    			+ "				and "
    			+ "				("
    			+ "					`encabezado_factura`.`numero_factura` <= `factura_ultima`"
    			+ "				) "
    			+ "				and "
    			+ "				("
    			+ "					`encabezado_factura`.`tipo_pago` = 1"
    			+ "				)"
    			+ "				and "
    			+ "				( "
    			+ "					encabezado_factura.usuario ='"
    			+ 					conexion.getUsuarioLogin().getUser()+"'"
    			+ "				)"
    			+ "			)"
    			+ "	) AS `total_efectivo`,"
    			+ "	("
    			+ "		select "
    			+ "			sum("
    			+ "				`encabezado_factura`.`total` "
    			+ "			) AS `total_efectivo` "
    			+ "		from "
    			+ "			`encabezado_factura` "
    			+ "		where "
    			+ "			("
    			+ "				("
    			+ "					`encabezado_factura`.`tipo_factura` = 1"
    			+ "				) "
    			+ "				and ("
    			+ "					`encabezado_factura`.`estado_factura` = 'ACT'"
    			+ "				) "
    			+ "				and ("
    			+ "					`encabezado_factura`.`numero_factura` > "+ultimoCierreUser.getNoFacturaFinal()
    			+ "				) "
    			+ "				and ("
    			+ "					`encabezado_factura`.`numero_factura` <= `factura_ultima`"
    			+ "				) "
    			+ "				and ("
    			+ "					`encabezado_factura`.`tipo_pago` = 2"
    			+ "				) "
    			+ "				and("
    			+ "					encabezado_factura.usuario ='"
    			+ 				conexion.getUsuarioLogin().getUser()+"'"
    			+ "				)"
    			+ "			)"
    			+ "	) AS `total_tarjeta`,"
    			+ "	("
    			+ "		select "
    			+ "			sum("
    			+ "				`encabezado_factura`.`total` "
    			+ "			) AS `total_efectivo` "
    			+ "		from "
    			+ "			`encabezado_factura` "
    			+ "		where "
    			+ "			("
    			+ "				("
    			+ "					`encabezado_factura`.`tipo_factura` = 2"
    			+ "				) "
    			+ "				and ("
    			+ "					`encabezado_factura`.`estado_factura` = 'ACT'"
    			+ "				) "
    			+ "				and ("
    			+ "					`encabezado_factura`.`numero_factura` > "+ultimoCierreUser.getNoFacturaFinal()
    			+ "				) "
    			+ "				and ("
    			+ "					`encabezado_factura`.`numero_factura` <= `factura_ultima`"
    			+ "				) "
    			+ "				and( "
    			+ "					encabezado_factura.usuario = '"
    			+ 						conexion.getUsuarioLogin().getUser()+"'"
    			+ "				)"
    			+ "			)"
    			+ ") AS `total_credito`,"
    			+ "("
    			+ "		SELECT "
    			+ "			sum( "
				+ "				`encabezado_factura`.`impuesto` "
				+"			) AS `total_isv15` "
				+"		FROM "
				+"			`encabezado_factura` "
				+"		WHERE "
				+"			( "
				+"				( "
				+"					`encabezado_factura`.`tipo_factura` = 1 "
				+"				) "
				+"				AND ( "
				+"					`encabezado_factura`.`estado_factura` = 'ACT' "
				+"				) "
				+"				AND ( "
				+"					`encabezado_factura`.`numero_factura` > "+ultimoCierreUser.getNoFacturaFinal()
				+"				) "
				+"				AND ( "
				+"					`encabezado_factura`.`numero_factura` <= `factura_ultima` "
				+				")"
				+"				AND ( "
				+"					encabezado_factura.usuario = '"
				+ 					conexion.getUsuarioLogin().getUser()+"'"
				+"				) "
				+"			) "
				+") AS `total_isv15`, "
    			+ "("
    			+"		SELECT "
    			+"			sum( "
				+"				`encabezado_factura`.`isv18` "
				+"			) AS `total_isv18` "
				+"		FROM "
				+"			`encabezado_factura` "
				+"		WHERE  "
				+"		( "
				+"				( "
				+"					`encabezado_factura`.`tipo_factura` = 1 "
				+"				) "
				+"			AND ( "
				+"				`encabezado_factura`.`estado_factura` = 'ACT' "
				+"			) "
				+"			AND ( "
				+"				`encabezado_factura`.`numero_factura` > "+ultimoCierreUser.getNoFacturaFinal()
				+"			) "
				+"			AND ( "
				+"				`encabezado_factura`.`numero_factura` <= `factura_ultima` "
				+"			) "
				+"			AND ( "
				+"					encabezado_factura.usuario = '"
				+ 				conexion.getUsuarioLogin().getUser()+"'"
				+"			) "
				+"		) "
				+") AS `total_isv18`, "
    			+ "	("
    			+ "		select "
    			+ "			sum("
    			+ "				`encabezado_factura`.`total` "
    			+ "			) AS `total_efectivo` "
    			+ "			from "
    			+ "				`encabezado_factura` "
    			+ "			where "
    			+ "			("
    			+ "				("
    			+ "					`encabezado_factura`.`estado_factura` = 'ACT'"
    			+ "				) "
    			+ "				and ("
    			+ "					`encabezado_factura`.`numero_factura` > "+ultimoCierreUser.getNoFacturaFinal()
    			+ "				) "
    			+ "				and ("
    			+ "					`encabezado_factura`.`numero_factura` <= `factura_ultima`"
    			+ "				) "
    			+ "				and( "
    			+ "					encabezado_factura.usuario = '"
    			+ 					conexion.getUsuarioLogin().getUser()+"'"
    			+ "				)"
    			+ "			)"
    			+ "	) AS `total` "
    			+ "	from "
    			+ "		`encabezado_factura` "
    			+ "	where ((`encabezado_factura`.`numero_factura` > (select `cierre_caja`.`factura_final` from `cierre_caja` order by `cierre_caja`.`idCierre` desc limit 1)) and (`encabezado_factura`.`numero_factura` <= (select `encabezado_factura`.`numero_factura` from `encabezado_factura` order by `encabezado_factura`.`numero_factura` desc limit 1))) limit 1; ";
        //Statement stmt = null;
    	CierreCaja unaCierre=new CierreCaja();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			seleccionarCierre = con.prepareStatement(sql2);
			
			//seleccionarCierre.setString(1, conexion.getUsuarioLogin().getUser());
			res = seleccionarCierre.executeQuery();
			while(res.next()){
				
				existe=true;
				
				unaCierre.setNoFacturaInicio(ultimoCierreUser.getNoFacturaFinal());
				unaCierre.setNoFacturaFinal(res.getInt("factura_ultima"));
				unaCierre.setEfectivo(res.getBigDecimal("total_efectivo"));
				unaCierre.setCredito(res.getBigDecimal("total_credito"));
				unaCierre.setTarjeta(res.getBigDecimal("total_tarjeta"));
				
				unaCierre.setIsv15(res.getBigDecimal("total_isv15"));
				unaCierre.setIsv18(res.getBigDecimal("total_isv18"));
				
				unaCierre.setTotal(res.getBigDecimal("total"));
				unaCierre.setUsuario(conexion.getUsuarioLogin().getUser());//.setTotal(res.getBigDecimal("total"));
			
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(seleccionarCierre != null)seleccionarCierre.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		return unaCierre;
			/*if (existe) {
				return unaCierre;
			}
			else return null;*/
		
	}
	
private CierreCaja getCalcularCierre(){
		
	
		//el cierre donde se volcaran todos los datos para regresarlo
		CierreCaja unaCierre=new CierreCaja();
		
		
        Connection con = null;
        
        
        //SE el ultimo cierre de caja activo del usuario
		 CierreCaja ultimoCierreUser=this.getCierreUltimoUser();
		 
		 
		 
		 
		 String sql="SELECT "
		 			+ "sum(subtotal15)AS subtotal15,"
		 			+ "SUM(subtotal18)AS subtotal18,"
		 			+ "SUM(cobro_tarjeta)AS cobro_tarjeta,"
		 			+ "SUM(cobro_efectivo)AS cobro_efectivo,"
		 			+ "SUM(subtotal_excento)AS subtotal_excento,"
		 			+ "SUM(total)AS total,"
		 			+ "sum(impuesto) as impuesto,"
		 			+ "sum(isv18) as isv18 "
		 		+ "from encabezado_factura "
		 			+ "where usuario=? "
		 			+ "and "
		 			+ "numero_factura >= ? "
		 			+ "and numero_factura <=? and estado_factura='ACT'";
        
		 
		 //se consigue de la base de datos la ultima factura realizada por el usuario
		 Factura ultimaFacturaUsuario=myFacturaDao.getUltimaFacturaUser(conexion.getUsuarioLogin().getUser());
		 
		 //se consigue de la base de datos el total de en efectivo de un terminando grupos de facturas para un usuario
		// BigDecimal totalEfectivo=this.getTotalEfectivo(ultimoCierreUser.getNoFacturaInicio(), ultimaFacturaUsuario.getIdFactura());
		
		//se consigue de la base de datos el total de en tarjeta de un terminando grupos de facturas para un usuario
		// BigDecimal totalTarjeta=this.getTotalTarjeta(ultimoCierreUser.getNoFacturaInicio(), ultimaFacturaUsuario.getIdFactura());
		
		//se consigue de la base de datos el total de en creditos de un terminando grupos de facturas para un usuario
		 BigDecimal totalCredito=this.getTotalCredito(ultimoCierreUser.getNoFacturaInicio(), ultimaFacturaUsuario.getIdFactura());
		
    	
        
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			
			seleccionarCierre = con.prepareStatement(sql);
			
			// se consigue se establece el nombre del usuario
			seleccionarCierre.setString(1,ultimoCierreUser.getUsuario() );
			
			//se establece el numero inicial de factura
			seleccionarCierre.setInt(2, ultimoCierreUser.getNoFacturaInicio());
			
			//se establece el numero final de la factura
			seleccionarCierre.setInt(3,ultimaFacturaUsuario.getIdFactura() );
			
			//seleccionarCierre.setString(1, conexion.getUsuarioLogin().getUser());
			res = seleccionarCierre.executeQuery();
			while(res.next()){
				
				existe=true;
				
				unaCierre.setId(ultimoCierreUser.getId());
				
				unaCierre.setNoFacturaInicio(ultimoCierreUser.getNoFacturaInicio());
				unaCierre.setNoSalidaInicial(ultimoCierreUser.getNoSalidaInicial());/// este es el error
				unaCierre.setNoCobroInicial(ultimoCierreUser.getNoCobroInicial());
				unaCierre.setNoPagoInicial(ultimoCierreUser.getNoPagoInicial());
				
				unaCierre.setEfectivo(res.getBigDecimal("cobro_efectivo"));
				unaCierre.setCredito(totalCredito);
				unaCierre.setTarjeta(res.getBigDecimal("cobro_tarjeta"));
				
				unaCierre.setTotalExcento(res.getBigDecimal("subtotal_excento"));
				unaCierre.setTotalIsv15(res.getBigDecimal("subtotal15"));
				unaCierre.setTotalIsv18(res.getBigDecimal("subtotal18"));
				unaCierre.setNoFacturaFinal(ultimaFacturaUsuario.getIdFactura());
				
				unaCierre.setIsv15(res.getBigDecimal("impuesto"));
				unaCierre.setIsv18(res.getBigDecimal("isv18"));
				unaCierre.setEfectivoInicial(ultimoCierreUser.getEfectivoInicial());
				//unaCierre.setNoCobroInicial(noCobro);
				
				//BigDecimal t_efectivo=ultimoCierreUser.getEfectivoInicial().add(res.getBigDecimal("total_efectivo"));//se suma el efectivo inicial y la venta con efectivo
				
				//unaCierre.setTotalEfectivo(t_efectivo);
				
				unaCierre.setTotal(res.getBigDecimal("total"));
				unaCierre.setUsuario(conexion.getUsuarioLogin().getUser());//.setTotal(res.getBigDecimal("total"));
			
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(seleccionarCierre != null)seleccionarCierre.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		return unaCierre;
			/*if (existe) {
				return unaCierre;
			}
			else return null;*/
		
	}

	public List<CierreCaja> todos(int limInf,int limSupe) {
		List<CierreCaja> cierres =new ArrayList<CierreCaja>();
		
		ResultSet res=null;
		
		Connection conn=null;
		
		boolean existe=false;
		
		try{
			conn=conexion.getPoolConexion().getConnection();
			seleccionarCierre=conn.prepareStatement("SELECT * FROM v_cierre_caja ORDER BY idCierre DESC LIMIT ?,?;");
			seleccionarCierre.setInt(1, limInf);
			seleccionarCierre.setInt(2, limSupe);
			res = seleccionarCierre.executeQuery();
			while(res.next()){
				existe=true;
				CierreCaja unaCierre=new CierreCaja();
				unaCierre.setFecha(res.getString("fecha"));
				unaCierre.setId(res.getInt("idCierre"));
				unaCierre.setNoFacturaInicio(res.getInt("factura_inicial"));
				unaCierre.setNoFacturaFinal(res.getInt("factura_final"));
				unaCierre.setEfectivo(res.getBigDecimal("efectivo"));
				unaCierre.setCredito(res.getBigDecimal("creditos"));
				unaCierre.setTarjeta(res.getBigDecimal("tarjeta"));
				
				unaCierre.setIsv15(res.getBigDecimal("isv15"));
				unaCierre.setIsv18(res.getBigDecimal("isv18"));
				
				unaCierre.setTotal(res.getBigDecimal("totalventa"));
				unaCierre.setUsuario(res.getString("usuario"));
				
				
				cierres.add(unaCierre);
				
				
			}
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error, no se conecto");
			System.out.println(e);
	}
	finally
	{
		try{
			if(res != null) res.close();
	        if(seleccionarCierre != null)seleccionarCierre.close();
	        if(conn != null) conn.close();
			
			} // fin de try
			catch ( SQLException excepcionSql )
			{
				excepcionSql.printStackTrace();
				//conexion.desconectar();
			} // fin de catch
	} // fin de finally
		
		
		if (existe) {
			return cierres;
		}
		else return null;
	}

	public List<CierreCaja> porFecha(String date, String date2) {
		List<CierreCaja> cierres =new ArrayList<CierreCaja>();
		
		ResultSet res=null;
		
		Connection conn=null;
		
		boolean existe=false;
		
		try{
			conn=conexion.getPoolConexion().getConnection();
			seleccionarCierre=conn.prepareStatement("SELECT * FROM v_cierre_caja where fecha2 BETWEEN ? and ?");
			seleccionarCierre.setString(1, date);
			seleccionarCierre.setString(2, date2);
			res = seleccionarCierre.executeQuery();
			while(res.next()){
				existe=true;
				CierreCaja unaCierre=new CierreCaja();
				unaCierre.setFecha(res.getString("fecha"));
				unaCierre.setId(res.getInt("idCierre"));
				unaCierre.setNoFacturaInicio(res.getInt("factura_inicial"));
				unaCierre.setNoFacturaFinal(res.getInt("factura_final"));
				unaCierre.setEfectivo(res.getBigDecimal("efectivo"));
				unaCierre.setCredito(res.getBigDecimal("creditos"));
				unaCierre.setTarjeta(res.getBigDecimal("tarjeta"));
				
				unaCierre.setIsv15(res.getBigDecimal("isv15"));
				unaCierre.setIsv18(res.getBigDecimal("isv18"));
				
				unaCierre.setTotal(res.getBigDecimal("totalventa"));
				unaCierre.setUsuario(res.getString("usuario"));
				
				
				cierres.add(unaCierre);
				
				
			}
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error, no se conecto");
			System.out.println(e);
	}
	finally
	{
		try{
			if(res != null) res.close();
	        if(seleccionarCierre != null)seleccionarCierre.close();
	        if(conn != null) conn.close();
			
			} // fin de try
			catch ( SQLException excepcionSql )
			{
				excepcionSql.printStackTrace();
				//conexion.desconectar();
			} // fin de catch
	} // fin de finally
		
		
		if (existe) {
			return cierres;
		}
		else return null;
	}

	public boolean verificarCierre() {
		// TODO Auto-generated method stub
				
				
		        Connection con = null;
		        //SE CONSIGUE EL ITEM PARA EL CIERRE DE CAJA
				 CierreCaja ultimoCierreUser=this.getCierreUltimoUser();
		        
		    	String sql="SELECT numero_factura FROM encabezado_factura WHERE	numero_factura >= ? AND usuario = ? ";
		    	
		    	
				
				ResultSet res=null;
				
				boolean existe=false;
				try {
					con = conexion.getPoolConexion().getConnection();
					
					
					seleccionarCierre = con.prepareStatement(sql);
					seleccionarCierre.setInt(1,ultimoCierreUser.getNoFacturaInicio() );
					
					//seleccionarCierre.setInt(2, ultimoCierreUser.getNoFacturaInicio());
					seleccionarCierre.setString(2,ultimoCierreUser.getUsuario() );
					//seleccionarCierre.setString(1, conexion.getUsuarioLogin().getUser());
					res = seleccionarCierre.executeQuery();
					while(res.next()){
						
						existe=true;
						
					
					 }
							
					} catch (SQLException e) {
						e.printStackTrace();
					}
				finally
				{
					try{
						
						if(res != null) res.close();
		                if(seleccionarCierre != null)seleccionarCierre.close();
		                if(con != null) con.close();
		                
						
						} // fin de try
						catch ( SQLException excepcionSql )
						{
							excepcionSql.printStackTrace();
							//conexion.desconectar();
						} // fin de catch
				} // fin de finally
		return existe;
	}
	
	
	

}
