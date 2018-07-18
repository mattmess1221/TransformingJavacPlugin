package transformingjavac.asm;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

class ClassTransformer extends ClassVisitor {

    private String NO_ARGS_VOID = Type.getMethodDescriptor(Type.VOID_TYPE);

    private boolean transform;

    public ClassTransformer(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);

        // don't put the static foo where it doesn't belong
        if (((access & ACC_INTERFACE) != ACC_INTERFACE || ((version & V1_8) == V1_8))
                && (access & ACC_ANNOTATION) != ACC_ANNOTATION) {
            transform = true;
        }
    }

    @Override
    public void visitEnd() {
        // inject method stubs at the end of the class file
        if (transform) {
            // public static void foo()
            MethodVisitor mv = this.visitMethod(ACC_PUBLIC | ACC_STATIC, "foo", NO_ARGS_VOID, null, null);
            mv.visitCode();
            mv.visitEnd();
        }
        super.visitEnd();
    }
}
