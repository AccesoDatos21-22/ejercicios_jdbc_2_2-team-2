package org.iesinfantaelena.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;
import org.iesinfantaelena.utils.Utilidades;


/**
 * @author Carlos
 * @version 1.0
 * @descrition
 * @date 23/10/2021
 * @license GPLv3
 */

public class Libros {

    // Consultas a realizar en BD


    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    private PreparedStatement pstmt;
    private static final String INSERT_LIBRO_QUERY = "insert into LIBROS values (?,?,?,?,?,?)";
    private static final String SEARCH_LIBRO_QUERY = "select * from LIBROS WHERE ISBN = ?";
    private static final String UPDATE_PAGINAS_QUERY = "update LIBROS set COPIAS ? WHERE ISBN = ?";
    private static final String SEARCH_CATALOGO_QUERY = "select * from LIBROS";
    private static final String SEARCH_COLUMNAS_QUERY = "SELECT * FROM LIBROS";
    private static final String DELETE_LIBRO_QUERY = "delete from LIBROS WHERE ISBN = ?";
    private static final String SELECT_CAMPOS_QUERY = "SELECT * FROM LIBROS LIMIT 1";

    /**
     * Constructor: inicializa conexión
     *
     * @throws AccesoDatosException
     */

    public Libros() throws AccesoDatosException {
        try {
            // Obtenemos la conexión
            this.con = new Utilidades().getConnection();
            this.stmt = null;
            this.rs = null;
            this.pstmt = null;
        } catch (IOException e) {
            // Error al leer propiedades
            // En una aplicación real, escribo en el log y delego
            System.err.println(e.getMessage());
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        }
    }


    /**
     * Método para cerrar la conexión
     *
     * @throws AccesoDatosException
     */
    public void cerrar() {

        if (con != null) {
            Utilidades.closeConnection(con);
        }

    }


    /**
     * Método para liberar recursos
     *
     * @throws AccesoDatosException
     */
    private void liberar() {
        try {
            // Liberamos todos los recursos pase lo que pase
            //Al cerrar un stmt se cierran los resultset asociados. Podíamos omitir el primer if. Lo dejamos por claridad.
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log, no delego porque
            // es error al liberar recursos
            Utilidades.printSQLException(sqle);
        }
    }

    /**
     * Metodo que muestra por pantalla los datos de la tabla libros
     *
     * @throws SQLException
     */

    public List<Libro> verCatalogo() throws AccesoDatosException {
        /* Sentencia sql */
        stmt = null;
        /* Conjunto de Resultados a obtener de la sentencia sql */
        ResultSet rs = null;
        try {
            // Creación de la sentencia
            stmt = con.createStatement();
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = stmt.executeQuery(SEARCH_CATALOGO_QUERY);
            List<Libro> libros =new ArrayList<>();
            while (rs.next()) {
                Libro temp = new Libro();
                int resISBN = rs.getInt("ISBN");
                temp.setISBN(resISBN);
                String titulo = rs.getString("TITULO");
                temp.setTitulo(titulo);
                String autor = rs.getString("AUTOR");
                temp.setAutor(autor);
                String editorial = rs.getString("EDITORIAL");
                temp.setEditorial(editorial);
                int paginas = rs.getInt("PAGINAS");
                temp.setPaginas(paginas);
                int copias = rs.getInt("COPIAS");
                temp.setCopias(copias);

                libros.add(temp);
            }
            return libros;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }

    /**
     * Actualiza el numero de copias para un libro
     *
     * @param libro
     * @throws AccesoDatosException
     */

    public void actualizarCopias(Libro libro) throws AccesoDatosException {
        /* Sentencia sql */
        pstmt = null;

        try {

            pstmt = con.prepareStatement(UPDATE_PAGINAS_QUERY);
            pstmt.setInt(1, libro.getCopias());
            pstmt.setInt(2, libro.getISBN());
            // Ejecución de la inserción
            pstmt.executeUpdate();


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            liberar();
        }
    }


    /**
     * Añade un nuevo libro a la BD
     *
     * @param libro
     * @throws AccesoDatosException
     */
    public void anadirLibro(Libro libro) throws AccesoDatosException {

        /* Sentencia sql */
        pstmt = null;

        try {

            pstmt = con.prepareStatement(INSERT_LIBRO_QUERY);
            pstmt.setInt(1, libro.getISBN());
            pstmt.setString(2, libro.getTitulo());
            pstmt.setString(3, libro.getAutor());
            pstmt.setString(4, libro.getEditorial());
            pstmt.setInt(5, libro.getPaginas());
            pstmt.setInt(6, libro.getCopias());
            // Ejecución de la inserción
            pstmt.executeUpdate();


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            liberar();
        }

    }

    /**
     * Borra un libro por ISBN
     *
     * @param libro
     * @throws AccesoDatosException
     */

    public void borrar(Libro libro) throws AccesoDatosException {

        /* Sentencia sql */
        pstmt = null;

        try {
            // Creación de la sentencia
            pstmt = con.prepareStatement(DELETE_LIBRO_QUERY);
            pstmt.setInt(1, libro.getISBN());
            // Ejecución del borrado
            pstmt.executeUpdate();
            System.out.println("Libro " + libro.getTitulo() + " ha sido borrado.");

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            liberar();
        }

    }

    /**
     * Devuelve los nombres de los campos de BD
     *
     * @return
     * @throws AccesoDatosException
     */

    public String[] getCamposLibro() throws AccesoDatosException {
        /* Sentencia sql */
        stmt = null;
        /* Conjunto de Resultados a obtener de la sentencia sql */
        ResultSet rs = null;
        try {
            // Creación de la sentencia
            stmt = con.createStatement();
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = stmt.executeQuery(SEARCH_COLUMNAS_QUERY);
            int columnCount = rs.getMetaData().getColumnCount();

            String []names=new String[columnCount];
            for (int i=0; i<columnCount; i++) {

                String columnName = rs.getMetaData().getColumnName(i+1);
                names[i] = columnName;

            }
            return names;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }


    public void obtenerLibro(int ISBN) throws AccesoDatosException {
        /* Sentencia sql */
        pstmt = null;
        /* Conjunto de Resultados a obtener de la sentencia sql */
        ResultSet rs = null;
        try {
            // Creación de la sentencia
            pstmt = con.prepareStatement(SEARCH_LIBRO_QUERY);
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            pstmt.setInt(1, ISBN);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Libro temp = new Libro();
                int resISBN = rs.getInt("ISBN");
                temp.setISBN(resISBN);
                String titulo = rs.getString("TITULO");
                temp.setTitulo(titulo);
                String autor = rs.getString("AUTOR");
                temp.setAutor(autor);
                String editorial = rs.getString("EDITORIAL");
                temp.setEditorial(editorial);
                int paginas = rs.getInt("PAGINAS");
                temp.setPaginas(paginas);
                int copias = rs.getInt("COPIAS");
                temp.setCopias(copias);

                System.out.println(temp.toString());
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String[] getCamposLibroCopia() throws AccesoDatosException {

        /*Sentencia sql con parámetros de entrada*/
        pstmt = null;
        /*Conjunto de Resultados a obtener de la sentencia sql*/
        rs= null;
        ResultSetMetaData rsmd = null;
        String[] campos = null;
        try {
            //Solicitamos a la conexion un objeto stmt para nuestra consulta
            pstmt = con.prepareStatement(SELECT_CAMPOS_QUERY);

            //Le solicitamos al objeto stmt que ejecute nuestra consulta
            //y nos devuelve los resultados en un objeto ResultSet
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            int columns = rsmd.getColumnCount();
            campos = new String[columns];
            for (int i = 0; i < columns; i++) {
                //Los indices de las columnas comienzan en 1
                campos[i] = rsmd.getColumnLabel(i + 1);
            }
            return campos;


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally{
            liberar();
        }
    }


    public void crearTablaLibros() {
        /* Sentencia sql */
        stmt = null;
        /* Conjunto de Resultados a obtener de la sentencia sql */
        ResultSet rs = null;
        try {
            // Creación de la sentencia
            stmt = con.createStatement();
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            String com = "drop table if exists libros; create table libros ( " +
                    "ISBN integer not null, " +
                    "TITULO varchar(50) not null, " +
                    "AUTOR varchar(50) not null, " +
                    "EDITORIAL varchar(25) not null, " +
                    "PAGINAS integer not null," +
                    "COPIAS integer not null, " +
                    "constraint isbn_pk primary key (isbn)" +
                    ");";
            stmt.executeUpdate(com);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

}