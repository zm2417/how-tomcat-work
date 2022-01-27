package ex05.core;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * basic valve in tomcat container Context
 */
public class SimpleContextValve implements Valve, Contained {

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
    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            return;
        }

        HttpServletRequest hreq = (HttpServletRequest) request;
        String contextPath = hreq.getContextPath();
        String requestURI = ((HttpRequest) request).getDecodedRequestURI();
        String relativeURI = requestURI.substring(contextPath.length()).toUpperCase();

        Context context = (Context) getContainer();

        Wrapper wrapper = null;
        try {
            wrapper = (Wrapper) context.map(request, true);
        } catch (Exception e) {
            badRequest(requestURI, (HttpServletResponse) response.getResponse());
            e.printStackTrace();
            return;
        }
        if (wrapper == null) {
            notFound(requestURI, (HttpServletResponse) response.getResponse());
            return;
        }

        // ask this Wrapper to process this Request
        response.setContext(context);
        wrapper.invoke(request, response);
    }

    private void badRequest(String requestURI, HttpServletResponse response) {
        try {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, requestURI);
        } catch (IllegalStateException e) {
            ;
        } catch (IOException e) {
            ;
        }
    }

    private void notFound(String requestURI, HttpServletResponse response) {
        try {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, requestURI);
        } catch (IllegalStateException e) {
            ;
        } catch (IOException e) {
            ;
        }
    }
}
