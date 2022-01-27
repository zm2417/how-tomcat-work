package ex05.startup;

import ex05.core.SimpleContext;
import ex05.core.SimpleContextMapper;
import ex05.core.SimpleLoader;
import ex05.core.SimpleWrapper;
import ex05.valve.ClientIPLoggerValve;
import ex05.valve.HeaderLoggerValve;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;

public class Bootstrap2 {

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

        Valve valve1 = new HeaderLoggerValve();
        Valve valve2 = new ClientIPLoggerValve();
        ((Pipeline) context).addValve(valve1);
        ((Pipeline) context).addValve(valve2);

        Mapper mapper = new SimpleContextMapper();
        mapper.setProtocol("http");
        context.addMapper(mapper);

        Loader loader = new SimpleLoader();
        context.setLoader(loader);

        context.addServletMapping("/Primitive", "Primitive");
        context.addServletMapping("/Modern", "Modern");

        httpConnector.setContainer(context);

        try {
            httpConnector.initialize();
            httpConnector.start();

            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
