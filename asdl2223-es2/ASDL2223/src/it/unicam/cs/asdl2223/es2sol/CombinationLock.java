package it.unicam.cs.asdl2223.es2sol;

/**
 * Un oggetto cassaforte con combinazione ha una manopola che può essere
 * impostata su certe posizioni contrassegnate da lettere maiuscole. La
 * serratura si apre solo se le ultime tre lettere impostate sono uguali alla
 * combinazione segreta.
 *
 * @author Luca Tesei
 */
public class CombinationLock {

    private String combinazioneAttuale;

    private boolean chiusa;

    private char ultimaPosizioneImpostata;

    private char penultimaPosizioneImpostata;

    private char terzultimaPosizioneImpostata;

    /**
     * Costruisce una cassaforte <b>aperta</b> con una data combinazioneAttuale
     *
     * @param aCombination
     *                         la combinazioneAttuale che deve essere una
     *                         stringa di 3 lettere maiuscole dell'alfabeto
     *                         inglese
     * @throw IllegalArgumentException se la combinazioneAttuale fornita non è
     *        una stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazioneAttuale fornita è nulla
     */
    public CombinationLock(String aCombination) {
        if (aCombination == null)
            throw new NullPointerException(
                    "Tentativo di creare una cassaforte con combinazioneAttuale nulla");
        if (aCombination.length() != 3)
            throw new IllegalArgumentException("Combinazione non di 3 lettere");
        // E' lunga 3
        if (!Character.isLetter(aCombination.charAt(0))
                || !Character.isUpperCase(aCombination.charAt(0)))
            throw new IllegalArgumentException(
                    "Primo simbolo della combinazioneAttuale non valido");
        if (!Character.isLetter(aCombination.charAt(1))
                || !Character.isUpperCase(aCombination.charAt(1)))
            throw new IllegalArgumentException(
                    "Secondo simbolo della combinazioneAttuale non valido");
        if (!Character.isLetter(aCombination.charAt(2))
                || !Character.isUpperCase(aCombination.charAt(2)))
            throw new IllegalArgumentException(
                    "Terzo simbolo della combinazioneAttuale non valido");
        this.combinazioneAttuale = aCombination;
        this.chiusa = false;
    }

    /**
     * Imposta la manopola su una certa posizione.
     *
     * @param aPosition
     *                      un carattere lettera maiuscola su cui viene
     *                      impostata la manopola
     * @throws IllegalArgumentException
     *                                      se il carattere fornito non è una
     *                                      lettera maiuscola dell'alfabeto
     *                                      inglese
     */
    public void setPosition(char aPosition) {
        if (!(Character.isLetter(aPosition)
                && Character.isUpperCase(aPosition)))
            throw new IllegalArgumentException(
                    "Posizinamento di carattere non consentito: " + aPosition);
        // Faccio scorrere le altre posizioni
        this.terzultimaPosizioneImpostata = this.penultimaPosizioneImpostata;
        this.penultimaPosizioneImpostata = this.ultimaPosizioneImpostata;
        this.ultimaPosizioneImpostata = aPosition;
    }

    /**
     * Tenta di aprire la serratura considerando come combinazioneAttuale
     * fornita le ultime tre posizioni impostate. Se l'apertura non va a buon
     * fine le lettere impostate precedentemente non devono essere considerate
     * per i prossimi tentativi di apertura.
     */
    public void open() {
        if (this.combinazioneAttuale
                .charAt(0) == this.terzultimaPosizioneImpostata
                && this.combinazioneAttuale
                        .charAt(1) == this.penultimaPosizioneImpostata
                && this.combinazioneAttuale
                        .charAt(2) == this.ultimaPosizioneImpostata)
            this.chiusa = false;
        else // resetto la combinazioneAttuale impostata fino ad ora
            this.ultimaPosizioneImpostata = 0;
    }

    /**
     * Determina se la cassaforte è aperta.
     *
     * @return true se la cassaforte è attualmente aperta, false altrimenti
     */
    public boolean isOpen() {
        return !this.chiusa;
    }

    /**
     * Chiude la cassaforte senza modificare la combinazioneAttuale attuale. Fa
     * in modo che se si prova a riaprire subito senza impostare nessuna nuova
     * posizione della manopola la cassaforte non si apre. Si noti che se la
     * cassaforte era stata aperta con la combinazioneAttuale giusta le ultime
     * posizioni impostate sono proprio la combinazioneAttuale attuale.
     */
    public void lock() {
        this.chiusa = true;
        // Faccio in modo che, una volta chiusa,
        // non si possa riaprire immediatamente
        this.ultimaPosizioneImpostata = 0;
    }

    /**
     * Chiude la cassaforte e modifica la combinazioneAttuale. Funziona solo se
     * la cassaforte è attualmente aperta. Se la cassaforte è attualmente chiusa
     * rimane chiusa e la combinazioneAttuale non viene cambiata, ma in questo
     * caso le le lettere impostate precedentemente non devono essere
     * considerate per i prossimi tentativi di apertura.
     *
     * @param aCombination
     *                         la nuova combinazioneAttuale che deve essere una
     *                         stringa di 3 lettere maiuscole dell'alfabeto
     *                         inglese
     * @throw IllegalArgumentException se la combinazioneAttuale fornita non è
     *        una stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazioneAttuale fornita è nulla
     */
    public void lockAndChangeCombination(String aCombination) {
        // se la cassaforte è attualmente aperta faccio l'operazione richiesta
        if (!this.chiusa) {
            if (aCombination == null)
                throw new NullPointerException(
                        "Tentativo di creare una cassaforte con combinazioneAttuale nulla");
            if (aCombination.length() != 3)
                throw new IllegalArgumentException(
                        "Combinazione non di 3 lettere");
            // E' lunga 3
            if (!Character.isLetter(aCombination.charAt(0))
                    || !Character.isUpperCase(aCombination.charAt(0)))
                throw new IllegalArgumentException(
                        "Primo simbolo della combinazioneAttuale non valido");
            if (!Character.isLetter(aCombination.charAt(1))
                    || !Character.isUpperCase(aCombination.charAt(1)))
                throw new IllegalArgumentException(
                        "Secondo simbolo della combinazioneAttuale non valido");
            if (!Character.isLetter(aCombination.charAt(2))
                    || !Character.isUpperCase(aCombination.charAt(2)))
                throw new IllegalArgumentException(
                        "Terzo simbolo della combinazioneAttuale non valido");
            combinazioneAttuale = aCombination;
            // Chiudo e faccio in modo che non sia
            // Immediatamente riapribile
            this.chiusa = true;
            this.ultimaPosizioneImpostata = 0;
        } else
            // altrimenti azzero solo le ultime posizioni
            this.ultimaPosizioneImpostata = 0;
    }

}