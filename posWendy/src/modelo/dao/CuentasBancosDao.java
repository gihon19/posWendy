package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import modelo.AbstractJasperReports;
import modelo.Articulo;
import modelo.Conexion;
import modelo.CuentasBancos;

public class CuentasBancosDao {
	private Conexion conexion;
	private PreparedStatement nuevo=null;
	private PreparedStatement seleccionar=null;
	private PreparedStatement buscar=null;
	private PreparedStatement actualizar=null;
	private PreparedStatement eliminar=null;
	private int idRegistrado=-1;
	
	
	
	public CuentasBancosDao(Conexion conn){
		conexion=conn;
	}
	public Vector<CuentasBancos> getCuentas(){
		Vector<CuentasBancos> cuentas=new Vector<CuentasBancos>();
		ResultSet res=null;
		
		Connection conn=null;
		
		boolean existe=false;
		
		try {
			
			conn=conexion.getPoolConexion().getConnection();
			buscar=conn.prepareStatement("SELECT cuentas_bancos.nombre,cuentas_bancos.no_cuenta, tipo_cuenta_bancos.tipo_cuenta, cuentas_bancos.id, tipo_cuenta_bancos.observaciones, cuentas_bancos.id_tipo_cuenta  "
					+ "FROM cuentas_bancos INNER JOIN tipo_cuenta_bancos ON cuentas_bancos.id_tipo_cuenta = tipo_cuenta_bancos.id");
			
			res = buscar.executeQuery();
			while(res.next()){
				
				CuentasBancos una=new CuentasBancos();
				existe=true;
				una.setId(res.getInt("id"));
				una.setNombre(res.getString("nombre"));
				una.setNoCuenta(res.getString("no_cuenta"));
				una.setTipoCuenta(res.getString("tipo_cuenta"));
				una.setIdTipoCuenta(res.getInt("id_tipo_cuenta"));
				cuentas.add(una);
			 }
			
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error, no se conecto");
			System.out.println(e);
	}finally
	{
		try{
			if(res!=null)res.close();
			if(buscar!=null)buscar.close();
			if(conn!=null)conn.close();
		} // fin de try
		catch ( SQLException excepcionSql )
		{
			excepcionSql.printStackTrace();
			//conexion.desconectar();
		} // fin de catch
	} // fin de finally
		
		if (existe) {
			return cuentas;
		}
		else return null;
		
	}
	public boolean registrar(CuentasBancos myCuenta) {
		// TODO Auto-generated method stub
		int resultado=0;
		ResultSet rs=null;
		Connection con = null;
		
		try 
		{
			con = conexion.getPoolConexion().getConnection();
			
			nuevo=con.prepareStatement( "INSERT INTO cuentas_bancos(nombre,no_cuenta,id_tipo_cuenta) VALUES (?,?,?)");
			
			nuevo.setString( 1, myCuenta.getNombre() );
			nuevo.setString( 2,myCuenta.getNoCuenta() );
			nuevo.setInt( 3, myCuenta.getIdTipoCuenta());
			
				
			
			
			resultado=nuevo.executeUpdate();
			
			rs=nuevo.getGeneratedKeys(); //obtengo las ultimas llaves generadas
			while(rs.next()){
				this.setIdRegistrado(rs.getInt(1));
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
				 if(nuevo != null)nuevo.close();
	              if(con != null) con.close();
			} // fin de try
			catch ( SQLException excepcionSql )
			{
				excepcionSql.printStackTrace();
				//conexion.desconectar();
			} // fin de catch
		} // fin de finally
	}
	public void setIdRegistrado(int i){
		idRegistrado=i;
	}
	public boolean actualizar(CuentasBancos myCuenta) {
		// TODO Auto-generated method stub
		int resultado=0;
		ResultSet rs=null;
		Connection con = null;
		
		try 
		{
			con = conexion.getPoolConexion().getConnection();
			
			actualizar=con.prepareStatement( "UPDATE cuentas_bancos SET nombre=?,no_cuenta=?,id_tipo_cuenta=? where id=?");
			
			actualizar.setString( 1, myCuenta.getNombre() );
			actualizar.setString( 2,myCuenta.getNoCuenta() );
			
			
			actualizar.setInt( 3, myCuenta.getIdTipoCuenta());
			
			
			actualizar.setInt(4, myCuenta.getId());
			
			
			resultado=actualizar.executeUpdate();
			


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
				 if(actualizar != null)actualizar.close();
	              if(con != null) con.close();
			} // fin de try
			catch ( SQLException excepcionSql )
			{
				excepcionSql.printStackTrace();
				//conexion.desconectar();
			} // fin de catch
		} // fin de finally
	}
	public List<CuentasBancos> getCuentas(int limiteInferior, int limiteSuperior) {
		// TODO Auto-generated method stub
		
		List<CuentasBancos> cuentas=new ArrayList<CuentasBancos>();
				ResultSet res=null;
				
				Connection conn=null;
				
				boolean existe=false;
				
				try {
					
					conn=conexion.getPoolConexion().getConnection();
					buscar=conn.prepareStatement("SELECT cuentas_bancos.nombre,cuentas_bancos.no_cuenta, tipo_cuenta_bancos.tipo_cuenta, cuentas_bancos.id, tipo_cuenta_bancos.observaciones, cuentas_bancos.id_tipo_cuenta  "
							+ "FROM cuentas_bancos INNER JOIN tipo_cuenta_bancos ON cuentas_bancos.id_tipo_cuenta = tipo_cuenta_bancos.id LIMIT ?,?;");
					buscar.setInt(1, limiteInferior);
					buscar.setInt(2, limiteSuperior);
					
					res = buscar.executeQuery();
					while(res.next()){
						
						CuentasBancos una=new CuentasBancos();
						existe=true;
						una.setId(res.getInt("id"));
						una.setNombre(res.getString("nombre"));
						una.setNoCuenta(res.getString("no_cuenta"));
						una.setTipoCuenta(res.getString("tipo_cuenta"));
						una.setIdTipoCuenta(res.getInt("id_tipo_cuenta"));
						cuentas.add(una);
					 }
					
				}catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error, no se conecto");
					System.out.println(e);
			}finally
			{
				try{
					if(res!=null)res.close();
					if(buscar!=null)buscar.close();
					if(conn!=null)conn.close();
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
			} // fin de finally
				
				if (existe) {
					return cuentas;
				}
				else return null;
			}

}
