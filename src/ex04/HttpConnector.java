package ex04;

import org.apache.catalina.*;
import org.apache.catalina.connector.http.Constants;
import org.apache.catalina.net.DefaultServerSocketFactory;
import org.apache.catalina.net.ServerSocketFactory;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;

import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.Vector;

public class HttpConnector implements Connector, Lifecycle, Runnable {

    // ----------------------------------------------------- Instance Variables


    /**
     * The <code>Service</code> we are associated with (if any).
     */
    private Service service = null;


    /**
     * The accept count for this Connector.
     */
    private int acceptCount = 10;


    /**
     * The IP address on which to bind, if any.  If <code>null</code>, all
     * addresses on the server will be bound.
     */
    private String address = null;


    /**
     * The input buffer size we should create on input streams.
     */
    private int bufferSize = 2048;


    /**
     * The Container used for processing requests received by this Connector.
     */
    protected Container container = null;


    /**
     * The set of processors that have ever been created.
     */
    private Vector created = new Vector();


    /**
     * The current number of processors that have been created.
     */
    private int curProcessors = 0;


    /**
     * The debugging detail level for this component.
     */
    private int debug = 0;


    /**
     * The "enable DNS lookups" flag for this Connector.
     */
    private boolean enableLookups = false;


    /**
     * The server socket factory for this component.
     */
    private ServerSocketFactory factory = null;


    /**
     * Descriptive information about this Connector implementation.
     */
    private static final String info =
            "org.apache.catalina.connector.http.HttpConnector/1.0";


    /**
     * The lifecycle event support for this component.
     */
    protected LifecycleSupport lifecycle = new LifecycleSupport(this);


    /**
     * The minimum number of processors to start at initialization time.
     */
    protected int minProcessors = 5;


    /**
     * The maximum number of processors allowed, or <0 for unlimited.
     */
    private int maxProcessors = 20;


    /**
     * Timeout value on the incoming connection.
     * Note : a value of 0 means no timeout.
     */
    private int connectionTimeout = Constants.DEFAULT_CONNECTION_TIMEOUT;


    /**
     * The port number on which we listen for HTTP requests.
     */
    private int port = 8080;


    /**
     * The set of processors that have been created but are not currently
     * being used to process a request.
     */
    private Stack processors = new Stack();


    /**
     * The server name to which we should pretend requests to this Connector
     * were directed.  This is useful when operating Tomcat behind a proxy
     * server, so that redirects get constructed accurately.  If not specified,
     * the server name included in the <code>Host</code> header is used.
     */
    private String proxyName = null;


    /**
     * The server port to which we should pretent requests to this Connector
     * were directed.  This is useful when operating Tomcat behind a proxy
     * server, so that redirects get constructed accurately.  If not specified,
     * the port number specified by the <code>port</code> property is used.
     */
    private int proxyPort = 0;


    /**
     * The redirect port for non-SSL to SSL redirects.
     */
    private int redirectPort = 443;


    /**
     * The request scheme that will be set on all requests received
     * through this connector.
     */
    private String scheme = "http";


    /**
     * The secure connection flag that will be set on all requests received
     * through this connector.
     */
    private boolean secure = false;


    /**
     * The server socket through which we listen for incoming TCP connections.
     */
    private ServerSocket serverSocket = null;


    /**
     * The string manager for this package.
     */
    private StringManager sm =
            StringManager.getManager(Constants.Package);


    /**
     * Has this component been initialized yet?
     */
    private boolean initialized = false;


    /**
     * Has this component been started yet?
     */
    private boolean started = false;


    /**
     * The shutdown signal to our background thread
     */
    private boolean stopped = false;


    /**
     * The background thread.
     */
    private Thread thread = null;


    /**
     * The name to register for the background thread.
     */
    private String threadName = null;


    /**
     * The thread synchronization object.
     */
    private Object threadSync = new Object();


    /**
     * Is chunking allowed ?
     */
    private boolean allowChunking = true;


    /**
     * Use TCP no delay ?
     */
    private boolean tcpNoDelay = true;

    // ---------------------------------------------- Background Thread Methods

    @Override
    public void run() {
        while (!stopped) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 从池中获取实例
            HttpProcessor processor = newProcessor();

            if (processor == null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            processor.assign(socket);
        }
    }

    private void threadStart() {
        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public Container getContainer() {
        return null;
    }

    @Override
    public void setContainer(Container container) {

    }

    @Override
    public boolean getEnableLookups() {
        return false;
    }

    @Override
    public void setEnableLookups(boolean enableLookups) {

    }

    @Override
    public ServerSocketFactory getFactory() {
        if (this.factory == null) {
            synchronized (this) {
                this.factory = new DefaultServerSocketFactory();
            }
        }
        return (this.factory);
    }

    @Override
    public void setFactory(ServerSocketFactory factory) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public int getRedirectPort() {
        return 0;
    }

    @Override
    public void setRedirectPort(int redirectPort) {

    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public void setScheme(String scheme) {

    }

    @Override
    public boolean getSecure() {
        return false;
    }

    @Override
    public void setSecure(boolean secure) {

    }

    @Override
    public Service getService() {
        return null;
    }

    @Override
    public void setService(Service service) {

    }

    @Override
    public Request createRequest() {
        return null;
    }

    @Override
    public Response createResponse() {
        return null;
    }

    /**
     * 创建ServerSocket
     *
     * @throws LifecycleException
     */
    @Override
    public void initialize() throws LifecycleException {
        serverSocket = open();
    }

    private ServerSocket open() {
        ServerSocketFactory serverSocketFactory = getFactory();
        try {
            return (serverSocketFactory.createSocket(8080, 1, InetAddress.getByName("127.0.0.1")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    /**
     * 启动HttpConnector线程，创建HttpProcessor池
     *
     * @throws LifecycleException
     */
    @Override
    public void start() throws LifecycleException {

        // 启动HttpConnector线程
        threadStart();

        while (curProcessors < minProcessors) {
            if ((maxProcessors > 0) && (curProcessors >= maxProcessors))
                break;
            HttpProcessor processor = newProcessor();
            recycle(processor);
        }
    }

    public void recycle(HttpProcessor processor) {
        processors.push(processor);
    }

    private HttpProcessor newProcessor() {
        HttpProcessor httpProcessor = new HttpProcessor(this, curProcessors++);
        if (httpProcessor instanceof Lifecycle) {
            try {
                // 启动HttpProcessor线程
                ((Lifecycle) httpProcessor).start();
            } catch (LifecycleException e) {
                return null;
            }
        }
        created.addElement(httpProcessor);
        return httpProcessor;
    }

    private HttpProcessor createProcessor() {
        synchronized (processors) {
            if (processors.size() > 0) {
                return (HttpProcessor) processors.pop();
            }
            if ((maxProcessors > 0) && (curProcessors < maxProcessors)) {
                return newProcessor();
            } else {
                if (maxProcessors < 0) {
                    return newProcessor();
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public void stop() throws LifecycleException {

    }
}
