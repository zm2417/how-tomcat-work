package ex05.core;

import org.apache.catalina.*;

import javax.servlet.http.HttpServletRequest;

/**
 * map method return a child container(a wrapper) that responsible for processing the request
 */
public class SimpleContextMapper implements Mapper {

    private SimpleContext context = null;

    @Override
    public Container getContainer() {
        return context;
    }

    @Override
    public void setContainer(Container container) {
        if (!(container instanceof SimpleContext)) {
            throw new IllegalArgumentException("Illegal type of container");
        }
        context = (SimpleContext) container;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public void setProtocol(String protocol) {

    }

    /**
     * return the child Container that should be used to process this Request,
     *
     * @param request Request being processed
     * @param update  Update the Request to reflect the mapping selection?
     * @return
     */
    @Override
    public Container map(Request request, boolean update) {
        String contextPath = ((HttpServletRequest) request.getRequest()).getContextPath();
        String requestURI = ((HttpRequest) request).getDecodedRequestURI();
        String relativeURI = requestURI.substring(contextPath.length());

        Wrapper wrapper = null;
        String servletPath = relativeURI;
        String pathInfo = null;
        String name = context.findServletMapping(relativeURI);
        if (name != null) {
            wrapper = (Wrapper) context.findChild(name);
        }

        return wrapper;
    }
}
