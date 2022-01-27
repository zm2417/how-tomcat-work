package ex08.core;

import org.apache.catalina.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * basic valve
 */
public class SimpleWrapperValve implements Valve, Contained {

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
        SimpleWrapper wrapper = (SimpleWrapper) getContainer();
        ServletRequest sreq = request.getRequest();
        ServletResponse sres = response.getResponse();
        Servlet servlet = null;
        HttpServletRequest hreq = null;
        if (sreq instanceof HttpServletRequest) {
            hreq = (HttpServletRequest) sreq;
        }
        HttpServletResponse hres = null;
        if (sres instanceof HttpServletResponse) {
            hres = (HttpServletResponse) sres;
        }

        try {
            servlet = wrapper.allocate();
            if (hreq != null && hres != null) {
                servlet.service(hreq, hres);
            } else {
                servlet.service(sreq, sres);
            }
        } catch (ServletException e) {

        }
    }

}
