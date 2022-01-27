package ex08.loader;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.loader.Reloader;

import java.net.URL;
import java.net.URLClassLoader;

public class SimpleWebappClassLoader extends URLClassLoader implements Lifecycle, Reloader {

    // ------------------------------------------------------- Static Variables

    /**
     * 不允许加载的类
     */
    private static final String[] triggers = {
            "javax.servlet.Servlet"                     // Servlet API
    };

    /**
     * 不允许加载的包下面的类
     */
    private static final String[] packageTriggers = {
            "javax",                                     // Java extensions
            "org.xml.sax",                               // SAX 1 & 2
            "org.w3c.dom",                               // DOM 1 & 2
            "org.apache.xerces",                         // Xerces 1 & 2
            "org.apache.xalan"                           // Xalan
    };

    // ----------------------------------------------------------- Constructors

    public SimpleWebappClassLoader() {
        super(new URL[0]);
    }

    // ----------------------------------------------------------- Lifecycle

    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return new LifecycleListener[0];
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void start() throws LifecycleException {

    }

    @Override
    public void stop() throws LifecycleException {

    }

    // ----------------------------------------------------------- ReLoader

    @Override
    public void addRepository(String repository) {

    }

    @Override
    public String[] findRepositories() {
        return new String[0];
    }

    @Override
    public boolean modified() {
        return false;
    }
}
