package ex05.startup;

import ex05.core.SimpleLoader;
import ex05.core.SimpleWrapper;
import ex05.valve.ClientIPLoggerValve;
import ex05.valve.HeaderLoggerValve;
import org.apache.catalina.*;
import org.apache.catalina.connector.http.HttpConnector;

public class Bootstrap1 {

    public static void main(String[] args) {
        HttpConnector httpConnector = new HttpConnector();
        Wrapper wrapper = new SimpleWrapper();
        wrapper.setServletClass("ModernServlet");
        Loader loader = new SimpleLoader();
        Valve valve1 = new HeaderLoggerValve();
        Valve valve2 = new ClientIPLoggerValve();

        wrapper.setLoader(loader);
        ((Pipeline) wrapper).addValve(valve1);
        ((Pipeline) wrapper).addValve(valve2);

        httpConnector.setContainer(wrapper);

        try {
            httpConnector.initialize();
            httpConnector.start();

            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
