package util;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Adaptateur pour simplifier l'utilisation d'un `DocumentListener`.
 * Permet de spécifier une seule action à exécuter lorsque le contenu d'un document change.
 */
public class DocumentListenerAdapter implements DocumentListener {
    private final Runnable onChangeCallback; // Callback exécuté lors d'une modification

    /**
     * Constructeur de la classe DocumentListenerAdapter.
     *
     * @param onChangeCallback Une action à exécuter lorsque le document est modifié.
     */
    public DocumentListenerAdapter(Runnable onChangeCallback) {
        this.onChangeCallback = onChangeCallback;
    }

    /**
     * Appelé lorsqu'un texte est inséré dans le document.
     *
     * @param e L'événement déclenché par l'insertion.
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        onChangeCallback.run();
    }

    /**
     * Appelé lorsqu'un texte est supprimé du document.
     *
     * @param e L'événement déclenché par la suppression.
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        onChangeCallback.run();
    }

    /**
     * Appelé lorsqu'un attribut du document est modifié.
     *
     * @param e L'événement déclenché par la modification.
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
        onChangeCallback.run();
    }
}
