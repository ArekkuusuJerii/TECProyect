package Controlador;

import Classes.Beans.SocioBean;
import Modelo.CargarInfoDAO;
import Utils.CleanupDone;
import Vista.VDetalles;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

@CleanupDone
public class CargarInfoControlador {

    public CargarInfoDAO Cargar = new CargarInfoDAO();

    /**
     * Cargar Información de las Listas en Vista Administador
     *
     * @param jFieldCombo // Contiene los objetos ComboBox
     * @param jFieldList // Contiene los objetos List
     */
    public void CargarInfoListas(JComboBox[] jFieldCombo, JList[] jFieldList) {
        if (!Cargar.LoadInfoVAdmin(jFieldCombo, jFieldList)) {
            JOptionPane.showMessageDialog(null, "Error al cargar Información!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cargar Información de las Listas en Vista Socio
     *
     * @param jFieldList // Contiene los objetos List
     */
    public void CargarInfoListas(JList[] jFieldList) {
        if (!Cargar.LoadInfoVSocio(jFieldList)) {
            JOptionPane.showMessageDialog(null, "Error al cargar Información!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cargar Información de Prestamos en Vista Administrador. Si ocurre un
     * error se manda un Texto en JOptionPane
     *
     * @param t // Contiene el objeto Tabla de la Vista
     * @param accion // Contiene el tipo de acción
     */
    public void CargarInfoPrestamos(DefaultTableModel t, int accion) {
        if (!Cargar.LoadPrestamos(t, accion)) {
            JOptionPane.showMessageDialog(null, "Error al cargar Información!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cargar Información de Prestamos en Vista Administrador. Si ocurre un
     * error se manda un Texto en JOptionPane
     *
     * @param t // Contiene el objeto Tabla de la Vista
     * @param Bean // Contiene los datos del Socio
     * @param accion // Contiene el tipo de acción
     */
    public void CargarInfoPrestamos(DefaultTableModel t, SocioBean Bean, int accion) {
        if (!Cargar.LoadPrestamos(t, Bean, accion)) {
            JOptionPane.showMessageDialog(null, "Error al cargar Información!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cargar Información de Estadisticas. Si ocurre un error se manda un Texto
     * en JOptionPane
     *
     */
    public void CargarInfoEstadisticas() {
        VDetalles Detalles = new VDetalles();
        Detalles.setLocationRelativeTo(null);
        Detalles.setVisible(true);
        if (!Cargar.LoadDetalles(Detalles)) {
            JOptionPane.showMessageDialog(null, "Error al cargar Información!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Buscar Información de Localizacion. Si ocurre un error se manda un Texto
     * en JOptionPane
     *
     * @param t // Contiene el objeto Tabla de la Vista
     */
    public void CargarLocalizacion(DefaultTableModel t) {
        if (!Cargar.LoadLocalizacion(t)) {
            JOptionPane.showMessageDialog(null, "Error al cargar Localización!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Buscar Información de Area. Si ocurre un error se manda un Texto en
     * JOptionPane
     *
     * @param t // Contiene el objeto Tabla de la Vista
     */
    public void CargarArea(DefaultTableModel t) {
        if (!Cargar.LoadArea(t)) {
            JOptionPane.showMessageDialog(null, "Error al cargar Área!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Buscar Información de Editorial. Si ocurre un error se manda un Texto en
     * JOptionPane
     *
     * @param t // Contiene el objeto Tabla de la Vista
     */
    public void CargarEditorial(DefaultTableModel t) {
        if (!Cargar.LoadEditorial(t)) {
            JOptionPane.showMessageDialog(null, "Error al cargar Editorial!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Buscar Texto de Libros. Si ocurre un error se manda un Texto en
     * JOptionPane
     *
     * @return // Regresa los Textos encontrados
     */
    public ArrayList<String> CargarTexts() {
        ArrayList array = Cargar.LoadTexts();
        return array;
    }
}
