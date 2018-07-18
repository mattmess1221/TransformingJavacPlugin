# TransformingJavacPlugin
Javac hack for transforming library classes

Nobody should ever feel a need to use this.

This plugin adds a static method `public static void foo()` to every class on the classpath.
Just add `Xplugin:FooJavacTransformer` to the javac arguments.
