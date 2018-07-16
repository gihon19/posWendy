package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.Categoria;
import modelo.Conexion;

public class CategoriaDao {
	
	private int idCategoriaRegistrada;
	private Conexion conexion;
	
	private PreparedStatement buscar = null;
	private PreparedStatement insertar = null;
	private PreparedStatement actualizar = null;
	private PreparedStatement eliminar = null;
	
	
	public CategoriaDao(Conexion conn){
		conexion=conn;
		/*try{
			
			//conexion= new Conexion();
			//seleccionarTodasLasMarcas = conexion.getPoolConexion().getConnection().prepareStatement("SELECT * FROM marcas");
			//insertarNuevaMarca=conexion.getPoolConexion().getConnection().prepareStatement( "INSERT INTO marcas(descripcion,observacion) VALUES (?,?)");
			//actualizarMarca=conexion.getPoolConexion().getConnection().prepareStatement("UPDATE marcas SET descripcion = ?, observacion = ? WHERE codigo_marca = ?");
			//eliminarMarca=conexion.getPoolConexion().getConnection().prepareStatement("DELETE FROM marcas WHERE codigo_marca = ?");
			//buscarMarca=conexion.getPoolConexion().getConnection().prepareStatement("SELECT codigo_marca,descripcion,observacion FROM marcas where codigo_marca =  ?");
			//buscarMarcaObseracion=conexion.getPoolConexion().getConnection().prepareStatement("SELECT codigo_marca,descripcion,observacion FROM marcas where observacion LIKE ? ;");
			///buscarMarcaNombre=conexion.getPoolConexion().getConnection().prepareStatement("SELECT codigo_marca,descripcion,observacion FROM marcas where descripcion LIKE ? ;");
		}
		catch ( SQLException excepcionSql )
		{
			excepcionSql.printStackTrace();
			System.exit( 1 );
		} // fin de catch*/
		
	}
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para eleminar marcas>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	/**
	 * @param id
	 * @return true=se elemino correctamen o false=no se puedo eliminar
	 */
	public boolean eliminarCategoria(int id){
		int resultado=0;
		Connection conn=null;
		try {
			conn=conexion.getPoolConexion().getConnection();
			eliminar=conn.prepareStatement("DELETE FROM marcas WHERE codigo_marca = ?");
			eliminar.setInt( 1, id );
			resultado=eliminar.executeUpdate();
			return true;
			
		} catch (SQLException e) {
				System.out.println(e.getMessage());
				return false;
		}
		finally
		{
			try{
				
				//if(res != null) res.close();
                if(eliminar != null)eliminar.close();
                if(conn != null) conn.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//Sconexion.desconectar();
				} // fin de catch
		} // fin de finally
	}
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para Actualizar los marcas>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	/**
	 * @param myCategoria
	 * @return true o false
	 */
	public boolean actualizarCategoria(Categoria myCategoria){
		
		Connection conn=null;
		
		
		try {
				conn=conexion.getPoolConexion().getConnection();
				actualizar=conn.prepareStatement("UPDATE marcas SET descripcion = ?, observacion = ? WHERE codigo_marca = ?");
				actualizar.setString(1, myCategoria.getMarca());
				actualizar.setString(2, myCategoria.getObservacion());
				
				actualizar.setInt(3, myCategoria.getId());
				
				
				actualizar.executeUpdate();
				return true;
			
		} catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
            
			
		}
		finally
		{
			try{
				
				//if(res != null) res.close();
                if(actualizar != null)actualizar.close();
                if(conn != null) conn.close();
                
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//Sconexion.desconectar();
				} // fin de catch
		} // fin de finally
		
	}
	
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< selecciona de la Bd todas las MArcas>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	/**
	 * @param limite inferior para la paginacion
	 * @param limite superio para la paginacion
	 * @return Lista de categorias
	 */
	public List<Categoria> todoCategorias(int limInf,int limSupe){
		
		List<Categoria> categorias=new ArrayList<Categoria>();
		
		ResultSet res=null;
		
		Connection conn=null;
		
		boolean existe=false;
		
		try {
			
			
			conn=conexion.getPoolConexion().getConnection();
			buscar = conn.prepareStatement("SELECT * FROM marcas ORDER BY codigo_marca DESC LIMIT ?,?;");
			buscar.setInt(1, limInf);
			buscar.setInt(2, limSupe);
			res = buscar.executeQuery();
			while(res.next()){
				Categoria unaCategoria=new Categoria();
				existe=true;
				unaCategoria.setId(Integer.parseInt(res.getString("codigo_marca")));
				unaCategoria.setMarca(res.getString("descripcion"));
				unaCategoria.setObservacion(res.getString("observacion"));
				categorias.add(unaCategoria);
			 }
			//res.close();
			//conexion.desconectar();
					
					
			} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error, no se conecto");
					System.out.println(e);
			}
		finally
		{
			try{
				if(res != null) res.close();
                if(buscar != null)buscar.close();
                if(conn != null) conn.close();
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
			if (existe) {
				return categorias;
			}
			else return null;
			
		}
	
	
	/**
	 * @param id de la categoria registrada
	 */
	public void setIdCategoriaRegistrada(int i){
		idCategoriaRegistrada=i;
	}
	/**
	 * @return id de la categoria registrada
	 */
	public int getIdCategoriaRegistrada(){
		return idCategoriaRegistrada;
	} 
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para agreagar Marcas>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	/**
	 * @param myCategoria
	 * @return true o false
	 */
	public boolean registrarCategoria(Categoria myCategoria)
	{
		
		int resultado=0;
		ResultSet res=null;
		Connection conn=null;
		
		try 
		{
			conn=conexion.getPoolConexion().getConnection();
			insertar=conn.prepareStatement( "INSERT INTO marcas(descripcion,observacion) VALUES (?,?)");
			insertar.setString( 1, myCategoria.getMarca() );
			insertar.setString( 2, myCategoria.getObservacion() );
			
			resultado=insertar.executeUpdate();
			
			res=insertar.getGeneratedKeys(); //obtengo las ultimas llaves generadas
			while(res.next()){
				this.setIdCategoriaRegistrada(res.getInt(1));
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
				if(res != null) res.close();
                if(insertar != null)insertar.close();
                if(conn != null) conn.close();
			} // fin de try
			catch ( SQLException excepcionSql )
			{
				excepcionSql.printStackTrace();
				//conexion.desconectar();
			} // fin de catch
		} // fin de finally
	}
	
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para buscar marca por ID>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	/**
	 * @param id de la categoria
	 * @return categoria entrada sino null
	 */
	public Categoria buscarCategoria(int i){
		
		Categoria unaCategoria=new Categoria();
		
		ResultSet res=null;
		
		boolean existe=false;
		
		Connection conn=null;
		try {
			conn=conexion.getPoolConexion().getConnection();
			buscar=conn.prepareStatement("SELECT codigo_marca,descripcion,observacion FROM marcas where codigo_marca =  ?");
			buscar.setInt(1, i);
			res = buscar.executeQuery();
			while(res.next()){
				existe=true;
				unaCategoria.setId(Integer.parseInt(res.getString("codigo_marca")));
				unaCategoria.setMarca(res.getString("descripcion"));
				unaCategoria.setObservacion(res.getString("observacion"));
				
			 }
					
					
			} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error, no se conecto");
					System.out.println(e);
			}
			finally
			{
				try{
					if(res != null) res.close();
	                if(buscar != null)buscar.close();
	                if(conn != null) conn.close();
				} // fin de try
				catch ( SQLException excepcionSql )
				{
				excepcionSql.printStackTrace();
				//conexion.desconectar();
				} // fin de catch
			} // fin de finally
		
			if (existe) {
				return unaCategoria;
			}
			else return null;
		
		
		
	}
	
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para busca los marcas por observacion>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	/**
	 * @param busqueda
	 * @return lista de categorias encontradas sino null
	 */
	public List<Categoria> buscarCategoriasObservacion(String busqueda){
		List<Categoria> categorias=new ArrayList<Categoria>();
		ResultSet res=null;
		 
		boolean existe=false;
		
		Connection conn=null;
		try {
			conn=conexion.getPoolConexion().getConnection();
			buscar=conn.prepareStatement("SELECT codigo_marca,descripcion,observacion FROM marcas where descripcion LIKE ? ;");
			buscar.setString(1, "%" + busqueda + "%");
			res = buscar.executeQuery();
			//System.out.println(buscarProveedorNombre);
			while(res.next()){
				Categoria unaCategoria=new Categoria();
				existe=true;
				unaCategoria.setId(Integer.parseInt(res.getString("codigo_marca")));
				unaCategoria.setMarca(res.getString("descripcion"));
				unaCategoria.setObservacion(res.getString("observacion"));
				categorias.add(unaCategoria);
			 }
					
					
			} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error, no se conecto");
					System.out.println(e);
			}finally
			{
				try{
					if(res != null) res.close();
	                if(buscar != null)buscar.close();
	                if(conn != null) conn.close();
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
			} // fin de finally
		
			if (existe) {
				return categorias;
			}
			else return null;
		
	}
	
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para busca los marcas por observacion>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	/**
	 * @param busqueda
	 * @return lista de categorias encontradas sino null
	 */
	public List<Categoria> buscarCategorias(String busqueda){
		List<Categoria> categorias=new ArrayList<Categoria>();
		ResultSet res=null;
		Connection conn=null;
		boolean existe=false;
		try {
			
			conn=conexion.getPoolConexion().getConnection();
			buscar=conn.prepareStatement("SELECT codigo_marca,descripcion,observacion FROM marcas where observacion LIKE ? ;");
			buscar.setString(1, "%" + busqueda + "%");
			res = buscar.executeQuery();
			//System.out.println(buscarProveedorNombre);
			while(res.next()){
				Categoria unaCategoria=new Categoria();
				existe=true;
				unaCategoria.setId(Integer.parseInt(res.getString("codigo_marca")));
				unaCategoria.setMarca(res.getString("descripcion"));
				unaCategoria.setObservacion(res.getString("observacion"));
				categorias.add(unaCategoria);
			 }
					
					
			} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error, no se conecto");
					System.out.println(e);
			}finally
			{
				try{
					if(res != null) res.close();
	                if(buscar != null)buscar.close();
	                if(conn != null) conn.close();
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
			} // fin de finally
		
			if (existe) {
				return categorias;
			}
			else return null;
		
	}
	
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<cierra la conexi�n a la base de datos>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public void close()
		{
			if(buscar!=null)
				try {
					buscar.close();
					if(insertar!=null)insertar.close();
					if(actualizar!=null)actualizar.close();
					if(eliminar!=null)eliminar.close();
					if(buscar!=null)buscar.close();
					if(buscar!=null)buscar.close();
					if(buscar!=null)buscar.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
			//conexion.desconectar();
		} //// fin del m�todo close
	

}
