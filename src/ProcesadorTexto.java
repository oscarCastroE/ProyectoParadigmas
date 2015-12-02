import java.util.ArrayList;

/**
 * Clase encargada de procesar principalmente hileras de texto.
 */
public class ProcesadorTexto 
{
    private Diccionario.Diccionario diccionario;

    // Método constructor de la clase.
    public ProcesadorTexto()
    {
        diccionario = new Diccionario.Diccionario();
    }
    
    // Separa las palabras de la oración,
    // omitiendo todo lo que no sean valores alfabéticos.
    public ArrayList<String> separateWords(String query)
    {
        ArrayList<String> result = new ArrayList<>();
        char c;
	String aux;
	int pos1 = 0;
	int pos2 = 0;

	for(int i = 0; i < query.length(); ++i)
	{
            c = query.charAt(i);
            // white spaces are ignored.
            if(validateChar(c) == true)
            {
                pos1 = i;
                // leaves the index on an invalid char.
                while((validateChar(c) == true) && i < query.length())
                {
                    pos2 = i;
                    ++i;
                    if(i < query.length()-1)
                    {
                        c = query.charAt(i);
                    }
                }
                if(pos2 == query.length())
                {
                    --pos2;
                }
                aux = query.substring(pos1, pos2+1);
                // saves the sub-string.
                result.add(aux);
            }		
        }
        return result;
    }
    
    // Valida qué puede o no ser procesado.
    public boolean validateChar(char c)
    {
        boolean ans = false;
        if(/*Character.isDigit(c) ||*/ Character.isLetter(c))
        {
            ans = true;
        }    
        return ans;
    }
    
    // Obtiene el sufijo según la terminación del verbo.
    public String obtenerSubHilera(String sufijo, int num)
    {
        String respuesta = "";
	int pos1 = sufijo.indexOf(' ');
	int pos2 = sufijo.indexOf(' ', pos1);
        switch(num)
        {
            case 1:
                respuesta = sufijo.substring(0, pos1);
                break;
            case 2:
                respuesta = sufijo.substring(pos1, pos2);
                break;
            case 3:
                respuesta = sufijo.substring(pos2, sufijo.length()-1);
                break;
        }
        return respuesta;
    }
    
    // Se convierte un ArrayList de hileras a un Array simple de hileras.
    public String[] toStringArray(ArrayList<String> palabrasAL)
    {
        int tam = palabrasAL.size();
        String[] palabrasA = new String[((tam > 0) ? tam : 1)];
        for(int i = 0; i < tam; ++i)
        {
            palabrasA[i] = palabrasAL.get(i);
        }
        return palabrasA; 
    }
    
    // Busco la traducción en el conjunto de traducciones.
    public boolean estarRepetido(ArrayList<String> Ct, String t)
    {
        for(int i = 0; i < Ct.size(); ++i)
        {
            if(t.compareToIgnoreCase(Ct.get(i)) == 0)
            {
                return true;
            }
        }
        return false;
    }
    
    // Calcula la persona a la que se le habla.
    public int calcularPersona(String tipo, String palabra)
    {
        if(tipo.compareToIgnoreCase("N") == 0)
        {
            if(palabra.compareToIgnoreCase("I") == 0 || palabra.compareToIgnoreCase("We") == 0)
            {
                return 1;
            }
            if(palabra.compareToIgnoreCase("You") == 0)
            {
                return 2;
            }
            return 3;        
        }
        return 0;
    }
    
    // Determina si un sustantivo es o no singular.
    // Regresa 'true' si es singular.
    public boolean calcularCantidad(String tipo, String palabra)
    {
        if(tipo.compareToIgnoreCase("N") == 0)
        {
            if(palabra.charAt(palabra.length()-1) == 's')
            {
                String traduccion = diccionario.traducir(palabra);
                if(traduccion.charAt(traduccion.length()-1) == 's')
                {
                    return false;
                }
            }
            if(palabra.compareToIgnoreCase("They") == 0 || palabra.compareToIgnoreCase("We") == 0)
            {
                return false;
            }
        }
        return true;
    }
    
    // Determina el tiempo del paradigma en el que se encuentra el texto.
    public int calcularTiempo(String tipoA, String anterior, String tipoV, String verbo)
    {
        int respuesta = 0;
        int tam = verbo.length();
        // Pretérito Imperfecto.
        if( (verbo.charAt(tam-1) == 'g' && verbo.charAt(tam-2) == 'n' && verbo.charAt(tam-1) == 'i') &&
        (anterior.compareToIgnoreCase("was") == 0 || anterior.compareToIgnoreCase("were") == 0))
        {
            return 1;
        }
        // Pretérito Perfecto Simple.
        if(verbo.charAt(tam-1) == 'd' && verbo.charAt(tam-2) == 'e')
        {
            return 2;
        }
        // Futuro simple.
        if(anterior.compareToIgnoreCase("will") == 0)
        {
            return 3;
        }
        // Condicional.
        if(anterior.compareToIgnoreCase("would") == 0)
        {
            return 4;
        }
        // Cualquier otro caso: Presente simple.
        return 0;
    }
    
    // Determina el artículo correspondiente al sustantivo.
    public String calcularArticulo(String tipoA, String articulo, String tipoS, String sustantivo)
    {
        boolean cantidad = calcularCantidad(tipoS, sustantivo);
        if(cantidad == true)
        {
            String traduccion = diccionario.traducir(sustantivo);
            if(traduccion.charAt(traduccion.length()-1) == 'a' || 
              (traduccion.charAt(traduccion.length()-2) == 'a' && traduccion.charAt(traduccion.length()-2) == 's'))
            {
                if(articulo.compareToIgnoreCase("a") == 0 || articulo.compareToIgnoreCase("an") == 0)
                {
                    return "una";
                }
                return "la";
            }
            if(articulo.compareToIgnoreCase("a") == 0 || articulo.compareToIgnoreCase("an") == 0)
            {
                return "un";
            }
            return "el";
        }
        else 
        {
            String traduccion = diccionario.traducir(sustantivo);
            if(traduccion.charAt(traduccion.length()-1) == 'a' || 
              (traduccion.charAt(traduccion.length()-2) == 'a' && traduccion.charAt(traduccion.length()-2) == 's'))
            {
                return "las";
            }
            return "los";
        }
    }
}