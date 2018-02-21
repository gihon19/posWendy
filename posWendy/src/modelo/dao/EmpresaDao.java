package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import modelo.Conexion;

public class EmpresaDao {
	
	private Conexion conexion=null;
	private PreparedStatement seleccionarCAIactivo=null;
	private PreparedStatement seleccionarTodasLosArticulos=null;
	private PreparedStatement eliminarArticulo = null;

	public EmpresaDao(Conexion conn) {
		// TODO Auto-generated constructor stub
		conexion=conn;
	}
	
	public int getIdCaiAct(){
		
		Integer codCai=0;
		ResultSet res=null;
		PreparedStatement buscarArt=null;
		Connection conn=null;
		boolean existe=false;
		
		try {
			conn=conexion.getPoolConexion().getConnection();
			buscarArt=conn.prepareStatement("SELECT * FROM datos_factura ORDER BY codigo_rango desc limit 1");
			
			res = buscarArt.executeQuery();
			while(res.next()){
				existe=true;
				codCai=res.getInt("codigo_rango");
				//unArticulo.setPreciosVenta(preciosDao.getPreciosArticulo(unArticulo.getId()));
				
			 }
					
					
			} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error, no se conecto");
					System.out.println(e);
			}
			finally
			{
				try{
					if(res != null) res.close();
	                if(buscarArt != null)buscarArt.close();
	                if(conn != null) conn.close();
				} // fin de try
				catch ( SQLException excepcionSql )
				{
				excepcionSql.printStackTrace();
				//conexion.desconectar();
				} // fin de catch
			} // fin de finally
		
			if (existe) {
				return codCai;
			}
			else return  -1;
	}

}
