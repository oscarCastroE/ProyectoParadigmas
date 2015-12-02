import java.util.Hashtable;
import java.util.ArrayList;
import org.jpl7.*;
import org.jpl7.Query;


// Clase encargada de automatizar el proceso de traducción
// e integrar todas las partes.
public class Automatizador 
{
    private Diccionario.Diccionario diccionario;
    private ProcesadorTexto procesadorTexto;
    private Procesador procesador;
    private Traductor traductor;
    private Parser parser;
    
    public Automatizador()
    {
        diccionario = new Diccionario.Diccionario();
        procesador = new Procesador();
        traductor = new Traductor();
        parser = new Parser();
    }
    
    public ArrayList<String> traducir(String texto)
    {
        ArrayList<String> palabras = new ArrayList<>();
        ArrayList<String> respuestasProlog = new ArrayList<>();
        // Traducciones parciales.
        ArrayList<String> traduccionesP = new ArrayList<>();
        // Traducciones finales.
        ArrayList<String> traduccionesF = new ArrayList<>();
        String arbolGramatical = "";
        String consultaProlog = "";
        String traduccion;
        String[] gramaticaI;
        String[] gramaticaE;
        
        if(texto.isEmpty())
        {
            traduccionesF.add("Traducción no disponible.");
            return traduccionesF;
        }
        
        // Se separa el texto en palabras.
        palabras = procesadorTexto.separateWords(texto);
        // Se transforman esas palabras a un formato que pueda ser parseado.
        String[] palabrasP = procesadorTexto.toStringArray(palabras);
        // El parser procesa las palabras.
        arbolGramatical = parser.procesar(palabrasP);
        // Se crea la estructura en inglés.
        gramaticaI = parser.generarListaTipos(palabrasP);
        // Se crea la consulta a prolog de acuerdo al contenido del árbol.
        // consultaProlog = procesadorTexto.generarConsulta(consulta, arbolGramatical);
        // Se envía la consulta a prolog.
        // consultaProlog lleva la estructura en inglés, la devuelve
        // en español.
        respuestasProlog = procesador.procesarConsulta(consultaProlog);
        for(int i = 0; i < respuestasProlog.size(); ++i)
        {
            // gramaticaE = traductor.prologToArray(respuestasProlog.get(i));
            // traduccionesP = traductor.traducir(palabrasP, gramaticaI, gramaticaE);
            for(int j = 0; j < traduccionesP.size(); ++j)
            {
                // Verificar que no esté ya la traducción.
                traduccion = traduccionesP.get(j);
                if(!procesadorTexto.estarRepetido(traduccionesF, traduccion))
                {
                    traduccionesF.add(traduccion);
                }
            }
        }
        return traduccionesF;    
    }
    
}
