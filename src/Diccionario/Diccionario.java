package Diccionario;

import Diccionario.Translate;
import Diccionario.Detect;

/**
 * Clase encargada de servir de diccionario para cada una de las
 * palabras que serán procesadas por el traductor.
 * Utiliza el Yandex API.
 */
public class Diccionario
{
    private Translate translator;
    private Detect detector;
    
    // Método constructor de la clase.
    public Diccionario()
    {
        translator = new Translate();
        detector = new Detect();
    }
    
    // Método encargado de traducir texto.
    public String traducir(String texto)
    {
        return translator.traducirPalabra(texto);
    }
    
    // Método encargado de traducir dado dos lenguajes cualquieras.
    // Los lenguajes deben ser soportados por el API del servicio web.
    public String traducir(String texto, String l1, String l2)
    {
        String l3 = detector.detectarIdioma(texto);
        if(l1.compareToIgnoreCase(l3) == 0)
        {
            return translator.traducirPalabra(texto, l1, l2);
        }
        return "";
    }    
}