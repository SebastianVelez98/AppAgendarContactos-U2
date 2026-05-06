package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class personaDAO {

    private static final String CARPETA = System.getProperty("user.home") + "/agendaContactos";
    private static final String ARCHIVO = CARPETA + "/agenda_contactos.txt";

    private persona contacto;

    public personaDAO(persona contacto) {
        this.contacto = contacto;
        inicializarArchivo();
    }

    public personaDAO() {
        this.contacto = null;
    }

    private void inicializarArchivo() {
        File carpeta = new File(CARPETA);
        if (!carpeta.exists()) carpeta.mkdir(); // se crea si no existe
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                agregarLinea("NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void agregarLinea(String linea) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean escribirArchivo() {
        if (contacto == null) return false;
        agregarLinea(contacto.datosContacto());
        return true;
    }

    public List<persona> leerArchivo() throws IOException {
        List<persona> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            br.readLine();
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length >= 5) {
                    lista.add(new persona(partes[0], partes[1], partes[2], partes[3], Boolean.parseBoolean(partes[4])));
                }
            }
        }
        return lista;
    }

    public void exportarCSV(List<persona> lista, String nombreArchivo) throws IOException {
        File destino = new File(System.getProperty("user.home") + "/Documents", nombreArchivo);
        try (PrintWriter pw = new PrintWriter(new FileWriter(destino))) {
            pw.println("Nombre,Telefono,Email,Categoria,Favorito");
            for (persona p : lista) {
                pw.println(p.toCSV());
            }
        }
    }
}
