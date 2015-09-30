package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import modelo.Articulo;
import modelo.Conexion;
import modelo.Empleado;

public class EmpleadoDao {
	
	private PreparedStatement seleccionarTodos=null;
	private Conexion conexion=null;

	public EmpleadoDao(Conexion conn) {
		// TODO Auto-generated constructor stub
		conexion=conn;
	}
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para seleccionar todos los articulos>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public Vector<Empleado> todoEmpleadosVendedores(){
		
		
		
        Connection con = null;
        
        
      
        Vector<Empleado> vendedores=new Vector<Empleado>();
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			seleccionarTodos = con.prepareStatement("SELECT * FROM v_empleados WHERE v_empleados.codigo_tipo_empleado = 1");
			
			res = seleccionarTodos.executeQuery();
			while(res.next()){
				Empleado unEmpleado=new Empleado();
				existe=true;
				unEmpleado.setCodigo(res.getInt("codigo_empleado"));
				unEmpleado.setNombre(res.getString("nombre"));
				unEmpleado.setApellido(res.getString("apellido"));
				/*unArticulo.setId(Integer.parseInt(res.getString("codigo_articulo")));
				unArticulo.setArticulo(res.getString("articulo"));
				unArticulo.getMarcaObj().setMarca(res.getString("marca"));
				unArticulo.getMarcaObj().setId(res.getInt("codigo_marca"));
				unArticulo.getImpuestoObj().setPorcentaje(res.getString("impuesto"));
				unArticulo.getImpuestoObj().setId(res.getInt("codigo_impuesto"));
				unArticulo.setPrecioVenta(res.getDouble("precio_articulo"));
				unArticulo.setTipoArticulo(res.getInt("tipo_articulo"));*/
				
				
				vendedores.add(unEmpleado);
			 }
					
			} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error, no se conecto");
					System.out.println(e);
			}
		finally
		{
			try{
				
				if(res != null) res.close();
                if(seleccionarTodos != null)seleccionarTodos.close();
                if(con != null) con.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return vendedores;
			}
			else return null;
		
	}

}
