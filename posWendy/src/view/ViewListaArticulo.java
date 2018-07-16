package view;


import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.Window;



import javax.swing.JButton;

import javax.swing.JRadioButton;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;


import view.botones.BotonBarcode;
import view.botones.BotonBuscar;

import view.botones.BotonKardex;
import view.botones.BotonLimpiar;

import view.rendes.TablaRenderizadorProveedor;
import view.tablemodel.TableModeloArticulo;
import controlador.CtlArticuloBuscar;
import controlador.CtlArticuloLista;

public class ViewListaArticulo extends ViewTabla {
	
	
	
	
	
	protected JButton btnLimpiar;
	
	
	private JRadioButton rdbtnMarca;

	private TableModeloArticulo modelo;
	
	
	private BotonKardex btnKardex;
	private BotonLimpiar btnInventario;


	private JRadioButton rdbtnArticulo;

	
	
	
	public ViewListaArticulo(Window view){
		super(view,"Articulos");
		

        
        btnLimpiar = new BotonBarcode();
        panelAccion.add(btnLimpiar);
        btnLimpiar.setEnabled(false);
        
        btnKardex=new BotonKardex();
        panelAccion.add(btnKardex);
        
        btnInventario=new BotonLimpiar();
        panelAccion.add(btnInventario);
        
		
		
		
		rdbtnArticulo = new JRadioButton("Articulo",false);
		panelOpcioneBusqueda.add(rdbtnArticulo);
		grupoOpciones.add(rdbtnArticulo);
		
		rdbtnMarca = new JRadioButton("Categoria",false);
		panelOpcioneBusqueda.add(rdbtnMarca);
		grupoOpciones.add(rdbtnMarca);
		
        
        //tabla y sus componentes
		modelo=new TableModeloArticulo();
		
		tabla.setModel(modelo);
		TablaRenderizadorProveedor renderizador = new TablaRenderizadorProveedor();
		tabla.setDefaultRenderer(String.class, renderizador);
		
		tabla.getColumnModel().getColumn(0).setPreferredWidth(5);     //Tamaño de las columnas de las tablas
		tabla.getColumnModel().getColumn(1).setPreferredWidth(200);	//
		tabla.getColumnModel().getColumn(2).setPreferredWidth(100);	//
		tabla.getColumnModel().getColumn(3).setPreferredWidth(10);		//

		
	}
	
	
	public void conectarControlador(CtlArticuloLista c){
		
		
		this.addWindowListener(c);
		
		
		rdbtnId.addActionListener(c);
		rdbtnId.setActionCommand("ID");
		
		rdbtnArticulo.addActionListener(c);
		rdbtnArticulo.setActionCommand("ESCRIBIR");
		
		rdbtnMarca.addActionListener(c);
		rdbtnMarca.setActionCommand("ESCRIBIR");
		
		btnBuscar.addActionListener(c);
		btnBuscar.setActionCommand("BUSCAR");
		
		 btnAgregar.addActionListener(c);
		 btnAgregar.setActionCommand("INSERTAR");
		 
		 btnEliminar.addActionListener(c);
		 btnEliminar.setActionCommand("ELIMINAR");
		 
		 btnLimpiar.addActionListener(c);
		 btnLimpiar.setActionCommand("LIMPIAR");
		 
		 txtBuscar.addActionListener(c);
		 txtBuscar.setActionCommand("BUSCAR");
		 
		 btnSiguiente.addActionListener(c);
		 btnSiguiente.setActionCommand("NEXT");
		 
		 btnAnterior.addActionListener(c);
		 btnAnterior.setActionCommand("LAST");
		 
		 btnKardex.addActionListener(c);
		 btnKardex.setActionCommand("KARDEX");
		 
		 btnInventario.addActionListener(c);
		 btnInventario.setActionCommand("INVENTARIO");
		 
		 tabla.addMouseListener(c);
		 tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	
	
	public TableModeloArticulo getModelo(){
		return modelo;
	}
	public JButton getBtnEliminar(){
		return btnEliminar;
	}
	
	public JRadioButton getRdbtnArticulo(){
		return rdbtnArticulo;
	}
	public JRadioButton getRdbtnMarca(){
		return  rdbtnMarca;
		
	}
	public JButton getBtnLimpiar(){
		return btnLimpiar;
	}
	
public void conectarControladorBuscar(CtlArticuloBuscar c){
	
		this.addWindowListener(c);
	
	
		rdbtnTodos.addKeyListener(c);
		
		rdbtnId.addActionListener(c);
		rdbtnId.setActionCommand("ID");
		rdbtnId.addKeyListener(c);
		
		rdbtnArticulo.addActionListener(c);
		rdbtnArticulo.setActionCommand("ESCRIBIR");
		rdbtnArticulo.addKeyListener(c);
		
		rdbtnMarca.addActionListener(c);
		rdbtnMarca.setActionCommand("ESCRIBIR");
		rdbtnMarca.addKeyListener(c);
		
		btnBuscar.addActionListener(c);
		btnBuscar.setActionCommand("BUSCAR");
		btnBuscar.addKeyListener(c);
		
		 btnAgregar.addActionListener(c);
		 btnAgregar.setActionCommand("INSERTAR");
		 btnAgregar.addKeyListener(c);
		 
		 btnEliminar.addActionListener(c);
		 btnEliminar.setActionCommand("ELIMINAR");
		 btnEliminar.addKeyListener(c);
		 
		 btnLimpiar.addActionListener(c);
		 btnLimpiar.setActionCommand("LIMPIAR");
		 btnLimpiar.addKeyListener(c);
		 
		
		 
		 
		 
		 
		 txtBuscar.addActionListener(c);
		 txtBuscar.setActionCommand("BUSCAR");
		 txtBuscar.addKeyListener(c);
		 
		 tabla.addMouseListener(c);
		 tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 tabla.addKeyListener(c);
		 
	}
	

}