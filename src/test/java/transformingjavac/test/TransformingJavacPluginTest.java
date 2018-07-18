package transformingjavac.test;

import org.junit.Assert;
import org.junit.Test;

public class TransformingJavacPluginTest {

    @Test(expected = NoSuchMethodError.class)
    public void testCompile() {
        // This isn't going to compile in the IDE. Gradle has no problem though.
        // Normally, you should just need to specify -Xplugin:MyPlugin in the
        // compiler options, but intellij doesn't support different options for
        // different modules. Everything is project-wide.
        Assert.foo();
    }
}
