package util;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DocumentListenerAdapter implements DocumentListener {
    private final Runnable onChangeCallback;

    public DocumentListenerAdapter(Runnable onChangeCallback) {
        this.onChangeCallback = onChangeCallback;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        onChangeCallback.run();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        onChangeCallback.run();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        onChangeCallback.run();
    }
}
