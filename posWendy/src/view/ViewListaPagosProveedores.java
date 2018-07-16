package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.toedter.calendar.JDateChooser;

import controlador.CtlPagosProveedoresLista;
import view.botones.BotonBuscar;
import view.botones.BotonImprimirSmall;
import view.rendes.PanelPadre;
import view.rendes.RenderizadorTablaFacturas;

import view.tablemodel.TmPagosProveedores;

public class ViewListaPagosProveedores extends ViewTabla {
	
protected BotonImprimirSmall btnImprimir;
	
	
	
	
	
	
	private TmPagosProveedores modelo;


	public ViewListaPagosProveedores(Window view) {
		super(view, "Pagos a proveedores");
		// TODO Auto-generated constructor stub
		

	    btnImprimir = new BotonImprimirSmall();
	    btnImprimir.setEnabled(false);
	    //btnLimpiar.setIcon(new ImageIcon("recursos/clear.png")); // NOI18N
	    panelAccion.add(btnImprimir);
	    //panelAccion.setVisible(false);
	    
	    
	 
        //tabla y sus componentes
		modelo=new TmPagosProveedores();
		tabla.setModel(modelo);
		RenderizadorTablaFacturas renderizador = new RenderizadorTablaFacturas();
		tabla.setDefaultRenderer(String.class, renderizador);
		
		tabla.getColumnModel().getColumn(0).setPreferredWidth(20);     //Tama�o de las columnas de las tablas
		tabla.getColumnModel().getColumn(1).setPreferredWidth(20);	//de las columnas
		tabla.getColumnModel().getColumn(2).setPreferredWidth(250);	//en la tabla
		tabla.getColumnModel().getColumn(3).setPreferredWidth(70);	//
		
		
		
		getContentPane().add(panelSuperior, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		rdbtnFecha.setVisible(true);
		
		
	}
	public TmPagosProveedores getModelo(){
		return modelo;
	}
	
	
	public BotonImprimirSmall getBtnImprimir(){
		return btnImprimir;
	}
	public JRadioButton getRdbtnFecha(){
		return rdbtnFecha;
	}
	
	
	public void conectarControlador(CtlPagosProveedoresLista c){
		this.addWindowListener(c);
		rdbtnTodos.addActionListener(c);
		rdbtnTodos.setActionCommand("TODAS");
		
		rdbtnId.addActionListener(c);
		//rdbtnId.getActionCommand();
		rdbtnId.setActionCommand("ID");
		
		rdbtnFecha.addActionListener(c);
		rdbtnFecha.setActionCommand("FECHA");
		
		
		 btnSiguiente.addActionListener(c);
		 btnSiguiente.setActionCommand("NEXT");
		 
		 btnAnterior.addActionListener(c);
		 btnAnterior.setActionCommand("LAST");
		 
		// super.bbtnLimpiar.addActionListener(c);
		// btnLimpiar.setActionCommand("LIMPIAR");
		
		
		
		
		btnBuscar.addActionListener(c);
		btnBuscar.setActionCommand("BUSCAR");
		
		 btnAgregar.addActionListener(c);
		 btnAgregar.setActionCommand("INSERTAR");
		 
		 btnEliminar.addActionListener(c);
		 btnEliminar.setActionCommand("ANULARFACTURA");
		 
		 btnImprimir.addActionListener(c);
		 btnImprimir.setActionCommand("IMPRIMIR");
		 
		 txtBuscar.addActionListener(c);
		 txtBuscar.setActionCommand("BUSCAR");
		 
		 tabla.addMouseListener(c);
		 tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

}
