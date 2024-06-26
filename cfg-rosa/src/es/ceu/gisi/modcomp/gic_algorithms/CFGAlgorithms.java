package es.ceu.gisi.modcomp.gic_algorithms;

import es.ceu.gisi.modcomp.gic_algorithms.exceptions.CFGAlgorithmsException;
import es.ceu.gisi.modcomp.gic_algorithms.interfaces.*;
import java.util.*;

/**
 * Esta clase contiene la implementación de las interfaces que establecen los
 * métodos necesarios para el correcto funcionamiento del proyecto de
 * programación de la asignatura Modelos de Computación.
 *
 * @author Sergio Saugar García <sergio.saugargarcia@ceu.es>
 */
public class CFGAlgorithms implements CFGInterface, WFCFGInterface, CNFInterface, CYKInterface {

    private HashSet<Character> noterminales = new HashSet();
    private HashMap<Character, ArrayList<String>> producciones = new HashMap<>();
    private HashSet<Character> terminales = new HashSet();
    private Character axioma;

    /**
     * Método que añade los elementos no terminales de la gramática.
     *
     * @param nonterminal Por ejemplo, 'S'
     *
     * @throws CFGAlgorithmsException Si el elemento no es una letra mayúscula o
     * si ya está en el conjunto.
     */
    public void addNonTerminal(char nonterminal) throws CFGAlgorithmsException {
        if (Character.isLetter(nonterminal) && Character.isUpperCase(nonterminal) && !noterminales.contains(nonterminal)) {
            noterminales.add(nonterminal);
        } else {
            throw new CFGAlgorithmsException();
        }
    }

    /**
     * Método que elimina el símbolo no terminal indicado de la gramática.
     * También debe eliminar todas las producciones asociadas a él y las
     * producciones en las que aparece.
     *
     * @param nonterminal Elemento no terminal a eliminar.
     *
     * @throws CFGAlgorithmsException Si el elemento no pertenece a la gramática
     */
    public void removeNonTerminal(char nonterminal) throws CFGAlgorithmsException {
        if (noterminales.contains(nonterminal)) {
            eliminarsiLacontiene(nonterminal);
            noterminales.remove(nonterminal);
        } else {
            throw new CFGAlgorithmsException();
        }
    }

    public void eliminarsiLacontiene(char letra) {
        String cad = "" + letra;
        for (Character nt : producciones.keySet()) {
            List<String> producc = producciones.get(nt);
            Iterator<String> iterador = producc.iterator();

            while (iterador.hasNext()) {
                String prod = iterador.next();
                if (prod.contains(cad)) {
                    iterador.remove();
                }
            }
        }
    }

    /**
     * Método que devuelve un conjunto con todos los símbolos no terminales de
     * la gramática.
     *
     * @return Un conjunto con los no terminales definidos.
     */
    public Set<Character> getNonTerminals() {
        return new HashSet<>(noterminales);
    }

    /**
     * Método que añade los elementos terminales de la gramática.
     *
     * @param terminal Por ejemplo, 'a'
     *
     * @throws CFGAlgorithmsException Si el elemento no es una letra minúscula o
     * si ya está en el conjunto.
     */
    public void addTerminal(char terminal) throws CFGAlgorithmsException {
        if (Character.isLetter(terminal) && Character.isLowerCase(terminal) && !terminales.contains(terminal)) {
            terminales.add(terminal);
        } else {
            throw new CFGAlgorithmsException();
        }
    }

    /**
     * Método que elimina el símbolo terminal indicado de la gramática. También
     * debe eliminar todas las producciones en las que aparece.
     *
     * @param terminal Elemento terminal a eliminar.
     *
     * @throws CFGAlgorithmsException Si el elemento no pertenece a la gramática
     */
    public void removeTerminal(char terminal) throws CFGAlgorithmsException {
        if (terminales.contains(terminal)) {
            eliminarsiLacontiene(terminal);
            terminales.remove(terminal);
        } else {
            throw new CFGAlgorithmsException();
        }
    }

    /**
     * Método que devuelve un conjunto con todos los símbolos terminales de la
     * gramática.
     *
     * @return Un conjunto con los terminales definidos.
     */
    public Set<Character> getTerminals() {
        return new HashSet<>(terminales);
    }

    /**
     * Método que indica, de los elementos no terminales, cuál es el axioma de
     * la gramática.
     *
     * @param nonterminal Por ejemplo, 'S'
     *
     * @throws CFGAlgorithmsException Si el elemento insertado no forma parte
     * del conjunto de elementos no terminales.
     */
    public void setStartSymbol(char nonterminal) throws CFGAlgorithmsException {
        if (noterminales.contains(nonterminal)) {
            axioma = nonterminal;
        } else {
            throw new CFGAlgorithmsException();
        }
    }

    /**
     * Método que devuelve el axioma de la gramática.
     *
     * @return El axioma de la gramática
     *
     * @throws CFGAlgorithmsException Si el axioma todavía no ha sido
     * establecido.
     */
    public Character getStartSymbol() throws CFGAlgorithmsException {
        if (!(axioma == null)) {
            return axioma;
        } else {
            throw new CFGAlgorithmsException();
        }
    }

    /**
     * Método utilizado para construir la gramática. Admite producciones de tipo
     * 2. También permite añadir producciones a lambda (lambda se representa con
     * el caracter 'l' -- ele minúscula). Se permite añadirla en cualquier no
     * terminal.
     *
     * @param nonterminal A
     * @param production Conjunto de elementos terminales y no terminales.
     *
     * @throws CFGAlgorithmsException Si está compuesta por elementos
     * (terminales o no terminales) no definidos previamente.
     */
    public void addProduction(char nonterminal, String production) throws CFGAlgorithmsException {
        if (noterminales.contains(nonterminal) && produccionValida(production)) {
            if (producciones.containsKey(nonterminal)) {
                if (!producciones.get(nonterminal).contains(production)) {
                    producciones.get(nonterminal).add(production);
                } else {
                    throw new CFGAlgorithmsException();
                }

            } else {
                ArrayList<String> produccion = new ArrayList<>();
                produccion.add(production);
                producciones.put(nonterminal, produccion);
            }
        } else {
            throw new CFGAlgorithmsException();
        }
    }

    public boolean produccionValida(String production) {
        if (production.equals("l")) {
            return true;
        }
        boolean salida = true;
        for (int i = 0; production.length() > i; i++) {
            char c = production.charAt(i);
            salida = salida && (noterminales.contains(c) || terminales.contains(c));
        }
        return salida;
    }

    /**
     * Elimina la producción indicada del elemento no terminal especificado.
     *
     * @param nonterminal Elemento no terminal al que pertenece la producción
     * @param production Producción a eliminar
     *
     * @return True si la producción ha sido correctamente eliminada
     *
     * @throws CFGAlgorithmsException Si la producción no pertenecía a ese
     * elemento no terminal.
     */
    public boolean removeProduction(char nonterminal, String production) throws CFGAlgorithmsException {
        if (producciones.get(nonterminal).contains(production)) {
            return producciones.get(nonterminal).remove(production);
        } else {
            throw new CFGAlgorithmsException();
        }
    }

    /**
     * Devuelve una lista de String que representan todas las producciones que
     * han sido agregadas a un elemento no terminal.
     *
     * @param nonterminal Elemento no terminal del que se buscan las
     * producciones
     *
     * @return Devuelve una lista de String donde cada String es la parte
     * derecha de cada producción
     */
    public List<String> getProductions(char nonterminal) {
        List<String> production = producciones.get(nonterminal);
        Collections.sort(production);
        return production;
    }

    /**
     * Devuelve un String que representa todas las producciones que han sido
     * agregadas a un elemento no terminal.
     *
     * @param nonterminal
     *
     * @return Devuelve un String donde se indica, el elemento no terminal, el
     * símbolo de producción "::=" y las producciones agregadas separadas, única
     * y exclusivamente por una barra '|' (no incluya ningún espacio). Por
     * ejemplo, si se piden las producciones del elemento 'S', el String de
     * salida podría ser: "S::=aBb|bC|dC". Las producciones DEBEN IR ORDENADAS
     * POR ORDEN ALFABÉTICO.
     */
    public String getProductionsToString(char nonterminal) {
        String p = "";
        if (noterminales.contains(nonterminal)) {
            p = nonterminal + "::=";
            p = p + getProductions(nonterminal).getFirst(); // Modificar getProductions para ordenar alfabéticamente
            for (int i = 1; producciones.get(nonterminal).size() > i; i++) {
                p = p + "|" + getProductions(nonterminal).get(i);
            }
        }
        return p;
    }

    /**
     * Devuelve un String con la gramática completa. Todos los elementos no
     * terminales deberán aparecer por orden alfabético (A,B,C...).
     *
     * @return Devuelve el agregado de hacer getProductionsToString sobre todos
     * los elementos no terminales ORDENADOS POR ORDEN ALFABÉTICO.
     */
    public String getGrammar() {
        List<Character> c = new ArrayList<>();
        char a;
        String cadena = "";
        for (Character nt : producciones.keySet()) {
            c.add(nt);
        }
        Collections.sort(c);
        for (int i = 0; c.size() > i; i++) {
            a = c.get(i);
            cadena = cadena + getProductionsToString(a) + ",\n";
        }
        return cadena;
    }

    /**
     * Elimina todos los elementos que se han introducido hasta el momento en la
     * gramática (elementos terminales, no terminales, axioma y producciones),
     * dejando el algoritmo listo para volver a insertar una gramática nueva.
     */
    public void deleteGrammar() {
        producciones.clear();
        noterminales.clear();
        terminales.clear();
    }

    /**
     * Método que comprueba si la gramática dada de alta es una gramática
     * independiente del contexto.
     *
     * @return true Si la gramática es una gramática independiente del contexto.
     */
    public boolean isCFG() { // No encuentro forma de hacerlo
        String lam = "l";
        for (Character nt : producciones.keySet()) {
            if (!nt.equals(axioma)) {
                for (int i = 0; producciones.get(nt).size() > i; i++) {
                    String prod = producciones.get(nt).get(i);
                    if (prod.contains(lam)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Método que comprueba si la gramática almacenada tiene reglas innecesarias
     * (A::=A).
     *
     * @return True si contiene ese tipo de reglas
     */
    public boolean hasUselessProductions() {
        String cad = "";
        for (Character nt : producciones.keySet()) {
            cad = "" + nt;
            for (String prod : producciones.get(nt)) {
                if (prod.equals(cad)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Método que elimina las reglas innecesarias de la gramática almacenada.
     *
     * @return Devuelve una lista de producciones (un String de la forma "A::=A"
     * por cada producción), con todas las reglas innecesarias eliminadas.
     */
    public List<String> removeUselessProductions() { //Ns
        String cad = "";
        String prodElim = "";
        List<String> prodsElim = new ArrayList<>();
        for (Character nt : producciones.keySet()) {
            cad = "" + nt;
            for (String prod : producciones.get(nt)) {
                if (prod.equals(cad)) {
                    prodElim = nt + "::=" + prod;
                    prodsElim.add(prodElim);
                    producciones.get(nt).remove(prod);
                }
            }
        }
        return prodsElim;
    }

    /**
     * Método que elimina los símbolos inútiles de la gramática almacenada.
     *
     * @return Devuelve una lista con todos los símbolos no terminales y
     * terminales eliminados.
     */
    public List<Character> removeUselessSymbols() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Método que comprueba si la gramática almacenada tiene reglas no
     * generativas (reglas lambda). Excepto S::=l si sólo es para reconocer la
     * palabra vacía.
     *
     * @return True si contiene ese tipo de reglas
     */
    public boolean hasLambdaProductions() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Método que elimina todas las reglas no generativas de la gramática
     * almacenada. La única regla que puede quedar es S::=l y debe haber sido
     * sustituida (y, por lo tanto, devuelta en la lista de producciones
     * "eliminadas").
     *
     * @return Devuelve una lista de no terminales que tenían reglas no
     * generativas y han sido tratadas.
     */
    public List<Character> removeLambdaProductions() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Método que comprueba si la gramática almacenada tiene reglas unitarias
     * (A::=B).
     *
     * @return True si contiene ese tipo de reglas
     */
    public boolean hasUnitProductions() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Método que elimina las reglas unitarias de la gramática almacenada.
     *
     * @return Devuelve una lista de producciones (un String de la forma "A::=B"
     * por cada producción), con todas las reglas unitarias eliminadas.
     */
    public List<String> removeUnitProductions() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Método que transforma la gramática almacenada en una gramática bien
     * formada: - 1. Elimina las reglas innecesarias. - 2. Elimina las reglas no
     * generativas. - 3. Elimina las reglas unitarias. - 4. Elimina los símbolo
     * inútiles.
     */
    public void transformToWellFormedGrammar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Método que chequea que las producciones estén en Forma Normal de Chomsky.
     *
     * @param nonterminal A
     * @param production A::=BC o A::=a (siendo B, C no terminales definidos
     * previamente y a terminal definido previamente). Se acepta S::=l si S es
     * no terminal y axioma.
     *
     * @throws CFGAlgorithmsException Si no se ajusta a Forma Normal de Chomsky
     * o si está compuesta por elementos (terminales o no terminales) no
     * definidos previamente.
     */
    public void checkCNFProduction(char nonterminal, String production) throws CFGAlgorithmsException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Método que comprueba si la gramática dada de alta se encuentra en Forma
     * Normal de Chomsky. Es una precondición para la aplicación del algoritmo
     * CYK.
     *
     * @return true Si la gramática está en Forma Normal de Chomsky
     */
    public boolean isCNF() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Método que transforma la gramática almacenada en su Forma Normal de
     * Chomsky equivalente.
     *
     * @throws CFGAlgorithmsException Si la gramática de la que partimos no es
     * una gramática bien formada.
     */
    public void transformIntoCNF() throws CFGAlgorithmsException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Método que indica si una palabra pertenece al lenguaje generado por la
     * gramática que se ha introducido. Se utilizará el algoritmo CYK para
     * decidir si la palabra pertenece al lenguaje.
     *
     * La gramática deberá estar en FNC.
     *
     * @param word La palabra a verificar, tiene que estar formada sólo por
     * elementos no terminales.
     *
     * @return TRUE si la palabra pertenece, FALSE en caso contrario
     *
     * @throws CFGAlgorithmsException Si la palabra proporcionada no está
     * formada sólo por terminales, si está formada por terminales que no
     * pertenecen al conjunto de terminales definido para la gramática
     * introducida, si la gramática es vacía o si el autómata carece de axioma.
     */
    public boolean isDerivedUsignCYK(String word) throws CFGAlgorithmsException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Método que, para una palabra, devuelve un String que contiene todas las
     * celdas calculadas por el algoritmo CYK (la visualización debe ser similar
     * al ejemplo proporcionado en el PDF que contiene el paso a paso del
     * algoritmo).
     *
     * @param word La palabra a verificar, tiene que estar formada sólo por
     * elementos no terminales.
     *
     * @return Un String donde se vea la tabla calculada de manera completa,
     * todas las celdas que ha calculado el algoritmo.
     *
     * @throws CFGAlgorithmsException Si la palabra proporcionada no está
     * formada sólo por terminales, si está formada por terminales que no
     * pertenecen al conjunto de terminales definido para la gramática
     * introducida, si la gramática es vacía o si carece de axioma.
     */
    public String algorithmCYKStateToString(String word) throws CFGAlgorithmsException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
