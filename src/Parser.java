import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;
import java.util.Collection;

import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import java.util.ArrayList;


public class Parser 
{
    LexicalizedParser lp;
    TreebankLanguagePack tlp;
    GrammaticalStructureFactory gsf;
    
    // Método constructor de la clase.
    public Parser()
    {
        // Se carga el modelo para parsear.
        lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz",
                                                "-maxLength", "80", "-retainTmpSubcategories");
        tlp = new PennTreebankLanguagePack();
        gsf = tlp.grammaticalStructureFactory();
    }
   
    // Método encargado de generar el árbol gramatical para un determinado texto.
    public String procesar(String[] texto)
    {
        ArrayList<Entrada> respuesta = new ArrayList();
        Collection<TypedDependency> tdl = generarArbol(texto);
        /* for(TypedDependency td : tdl)
        {
            System.out.println(td.gov().index());
            System.out.println(td.dep().index());
            System.out.println(td.reln().toString());
        } */
        // Contenido del árbol generado por el parser.
        // System.out.println(tdl.toString());
        respuesta = traducirArbol(tdl);
        return transformarArbol(respuesta);
    }
    
    // Se genera el árbol de gramática del parser.
    public Collection<TypedDependency> generarArbol(String[] texto)
    {
        Tree parse = lp.apply(Sentence.toWordList(texto));
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
        return tdl;
    }
    
    // Método encargado de tomar la estructura árbol del parser
    // y volverlo una lista para recorrerlo con facilidad.
    public ArrayList<Entrada> traducirArbol(Collection<TypedDependency> tdl)
    {
        ArrayList<Entrada> respuesta = new ArrayList();
        Entrada ent;
        int dependencia;
        int indice;
        String tipo;
        for(TypedDependency td : tdl)
        {
            dependencia = td.gov().index();
            indice = td.dep().index();
            tipo = td.reln().toString();
            ent = new Entrada(tipo, indice, dependencia);
            respuesta.add(ent);
        }    
        return respuesta;
    }
    
    // Se encarga de traducir el árbol generado por el parser a uno
    // que pueda ser entendido por Prolog.
    public String transformarArbol(ArrayList<Entrada> arbolG)
    {
        String respuesta = "";
        boolean adjB = false;
        boolean doB = false;
        String[] tipos = new String[(arbolG.size() > 0 ? arbolG.size() : 1)];
        // Se llena el vector de tipos.
        for(int i = 0; i < arbolG.size(); ++i)
        {
            tipos[i] = arbolG.get(i).getTipo();
        }
        String[] tiposC = interpretarTipos(tipos);
        /* for(int j = 0; j < tiposC.length; ++j)
        {
            System.out.println(tiposC[j]);
        } */ 
        // Se procesan las reglas.
        for(int i = 0; i < arbolG.size(); ++i)
        {
            if(i == 0)
            {
                respuesta += "S(";
            }
            else
            {
                if(respuesta.charAt(i-1) != '(')
                {
                    respuesta += ", ";
                }
            }
            if(tiposC[i].compareToIgnoreCase("ADV") == 0)
            {
                respuesta += ", ";
            }
            respuesta += tiposC[i];
            if((tiposC[i].compareToIgnoreCase("DO") == 0 && doB == true) || 
               (tiposC[i].compareToIgnoreCase("N") == 0 && adjB == true))
            {
                respuesta += ")";
            }
            // Oraciones Transitivas.
            if(tiposC[i].compareToIgnoreCase("V") == 0 && estarEnLista(tiposC, "DO", i) != -1 && estarEnLista(tiposC, "ADV", i) == -1)
            {
                if(estarEnLista(tiposC, "DO", 0) != i+1)
                {
                    respuesta += ", NP(";
                    doB = true;
                }
                else
                {
                    respuesta += ", ";
                }
            }
            if(tiposC[i].compareToIgnoreCase("V") == 0 && estarEnLista(tiposC, "ADJ", i) != -1 && estarEnLista(tiposC, "DO", i) == -1)
            {
                if(estarEnLista(tiposC, "ADJ", 0) != i+1)
                {
                    respuesta += ", ADJP(";
                    adjB = true;
                }
                else
                {
                    respuesta += ", ";
                }
            }
        }
        respuesta += ")";
        return respuesta;
    }
    
    // Método encargado de transformar el vector de tipos generado por el parser
    // a tipos comprensibles para el análisis gramatical que se desea.
    private String[] interpretarTipos(String[] tipos)
    {
        String[] tiposN = new String[(tipos.length > 0 ? tipos.length : 1)];
        for(int i = 0; i < tipos.length; ++i)
        {
            if(tipos[i].compareToIgnoreCase("nsubj") == 0)
            {
                tiposN[i] = "N";
            }
            if(tipos[i].compareToIgnoreCase("root") == 0)
            {
                if(estarEnLista(tipos, "cop", 0) != -1)
                {
                    tiposN[i] = "ADJ";
                }
                else
                {
                    tiposN[i] = "V"; 
                }
            }
            if(tipos[i].compareToIgnoreCase("det") == 0)
            {
                tiposN[i] = "DET";
            }
            if(tipos[i].compareToIgnoreCase("amod") == 0)
            {
                tiposN[i] = "ADJ";
            }
            if(tipos[i].compareToIgnoreCase("root") == 0 && tipos[(i-1 > 1) ? i-1 : i].compareToIgnoreCase("amod") == 0)
            {
                tiposN[i] = "N";
            }
            if(tipos[i].compareToIgnoreCase("dobj") == 0)
            {
                tiposN[i] = "DO";
            }
            if(tipos[i].compareToIgnoreCase("case") == 0)
            {
                tiposN[i] = "P";
            }
            if(tipos[i].compareToIgnoreCase("nmod:to") == 0 || tipos[i].compareToIgnoreCase("xcomp") == 0)
            {
                tiposN[i] = "DO";
            }
            if(tipos[i].compareToIgnoreCase("xcomp") == 0 && tipos[i-1].compareToIgnoreCase("nsubj") == 0)
            {
                tiposN[i] = "ADV";
            }
            if(tipos[i].compareToIgnoreCase("advmod") == 0)
            {
                tiposN[i] = "ADV";
            }
            if(tipos[i].compareToIgnoreCase("cop") == 0)
            {
                tiposN[i] = "V";
            }
        }
        return tiposN;
    }
    
    // Método encargado de verificar si el elemento deseado se encuentra
    // en un array a partir del indice dado.
    // Devuelve la posición donde está el elemento, y -1 si no se encuentra.
    private int estarEnLista(String[] arr, String buscado, int ind)
    {
        for(int i = ind; i < arr.length && arr.length != 0; ++i)
        {
            if(arr[i].compareToIgnoreCase(buscado) == 0)
            {
                return i;
            }
        }
        return -1;
    }
    
    // Método encargado de crear una lista con los tipos que componen el texto.
    public String[] generarListaTipos(String[] texto)
    {
        Collection<TypedDependency> tdl = generarArbol(texto);
        ArrayList<Entrada> arbolG = traducirArbol(tdl);
        String[] tipos = new String[(arbolG.size() > 0 ? arbolG.size() : 1)];
        // Se llena el vector de tipos.
        for(int i = 0; i < arbolG.size(); ++i)
        {
            tipos[i] = arbolG.get(i).getTipo();
        }
        return(interpretarTipos(tipos));
    }
}
