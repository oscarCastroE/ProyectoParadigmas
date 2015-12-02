/* 
*  Clase encargada de integrar toda la aplicación.
*/
public class Controlador 
{
    private static Automatizador automatizador;
    private static Formulario formulario;
    
    // Método constructor de la clase.
    public Controlador()
    {
        automatizador = new Automatizador();
        formulario = new Formulario(automatizador);
    }
    
    // Método encargado de iniciar el corrimiento del programa.
    public void correr()
    {
        formulario.setVisible(true);
    }
    
    // Método encargado de correr toda la aplicación.
    public static void main(String args[])
    {
        Controlador controlador = new Controlador();
        controlador.correr();
    }

                
        /*
        // Se carga el archivo Prolog.
        String t1 = "consult('transformador.pl')";
        
        Query q1 = new Query(t1);
        System.out.println( t1 + " " + (q1.hasSolution() ? "correcto" : "fallo") );
        String t2 = "encima_de(a,b)";
        Query q2 = new Query(t2);
        System.out.println( t2 + " is " + (q2.hasSolution() ? "probado" : "no probado") );
        String t4 = "mas_arriba_de(X,Y)";
        Query q4 = new Query(t4);
        System.out.println( "Solucion para t4 " + t4 );
        while ( q4.hasMoreSolutions() )
        {
            java.util.Map<String, Term> s4= q4.nextSolution();
            System.out.println( "X = " + s4.get("X") + ", Y = " + s4.get("Y"));
        } */
}