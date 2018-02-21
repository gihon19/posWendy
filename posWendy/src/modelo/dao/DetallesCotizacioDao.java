package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import modelo.Articulo;
import modelo.Conexion;
import modelo.DetalleFactura;

public class DetallesCotizacioDao {
	
	
	private PreparedStatement agregarDetalle=null;
	private PreparedStatement actualizarDetalle=null;
	private PreparedStatement detallesFacturaPendiente=null;
	private Conexion conexion=null;
	private PreparedStatement elimiarTem = null;
	private InventarioDao inventarioDao;
	private KardexDao kardexDao;
	private ArticuloDao articuloDao=null;

	public DetallesCotizacioDao(Conexion conn) {
		// TODO Auto-generated constructor stub
		conexion=conn;
		inventarioDao=new InventarioDao(conexion);
		kardexDao=new KardexDao(conexion);
		articuloDao=new ArticuloDao(conexion);
	}

	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Metodo para agreagar detalles de facturas>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	public boolean agregarDetalle(DetalleFactura detalle, int idFactura) {
		boolean resultado=false;
		
		String sql="INSERT INTO detalle_cotizacion("
				+ "numero_cotizacion,"
				+ "codigo_articulo,"
				+ "precio,"
				+ "cantidad,"
				+ "impuesto,"
				+ "subtotal,"
				+ "descuento,"
				+ "total,"
				+ "codigo_barra"
				+ ") VALUES (?,?,?,?,?,?,?,?,?)";
		Connection conn=null;
		
		try{
			conn=conexion.getPoolConexion().getConnection();
			agregarDetalle=conn.prepareStatement( sql);
			
			agregarDetalle.setInt(1, idFactura);
			agregarDetalle.setInt(2, detalle.getArticulo().getId());
			agregarDetalle.setDouble(3, detalle.getArticulo().getPrecioVenta());
			agregarDetalle.setBigDecimal(4, detalle.getCantidad());
			agregarDetalle.setBigDecimal(5, detalle.getImpuesto());
			agregarDetalle.setBigDecimal(6, detalle.getSubTotal());
			agregarDetalle.setBigDecimal(7, detalle.getDescuentoItem());
			agregarDetalle.setBigDecimal(8, detalle.getTotal());
			agregarDetalle.setString(9, detalle.getArticulo().getCodigoBarra());
			agregarDetalle.executeUpdate();
			
			resultado=true;
		}catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
			resultado= false;
		}
		finally
		{
			try{
				
				//if(res != null) res.close();
	            if(agregarDetalle != null)agregarDetalle.close();
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

	public List<DetalleFactura> detallesFacturaPendiente(int idFactura) {
		
		
		List<DetalleFactura> detalles=new ArrayList<DetalleFactura>();
		
	
	    Connection con = null;
	
		
		ResultSet res=null;
		
		boolean existe=false;
		try {
			con = conexion.getPoolConexion().getConnection();
			
			detallesFacturaPendiente = con.prepareStatement("SELECT * FROM v_detalle_cotizacion where numero_cotizacion_detalle=? order by id ASC;");
			detallesFacturaPendiente.setInt(1, idFactura);
			
			res = detallesFacturaPendiente.executeQuery();
			while(res.next()){
				DetalleFactura unDetalle=new DetalleFactura();
				existe=true;
				//se consigue el articulo del detalles
				
				Articulo articuloDetalle=new Articulo();//articuloDao.buscarArticulo(res.getInt("codigo_articulo"));
				articuloDetalle.setId(res.getInt("codigo_articulo"));
				articuloDetalle.setArticulo(res.getString("articulo"));
				articuloDetalle.setPrecioVenta(res.getDouble("precio_detalle"));//se estable el precio del articulo
				unDetalle.setListArticulos(articuloDetalle);//se agrega el articulo al 
				unDetalle.setCantidad(res.getBigDecimal("cantidad_detalle"));
				unDetalle.setImpuesto(res.getBigDecimal("impuesto_detalle"));
				unDetalle.setSubTotal(res.getBigDecimal("subtotal_detalle"));
				unDetalle.setDescuentoItem(res.getBigDecimal("descuento_detalle"));
				unDetalle.setTotal(res.getBigDecimal("total_detalle"));
				
				
				
				
				detalles.add(unDetalle);
			 }
					
			} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error, no se conecto");
					System.out.println(e);
			}
		finally
		{
			try{
				
				if(res != null) res.close();
	            if(detallesFacturaPendiente != null)detallesFacturaPendiente.close();
	            if(con != null) con.close();
	            
				
				} // fin de try
				catch ( SQLException excepcionSql )
				{
					excepcionSql.printStackTrace();
					//conexion.desconectar();
				} // fin de catch
		} // fin de finally
		
		
			if (existe) {
				return detalles;
			}
			else return null;
	}

}
