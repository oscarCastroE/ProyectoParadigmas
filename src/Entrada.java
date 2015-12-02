
import java.util.ArrayList;

/**
 *
 * Entrada para un término del árbol de dependencias.
 */
public class Entrada 
{
    String tipo;
    int indice;
    int dependencia;
    
    public Entrada(String tipo, int indice, int dependencia)
    {
        this.tipo = tipo;
        this.indice = indice;
        this.dependencia = dependencia;
    }
    
    public String getTipo()
    {
        return tipo;
    }
    
    public int getIndice()
    {
        return indice;
    }
    
    public int getDependencia()
    {
        return dependencia;
    }
    
    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }
    
    public void setIndice(int indice)
    {
        this.indice = indice;
    }
    
    public void setDependencia(int dependencia)
    {
        this.dependencia = dependencia;
    }
}
