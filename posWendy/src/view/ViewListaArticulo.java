package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JPopupMenu;

import view.botones.BotonAgregar;
import view.botones.BotonBarcode;
import view.botones.BotonBuscar;
import view.botones.BotonEliminar;
import view.botones.BotonKardex;
import view.botones.BotonLimpiar;
import view.rendes.PanelPadre;
import view.rendes.TablaRenderizadorProveedor;
import view.tablemodel.TableModeloArticulo;
import controlador.CtlArticuloBuscar;
import controlador.CtlArticuloLista;

public class ViewListaArticulo extends JDialog {
	
	
	protected BorderLayout miEsquema;
	protected GridLayout miEsquemaTabla;
	
	protected JPanel panelAccion;
	protected JPanel panelSuperior;
	protected JPanel panelBusqueda;
	protected JPanel panelPaginacion;
	
	
	protected BotonAgregar btnAgregar;
	protected BotonEliminar btnEliminar;
	protected JButton btnLimpiar;
	
	
	private JRadioButton rdbtnId;
	private JRadioButton rdbtnArticulo;
	private JRadioButton rdbtnMarca;
	private ButtonGroup grupoOpciones; // grupo de botones que contiene los botones de opción
	private JRadioButton rdbtnTodos;
	protected BotonBuscar btnBuscar;
	protected JTextField txtBuscar;
	
	
	
	private JTable tablaArticulos;
	private TableModeloArticulo modelo;
	
	private JButton btnSiguiente;
	private JButton btnAnterior;
	private JTextField txtPagina;
	private BotonKardex btnKardex;
	private BotonLimpiar btnInventario;

	
	
	
	public ViewListaArticulo(Window view){
		super(view,"Articulos",Dialog.ModalityType.DOCUMENT_MODAL);
		miEsquema=new BorderLayout();
		//this.setTitle("Articulos");
		getContentPane().setLayout(miEsquema);
		
		
		
		//creacion de los paneles
		panelAccion=new PanelPadre();
		panelBusqueda=new PanelPadre();
		panelSuperior=new PanelPadre();
		panelPaginacion=new PanelPadre();
		
		panelAccion.setBorder(new TitledBorder(new LineBorder(new Color(130, 135, 144)), "Acciones de registro", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelBusqueda.setBorder(new TitledBorder(new LineBorder(new Color(130, 135, 144)), "Busqueda de registros", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		//agregar componentes al panel acciones
		btnAgregar = new BotonAgregar();
		btnAgregar.setMnemonic('r');
		panelAccion.add(btnAgregar);
       
		btnEliminar = new BotonEliminar();
        btnEliminar.setEnabled(false);
        panelAccion.add(btnEliminar);
        
        btnLimpiar = new BotonBarcode();
       // btnLimpiar.setIcon(new ImageIcon(ViewListaArticulo.class.getResource("/View/imagen/clear.png"))); // NOI18N
        panelAccion.add(btnLimpiar);
        btnLimpiar.setEnabled(false);
        
        btnKardex=new BotonKardex();
        panelAccion.add(btnKardex);
        
        btnInventario=new BotonLimpiar();
        panelAccion.add(btnInventario);
        
        
        //configuracion del panel busqueda
        grupoOpciones = new ButtonGroup(); // crea ButtonGroup
        rdbtnTodos = new JRadioButton("Todos");
		rdbtnTodos.setSelected(true);
		panelBusqueda.add(rdbtnTodos);
		grupoOpciones.add(rdbtnTodos);
		
		//opciones de busquedas
		rdbtnId = new JRadioButton("ID",false);
		panelBusqueda.add(rdbtnId);
		grupoOpciones.add(rdbtnId);
		
		rdbtnArticulo = new JRadioButton("Articulo",false);
		panelBusqueda.add(rdbtnArticulo);
		grupoOpciones.add(rdbtnArticulo);
		
		rdbtnMarca = new JRadioButton("Marca",false);
		panelBusqueda.add(rdbtnMarca);
		grupoOpciones.add(rdbtnMarca);
		
		//elementos del panel buscar
		txtBuscar=new JTextField(10);
		panelBusqueda.add(txtBuscar);
				
		btnBuscar=new BotonBuscar();
		panelBusqueda.add(btnBuscar);
        
        //tabla y sus componentes
		modelo=new TableModeloArticulo();
		tablaArticulos=new JTable();
		tablaArticulos.setModel(modelo);
		TablaRenderizadorProveedor renderizador = new TablaRenderizadorProveedor();
		tablaArticulos.setDefaultRenderer(String.class, renderizador);
		
		tablaArticulos.getColumnModel().getColumn(0).setPreferredWidth(5);     //Tamaño de las columnas de las tablas
		tablaArticulos.getColumnModel().getColumn(1).setPreferredWidth(200);	//
		tablaArticulos.getColumnModel().getColumn(2).setPreferredWidth(100);	//
		tablaArticulos.getColumnModel().getColumn(3).setPreferredWidth(10);		//
		
		JScrollPane scrollPane = new JScrollPane(tablaArticulos);
		scrollPane.setBounds(36, 97, 742, 136);
		
		
		//configuracion de los paneles
		panelSuperior.add(panelAccion);
		panelSuperior.add(panelBusqueda);
		getContentPane().add(panelSuperior, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
		getContentPane().add(panelPaginacion, BorderLayout.SOUTH);
		panelPaginacion.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnAnterior = new JButton("Anterior");
		panelPaginacion.add(btnAnterior);
		
		txtPagina = new JTextField();
		txtPagina.setEditable(false);
		txtPagina.setHorizontalAlignment(SwingConstants.CENTER);
		txtPagina.setText("1");
		panelPaginacion.add(txtPagina);
		txtPagina.setColumns(4);
		
		btnSiguiente = new JButton("Siguiente");
		panelPaginacion.add(btnSiguiente);
		
		//setSize(741,600);
		//se hace visible
		//setVisible(true);
		this.setResizable(false);
		
		setSize(799,600);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		
		
	
		
	}
	
	
	public void conectarControlador(CtlArticuloLista c){
		
		
		this.addWindowListener(c);
		
		
		rdbtnId.addActionListener(c);
		//rdbtnId.getActionCommand();
		rdbtnId.setActionCommand("ID");
		
		rdbtnArticulo.addActionListener(c);
		rdbtnArticulo.setActionCommand("ARTICULO");
		
		rdbtnMarca.addActionListener(c);
		rdbtnMarca.setActionCommand("MARCA");
		
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
		 
		 tablaArticulos.addMouseListener(c);
		 tablaArticulos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public JTextField getTxtPagina(){
		return txtPagina;
	}
	
	public JTable getTablaArticulos(){
		return tablaArticulos;
	}
	public TableModeloArticulo getModelo(){
		return modelo;
	}
	public JButton getBtnEliminar(){
		return btnEliminar;
	}
	public JRadioButton getRdbtnId(){
		return rdbtnId;
	}
	public JTextField getTxtBuscar(){
		return txtBuscar;
	}
	public JRadioButton getRdbtnArticulo(){
		return rdbtnArticulo;
	}
	public JRadioButton getRdbtnMarca(){
		return  rdbtnMarca;
		
	}
	public JRadioButton getRdbtnTodos(){
		return rdbtnTodos;
		
	}
	public BotonAgregar getBtnAgregar(){
		return btnAgregar;
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
		rdbtnArticulo.setActionCommand("ARTICULO");
		rdbtnArticulo.addKeyListener(c);
		
		rdbtnMarca.addActionListener(c);
		rdbtnMarca.setActionCommand("MARCA");
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
		 
		 tablaArticulos.addMouseListener(c);
		 tablaArticulos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 tablaArticulos.addKeyListener(c);
		 
	}
	

}