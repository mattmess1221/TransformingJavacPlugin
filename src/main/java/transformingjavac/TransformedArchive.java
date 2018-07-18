package transformingjavac;

import com.sun.tools.javac.file.BaseFileObject;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.file.RelativePath;
import transformingjavac.asm.TransformedJavaFileObject;
import transformingjavac.forward.ForwardingArchive;

import javax.tools.JavaFileObject;

class TransformedArchive extends ForwardingArchive {

    public TransformedArchive(JavacFileManager.Archive parent) {
        super(parent);
    }

    @Override
    public JavaFileObject getFileObject(RelativePath.RelativeDirectory relativeDirectory, String s) {
        // filter out any blacklisted packages
        if (TransformingJavacPlugin.transformerBlacklist.stream()
                .anyMatch(relativeDirectory.getPath()::startsWith)) {
            return super.getFileObject(relativeDirectory, s);
        }
        return new TransformedJavaFileObject<>((BaseFileObject) super.getFileObject(relativeDirectory, s));
    }
}
