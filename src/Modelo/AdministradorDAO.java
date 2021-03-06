package Modelo;

import Classes.Beans.AutorBean;
import Classes.Beans.LibroBean;
import Classes.Beans.SocioBean;
import Utils.CleanupDone;
import Utils.Connexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 * Descripción: MYQSL para Administrador
 *
 */
@CleanupDone
public class AdministradorDAO {

    Connection conn;

    /**
     * Funciones de Libro*
     */
    private final String SQL_ADD_BOOKS = "INSERT INTO Libro (IdLibro,Isbn,Titulo,Paginas,Estatus,NumeroPrestamos,Editorial,Area,Localizacion)";
    private final String SQL_ADD_AUTOR_TO_LIBRO = "INSERT INTO Escribe values (?,?)";
    private final String SQL_ADD_LIBRO_TO_EJEMPLAR = "INSERT INTO Ejemplar values(null,?,?)";
    ////////////////////////////////////////////////////////////////////////////
    private final String SQL_MODIFY_BOOKS = "UPDATE Libro SET Isbn=?, Titulo=?, Paginas=?, Estatus=?, Editorial=?, Area=?, Localizacion=? WHERE IdLibro=?";
    private final String SQL_MODIFY_ESCRIBE = "UPDATE Escribe SET Autor=? WHERE Libro=?";
    private final String SQL_MODIFY_EJEMPLAR = "UPDATE Ejemplar SET Existencias=? WHERE Libro=?";
    private final String SQL_REMOVE_BOOKS = "UPDATE Libro SET Estatus='Inactivo' WHERE IdLibro=?";
    private final String SQL_SEARCH_BOOKS = "SELECT IdLibro,Isbn,Titulo,Paginas,Estatus,NombreAutor,NombreEditorial,Seccion,Pasillo,Existencias FROM Libro AS A join Editorial B,Area C,Localizacion D,Autor E,Escribe F,Ejemplar G WHERE A.Editorial=B.IdEditorial AND A.Area=C.IdArea AND A.Localizacion=D.IdLocalizacion AND F.Autor=E.IdAutor AND G.Libro=IdLibro AND F.Libro=A.IdLibro";
    private static final String ORDER = " ORDER BY IdLibro";

    /**
     * Uso: Registra un Libro en la base de datos con los datos: id, isbn,
     * titulo, paginas, estatus, prestamos, editorial, area, localizacion, en el
     * Bean.
     *
     * Descripción: Ingresa el libro con los datos del bean y busca el id del
     * nuevo libro. Relaciona el nuevo libro con un autor. Relaciona el nuevo
     * libro con un ejemplar nuevo.
     *
     * @param Bean // Contiene los datos del Libro
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean IngresarLibro(LibroBean Bean) {
        boolean SUCCESSI = false;
        boolean SUCCESSM = false;
        boolean SUCCESSF = false;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_ADD_BOOKS + " values (null,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            prs.setString(1, Bean.getIsbn());
            prs.setString(2, Bean.getTitulo());
            prs.setInt(3, Bean.getPaginas());
            prs.setString(4, Bean.getEstatus());
            prs.setInt(5, 0);
            prs.setInt(6, Bean.getEditorial());
            prs.setInt(7, Bean.getArea());
            prs.setInt(8, Bean.getLocalizacion());
            SUCCESSI = prs.executeUpdate() == 1;

            ResultSet rs = prs.getGeneratedKeys();
            if (rs.next()) {
                Bean.setIdLibro(rs.getInt(1));
            }
            rs.close();
            prs.close();
        } catch (SQLException n) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            ////////////////////////////////////////////////////////////////////
            if (SUCCESSI) {
                try {
                    PreparedStatement prs = conn.prepareStatement(SQL_ADD_AUTOR_TO_LIBRO);
                    prs.setInt(1, Bean.getAutor());
                    prs.setInt(2, Bean.getIdLibro());
                    SUCCESSM = prs.executeUpdate() == 1;
                    prs.close();
                } catch (SQLException n) {
                    Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
                } finally {
                    ////////////////////////////////////////////////////////////
                    if (SUCCESSM) {
                        try {
                            PreparedStatement prs = conn.prepareStatement(SQL_ADD_LIBRO_TO_EJEMPLAR);
                            prs.setInt(1, Bean.getNumero());
                            prs.setInt(2, Bean.getIdLibro());
                            SUCCESSM = prs.executeUpdate() == 1;
                            prs.close();
                        } catch (SQLException n) {
                            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
                        } finally {
                            try {
                                conn.close();
                            } catch (SQLException m) {
                                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", m);
                            }
                        }
                    }
                    if (SUCCESSI && SUCCESSM) {
                        SUCCESSF = true;
                    }
                }
            }
        }
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", ex);
        }
        return SUCCESSF;
    }

    /**
     * Descripción: Modifica un Libro con id específico en la base de datos con
     * los datos: isbn, titulo, paginas, estatus, prestamos, editorial, area,
     * localizacion, en el Bean.
     *
     * @param Bean // Contiene los datos del Libro
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean ModificarLibro(LibroBean Bean) {
        boolean SUCCESS = false;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_MODIFY_BOOKS);
            prs.setString(1, Bean.getIsbn());
            prs.setString(2, Bean.getTitulo());
            prs.setInt(3, Bean.getPaginas());
            prs.setString(4, Bean.getEstatus());
            prs.setInt(5, Bean.getEditorial());
            prs.setInt(6, Bean.getArea());
            prs.setInt(7, Bean.getLocalizacion());
            prs.setInt(8, Bean.getIdLibro());
            SUCCESS = prs.executeUpdate() == 1;
            prs.close();
        } catch (SQLException n) {
            Logger.getLogger(IniciarSesionDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            if (SUCCESS) {
                try {
                    PreparedStatement prs = conn.prepareStatement(SQL_MODIFY_ESCRIBE);
                    prs.setInt(1, Bean.getAutor());
                    prs.setInt(2, Bean.getIdLibro());
                    SUCCESS = prs.executeUpdate() == 1;
                    prs.close();
                } catch (SQLException n) {
                    Logger.getLogger(IniciarSesionDAO.class.getName()).log(Level.SEVERE, "Error", n);
                } finally {
                    if (SUCCESS) {
                        try {
                            PreparedStatement prs = conn.prepareStatement(SQL_MODIFY_EJEMPLAR);
                            prs.setInt(1, Bean.getNumero());
                            prs.setInt(2, Bean.getIdLibro());
                            SUCCESS = prs.executeUpdate() == 1;
                            prs.close();
                        } catch (SQLException n) {
                            Logger.getLogger(IniciarSesionDAO.class.getName()).log(Level.SEVERE, "Error", n);
                        } finally {
                            try {
                                conn.close();
                            } catch (SQLException m) {
                                Logger.getLogger(IniciarSesionDAO.class.getName()).log(Level.SEVERE, "Error", m);
                            }
                        }
                    }
                }
            }
        }
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SUCCESS;
    }

    /**
     * Descripción: Elimina un Libro con id específico en la base de datos.
     *
     * @param id // Contiene el id del Libro
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean EliminarLibro(int id) {
        boolean SUCCESS = true;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_REMOVE_BOOKS);
            prs.setInt(1, id);
            SUCCESS = prs.executeUpdate() == 1;
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(IniciarSesionDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(IniciarSesionDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
        return SUCCESS;
    }

    /**
     * Uso: Busca todos Libros con los datos de titulo, isbn, autor, editorial,
     * accion en la base de datos.
     *
     * Descripción: Con la acción, busca los libros que tienen los datos
     * similares. Ingresa los datos encontrados con los datos del libro en un
     * array list. Ingresa el array list en la tabla, elimina los datos del
     * array list y repite hasta encontrar todos los datos.
     *
     * @param t // Contiene el objeto Tabla de la Vista
     * @param Bean // Contiene los datos del Libro
     * @param Editorial // Contiene la editorial
     * @param Autor // Contiene el Autor
     * @param action // Contiene el tipo de acción 0-15 segun la búsqueda
     */
    public void BuscarLibro(DefaultTableModel t, LibroBean Bean, String Editorial, String Autor, int action) {
        ArrayList<String> Array = new ArrayList<String>();
        try {
            conn = Connexion.getConnection();
            ResultSet rs = null;
            PreparedStatement prs = null;
            switch (action) {
                case 0: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + ORDER);
                    rs = prs.executeQuery();
                    break;
                }
                case 1: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND A.Titulo like ? " + ORDER);
                    prs.setString(1, '%' + Bean.getTitulo() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 2: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND A.Isbn like ?" + ORDER);
                    prs.setString(1, '%' + Bean.getIsbn() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 3: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND E.NombreAutor=?" + ORDER);
                    prs.setString(1, Autor);
                    rs = prs.executeQuery();
                    break;
                }
                case 4: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND B.NombreEditorial=?" + ORDER);
                    prs.setString(1, Editorial);
                    rs = prs.executeQuery();
                    break;
                }
                case 5: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND B.NombreEditorial=? AND A.Titulo like ?" + ORDER);
                    prs.setString(1, Editorial);
                    prs.setString(2, '%' + Bean.getTitulo() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 6: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND E.NombreAutor=? AND A.Titulo like ?" + ORDER);
                    prs.setString(1, Autor);
                    prs.setString(2, '%' + Bean.getTitulo() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 7: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND A.Isbn like ? AND A.Titulo like ?" + ORDER);
                    prs.setString(1, '%' + Bean.getIsbn() + "%");
                    prs.setString(2, '%' + Bean.getTitulo() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 8: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND E.NombreAutor=? AND A.Isbn like ?" + ORDER);
                    prs.setString(1, Autor);
                    prs.setString(2, '%' + Bean.getIsbn() + "%");
                    rs = prs.executeQuery();
                    break;
                }
                case 9: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND B.NombreEditorial=? AND A.Isbn like? " + ORDER);
                    prs.setString(1, Editorial);
                    prs.setString(2, '%' + Bean.getIsbn() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 10: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND B.NombreEditorial=? AND E.NombreAutor=?" + ORDER);
                    prs.setString(1, Editorial);
                    prs.setString(2, Autor);
                    rs = prs.executeQuery();
                    break;
                }
                case 11: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND E.NombreAutor=? AND A.Titulo like ? AND A.Isbn like ?" + ORDER);
                    prs.setString(1, Autor);
                    prs.setString(2, '%' + Bean.getTitulo() + '%');
                    prs.setString(3, '%' + Bean.getIsbn() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 12: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND B.NombreEditorial=? AND A.Titulo like ? AND A.Isbn like ?" + ORDER);
                    prs.setString(1, Editorial);
                    prs.setString(2, '%' + Bean.getTitulo() + '%');
                    prs.setString(3, '%' + Bean.getIsbn() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 13: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND E.NombreAutor=? AND B.NombreEditorial=? AND A.Isbn like ?" + ORDER);
                    prs.setString(1, Autor);
                    prs.setString(2, Editorial);
                    prs.setString(3, '%' + Bean.getIsbn() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 14: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND E.NombreAutor=? AND B.NombreEditorial=? AND A.Titulo like ?" + ORDER);
                    prs.setString(1, Autor);
                    prs.setString(2, Editorial);
                    prs.setString(3, '%' + Bean.getTitulo() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 15: {
                    prs = conn.prepareStatement(SQL_SEARCH_BOOKS + " AND E.NombreAutor=? AND B.NombreEditorial=? AND A.Titulo like ? AND A.Isbn like ?" + ORDER);
                    prs.setString(1, Autor);
                    prs.setString(2, Editorial);
                    prs.setString(3, '%' + Bean.getTitulo() + '%');
                    prs.setString(4, '%' + Bean.getIsbn() + '%');
                    rs = prs.executeQuery();
                    break;
                }
            }
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            t.setRowCount(0);
            while (rs.next()) {
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    Array.add(rs.getString(columnIndex));
                }
                t.addRow(Array.toArray());
                Array.clear();
            }
            rs.close();
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
    }

    /**
     * Funciones de Socio*
     */
    private final String SQL_ADD_SOCIO = "INSERT INTO Socio values(null,?,?,?,?,?,?,?,?,?,'Activo',?,0)";
    private final String SQL_MODIFY_SOCIO = "UPDATE Socio SET Nombre=?, ApellidoP=?, ApellidoM=?, Estado=?, Municipio=?, Calle=?, Numero=?, Telefono=?, Usuario=?, Estatus=?, Contraseña=? WHERE IdSocio=?";
    private final String SQL_DELETE_SOCIO = "UPDATE Socio SET Estatus='Inactivo' WHERE IdSocio=?";
    private final String SQL_SEARCH_SOCIO = "SELECT IdSocio,Nombre,ApellidoP,ApellidoM,CONCAT(Estado,' ',Municipio,' ',Calle,' ',Numero),Telefono,Estatus,Prestamos,Usuario FROM Socio";
    private final String ORDER_SOCIO = " ORDER BY Prestamos DESC";

    /**
     * Descripción: Registra un Socio en la base de datos con los datos: id,
     * nombre, apellido paterno, apellido materno, estado, municipio, calle,
     * numero, telefono, usuario, estatus, contraseña, en el Bean. Busca el id
     * del nuevo socio.
     *
     * @param Bean // Contiene los datos del socio
     * @param secretPass // Contraseña segura del Socio
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean IngresarSocio(SocioBean Bean, char[] secretPass) {
        boolean SUCCESS = false;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_ADD_SOCIO, PreparedStatement.RETURN_GENERATED_KEYS);
            prs.setString(1, Bean.getNombre());
            prs.setString(2, Bean.getApellidoP());
            prs.setString(3, Bean.getApellidoM());
            prs.setString(4, Bean.getEstado());
            prs.setString(5, Bean.getMunicipio());
            prs.setString(6, Bean.getCalle());
            prs.setString(7, Bean.getNumero());
            prs.setString(8, Bean.getTelefono());
            prs.setString(9, Bean.getUsuario());
            prs.setString(10, new String(secretPass));
            SUCCESS = prs.executeUpdate() == 1;

            ResultSet rs = prs.getGeneratedKeys();
            if (rs.next()) {
                Bean.setIdUsuario(rs.getInt(1));
            }
            rs.close();
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
        return SUCCESS;
    }

    /**
     * Descripción: Modifica un Socio con id específico en la base de datos con
     * los datos: id, nombre, apellido paterno, apellido materno, estado,
     * municipio, calle, numero, telefono, usuario, estatus, contraseña, en el
     * Bean.
     *
     * @param Bean // Contiene los datos del socio
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean ModificarSocio(SocioBean Bean, char[] secretPass) {
        boolean SUCCESS = false;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_MODIFY_SOCIO);
            prs.setString(1, Bean.getNombre());
            prs.setString(2, Bean.getApellidoP());
            prs.setString(3, Bean.getApellidoM());
            prs.setString(4, Bean.getEstado());
            prs.setString(5, Bean.getMunicipio());
            prs.setString(6, Bean.getCalle());
            prs.setString(7, Bean.getNumero());
            prs.setString(8, Bean.getTelefono());
            prs.setString(9, Bean.getUsuario());
            prs.setString(10, Bean.getEstatus());
            prs.setString(11, new String(secretPass));
            prs.setInt(12, Bean.getIdUsuario());
            SUCCESS = prs.executeUpdate() == 1;
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
        return SUCCESS;
    }

    /**
     * Descripción: Elimina un Socio con id específico en la base de datos.
     *
     * @param id // Contiene el id del socio
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean EliminarSocio(int id) {
        boolean SUCCESS = true;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_DELETE_SOCIO);
            prs.setInt(1, id);
            SUCCESS = prs.executeUpdate() == 1;
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
        return SUCCESS;
    }

    /**
     * Uso: Busca todos Socios con los datos de nombre, apellido paterno,
     * apellido materno, usuario, accion en la base de datos.
     *
     * Descripción: Con la acción, busca los socios que tienen los datos
     * similares. Ingresa los datos encontrados con los datos del socio en un
     * array list. Ingresa el array list en la tabla, elimina los datos del
     * array list y repite hasta encontrar todos los datos.
     *
     * @param t // Contiene el objeto Tabla de la Vista
     * @param Bean // Contiene los datos del Libro
     * @param action // Contiene el tipo de acción 0-15 segun la búsqueda
     */
    public void BuscarSocio(DefaultTableModel t, SocioBean Bean, int action) {
        ArrayList<String> Array = new ArrayList<String>();
        try {
            conn = Connexion.getConnection();
            ResultSet rs = null;
            PreparedStatement prs = null;
            switch (action) {
                case 0: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + ORDER_SOCIO);
                    rs = prs.executeQuery();
                    break;
                }
                case 1: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE Nombre like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getNombre() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 2: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE ApellidoP like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getApellidoP() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 3: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE ApellidoM like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getApellidoM() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 4: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE Usuario like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getUsuario() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 5: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE Usuario like ? AND Nombre like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getUsuario() + '%');
                    prs.setString(2, '%' + Bean.getNombre() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 6: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE ApellidoM like ? AND Nombre like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getApellidoM() + '%');
                    prs.setString(2, '%' + Bean.getNombre() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 7: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE ApellidoP like ? AND Nombre like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getApellidoP() + "%");
                    prs.setString(2, '%' + Bean.getNombre() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 8: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE ApellidoM like ? AND ApellidoP like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getApellidoM() + '%');
                    prs.setString(2, '%' + Bean.getApellidoP() + "%");
                    rs = prs.executeQuery();
                    break;
                }
                case 9: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE Usuario like ? AND ApellidoP like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getUsuario() + '%');
                    prs.setString(2, '%' + Bean.getApellidoP() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 10: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE Usuario like ? AND ApellidoM like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getUsuario() + '%');
                    prs.setString(2, '%' + Bean.getApellidoM() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 11: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE ApellidoM like ? AND Nombre like ? AND ApellidoP like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getApellidoM() + '%');
                    prs.setString(2, '%' + Bean.getNombre() + '%');
                    prs.setString(3, '%' + Bean.getApellidoP() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 12: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE Usuario like ? AND Nombre like ? AND ApellidoP like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getUsuario() + '%');
                    prs.setString(2, '%' + Bean.getNombre() + '%');
                    prs.setString(3, '%' + Bean.getApellidoP() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 13: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE ApellidoM like ? AND Usuario like ? AND ApellidoP like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getApellidoM() + '%');
                    prs.setString(2, '%' + Bean.getUsuario() + '%');
                    prs.setString(3, '%' + Bean.getApellidoP() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 14: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE ApellidoM like ? AND Usuario like ? AND Nombre like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getApellidoM() + '%');
                    prs.setString(2, '%' + Bean.getUsuario() + '%');
                    prs.setString(3, '%' + Bean.getNombre() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 15: {
                    prs = conn.prepareStatement(SQL_SEARCH_SOCIO + " WHERE ApellidoM like ? AND Usuario like ? AND Nombre like ? AND ApellidoP like ?" + ORDER_SOCIO);
                    prs.setString(1, '%' + Bean.getApellidoM() + '%');
                    prs.setString(2, '%' + Bean.getUsuario() + '%');
                    prs.setString(3, '%' + Bean.getNombre() + '%');
                    prs.setString(4, '%' + Bean.getApellidoP() + '%');
                    rs = prs.executeQuery();
                    break;
                }
            }
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            t.setRowCount(0);
            while (rs.next()) {
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    Array.add(rs.getString(columnIndex));
                }
                Array.add("**********");
                t.addRow(Array.toArray());
                Array.clear();
            }
            rs.close();
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
    }

    /**
     * Funciones de Autor*
     */
    private final String SQL_ADD_AUTOR = "INSERT INTO Autor (IdAutor,NombreAutor,ApellidoPAutor,ApellidoMAutor,EstatusAutor)";
    private final String SQL_MODIFY_AUTOR = "UPDATE Autor SET";
    private final String SQL_REMOVE_AUTOR = "UPDATE Autor SET";
    private final String SQL_SEARCH_AUTOR = "SELECT DISTINCT * FROM Autor";

    /**
     * Uso: Registra un Autor en la base de datos con los datos: id, nombre,
     * apellido paterno, apellido materno y estatus, en el Bean.
     *
     * Descripción: Busca el id de la nueva área y la ingresa en la Tabla junto
     * con sus datos.
     *
     * @param Bean // Contiene el nombre del autor
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean IngresarAutor(AutorBean Bean) {
        boolean SUCCESS = false;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_ADD_AUTOR + " values(null,?,?,?,'Activo')", PreparedStatement.RETURN_GENERATED_KEYS);
            prs.setString(1, Bean.getNombre());
            prs.setString(2, Bean.getApellidoP());
            prs.setString(3, Bean.getApellidoM());
            SUCCESS = prs.executeUpdate() == 1;

            ResultSet rs = prs.getGeneratedKeys();
            if (rs.next()) {
                Bean.setIdAutor(rs.getInt(1));
            }
            rs.close();
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
        return SUCCESS;
    }

    /**
     * Descripción: Modifica un Autor con id específico en la base de datos con
     * los datos: nombre, apellido paterno, apellido materno y estatus, en el
     * Bean.
     *
     * @param Bean // Contiene el id, nombre, apellido paterno, apellido materno
     * y estatus del autor
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean ModificarAutor(AutorBean Bean) {
        boolean SUCCESS = false;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_MODIFY_AUTOR + " NombreAutor=?,ApellidoPAutor=?,ApellidoMAutor=?,EstatusAutor=? WHERE IdAutor=?");
            prs.setString(1, Bean.getNombre());
            prs.setString(2, Bean.getApellidoP());
            prs.setString(3, Bean.getApellidoM());
            prs.setString(4, Bean.getStatus());
            prs.setInt(5, Bean.getIdAutor());
            SUCCESS = prs.executeUpdate() == 1;
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
        return SUCCESS;
    }

    /**
     * Descripción: Elimina un Autor con id específico en la base de datos.
     *
     * @param id // Contiene el id del autor
     * @return // Regresa true si es exitosa y false si ocurre un error
     */
    public boolean EliminarAutor(int id) {
        boolean SUCCESS = true;
        try {
            conn = Connexion.getConnection();
            PreparedStatement prs = conn.prepareStatement(SQL_REMOVE_AUTOR + " EstatusAutor='Inactivo' WHERE IdAutor=?");
            prs.setInt(1, id);
            SUCCESS = prs.executeUpdate() == 1;
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
        return SUCCESS;
    }

    /**
     * Uso: Busca todos Autores con los datos de nombre, apellido paterno,
     * apellido materno, accion en la base de datos.
     *
     * Descripción: Con la acción, busca los autores que tienen los datos
     * similares. Ingresa los datos encontrados con los datos del socio en un
     * array list. Ingresa el array list en la tabla, elimina los datos del
     * array list y repite hasta encontrar todos los datos.
     *
     * @param t // Contiene el objeto Tabla de la Vista
     * @param Bean // Contiene los datos del autor
     * @param action // Contiene el tipo de acción 0-7 segun la búsqueda
     */
    public void BuscarAutor(DefaultTableModel t, AutorBean Bean, int action) {
        ArrayList<String> Array = new ArrayList<String>();
        try {
            conn = Connexion.getConnection();
            ResultSet rs = null;
            PreparedStatement prs = null;
            switch (action) {
                case 0: {
                    prs = conn.prepareStatement(SQL_SEARCH_AUTOR);
                    rs = prs.executeQuery();
                    break;
                }
                case 1: {
                    prs = conn.prepareStatement(SQL_SEARCH_AUTOR + " WHERE NombreAutor like ?");
                    prs.setString(1, '%' + Bean.getNombre() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 2: {
                    prs = conn.prepareStatement(SQL_SEARCH_AUTOR + " WHERE ApellidoPAutor like ?");
                    prs.setString(1, '%' + Bean.getApellidoP() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 3: {
                    prs = conn.prepareStatement(SQL_SEARCH_AUTOR + " WHERE ApellidoMAutor like ?");
                    prs.setString(1, '%' + Bean.getApellidoM() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 4: {
                    prs = conn.prepareStatement(SQL_SEARCH_AUTOR + " WHERE ApellidoMAutor like ? NombreAutor like ?");
                    prs.setString(1, '%' + Bean.getApellidoM() + '%');
                    prs.setString(2, '%' + Bean.getNombre() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 5: {
                    prs = conn.prepareStatement(SQL_SEARCH_AUTOR + " WHERE ApellidoPAutor like ? AND NombreAutor like ?");
                    prs.setString(1, '%' + Bean.getApellidoP() + "%");
                    prs.setString(2, '%' + Bean.getNombre() + '%');
                    rs = prs.executeQuery();
                    break;
                }
                case 6: {
                    prs = conn.prepareStatement(SQL_SEARCH_AUTOR + " WHERE ApellidoMAutor like ? AND ApellidoPAutor like ?");
                    prs.setString(1, '%' + Bean.getApellidoM() + '%');
                    prs.setString(2, '%' + Bean.getApellidoP() + "%");
                    rs = prs.executeQuery();
                    break;
                }
                case 7: {
                    prs = conn.prepareStatement(SQL_SEARCH_AUTOR + " WHERE ApellidoMAutor like ? AND NombreAutor like ? AND ApellidoPAutor like ?");
                    prs.setString(1, '%' + Bean.getApellidoM() + '%');
                    prs.setString(2, '%' + Bean.getNombre() + '%');
                    prs.setString(3, '%' + Bean.getApellidoP() + '%');
                    rs = prs.executeQuery();
                    break;
                }
            }
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            t.setRowCount(0);
            while (rs.next()) {
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    Array.add(rs.getString(columnIndex));
                }
                t.addRow(Array.toArray());
                Array.clear();
            }
            rs.close();
            prs.close();
            conn.close();
        } catch (SQLException n) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", n);
        } finally {
            try {
                conn.close();
            } catch (SQLException m) {
                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, "Error", m);
            }
        }
    }
}
