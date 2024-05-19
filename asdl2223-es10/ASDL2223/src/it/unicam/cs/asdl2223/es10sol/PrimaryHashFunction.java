package it.unicam.cs.asdl2223.es10sol;

/**
 * Una classe che implementa questa interfaccia deve fornire una particolare
 * implementazione di una funzione hash primaria.
 */
public interface PrimaryHashFunction {
    /**
     * Calcola l'hash primario di una chiave per il collocamento in una tabella
     * hash di una certa dimensione.
     * 
     * @param key
     *                la chiave dell'elemento da collocare
     * @param m
     *                la dimensione della tabella hash
     * @return un valore compreso tra {@code 0} ed {@code m-1} che corrisponde
     *         alla posizione della chiave passata nella tabella
     */
    public int hash(int key, int m);
}
