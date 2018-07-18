package transformingjavac.forward;

import com.sun.tools.javac.file.BaseFileObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.CharsetDecoder;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;

public class ForwardingBaseFileObject<T extends BaseFileObject> extends BaseFileObject {

    private final T forward;

    public ForwardingBaseFileObject(T obj) {
        super(null);
        this.forward = obj;
    }

    @Override
    public String getShortName() {
        return forward.getShortName();
    }

    @Override
    public String toString() {
        return forward.toString();
    }

    @Override
    public NestingKind getNestingKind() {
        return forward.getNestingKind();
    }

    @Override
    public Modifier getAccessLevel() {
        return forward.getAccessLevel();
    }

    @Override
    public Reader openReader(boolean b) throws IOException {
        return forward.openReader(b);
    }

    @Override
    public boolean equals(Object o) {
        return forward.equals(o);
    }

    @Override
    public int hashCode() {
        return forward.hashCode();
    }

    @Override
    public Kind getKind() {
        return forward.getKind();
    }

    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        return forward.isNameCompatible(simpleName, kind);
    }

    @Override
    public URI toUri() {
        return forward.toUri();
    }

    @Override
    public String getName() {
        return forward.getName();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return forward.openInputStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return forward.openOutputStream();
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return forward.getCharContent(ignoreEncodingErrors);
    }

    @Override
    public Writer openWriter() throws IOException {
        return forward.openWriter();
    }

    @Override
    public long getLastModified() {
        return forward.getLastModified();
    }

    @Override
    public boolean delete() {
        return forward.delete();
    }

    // dumb protected methods

    private static final Method getDecoder = getMethod("getDecoder", boolean.class);
    private static final Method inferBinaryName = getMethod("inferBinaryName", Iterable.class);

    @Override
    protected CharsetDecoder getDecoder(boolean b) {
        return callProtected(getDecoder, b);
    }

    @Override
    protected String inferBinaryName(Iterable<? extends File> iterable) {
        return callProtected(inferBinaryName, iterable);
    }

    private static Method getMethod(String name, Class<?>... args) {
        try {
            Method m = BaseFileObject.class.getDeclaredMethod(name, args);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <R> R callProtected(Method mthd, Object... args) {
        try {
            return (R) mthd.invoke(forward, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
