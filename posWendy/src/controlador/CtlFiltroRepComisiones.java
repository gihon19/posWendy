package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import modelo.AbstractJasperReports;
import modelo.Conexion;
import view.ViewFiltroComisiones;

public class CtlFiltroRepComisiones implements ActionListener {
	
	
	private Conexion conexion;
	private ViewFiltroComisiones view;

	public CtlFiltroRepComisiones(ViewFiltroComisiones v,Conexion conn) {
		// TODO Auto-generated constructor stub
		conexion=conn;
		view =v;
		view.conectarCtl(this);
		view.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		String comando=e.getActionCommand();
		
		switch(comando){
		
			case "GENERAR":
					
					try {
						
						AbstractJasperReports.createReportComisiones(conexion.getPoolConexion().getConnection(),view.getBuscar1().getDate(), view.getBuscar2().getDate());
						
						AbstractJasperReports.showViewer(view);
					} catch (SQLException ee) {
						// TODO Auto-generated catch block
						ee.printStackTrace();
					}
					
			break;
		}
		
	}

}
