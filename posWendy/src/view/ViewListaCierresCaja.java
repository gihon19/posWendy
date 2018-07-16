package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controlador.CtlCierresCajaLista;
import view.botones.BotonAgregar;
import view.botones.BotonBuscar;
import view.botones.BotonEliminar;
import view.botones.BotonImprimirSmall;
import view.botones.BotonLimpiar;
import view.rendes.PanelPadre;
import view.rendes.RenderizadorTablaFactura;
import view.rendes.RenderizadorTablaFacturaCompra;
import view.rendes.TablaRenderizadorProveedor;
import view.tablemodel.TmCierres;
import javax.swing.JLabel;
import com.toedter.calendar.JDateChooser;

public class ViewListaCierresCaja extends ViewTabla {
	
	
	protected JButton btnLimpiar;
	
	
	
	private TmCierres modelo;
	private JLabel lblA;
	

	public ViewListaCierresCaja(Window view) {
		// TODO Auto-generated constructor stub
		super(view,"Cierres de caja");
		Init();
	}
	public void Init() {
		
	
        
        btnLimpiar = new BotonImprimirSmall();
        //btnLimpiar.setIcon(new ImageIcon(ViewListaMarca.class.getResource("/View/imagen/clear.png"))); // NOI18N
        panelAccion.add(btnLimpiar);
        
        rdbtnFecha.setVisible(true);
		
		//tabla y sus componentes
		modelo=new TmCierres();
		
		tabla.setModel(modelo);
		RenderizadorTablaFacturaCompra renderizador = new RenderizadorTablaFacturaCompra();
		tabla.setDefaultRenderer(String.class, renderizador);
		
		
		tabla.getColumnModel().getColumn(0).setPreferredWidth(100);     //Tama�o de las columnas de las tablas
		tabla.getColumnModel().getColumn(1).setPreferredWidth(100);	//
		tabla.getColumnModel().getColumn(2).setPreferredWidth(100);	//
		tabla.setAutoCreateRowSorter(true);
		//setSize(900,591);
		
		this.btnEliminar.setEnabled(false);
		//se hace visible
		//setVisible(true);
		
		
		
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	
	}
	
	public TmCierres getModelo(){
		return this.modelo;
	}
	
	
	public void conectarCtl(CtlCierresCajaLista c){
		btnAgregar.addActionListener(c);
		btnAgregar.setActionCommand("INSERTAR");
		
		btnEliminar.addActionListener(c);
		btnEliminar.setActionCommand("ELIMINAR");
		
		btnLimpiar.addActionListener(c);
		btnLimpiar.setActionCommand("IMPRIMIR");
		
		btnBuscar.addActionListener(c);
		btnBuscar.setActionCommand("BUSCAR");
		
		 btnSiguiente.addActionListener(c);
		 btnSiguiente.setActionCommand("NEXT");
		 
		 btnAnterior.addActionListener(c);
		 btnAnterior.setActionCommand("LAST");
		
		
		
		tabla.addMouseListener(c);
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
	}

}
