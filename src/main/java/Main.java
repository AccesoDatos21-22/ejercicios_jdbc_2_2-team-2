import org.iesinfantaelena.dao.Libros;
import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;

import java.util.HashMap;
import java.util.List;

public class Main {

public static void main(String[] args) {


		try {
            Libros libro = new Libros();
            Libro libro1 = new Libro(69, "Micha en el reino perdido", "Gabo Precioso", "Salvat", 2, 5);
            Libro libro2 = new Libro(6969, "Micha en el reino de los dragones malotes", "Gabo Sigue Siendo Precioso", "Anaya", 1, 10);

            //Creamos la tabla libros
            libro.crearTablaLibros();

            //Añadimos dos libros
            libro.anadirLibro(libro1);
            libro.anadirLibro(libro2);

            //Actualizamos copias
            System.out.println("Actualizando copias");
            HashMap<Integer, Integer> copias = new HashMap<>();
            copias.put(69, 5);
            copias.put(6969, 10);
            libro.actualizarCopias(copias);

            //Vemos el catalogo de libros
            System.out.println("Libros: ");
            List<Libro> lista = libro.verCatalogo();
            for (Libro l : lista) {
                System.out.println(l.toString());
            }

            //Vemos el catalogo inverso
            System.out.println("Catalogo inverso: ");
            libro.verCatalogoInverso();

            //Buscamos un libro
            System.out.println("Buscando libro con ISBN: "+libro2.getISBN());
            libro.obtenerLibro(libro2.getISBN());

            //Borramos un libro
            System.out.println("Borrando libro: "+ libro1);
            libro.borrar(libro1);
            //Volvemos a ver el catalogo de libros
            System.out.println("Libros: ");
            lista = libro.verCatalogo();
            for (Libro l : lista) {
                System.out.println(l.toString());
            }

            //Buscamos los campos de LIBROS
            System.out.println("Campos de LIBROS: ");
            String[]libros = libro.getCamposLibro();
            for (String col : libros) {
                System.out.println(col);
            }

            //Metodo getCamposLibros del word de los ejercicios
            System.out.println("Campos de LIBROS con el metodo del word");
            libros = libro.getCamposLibroCopia();
            for (String col : libros) {
                System.out.println(col);
            }

            libro.cerrar();
		} catch (AccesoDatosException e) {
			e.printStackTrace();
		}
	}

}