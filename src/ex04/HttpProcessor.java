package ex04;

import org.apache.catalina.Globals;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.connector.http.*;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;
import org.apache.catalina.util.StringParser;

import java.net.Socket;

public class HttpProcessor implements Runnable, Lifecycle {

    // ----------------------------------------------------- Instance Variables


    /**
     * Is there a new socket available?
     */
    private boolean available = false;


    /**
     * The HttpConnector with which this processor is associated.
     */
    private HttpConnector connector = null;


    /**
     * The debugging detail level for this component.
     */
    private int debug = 0;


    /**
     * The identifier of this processor, unique per connector.
     */
    private int id = 0;


    /**
     * The lifecycle event support for this component.
     */
    private LifecycleSupport lifecycle = new LifecycleSupport(this);


    /**
     * The match string for identifying a session ID parameter.
     */
    private static final String match =
            ";" + Globals.SESSION_PARAMETER_NAME + "=";


    /**
     * The match string for identifying a session ID parameter.
     */
    private static final char[] SESSION_ID = match.toCharArray();


    /**
     * The string parser we will use for parsing request lines.
     */
    private StringParser parser = new StringParser();


    /**
     * The proxy server name for our Connector.
     */
    private String proxyName = null;


    /**
     * The proxy server port for our Connector.
     */
    private int proxyPort = 0;


    /**
     * The HTTP request object we will pass to our associated container.
     */
//    private HttpRequestImpl request = null;


    /**
     * The HTTP response object we will pass to our associated container.
     */
//    private HttpResponseImpl response = null;


    /**
     * The actual server port for our Connector.
     */
    private int serverPort = 0;


    /**
     * The string manager for this package.
     */
    protected StringManager sm =
            StringManager.getManager(Constants.Package);


    /**
     * The socket we are currently processing a request for.  This object
     * is used for inter-thread communication only.
     */
    private Socket socket = null;


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
     * Keep alive indicator.
     */
    private boolean keepAlive = false;


    /**
     * HTTP/1.1 client.
     */
    private boolean http11 = true;


    /**
     * True if the client has asked to recieve a request acknoledgement. If so
     * the server will send a preliminary 100 Continue response just after it
     * has successfully parsed the request headers, and before starting
     * reading the request entity body.
     */
    private boolean sendAck = false;


    /**
     * Ack string when pipelining HTTP requests.
     */
    private static final byte[] ack =
            (new String("HTTP/1.1 100 Continue\r\n\r\n")).getBytes();


    /**
     * CRLF.
     */
    private static final byte[] CRLF = (new String("\r\n")).getBytes();


    /**
     * Line buffer.
     */
    //private char[] lineBuffer = new char[4096];


    /**
     * Request line buffer.
     */
//    private HttpRequestLine requestLine = new HttpRequestLine();


    /**
     * Processor state
     */
    private int status = Constants.PROCESSOR_IDLE;

    public HttpProcessor(HttpConnector httpConnector, int id) {
        super();
        this.connector = httpConnector;
        this.id = id;

//        this.request = httpConnector.createRequest();
//        this.response = httpConnector.createResponse();
    }

    @Override
    public void run() {
        while (!stopped) {
            // 阻塞，直到用户请求到来获取到这个处理器才被唤醒
            Socket socket = await();
            if (socket == null) {
                continue;
            }
            try {
                // 处理请求
                process(socket);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            connector.recycle(this);
        }
    }

    /**
     * 处理请求
     *
     * @param socket
     */
    private void process(Socket socket) {

    }

    synchronized void assign(Socket socket) {
        while (available) {
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.socket = socket;
        available = true;
        notifyAll();
    }

    private synchronized Socket await() {
        while (!available) {
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Socket socket = this.socket;
        available = false;
        notifyAll();

        return socket;
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

    }

    @Override
    public void stop() throws LifecycleException {

    }
}
