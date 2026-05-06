package controlador;

import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import modelo.persona;
import modelo.personaDAO;
import vista.ventana;

public class logica_ventana implements ActionListener, ItemListener {

    private ventana vista;
    private String categoriaSeleccionada = "";
    private boolean esFavorito = false;
    private List<persona> contactos;

    public logica_ventana(ventana vista) {
        this.vista = vista;

        refrescarTabla();

        vista.btn_agregar.addActionListener(this);
        vista.btn_exportarCSV.addActionListener(this);
        vista.cmb_categoria.addItemListener(this);
        vista.chk_favorito.addItemListener(this);

        // atajo de teclado CTRL+G para guardar sin usar el botón
        vista.txt_nombre.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_G) {
                    guardarContacto();
                }
            }
        });

        vista.tabla_contactos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int fila = vista.tabla_contactos.rowAtPoint(e.getPoint());
                    if (fila < 0) return;
                    vista.tabla_contactos.setRowSelectionInterval(fila, fila);
                    String nombre = vista.modeloTabla.getValueAt(fila, 0).toString();
                    String telefono = vista.modeloTabla.getValueAt(fila, 1).toString();

                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem item = new JMenuItem("Ver: " + nombre + " - " + telefono);
                    item.setEnabled(false);
                    menu.add(item);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void refrescarTabla() {
        SwingWorker<List<persona>, Integer> worker = new SwingWorker<>() {
            @Override
            protected List<persona> doInBackground() throws Exception {
                publish(0);
                personaDAO dao = new personaDAO();
                List<persona> lista = dao.leerArchivo();
                for (int i = 10; i <= 100; i += 10) {
                    Thread.sleep(15); // simula tiempo de carga para la barra de progreso
                    publish(i);
                }
                return lista;
            }

            @Override
            protected void process(List<Integer> values) {
                vista.barra_progreso.setValue(values.get(values.size() - 1));
            }

            @Override
            protected void done() {
                try {
                    contactos = get();
                    DefaultTableModel modelo = vista.modeloTabla;
                    modelo.setRowCount(0);
                    for (persona p : contactos) {
                        modelo.addRow(new Object[]{
                            p.getNombre(), p.getTelefono(), p.getEmail(),
                            p.getCategoria(), p.isFavorito() ? "★" : "-"
                        });
                    }
                    vista.lbl_totalContactos.setText("Total: " + contactos.size() + " contactos");
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
                    vista.tabla_contactos.setRowSorter(sorter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void guardarContacto() {
        String nombre = vista.txt_nombre.getText().trim();
        String telefono = vista.txt_telefono.getText().trim();
        String email = vista.txt_email.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || categoriaSeleccionada.equals("Seleccione una categoría") || categoriaSeleccionada.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete todos los campos obligatorios.");
            return;
        }

        persona nuevo = new persona(nombre, telefono, email, categoriaSeleccionada, esFavorito);
        new personaDAO(nuevo).escribirArchivo();
        JOptionPane.showMessageDialog(vista, "Contacto registrado correctamente.");
        limpiarFormulario();
        refrescarTabla();
    }

    private void limpiarFormulario() {
        vista.txt_nombre.setText("");
        vista.txt_telefono.setText("");
        vista.txt_email.setText("");
        vista.cmb_categoria.setSelectedIndex(0);
        vista.chk_favorito.setSelected(false);
        categoriaSeleccionada = "";
        esFavorito = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btn_agregar) {
            guardarContacto();
        } else if (e.getSource() == vista.btn_exportarCSV) {
            try {
                new personaDAO().exportarCSV(contactos, "Agenda_UPS.csv");
                JOptionPane.showMessageDialog(vista, "Exportación completada. Revise su carpeta Documentos.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vista, "Error al exportar: " + ex.getMessage());
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == vista.cmb_categoria) {
            categoriaSeleccionada = vista.cmb_categoria.getSelectedItem().toString();
        } else if (e.getSource() == vista.chk_favorito) {
            esFavorito = vista.chk_favorito.isSelected();
        }
    }
}
