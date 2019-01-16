package ch.epfl.andropoly.domolab;

public class BooleanVariable {
    private boolean boo = false;
    private ChangeListener listener;

    public boolean isBoolean() {
        return boo;
    }

    public void setBoolean(boolean boo) {
        this.boo = boo;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public void delListener() {
        this.listener = null;
    }

    public interface ChangeListener {
        void onChange();
    }
}
