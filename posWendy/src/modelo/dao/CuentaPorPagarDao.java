package modelo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import modelo.Cliente;
import modelo.Conexion;
import modelo.CuentaPorCobrar;
import modelo.CuentaPorPagar;
import modelo.Factura;
import modelo.FacturaCompra;
import modelo.Proveedor;
import modelo.ReciboPago;
import modelo.ReciboPagoProveedor;

public class CuentaPorPagarDao {
	
	private Conexion conexion;
	private PreparedStatement insertar=null;
	private PreparedStatement seleccionar=null;
	private PreparedStatement buscar=null;
	private PreparedStatement actualizar=null;
	private PreparedStatement eliminar=null;

	public CuentaPorPagarDao(Conexion conn) {
		conexion=conn;
	}
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<      se obtiene el ultimo registro        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public CuentaPorPagar getSaldoProveedor(Proveedor myProveedor){
		CuentaPorPagar un=new CuentaPorPagar();
		
		Connection con = null;
        
    	//String sql="select * from cierre where usuario = ?";
    	
    	String sql2="SELECT * FROM cuentas_por_pagar WHERE cuentas_por_pagar.codigo_proveedor = ? ORDER BY cuentas_por_pagar.codigo_reguistro DESC LIMIT 1";
ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			seleccionar = con.prepareStatement(sql2);
			
			seleccionar.setInt(1, myProveedor.getId());
			res = seleccionar.executeQuery();
			while(res.next()){
				
				existe=true;
				un.setNoReguistro(res.getInt("codigo_reguistro"));
				un.setProveedor(myProveedor);
				un.setDescripcion(res.getString("descripcion"));
				un.setFecha(res.getString("fecha"));
				un.setCredito(res.getBigDecimal("credito"));
				un.setDebito(res.getBigDecimal("debito"));
				un.setSaldo(res.getBigDecimal("saldo"));
			
			 }
					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(seleccionar != null)seleccionar.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		return un;
	}
	
	
	
	public boolean reguistrarDebito(ReciboPagoProveedor myRecibo) {
		// TODO Auto-generated method stub
		CuentaPorPagar aRegistrar=new CuentaPorPagar();
		CuentaPorPagar ultima=this.getSaldoProveedor(myRecibo.getProveedor());
		
		
		aRegistrar.setProveedor(myRecibo.getProveedor());
		aRegistrar.setDescripcion(myRecibo.getConcepto());
		
		aRegistrar.setDebito(myRecibo.getTotal());
		BigDecimal newSaldo=ultima.getSaldo().subtract(myRecibo.getTotal());//.add(myFactura.getTotal());
		aRegistrar.setSaldo(newSaldo);
		
		//JOptionPane.showConfirmDialog(null, myCliente);
				int resultado=0;
				ResultSet rs=null;
				Connection con = null;
				
				try 
				{
					con = conexion.getPoolConexion().getConnection();
					
					insertar=con.prepareStatement( "INSERT INTO cuentas_por_pagar(fecha,codigo_proveedor,descripcion,credito,saldo) VALUES (now(),?,?,?,?)");
					insertar.setInt(1, aRegistrar.getProveedor().getId());
					insertar.setString(2, aRegistrar.getDescripcion());
					insertar.setBigDecimal(3, aRegistrar.getDebito());
					insertar.setBigDecimal(4, aRegistrar.getSaldo());
					resultado=insertar.executeUpdate();
					
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
		
		//return false;
	}
	
	public boolean reguistrarCredito(FacturaCompra myFactura) {
		// TODO Auto-generated method stub
		CuentaPorPagar aRegistrar=new CuentaPorPagar();
		CuentaPorPagar ultima=this.getSaldoProveedor(myFactura.getProveedor());
		
		
		aRegistrar.setProveedor(myFactura.getProveedor());
		aRegistrar.setDescripcion("compra segun factura no. "+myFactura.getIdFactura());
		aRegistrar.setCredito(myFactura.getTotal());
		BigDecimal newSaldo=ultima.getSaldo().add(myFactura.getTotal());
		aRegistrar.setSaldo(newSaldo);
		
		//JOptionPane.showConfirmDialog(null, myCliente);
				int resultado=0;
				ResultSet rs=null;
				Connection con = null;
				
				try 
				{
					con = conexion.getPoolConexion().getConnection();
					
					insertar=con.prepareStatement( "INSERT INTO cuentas_por_pagar(fecha,codigo_proveedor,descripcion,credito,saldo) VALUES (now(),?,?,?,?)");
					insertar.setInt(1, aRegistrar.getProveedor().getId());
					insertar.setString(2, aRegistrar.getDescripcion());
					insertar.setBigDecimal(3, aRegistrar.getCredito());
					insertar.setBigDecimal(4, aRegistrar.getSaldo());
					resultado=insertar.executeUpdate();
					
					return true;
					
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage());
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
		
		//return false;
	}

}
