package transformingjavac;

import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Context;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import javax.tools.JavaFileManager;

/**
 * This is a javac plugin that hacks at the internals of the program in order to
 * be able to transform class files before source files are compiled.
 *
 * This program is meant to act as an example for a larger, more appropriate
 * library to implement. As such, this plugin only adds a static method to every
 * class which can support one. This is the method that was added.
 *
 * <pre>
 * public static void foo() {
 *
 * }
 * </pre>
 */
public class TransformingJavacPlugin implements Plugin {

    public static List<String> transformerBlacklist = new ArrayList<>();

    @Override
    public String getName() {
        return "FooJavacTransformer";
    }

    @Override
    public void init(JavacTask task, String... args) {

        BasicJavacTask basicTask = (BasicJavacTask) task;
        Context context = basicTask.getContext();
        Messager msg = JavacProcessingEnvironment.instance(context).getMessager();

        msg.printMessage(Diagnostic.Kind.NOTE, "Running javac with transformed classes");

        transformerBlacklist.add("java/");
        transformerBlacklist.add("sun/");

        JavaFileManager jfm = context.get(JavaFileManager.class);

        // This is the entry point. There may be other ways, but this is the most direct.
        try {
            // set the archives so I can MITM attack the file manager.
            // TODO: Does openjdk have a similar implementation? Non-api might break across versions
            Field f = JavacFileManager.class.getDeclaredField("archives");
            f.setAccessible(true);
            f.set(jfm, new HashMap<File, JavacFileManager.Archive>() {
                @Override
                public JavacFileManager.Archive put(File key, JavacFileManager.Archive value) {
                    // wrap the value in my own forwarding archive.
                    return super.put(key, new TransformedArchive(value));
                }
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


}
