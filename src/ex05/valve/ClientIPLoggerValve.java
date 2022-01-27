package ex05.valve;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import java.io.IOException;

/**
 * the clientIPLoggerValve is a valve that print the client's IP address;
 */
public class ClientIPLoggerValve implements Valve, Contained {

    private Container container;

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(Request request, Response response, ValveContext context) throws IOException, ServletException {
        context.invokeNext(request, response);
        System.out.println("---------------------------------------");
        System.out.println("Client IP Logger Valve");
        ServletRequest sreq = request.getRequest();
        System.out.println(sreq.getRemoteAddr());
        System.out.println("---------------------------------------");
    }
}
