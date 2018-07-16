package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import modelo.AbstractJasperReports;
import modelo.Conexion;
import modelo.Empleado;
import modelo.SalidaCaja;
import modelo.dao.EmpleadoDao;
import modelo.dao.SalidaCajaDao;
import view.ViewListaEmpleados;
import view.ViewSalidaCaja;

public class CtlSalidaCaja implements ActionListener, KeyListener {
	
	private ViewSalidaCaja view;
	private Conexion conexion;
	private SalidaCaja mySalida;
	private SalidaCajaDao myDao;
	private boolean resul=false;
	private Empleado myEmpleado=null;
	private EmpleadoDao myEmpleadoDao;
	private static final Pattern numberPattern=Pattern.compile("-?\\d+");

	public CtlSalidaCaja(ViewSalidaCaja v , Conexion conn) {
		// TODO Auto-generated constructor stub
		conexion=conn;
		view=v;
		
		mySalida=new SalidaCaja();
		
		myEmpleadoDao=new EmpleadoDao(conexion);
		
		//se busca el empleado por defecto es con el id 0
		myEmpleado=myEmpleadoDao.buscarPorId(1);
		if(myEmpleado!=null){
			view.getTxtEmpleado().setText(myEmpleado.toString());
		}

		
		myDao=new SalidaCajaDao(conexion);
		
		view.conectarControlador(this);
		view.setVisible(true);
		
		
	}
	
	public SalidaCaja getSalidaCaja(){
		return this.mySalida;
	}
	public boolean getResultado(){
		
		return resul;
	}
	private static boolean isNumber(String string){
		return string !=null && numberPattern.matcher(string).matches();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando=e.getActionCommand();
		
		switch(comando){
		
		case "GUARDAR":
			if(validar()){
				setSalida();
				if(myDao.registrarSalida(mySalida)){
					resul=true;
					
					try {
						//AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Cierre_Caja_Saint_Paul.jasper",1 );
						AbstractJasperReports.createReportSalidaCaja(conexion.getPoolConexion().getConnection(), myDao.getIdRegistrado());
						
						//AbstractJasperReports.Imprimir2();
						//JOptionPane.showMessageDialog(view, "Se realizo el corte correctamente.");
						
						view.setVisible(false);
						AbstractJasperReports.imprimierFactura();
						AbstractJasperReports.showViewer(view);
						
						//this.view.setModal(false);
						//AbstractJasperReports.imprimierFactura();
						
					} catch (SQLException ee) {
						// TODO Auto-generated catch block
						ee.printStackTrace();
					}
				}else{
					JOptionPane.showMessageDialog(view, "Ocurrio un problema para registrar la salida");
				}
				
			}
			
			break;
			
		case "CANCELAR":
			view.setVisible(false);
			break;
		}
		
	}

	private void setSalida() {
		// TODO Auto-generated method stub
		mySalida.setCantidad(new BigDecimal(view.getTxtCantidad().getText()));
		mySalida.setConcepto(view.getTxtConcepto().getText());
		mySalida.setEmpleado(myEmpleado);
	}

	private boolean validar() {
		// TODO Auto-generated method stub
		boolean resul=false;
		if(view.getTxtCantidad().getText().trim().length()==0 ){
			JOptionPane.showMessageDialog(view, "Debe rellenar todos los campos","Erro validacion",JOptionPane.ERROR_MESSAGE);
			view.getTxtCantidad().requestFocusInWindow();
		}else
			if(view.getTxtConcepto().getText().trim().length()==0){
				JOptionPane.showMessageDialog(view, "Debe rellenar todos los campos","Erro validacion",JOptionPane.ERROR_MESSAGE);
				view.getTxtConcepto().requestFocusInWindow();
			}else if(!isNumber(view.getTxtCantidad().getText())){
				
						JOptionPane.showMessageDialog(view, "La cantidad debe ser un numero","Erro validacion",JOptionPane.ERROR_MESSAGE);
						view.getTxtCantidad().selectAll();
						view.getTxtCantidad().requestFocusInWindow();
					}
					else{
							resul=true;
						}
		return resul;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		switch(e.getKeyCode()){
		
			case KeyEvent.VK_F1:
				buscarEmpleado();
				break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
			
			myEmpleado=null;
			mySalida=null;
			
	        view.setVisible(false);
	        view=null;
	      }
		
	}
	
	public void buscarEmpleado(){
		ViewListaEmpleados viewBuscarEmpleado=new ViewListaEmpleados(view);
		CtlEmpleadosListaBuscar ctBuscarEmpleado=new CtlEmpleadosListaBuscar(viewBuscarEmpleado,this.conexion);
		viewBuscarEmpleado.pack();
		boolean resultado=ctBuscarEmpleado.buscar();
		
		if(resultado){
			myEmpleado=ctBuscarEmpleado.getEmpleadoSelected();
			view.getTxtEmpleado().setText(myEmpleado.toString());
		}
		
	}

	
}
