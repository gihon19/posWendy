package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modelo.Conexion;
import modelo.ReciboPagoProveedor;
import modelo.dao.ReciboPagoDao;
import view.ViewListaPagosProveedores;
import view.ViewPagoProveedor;

public class CtlPagosProveedoresLista implements ActionListener, MouseListener, ChangeListener, WindowListener {
	
	private ViewListaPagosProveedores view;
	private Conexion conexion=null;
	private ReciboPagoProveedor myRecibo=null;
	private ReciboPagoDao reciboDao=null;
	
	public CtlPagosProveedoresLista(ViewListaPagosProveedores v, Conexion conn){
		view =v;
		conexion=conn;
		view.conectarControlador(this);
		myRecibo=new ReciboPagoProveedor();
		reciboDao=new ReciboPagoDao(conexion);
		cargarTabla(reciboDao.todosReciboProveedor(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
		view.setVisible(true);
	}
	
	public void cargarTabla(List<ReciboPagoProveedor> pagos){
		//JOptionPane.showMessageDialog(view, " "+facturas.size());
		this.view.getModelo().limpiar();
		
		if(pagos!=null){
			for(int c=0;c<pagos.size();c++){
				this.view.getModelo().agregarPago(pagos.get(c));
				
			}
		}
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		String comando=e.getActionCommand();
		
		switch (comando){
		
		case "INSERTAR":
			ViewPagoProveedor vPagoProveedores=new ViewPagoProveedor(view);
			CtlPagoProveedor cPagoProveedores=new CtlPagoProveedor(vPagoProveedores,conexion);
			vPagoProveedores.dispose();
			cPagoProveedores=null;
			view.getModelo().setPaginacion();
			cargarTabla(reciboDao.todosReciboProveedor(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			break;
		case "FECHA":
			
			
		case "TODAS":
			
			
			break;
		case "ID":
			
			
			break;
		case "BUSCAR":
			
			//si la busqueda es por id
			if(this.view.getRdbtnId().isSelected()){
				myRecibo=reciboDao.reciboProveedorPorNo(Integer.parseInt(this.view.getTxtBuscar().getText()));
				if(myRecibo!=null){												
					this.view.getModelo().limpiar();
					this.view.getModelo().agregarPago(myRecibo); 
				}else{
					JOptionPane.showMessageDialog(view, "No se encuentro la factura");
				}
				
			}
			//si la busqueda es por fecha
			if(this.view.getRdbtnFecha().isSelected()){  
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date1 = sdf.format(this.view.getDcFecha1().getDate());
				String date2 = sdf.format(this.view.getDcFecha2().getDate());
				
				cargarTabla(reciboDao.reciboProveedorPorFecha(date1,date2));
				//this.view.getTxtBuscar1().setText("");
				//this.view.getTxtBuscar2().setText("");
				}
			
			
			//si la busqueda son tadas
			if(this.view.getRdbtnTodos().isSelected()){  
				cargarTabla(reciboDao.todosReciboProveedor(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
				this.view.getTxtBuscar().setText("");
				}
			break;
		
			
		case "NEXT":
			view.getModelo().netPag();
			cargarTabla(reciboDao.todosReciboProveedor(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			
			view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
			break;
		case "LAST":
			view.getModelo().lastPag();
			cargarTabla(reciboDao.todosReciboProveedor(view.getModelo().getLimiteInferior(),view.getModelo().getLimiteSuperior()));
			
			view.getTxtPagina().setText(""+view.getModelo().getNoPagina());
			break;
		}

	
		
	}

}
