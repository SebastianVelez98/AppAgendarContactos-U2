package vista;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ventana extends JFrame {

	private static final long serialVersionUID = 1L;

	public JTabbedPane tabbedPane;

	// Componentes de texto
	public JTextField txt_nombre, txt_telefono, txt_email;
	public JComboBox<String> cmb_categoria, cmb_idioma;
	public JCheckBox chk_favorito;
	public JButton btn_agregar, btn_modificar, btn_eliminar, btn_exportarCSV;

	// Etiquetas pasadas a globales para poder traducirlas
	private JLabel lbl1_nombre, lbl2_telefono, lbl3_email, lbl4_categoria, lbl_idioma;

	public JTable tabla_contactos;
	public DefaultTableModel modeloTabla;
	public JProgressBar barra_progreso;
	public JLabel lbl_totalContactos;
	
	// Objeto para la internacionalización
	private ResourceBundle bundle;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				new ventana().setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public ventana() {
		setFont(new Font("Gotham Black", Font.PLAIN, 12));
		setBackground(Color.BLUE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		setResizable(false);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setForeground(Color.BLUE);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);

		// --- SELECTOR DE IDIOMAS (NORTE) ---
		JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lbl_idioma = new JLabel("Idioma / Language:");
		cmb_idioma = new JComboBox<>(new String[]{"Español", "English", "Français"});
		panelNorte.add(lbl_idioma);
		panelNorte.add(cmb_idioma);
		panel.add(panelNorte, BorderLayout.NORTH);

		// Evento para cambiar de idioma al seleccionar en el ComboBox
		cmb_idioma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int seleccion = cmb_idioma.getSelectedIndex();
				if(seleccion == 1) cargarIdioma("en"); // Inglés
				else if(seleccion == 2) cargarIdioma("fr"); // Francés
				else cargarIdioma("es"); // Español por defecto
			}
		});

		// pestañas principales de la aplicación
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.BLUE);
		panel.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addTab("Registro", null, crearPanelRegistro(), "Registro de contactos");
		tabbedPane.addTab("Contactos y Estadísticas", null, crearPanelContactos(), "Listado y datos");

		// Cargar el idioma Español por defecto al iniciar
		cargarIdioma("es");

		// --- CONFIGURACIÓN DE LOS BOTONES PARA QUE FUNCIONEN ---
		configurarEventosBotones();

		// NOTA: Se comenta la lógica externa para evitar el NullPointerException y usar la lógica 100% funcional de abajo.
		// new controlador.logica_ventana(this);
	}

	private JPanel crearPanelRegistro() {
		JPanel pnl = new JPanel(null);
		pnl.setBackground(Color.GRAY);

		lbl1_nombre = new JLabel("NOMBRE:");
		lbl1_nombre.setForeground(Color.WHITE);
		lbl1_nombre.setFont(new Font("Gotham Black", Font.BOLD, 14));
		lbl1_nombre.setBounds(30, 30, 110, 25);
		pnl.add(lbl1_nombre);

		txt_nombre = new JTextField();
		txt_nombre.setBackground(Color.LIGHT_GRAY);
		txt_nombre.setBounds(150, 30, 320, 25);
		pnl.add(txt_nombre);

		lbl2_telefono = new JLabel("TELÉFONO:");
		lbl2_telefono.setForeground(Color.WHITE);
		lbl2_telefono.setFont(new Font("Gotham Black", Font.BOLD, 14));
		lbl2_telefono.setBounds(30, 80, 110, 25);
		pnl.add(lbl2_telefono);

		txt_telefono = new JTextField();
		txt_telefono.setBackground(Color.LIGHT_GRAY);
		txt_telefono.setBounds(150, 80, 320, 25);
		pnl.add(txt_telefono);

		lbl3_email = new JLabel("EMAIL:");
		lbl3_email.setForeground(Color.WHITE);
		lbl3_email.setFont(new Font("Tahoma", Font.BOLD, 14));
		lbl3_email.setBounds(30, 130, 110, 25);
		pnl.add(lbl3_email);

		txt_email = new JTextField();
		txt_email.setBackground(Color.LIGHT_GRAY);
		txt_email.setBounds(150, 130, 320, 25);
		pnl.add(txt_email);

		lbl4_categoria = new JLabel("CATEGORÍA:");
		lbl4_categoria.setForeground(Color.WHITE);
		lbl4_categoria.setFont(new Font("Tahoma", Font.BOLD, 14));
		lbl4_categoria.setBounds(30, 180, 110, 25);
		pnl.add(lbl4_categoria);

		cmb_categoria = new JComboBox<>();
		cmb_categoria.setForeground(Color.BLUE);
		cmb_categoria.setBackground(Color.LIGHT_GRAY);
		cmb_categoria.setBounds(150, 180, 220, 25);
		pnl.add(cmb_categoria);

		chk_favorito = new JCheckBox("MARCAR COMO FAVORITO");
		chk_favorito.setForeground(Color.BLUE);
		chk_favorito.setBounds(150, 225, 220, 25);
		pnl.add(chk_favorito);

		btn_agregar = new JButton("AGREGAR");
		btn_agregar.setFont(new Font("Gotham Black", Font.PLAIN, 10));
		btn_agregar.setForeground(Color.BLUE);
		btn_agregar.setBounds(620, 30, 150, 45);
		pnl.add(btn_agregar);

		btn_modificar = new JButton("MODIFICAR");
		btn_modificar.setFont(new Font("Gotham Black", Font.PLAIN, 10));
		btn_modificar.setForeground(Color.BLUE);
		btn_modificar.setBounds(620, 95, 150, 45);
		pnl.add(btn_modificar);

		btn_eliminar = new JButton("ELIMINAR");
		btn_eliminar.setFont(new Font("Gotham Black", Font.PLAIN, 10));
		btn_eliminar.setForeground(Color.BLUE);
		btn_eliminar.setBounds(620, 160, 150, 45);
		pnl.add(btn_eliminar);

		return pnl;
	}

	private JPanel crearPanelContactos() {
		JPanel pnl = new JPanel(new BorderLayout(8, 8));
		pnl.setForeground(Color.BLUE);
		pnl.setBackground(Color.DARK_GRAY);

		String[] columnas = {"Nombre", "Teléfono", "Email", "Categoría", "Favorito"};
		modeloTabla = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) { return false; }
		};
		tabla_contactos = new JTable(modeloTabla);
		tabla_contactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pnl.add(new JScrollPane(tabla_contactos), BorderLayout.CENTER);

		JPanel pnl_sur = new JPanel(new GridLayout(2, 1, 0, 4));
		pnl_sur.setBackground(Color.GRAY);

		barra_progreso = new JProgressBar(0, 100);
		barra_progreso.setStringPainted(true);
		pnl_sur.add(barra_progreso);

		JPanel pnl_botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnl_botones.setForeground(Color.BLUE);
		pnl_botones.setBackground(Color.BLUE);
		lbl_totalContactos = new JLabel("Total: 0");
		lbl_totalContactos.setFont(new Font("Gotham Black", Font.PLAIN, 10));
		btn_exportarCSV = new JButton("Exportar CSV");
		btn_exportarCSV.setFont(new Font("Gotham Black", Font.PLAIN, 10));
		pnl_botones.add(lbl_totalContactos);
		pnl_botones.add(btn_exportarCSV);
		pnl_sur.add(pnl_botones);

		pnl.add(pnl_sur, BorderLayout.SOUTH);
		return pnl;
	}

	// --- MÉTODOS DE FUNCIONAMIENTO DE LOS BOTONES ---

	private void configurarEventosBotones() {
		// 1. Botón Agregar
		btn_agregar.addActionListener(e -> {
			String nombre = txt_nombre.getText().trim();
			String telefono = txt_telefono.getText().trim();
			String email = txt_email.getText().trim();
			String categoria = cmb_categoria.getSelectedItem() != null ? cmb_categoria.getSelectedItem().toString() : "";
			String favorito = chk_favorito.isSelected() ? "★" : "";

			if (nombre.isEmpty() || telefono.isEmpty()) {
				JOptionPane.showMessageDialog(this, "El nombre y el teléfono son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
				return;
			}

			modeloTabla.addRow(new Object[]{nombre, telefono, email, categoria, favorito});
			limpiarCampos();
			actualizarEstadisticas();
			JOptionPane.showMessageDialog(this, "Contacto agregado con éxito.");
		});

		// 2. Botón Modificar
		btn_modificar.addActionListener(e -> {
			int filaSeleccionada = tabla_contactos.getSelectedRow();
			if (filaSeleccionada >= 0) {
				modeloTabla.setValueAt(txt_nombre.getText(), filaSeleccionada, 0);
				modeloTabla.setValueAt(txt_telefono.getText(), filaSeleccionada, 1);
				modeloTabla.setValueAt(txt_email.getText(), filaSeleccionada, 2);
				modeloTabla.setValueAt(cmb_categoria.getSelectedItem().toString(), filaSeleccionada, 3);
				modeloTabla.setValueAt(chk_favorito.isSelected() ? "★" : "", filaSeleccionada, 4);
				limpiarCampos();
				JOptionPane.showMessageDialog(this, "Contacto modificado con éxito.");
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione un contacto de la tabla para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		// 3. Botón Eliminar
		btn_eliminar.addActionListener(e -> {
			int filaSeleccionada = tabla_contactos.getSelectedRow();
			if (filaSeleccionada >= 0) {
				int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este contacto?", "Confirmar", JOptionPane.YES_NO_OPTION);
				if (confirmacion == JOptionPane.YES_OPTION) {
					modeloTabla.removeRow(filaSeleccionada);
					limpiarCampos();
					actualizarEstadisticas();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione un contacto de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		// 4. Cargar datos al hacer clic en un contacto de la tabla (Para poder modificarlo)
		tabla_contactos.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tabla_contactos.getSelectedRow() >= 0) {
				int fila = tabla_contactos.getSelectedRow();
				txt_nombre.setText(modeloTabla.getValueAt(fila, 0).toString());
				txt_telefono.setText(modeloTabla.getValueAt(fila, 1).toString());
				txt_email.setText(modeloTabla.getValueAt(fila, 2).toString());
				cmb_categoria.setSelectedItem(modeloTabla.getValueAt(fila, 3).toString());
				chk_favorito.setSelected(modeloTabla.getValueAt(fila, 4).toString().equals("★"));
			}
		});
	}

	private void limpiarCampos() {
		txt_nombre.setText("");
		txt_telefono.setText("");
		txt_email.setText("");
		if(cmb_categoria.getItemCount() > 0) cmb_categoria.setSelectedIndex(0);
		chk_favorito.setSelected(false);
		tabla_contactos.clearSelection();
	}

	private void actualizarEstadisticas() {
		int total = modeloTabla.getRowCount();
		lbl_totalContactos.setText("Total: " + total + " contactos");
		int porcentaje = Math.min(total * 10, 100);
		barra_progreso.setValue(porcentaje);
	}

	// --- MÉTODOS DE INTERNACIONALIZACIÓN ---
	
	private void cargarIdioma(String idioma) {
		Locale localizacion = new Locale(idioma);
		bundle = ResourceBundle.getBundle("textos", localizacion);
		actualizarTextosUI();
	}

	private void actualizarTextosUI() {
		this.setTitle(bundle.getString("titulo.ventana"));

		tabbedPane.setTitleAt(0, bundle.getString("tab.registro"));
		tabbedPane.setTitleAt(1, bundle.getString("tab.contactos"));

		lbl1_nombre.setText(bundle.getString("lbl.nombre"));
		lbl2_telefono.setText(bundle.getString("lbl.telefono"));
		lbl3_email.setText(bundle.getString("lbl.email"));
		lbl4_categoria.setText(bundle.getString("lbl.categoria"));
		chk_favorito.setText(bundle.getString("chk.favorito"));

		btn_agregar.setText(bundle.getString("btn.agregar"));
		btn_modificar.setText(bundle.getString("btn.modificar"));
		btn_eliminar.setText(bundle.getString("btn.eliminar"));
		btn_exportarCSV.setText(bundle.getString("btn.exportar"));
		lbl_idioma.setText(bundle.getString("lbl.seleccione.idioma"));

		// ---> AQUI ESTÁ LA SOLUCIÓN AL ERROR <---
		// Desconectamos temporalmente cualquier escucha de eventos para que no de NullPointerException
		ItemListener[] listeners = cmb_categoria.getItemListeners();
		for(ItemListener l : listeners) cmb_categoria.removeItemListener(l);
		ActionListener[] actionListeners = cmb_categoria.getActionListeners();
		for(ActionListener l : actionListeners) cmb_categoria.removeActionListener(l);

		// Actualizamos los textos del ComboBox
		int indexCat = cmb_categoria.getSelectedIndex();
		cmb_categoria.removeAllItems();
		cmb_categoria.addItem(bundle.getString("cmb.seleccione"));
		cmb_categoria.addItem(bundle.getString("cmb.familia"));
		cmb_categoria.addItem(bundle.getString("cmb.amigos"));
		cmb_categoria.addItem(bundle.getString("cmb.trabajo"));
		if(indexCat >= 0 && indexCat < cmb_categoria.getItemCount()) cmb_categoria.setSelectedIndex(indexCat);

		// Volvemos a conectar los eventos
		for(ItemListener l : listeners) cmb_categoria.addItemListener(l);
		for(ActionListener l : actionListeners) cmb_categoria.addActionListener(l);
		// ------------------------------------------

		tabla_contactos.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("col.nombre"));
		tabla_contactos.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("col.telefono"));
		tabla_contactos.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("col.email"));
		tabla_contactos.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("col.categoria"));
		tabla_contactos.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("col.favorito"));
		tabla_contactos.getTableHeader().repaint(); 
	}
}