package performancescheduler.data;

public class Wrapper<T> {
    protected T wrapped;
    
    protected Wrapper(T toWrap) {
        setWrapped(toWrap);
    }
    
    public final void setWrapped(T toWrap) {
        if (toWrap == null) {
            throw new NullPointerException("Wrapper: wrapped object must be non-null.");
        }
        // do not allow wrapping this; infinite recursion would ensue!
        preventRecursiveWrap(toWrap);
        wrapped = toWrap;
    }
    
    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        return wrapped.equals(o);
    }

    private void preventRecursiveWrap(Object toWrap) {
        if (toWrap instanceof Wrapper) {
            if (toWrap == this) {
                throw new IllegalArgumentException("Wrapper may not wrap itself.");
            } else {
                preventRecursiveWrap(((Wrapper<?>)toWrap).wrapped);
            }
        }
    }
}
