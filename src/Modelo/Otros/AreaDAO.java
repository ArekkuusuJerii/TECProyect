package Modelo.Otros;

import Classes.Beans.AreaBean;
import Utils.CleanupDone;
import Utils.Connexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Descripción: MYQSL para Área
 *
 */
@CleanupDone
public class AreaDAO {

    Connection conn;

    /**
     * Funciones de Libro*
     */
    private final String SQL_ADD_AREA = "INSERT INTO Area (IdArea,Seccion,EstatusArea)";
    private final String SQL_ID_AREA = "SELECT IdArea FROM Area WHERE Seccion=? AND EstatusArea='Activo'";
    private final String SQL_MODIFY_REMOVE_AREA = "UPDATE Area SET";

    /**
     * Uso: Registra un Área en la base de datos con los datos: id, nombre y
     * estatus, en el Bean.
     *
     * Descripción: Busca el id de la nueva área y la ingresa en la Tabla junto
     * con sus datos.
     *
     * @param Bean // Contiene el nombre del área
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean IngresarArea(AreaBean Bean) {
        boolean SUCCESS = false;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_ADD_AREA + " values (null,?,'Activo')");
            prs.setString(1, Bean.getSeccion());
            SUCCESS = prs.executeUpdate() == 1;
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AreaDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AreaDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
        try {
            conn = Connexion.getConnection();
            ResultSet rs;
            PreparedStatement prs = conn.prepareStatement(SQL_ID_AREA);
            prs.setString(1, Bean.getSeccion());
            rs = prs.executeQuery();
            if (rs.next()) {
                Bean.setIdArea(rs.getInt(1));
            }
            rs.close();
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AreaDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AreaDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
        return SUCCESS;
    }

    /**
     * Descripción: Modifica un Área con id específico en la base de datos con
     * los datos: nombre y estatus, en el Bean.
     *
     * @param Bean // Contiene el id, nombre y estatus del área
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean ModificarArea(AreaBean Bean) {
        boolean SUCCESS = false;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_MODIFY_REMOVE_AREA + " Seccion=?,EstatusArea=? WHERE IdArea=?");
            prs.setString(1, Bean.getSeccion());
            prs.setString(2, Bean.getEstatus());
            prs.setInt(3, Bean.getIdArea());
            SUCCESS = prs.executeUpdate() == 1;
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AreaDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AreaDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
        return SUCCESS;
    }

    /**
     * Descripción: Elimina un Área con id específico en la base de datos.
     *
     * @param id // Contiene el id del área
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean EliminarArea(int id) {
        boolean SUCCESS = true;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_MODIFY_REMOVE_AREA + " EstatusArea='Inactivo' WHERE IdArea=?");
            prs.setInt(1, id);
            SUCCESS = prs.executeUpdate() == 1;
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AreaDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AreaDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
        return SUCCESS;
    }
}
