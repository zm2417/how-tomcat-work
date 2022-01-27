package ex04;

import org.apache.catalina.connector.http.HttpConnector;

public class Bootstrap {

    public static void main(String[] args) {
        HttpConnector httpConnector = new HttpConnector();
        SimpleContainer simpleContainer = new SimpleContainer();
        httpConnector.setContainer(simpleContainer);
        try {
            httpConnector.initialize();
            httpConnector.start();

            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
