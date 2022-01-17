package ex03.startup;

import ex03.HttpConnector;

public class Bootstrap {

    public static void main(String[] args) {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.start();
    }

}
