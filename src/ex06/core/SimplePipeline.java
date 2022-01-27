package ex06.core;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 执行valve chain，包含basic valve
 */
public class SimplePipeline implements Pipeline, Lifecycle {

    /**
     * basic valve ，在最后一个执行
     */
    private Valve basic = null;
    private Valve[] valves = new Valve[0];
    private Container container = null;

    public SimplePipeline(Container container) {
        setContainer(container);
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return new LifecycleListener[0];
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void start() throws LifecycleException {
        System.out.println("start pipeline");
    }

    @Override
    public void stop() throws LifecycleException {
        System.out.println("stop pipeline");
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public Valve getBasic() {
        return basic;
    }

    @Override
    public void setBasic(Valve valve) {
        this.basic = valve;
        ((Contained) valve).setContainer(container);
    }

    @Override
    public void addValve(Valve valve) {
        if (valve instanceof Contained) {
            ((Contained) valve).setContainer(container);
        }

        synchronized (valves) {
            Valve[] result = new Valve[valves.length + 1];
            System.arraycopy(valves, 0, result, 0, valves.length);
            result[valves.length] = valve;
            valves = result;
        }
    }

    @Override
    public Valve[] getValves() {
        return valves;
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        // 执行valves chain
        (new SimplePipelineValveContext()).invokeNext(request, response);
    }

    @Override
    public void removeValve(Valve valve) {

    }

    protected class SimplePipelineValveContext implements ValveContext {

        protected int stage = 0;

        @Override
        public String getInfo() {
            return null;
        }

        @Override
        public void invokeNext(Request request, Response response) throws IOException, ServletException {
            int subscript = stage;
            stage = stage + 1;
            if (subscript < valves.length) {
                // 将simplePipelineValveContext传入到invoke方法中，在invoke中在调用invokeNext方法，可以继续执行下一个的valve
                valves[subscript].invoke(request, response, this);
            } else if (subscript == valves.length && basic != null) {
                basic.invoke(request, response, this);
            } else {
                throw new ServletException("No valve");
            }
        }
    }
}
