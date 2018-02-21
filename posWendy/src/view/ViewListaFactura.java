package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import view.botones.BotonAgregar;
import view.botones.BotonBuscar;
import view.botones.BotonCobrarSmall;
import view.botones.BotonEliminar;
import view.rendes.PanelPadre;
import view.rendes.RenderizadorTablaFacturas;
import view.tablemodel.TablaModeloFacturas;
import controlador.CtlArticuloLista;
import controlador.CtlFacturaLista;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ViewListaFactura extends JDialog {
	protected BorderLayout miEsquema;
	protected GridLayout miEsquemaTabla;
	
	protected JPanel panelAccion;
	protected JPanel panelSuperior;
	protected JPanel panelBusqueda;
	
	
	protected BotonAgregar btnAgregar;
	protected BotonEliminar btnEliminar;
	protected BotonCobrarSmall btnCobrar;
	
	
	private JRadioButton rdbtnId;
	private JRadioButton rdbtnCliente;
	private JRadioButton rdbtnFecha;
	private ButtonGroup grupoOpciones; // grupo de botones que contiene los botones de opci�n
	private JRadioButton rdbtnTodos;
	protected BotonBuscar btnBuscar;
	protected JTextField txtBuscar;
	
	private JFormattedTextField txtBuscarFecha;
	private static final DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
	
	
	
	private JTable tablaFacturas;
	private TablaModeloFacturas modelo;
	
	public ViewListaFactura(Window view){
		
		miEsquema=new BorderLayout();
		this.setTitle("Cotizaciones");
		this.setLocationRelativeTo(view);
		this.setModal(true);
		getContentPane().setLayout(miEsquema);
		
		
		
		//creacion de los paneles
		panelAccion=new PanelPadre();
		panelBusqueda=new PanelPadre();
		panelSuperior=new PanelPadre();
		//panelSuperior.setBackground(Color.CYAN);
		
		panelAccion.setBorder(new TitledBorder(new LineBorder(new Color(130, 135, 144)), "Acciones de registro", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelBusqueda.setBorder(new TitledBorder(new LineBorder(new Color(130, 135, 144)), "Busqueda de registros", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		//agregar componentes al panel acciones
		btnAgregar = new BotonAgregar();
		btnAgregar.setEnabled(false);
		btnAgregar.setMnemonic('r');
		panelAccion.add(btnAgregar);
       
		btnEliminar = new BotonEliminar();
        btnEliminar.setEnabled(false);
        panelAccion.add(btnEliminar);
        
        btnCobrar = new BotonCobrarSmall();
        btnCobrar.setEnabled(false);
        //btnLimpiar.setIcon(new ImageIcon("recursos/clear.png")); // NOI18N
        panelAccion.add(btnCobrar);
        
    
        
        //configuracion del panel busqueda
        grupoOpciones = new ButtonGroup(); // crea ButtonGroup
        rdbtnTodos = new JRadioButton("Todos");
        rdbtnTodos.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setBuscar1();
        	}
        });
		rdbtnTodos.setSelected(true);
		panelBusqueda.add(rdbtnTodos);
		grupoOpciones.add(rdbtnTodos);
		
		//opciones de busquedas
		rdbtnId = new JRadioButton("ID",false);
		rdbtnId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBuscar1();
			}
		});
		panelBusqueda.add(rdbtnId);
		grupoOpciones.add(rdbtnId);
		
		rdbtnCliente = new JRadioButton("Cliente",false);
		rdbtnCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBuscar1();
			}
		});
		panelBusqueda.add(rdbtnCliente);
		grupoOpciones.add(rdbtnCliente);
		
		rdbtnFecha = new JRadioButton("Fecha",false);
		rdbtnFecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBuscar2();
			}
		});
		panelBusqueda.add(rdbtnFecha);
		grupoOpciones.add(rdbtnFecha);
		
		//elementos del panel buscar
		txtBuscar=new JTextField(10);
		panelBusqueda.add(txtBuscar);
		
		//caja de texto para buscar por fecha buscar por fecha
		txtBuscarFecha=new JFormattedTextField(dateFormat);
		txtBuscarFecha.setColumns(10);
		MaskFormatter dateMask;
	    try {
	        dateMask = new MaskFormatter("##/##/####");
	        dateMask.install(txtBuscarFecha);
	        dateMask.setValidCharacters("0123456789");
	    } catch (ParseException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    txtBuscarFecha.setHorizontalAlignment(JTextField.RIGHT);
	    txtBuscarFecha.setVisible(false);
	    panelBusqueda.add(txtBuscarFecha);
		
				
		btnBuscar=new BotonBuscar();
		panelBusqueda.add(btnBuscar);
        //tabla y sus componentes
		modelo=new TablaModeloFacturas();
		tablaFacturas=new JTable();
		tablaFacturas.setModel(modelo);
		RenderizadorTablaFacturas renderizador = new RenderizadorTablaFacturas();
		tablaFacturas.setDefaultRenderer(String.class, renderizador);
		
		tablaFacturas.getColumnModel().getColumn(0).setPreferredWidth(100);     //Tama�o de las columnas de las tablas
		tablaFacturas.getColumnModel().getColumn(1).setPreferredWidth(300);	//de las columnas
		tablaFacturas.getColumnModel().getColumn(2).setPreferredWidth(70);	//en la tabla
		tablaFacturas.getColumnModel().getColumn(2).setPreferredWidth(70);	//
		
		
		JScrollPane scrollPane = new JScrollPane(tablaFacturas);
		scrollPane.setBounds(36, 97, 742, 136);
		
		
		//configuracion de los paneles
		panelSuperior.add(panelAccion);
		panelSuperior.add(panelBusqueda);
		getContentPane().add(panelSuperior, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		setSize(900,600);
		
		//se hace visible
		//setVisible(true);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		
	}
	public void setBuscar1(){
		txtBuscar.setVisible(true);
		txtBuscarFecha.setVisible(false);
		txtBuscar.repaint();
		this.pack();
	}
	public void setBuscar2(){
		txtBuscar.setVisible(false);
		txtBuscarFecha.setVisible(true);
		txtBuscarFecha.repaint();
		this.pack();
		
		txtBuscarFecha.requestFocusInWindow();
	}
	
public void conectarControlador(CtlFacturaLista c){
		
		
		rdbtnId.addActionListener(c);
		//rdbtnId.getActionCommand();
		rdbtnId.setActionCommand("ID");
		
		rdbtnCliente.addActionListener(c);
		rdbtnCliente.setActionCommand("ARTICULO");
		
		rdbtnFecha.addActionListener(c);
		rdbtnFecha.setActionCommand("MARCA");
		
		btnBuscar.addActionListener(c);
		btnBuscar.setActionCommand("BUSCAR");
		//txtBuscarFecha
		txtBuscarFecha.addActionListener(c);
		txtBuscarFecha.setActionCommand("BUSCAR");
		
		 btnAgregar.addActionListener(c);
		 btnAgregar.setActionCommand("INSERTAR");
		 
		 btnEliminar.addActionListener(c);
		 btnEliminar.setActionCommand("ELIMINAR");
		 
		 btnCobrar.addActionListener(c);
		 btnCobrar.setActionCommand("COBRAR");
		 
		 txtBuscar.addActionListener(c);
		 txtBuscar.setActionCommand("BUSCAR");
		 
		 tablaFacturas.addMouseListener(c);
		 tablaFacturas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public JTable getTablaFacturas(){
		return tablaFacturas;
	}
	public TablaModeloFacturas getModelo(){
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
	public BotonCobrarSmall getBtnCobrar(){
		return btnCobrar;
	}
	public JRadioButton getRdbtnCliente(){
		return rdbtnCliente;
	}
	public JRadioButton getRdbtnFecha(){
		return  rdbtnFecha;
		
	}
	public JRadioButton getRdbtnTodos(){
		return rdbtnTodos;
		
	}
	public BotonAgregar getBtnAgregar(){
		return btnAgregar;
	}

	public JFormattedTextField getTxtBuscarFecha() {
		return txtBuscarFecha;
	}

	public void setTxtBuscarFecha(JFormattedTextField txtBuscarFecha) {
		this.txtBuscarFecha = txtBuscarFecha;
	}

}
