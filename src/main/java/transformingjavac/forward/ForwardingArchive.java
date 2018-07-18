package transformingjavac.forward;

import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.file.RelativePath;

import java.io.IOException;
import java.util.Set;

import javax.tools.JavaFileObject;

public class ForwardingArchive implements JavacFileManager.Archive {

    private final JavacFileManager.Archive parent;

    public ForwardingArchive(JavacFileManager.Archive parent) {
        this.parent = parent;
    }

    @Override
    public void close() throws IOException {
        parent.close();
    }

    @Override
    public boolean contains(RelativePath relativePath) {
        return parent.contains(relativePath);
    }

    @Override
    public JavaFileObject getFileObject(RelativePath.RelativeDirectory relativeDirectory, String s) {
        return parent.getFileObject(relativeDirectory, s);
    }

    @Override
    public com.sun.tools.javac.util.List<String> getFiles(RelativePath.RelativeDirectory relativeDirectory) {
        return parent.getFiles(relativeDirectory);
    }

    @Override
    public Set<RelativePath.RelativeDirectory> getSubdirectories() {
        return parent.getSubdirectories();
    }
}
