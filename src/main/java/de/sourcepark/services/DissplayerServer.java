package de.sourcepark.services;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.port;

/**
 * The actual main (server) class.
 * @author smatyba
 */
public class DissplayerServer {
    /**
     * Logger.
     */
    private static final transient Logger LOG = LoggerFactory.getLogger(DissplayerServer.class);
    
    public static final transient int ERROR_CODE_DUCK_IO = 100;
    public static final transient String ERROR_NAME_DUCK_IO = "DUCK_IO_ERROR";
    public static final transient int ERROR_CODE_GENERAL_IO = 101;
    public static final transient String ERROR_NAME_GENERAL_IO = "REGISTER_IO_ERROR";
    public static final transient int ERROR_CODE_PARAM = 102;
    public static final transient String ERROR_NAME_PARAM = "PARAM_ERR";
    public static final transient int ERROR_CODE_USER_REGISTRY_NOT_FOUND = 103;
    public static final transient String ERROR_NAME_USER_REGISTRY_NOT_FOUND = "USER_REG_NOT_FOUND_ERR";
    public static final transient int ERROR_CODE_USER_REGISTRY_IO = 104;
    public static final transient String ERROR_NAME_USER_REGISTRY_IO = "USER_REG_IO_ERR";
    public static final transient int ERROR_CODE_USER_NOT_FOUND = 105;
    public static final transient String ERROR_NAME_USER_NOT_FOUND = "USER_NOT_FOUND_ERR";
    
    /**
     * Initializes a new instance of the HubbaBubba class.
     */
    public DissplayerServer() {
    }
    
    /**
     * Starts a new server on a port and with the given services.
     * @param port The port the server is supposed to listen on.
     * @param services The services the server offers.
     */
    public final void startServer(final int port, final CandyService... services) {
        port(port);
        
        LOG.info("Starting server on port {}", port);
        
        for(final CandyService service : services) {
            LOG.info("Registering candy service '{}'...", service.getName());
            for(final Map.Entry<HTTPMethod, Map<String, CandyRoute>> entry : service.initializeRoutes().entrySet()) {
                switch(entry.getKey()) {
                    case GET: { 
                        for(final Map.Entry<String, CandyRoute> mapping : entry.getValue().entrySet()) {
                            get(mapping.getKey(), mapping.getValue());
                            LOG.info("Mapped '{}' --> '{}' (GET)", mapping.getKey(), mapping.getValue().toString());
                        }
                        break;
                    } case POST: {
                        for(final Map.Entry<String, CandyRoute> mapping : entry.getValue().entrySet()) {
                            post(mapping.getKey(), mapping.getValue());
                            LOG.info("Mapped '{}' --> '{}' (POST)", mapping.getKey(), mapping.getValue().toString());
                        }
                        break;
                    }
                }
            }
        }
    }
}