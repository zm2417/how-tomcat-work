package ex08.startup;

import ex08.core.*;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoader;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.logger.FileLogger;
import org.apache.naming.resources.ProxyDirContext;

public class Bootstrap {

    public static void main(String[] args) {

        System.setProperty("catalina.base", System.getProperty("user.dir"));

        HttpConnector httpConnector = new HttpConnector();

        Wrapper wrapper1 = new SimpleWrapper();
        wrapper1.setName("Primitive");
        wrapper1.setServletClass("PrimitiveServlet");
        Wrapper wrapper2 = new SimpleWrapper();
        wrapper2.setName("Modern");
        wrapper2.setServletClass("ModernServlet");

        Context context = new StandardContext();

        // 等于server.xml <Context path="/myApp" docBase="myApp"/>
        context.setPath("/myApp");
        context.setDocBase("myApp");

        context.addChild(wrapper1);
        context.addChild(wrapper2);
        context.addServletMapping("/Primitive", "Primitive");
        context.addServletMapping("/Modern", "Modern");

        LifecycleListener listener = new SimpleContextConfig();
        ((Lifecycle) context).addLifecycleListener(listener);

        Loader loader = new WebappLoader();
        context.setLoader(loader);

        httpConnector.setContainer(context);

        try {
            httpConnector.initialize();
            httpConnector.start();
            ((Lifecycle) context).start();

            WebappClassLoader classLoader = (WebappClassLoader) loader.getClassLoader();
            System.out.println("Bootstrap main() print: " + " Resource docBase: " + ((ProxyDirContext) classLoader.getResources()).getDocBase());
            String[] repositories = classLoader.findRepositories();
            for (String repository : repositories) {
                System.out.println("Bootstrap main() print: " + " repository: " + repository);
            }

            System.in.read();
            ((Lifecycle) context).stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
