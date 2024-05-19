package it.unicam.cs.asdl2223.es13sol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Classe che implementa l'algoritmo di Dijkstra per il calcolo dei cammini
 * minimi da una sorgente singola. L'algoritmo usa una coda con priorità
 * inefficiente (implementata con una List) che per estrarre il minimo impiega
 * O(n).
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *                le etichette dei nodi del grafo
 */
public class DijkstraShortestPathComputer<L>
        implements SingleSourceShortestPathComputer<L> {
    // ultima sorgente su cui sono stati calcolati i cammini minimi
    private GraphNode<L> lastSource;

    // il grafo su cui opera questo oggetto
    private final Graph<L> grafo;

    // flag che indica se i cammini minimi sono stati calcolati almeno una volta
    private boolean isComputed = false;

    /*
     * Contiene i nodi ancora da analizzare, la coda con priorità viene gestita
     * tramite lista e l'elemento minimo viene cercato e rimosso con costo O(n)
     */
    private List<GraphNode<L>> queue;

    /**
     * Crea un calcolatore di cammini minimi a sorgente singola per un grafo
     * diretto e pesato privo di pesi negativi.
     * 
     * @param graph
     *                  il grafo su cui opera il calcolatore di cammini minimi
     * @throws NullPointerException
     *                                      se il grafo passato è nullo
     * 
     * @throws IllegalArgumentException
     *                                      se il grafo passato è vuoto
     * 
     * @throws IllegalArgumentException
     *                                      se il grafo passato non è orientato
     * 
     * @throws IllegalArgumentException
     *                                      se il grafo passato non è pesato,
     *                                      cioè esiste almeno un arco il cui
     *                                      peso è {@code Double.NaN}
     * @throws IllegalArgumentException
     *                                      se il grafo passato contiene almeno
     *                                      un peso negativo
     */
    public DijkstraShortestPathComputer(Graph<L> graph) {
        if (graph == null) {
            throw new NullPointerException("Il grafo passato è nullo");
        }
        if (graph.isEmpty()) {
            throw new IllegalArgumentException("Il grafo passato è vuoto");
        }
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Il grafo passato non è orientato");
        }
        for (GraphEdge<L> edge : graph.getEdges()) {
            if (!edge.hasWeight() || edge.getWeight() < 0) {
                throw new IllegalArgumentException("Il grafo passato non è "
                        + "pesato o ha pesi negativi");
            }
        }
        this.grafo = graph;
        this.isComputed = false;
        this.lastSource = null;
        this.queue = new ArrayList<GraphNode<L>>();
    }

    @Override
    public void computeShortestPathsFrom(GraphNode<L> sourceNode) {
        if (sourceNode == null) {
            throw new NullPointerException("Il nodo passato è nullo");
        }
        if (this.grafo.getNodeOf(sourceNode.getLabel()) == null) {
            throw new IllegalArgumentException(
                    "Il nodo passato non è contenuto nel grafo");
        }
        // inizializzazione
        for (GraphNode<L> node : grafo.getNodes()) {
            if (node.equals(sourceNode)) {
                // distanza dalla sorgente è 0
                node.setFloatingPointDistance(0.0);
            } else {
                // distanza iniziale sconosciuta
                // Imposto tutte le distanze dei nodi con un valore che non
                // potrà mai assumere
                node.setFloatingPointDistance(Double.POSITIVE_INFINITY);
            }
            // inizializzo il campo previuous a null
            node.setPrevious(null);
            // aggiungo il nodo alla coda
            queue.add(node);
        }
        while (!queue.isEmpty()) {
            GraphNode<L> currentNode = extractMinimumFrom(queue);
            // per ogni arco che fa parte degli archi connessi al nodo corrente
            for (GraphEdge<L> edge : grafo.getEdgesOf(currentNode))
                // pongo il valore della distanza = distanza nodo corrente +
                // peso dell'arco
                relax(currentNode, edge);
        }
        this.lastSource = sourceNode;
        this.isComputed = true;
    }

    private void relax(GraphNode<L> currentNode, GraphEdge<L> edge) {
        double newDistance = currentNode.getFloatingPointDistance()
                + edge.getWeight();
        // relax del nodo
        if (newDistance < edge.getNode2().getFloatingPointDistance()) {
            edge.getNode2().setFloatingPointDistance(newDistance);
            edge.getNode2().setPrevious(currentNode);

        }
    }

    private GraphNode<L> extractMinimumFrom(List<GraphNode<L>> l) {
        // Cerca nella lista il nodo con distanza minima
        int minDistanceNodeIndex = 0;
        for (int i = 1; i < l.size(); i++)
            if (l.get(i).getFloatingPointDistance() < l
                    .get(minDistanceNodeIndex).getFloatingPointDistance()) {
                minDistanceNodeIndex = i;
            }
        // il nodo in posizione minDistanceNodeIndex della lista è quello
        // che ha distanza minima
        GraphNode<L> toReturn = l.get(minDistanceNodeIndex);
        l.remove(minDistanceNodeIndex);
        return toReturn;
    }

    @Override
    public boolean isComputed() {
        return this.isComputed;
    }

    @Override
    public GraphNode<L> getLastSource() {
        if (!this.isComputed)
            throw new IllegalStateException("Richiesta last source, ma non "
                    + "sono mai stati calcolati i cammini minimi");
        return this.lastSource;
    }

    @Override
    public Graph<L> getGraph() {
        return this.grafo;
    }

    @Override
    public List<GraphEdge<L>> getShortestPathTo(GraphNode<L> targetNode) {
        // In caso di parametro null
        if (targetNode == null) {
            throw new NullPointerException(
                    "Richiesta di un cammino verso un " + "nodo target nullo");
        }
        // Nodo non appartenente al grafo
        if (grafo.getNodeOf(targetNode.getLabel()) == null) {
            throw new IllegalArgumentException("Richiesta di un cammino verso"
                    + " un nodo target che non esiste");
        }
        // Se i cammini minimi non sono stati computati
        if (!isComputed()) {
            throw new IllegalStateException("Cammini minimi non computati!");
        }
        // prendo il nodo del grafo che corrisponde al targetNode
        GraphNode<L> currentNode = grafo.getNodeOf(targetNode.getLabel());
        // Se il nodo non può essere raggiunto dalla sorgente e non è la
        // sorgente
        if (!currentNode.equals(this.lastSource)
                && currentNode.getPrevious() == null) {
            return null;
        }
        // creo la lista che rappresenta il cammino
        List<GraphEdge<L>> shortestPath = new ArrayList<GraphEdge<L>>();
        // Se il nodo target ha un nodo precedente
        if (currentNode.equals(this.lastSource))
            // Cammino vuoto
            return shortestPath;
        // Finché ha un precedente
        while (currentNode.getPrevious() != null) {
            // Cerco l'arco che lo collega al precedente e lo aggiungo alla
            // liata
            for (GraphEdge<L> e : grafo.getEdgesOf(currentNode.getPrevious()))
                if (e.getNode2().equals(currentNode)) {
                    shortestPath.add(e);
                    break; // esco dalla ricerca dell'arco da inserire
                }
            // Il targetNode diventa il suo previous e continuo finché non
            // ne ha più
            currentNode = currentNode.getPrevious();
        }

        /*
         * sono dovuto partire dal nodo di arrivo ed andare a ritroso fino al
         * nodo di partenza attraverso il metodo getPrevious() dei nodi,
         * aggiungere ogni arco attraversato in una List ed una volta arrivato
         * al nodo di partenza fare il reverse() della lista in modo da avere il
         * percorso in ordine
         *
         */
        Collections.reverse(shortestPath);
        return shortestPath;
    }
}
