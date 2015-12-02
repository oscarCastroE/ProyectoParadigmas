 // Clases de Prolog
import java.util.ArrayList;
import org.jpl7.*;
import org.jpl7.Query;

/**
 *  Clase encargada de ejecutar los comandos del archivo de Prolog,
 *  es decir, llevar a cabo la aplicación del conjunto de reglas.
 */
public class Procesador
{
    // Ruta del archivo a ejecutar.
    String ruta = "";
    
    // Método constructor de la clase.
    public Procesador()
    {
        // Se define el archivo de Prolog a cargar.
        ruta = "consult('transformador.pl')";
        // Se carga el archivo de Prolog.
        procesarConsulta(ruta);
    }

    // Método encargado de procesar una consulta utilizando el
    // código Prolog de un archivo dado.
    public ArrayList<String> procesarConsulta(String consulta)
    {
        ArrayList<String> respuesta = new ArrayList<>();
        String aux;
        
        // Se realiza la consulta.
        Query q = new Query(consulta);
        
        // Mientras haya soluciones restantes.
        while(q.hasMoreSolutions())
        {
            java.util.Map<String, Term> s = q.nextSolution();
            // System.out.println( "X = " + s.get("X") + ", Y = " + s.get("Y"));
            aux = "X = " + s.get("X") + ", Y = " + s.get("Y");
            if(!aux.isEmpty())
            {
                respuesta.add(aux);
            }            
        }
        return respuesta;
    }   
}