import java.util.ArrayList;

public class Traductor 
{
    private Diccionario.Diccionario diccionario;
    private ProcesadorTexto procesador;
    private String[][] paradigmaVerbal;
    
    public Traductor()
    {
        diccionario = new Diccionario.Diccionario();
        procesador = new ProcesadorTexto();
        iniciarPV();
    }
    
    // Inicializa la tabla 'paradigmaVerbal' con los sufijos del paradigma
    // verbal en español para verbos regulares en modo indicativo e imperativo.
    private void iniciarPV()
    {
        /* 
        - Definición de Número de Persona:
        paradigmaVerbal[0][x] : primera persona singular.
        paradigmaVerbal[1][x] : segunda persona singular.
        paradigmaVerbal[2][x] : tercera persona singular.
        paradigmaVerbal[3][x] : primera persona plural.
        paradigmaVerbal[4][x] : segunda persona plural.
        paradigmaVerbal[5][x] : tercera persona plural.
        - Definición de Tiempo:
        paradigmaVerbal[y][0] : presente simple.
        paradigmaVerbal[y][1] : pretérito imperfecto.
        paradigmaVerbal[y][2] : pretérito perfecto simple.
        paradigmaVerbal[y][3] : futuro simple.
        paradigmaVerbal[y][4] : condicional simple.
        paradigmaVerbal[y][5] : imperativo.
        */
        // De la forma: "ar er ir"
        // Segunda persona singular: tú. Segunda persona plural: vosotros.
        paradigmaVerbal = new String[6][6];
        // Presente simple.
        paradigmaVerbal[0][0] = "a o o";
        paradigmaVerbal[0][1] = "as es es";
        paradigmaVerbal[0][2] = "a e e";
        paradigmaVerbal[0][3] = "amos emos imos";
        paradigmaVerbal[0][4] = "áis éis ís";
        paradigmaVerbal[0][5] = "an en en";
        // Pretérito imperfecto.
        paradigmaVerbal[1][0] = "aba ía ía";
        paradigmaVerbal[1][1] = "abas ías ías";
        paradigmaVerbal[1][2] = "aba ía ía";
        paradigmaVerbal[1][3] = "ábamos íamos íamos";
        paradigmaVerbal[1][4] = "abais íais íais";
        paradigmaVerbal[1][5] = "aban ían ían";
        // Pretérito perfecto.
        paradigmaVerbal[2][0] = "é í í";
        paradigmaVerbal[2][1] = "aste iste iste";
        paradigmaVerbal[2][2] = "ó ió ió";
        paradigmaVerbal[2][3] = "amos imos imos";
        paradigmaVerbal[2][4] = "asteis isteis isteis";
        paradigmaVerbal[2][5] = "aron ieron ieron";
        // Futuro simple.
        paradigmaVerbal[3][0] = "aré eré iré";
        paradigmaVerbal[3][1] = "arás erás irás";
        paradigmaVerbal[3][2] = "ará erá irá";
        paradigmaVerbal[3][3] = "aremos eremos iremos";
        paradigmaVerbal[3][4] = "aráis eréis iréis";
        paradigmaVerbal[3][5] = "arán erán irán";
        // Condicional simple.
        paradigmaVerbal[4][0] = "aría ería iría";
        paradigmaVerbal[4][1] = "arías erías iríamos";
        paradigmaVerbal[4][2] = "aría ería iría";
        paradigmaVerbal[4][3] = "aríamos eríamos iríamos";
        paradigmaVerbal[4][4] = "arías eríais iríais";
        paradigmaVerbal[4][5] = "arían erían irían";
        // Imperativo.
        paradigmaVerbal[5][0] = "";
        paradigmaVerbal[5][1] = "a eme e";
        paradigmaVerbal[5][2] = "";
        paradigmaVerbal[5][3] = "";
        paradigmaVerbal[5][4] = "ad ed id";
        paradigmaVerbal[5][5] = "amen eman an";
    }
    
    // Método principal de la clase.
    // Se encarga de traducir el texto completo.
    // Ofrece las posibles traducciones para el texto dado en 'palabras'.
    // Utiliza las estructuras en los dos idiomas para traducir.
    public ArrayList<String> traducir(String[] palabras, String[] estructuraI, String[] estructuraE)
    {
        ArrayList<String> traducciones = new ArrayList<>();
        String traduccion = "";
        String parte1 = "";
        String parte2 = "";
        String articulo = "";
        int persona = 0;
        boolean cantidad = false;
        int tiempo = 0;
        for(int i = 0; i < estructuraE.length; ++i)
        {
            parte1 = estructuraE[i];
            for(int j = 0; j < estructuraI.length; ++j)
            {
                parte2 = estructuraI[j];
                if(parte1.compareToIgnoreCase(parte2) == 0)
                {
                    if(i != 0 && i != estructuraE.length-1)
                    {
                        traduccion += " ";
                    }
                    // Se verifica si es un caso especial.
                    if(parte1.compareToIgnoreCase("V") == 0)
                    {
                        // Verbo.
                        if(parte1.compareToIgnoreCase("V") == 0)
                        {
                            if(i != 0 || j != 0)
                            {
                                // Calcular persona.
                                persona = procesador.calcularPersona(estructuraI[j-1], palabras[j-1]);
                                // Calcular cantidad.
                                cantidad = procesador.calcularCantidad(estructuraI[j-1], palabras[j-1]);
                                // Calcular tiempo.
                                tiempo = procesador.calcularTiempo(estructuraI[j-1], palabras[j-1], estructuraI[j], palabras[j]);
                                traduccion += traducirVerbo(palabras[j], persona, cantidad, tiempo);
                            }
                            else
                            {
                                // Imperativo.
                                persona = 2;
                                cantidad = true;
                                tiempo = 5;
                                traduccion += traducirVerbo(palabras[j], persona, cantidad, tiempo);
                            }
                        }
                        // Artículo.
                        if(parte1.compareToIgnoreCase("D") == 0)
                        {
                            articulo = procesador.calcularArticulo(estructuraI[j], palabras[j], estructuraI[j+1], palabras[j+1]);
                            traduccion += articulo;
                        }                        
                    }
                    else
                    {
                        traduccion += diccionario.traducir(estructuraI[j]);
                    }
                    if(i == estructuraE.length-1)
                    {
                        traduccion += ".";
                    }
                }
            }
        }        
        return traducciones;
    }
    
    // Método encargado de conjugar el verbo de acuerdo a la persona,
    // cantidad y tiempo.
    private String traducirVerbo(String verboI, int persona, boolean sing, int tiempo)
    {
        String respuesta = "";
        String sufijo = "";
        String raiz = "";
        // Obtener verbo del diccionario.
        String verboE = diccionario.traducir(verboI);
        if(verboE.compareTo("") != 0)
        {
            raiz = verboE.substring(0, verboE.length()-3);
            sufijo = obtenerSufijoV(verboE, persona, sing, tiempo);
        }
        respuesta = raiz + sufijo;
        return respuesta;
    }
    
    // Se recibe un verbo para determinar cuál es la conjugación adecuada.
    private String obtenerSufijoV(String verbo, int persona, boolean sing, int tiempo)
    {
        String respuesta = "";
        String aux;
        // Se verifica si termina el verbo en -ar, -er, -ir.
        char term = verbo.charAt(verbo.length()-2);
        int p = persona - 1 + ((sing == true) ? 0 : 3);
        aux = paradigmaVerbal[p][tiempo];
        switch(term)
        {
            case 'a':
                respuesta = procesador.obtenerSubHilera(aux, 1);
                break;
            case 'e':
                respuesta = procesador.obtenerSubHilera(aux, 2);
                break;
            case 'i':
                respuesta = procesador.obtenerSubHilera(aux, 1);
                break;
        }
        return respuesta;
    }
}