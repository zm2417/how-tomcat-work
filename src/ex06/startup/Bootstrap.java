package ex06.startup;

import ex06.core.*;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;

public class Bootstrap {

    public static void main(String[] args) {
        HttpConnector httpConnector = new HttpConnector();

        Wrapper wrapper1 = new SimpleWrapper();
        wrapper1.setName("Primitive");
        wrapper1.setServletClass("PrimitiveServlet");
        Wrapper wrapper2 = new SimpleWrapper();
        wrapper2.setName("Modern");
        wrapper2.setServletClass("ModernServlet");

        Context context = new SimpleContext();
        context.addChild(wrapper1);
        context.addChild(wrapper2);

        Mapper mapper = new SimpleContextMapper();
        mapper.setProtocol("http");
        context.addMapper(mapper);

        SimpleContextLifecycleListener listener = new SimpleContextLifecycleListener();
        ((Lifecycle) context).addLifecycleListener(listener);

        Loader loader = new SimpleLoader();
        context.setLoader(loader);

        context.addServletMapping("/Primitive", "Primitive");
        context.addServletMapping("/Modern", "Modern");

        httpConnector.setContainer(context);

        try {
            httpConnector.initialize();
            httpConnector.start();
            ((Lifecycle) context).start();

            System.in.read();
            ((Lifecycle) context).stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
