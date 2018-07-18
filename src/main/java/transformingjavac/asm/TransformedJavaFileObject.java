package transformingjavac.asm;

import static org.objectweb.asm.Opcodes.ASM5;

import com.sun.tools.javac.file.BaseFileObject;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import transformingjavac.forward.ForwardingBaseFileObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TransformedJavaFileObject<F extends BaseFileObject> extends ForwardingBaseFileObject<F> {

    public TransformedJavaFileObject(F fileObject) {
        super(fileObject);
    }

    @Override
    public InputStream openInputStream() throws IOException {
        // Do transforms here
        ClassReader cr = new ClassReader(super.openInputStream());
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ClassTransformer(ASM5, cw);

        // Code is not needed when compiling. Only the stubs are needed.
        cr.accept(cv, ClassReader.SKIP_CODE);

        return new ByteArrayInputStream(cw.toByteArray());
    }
}
