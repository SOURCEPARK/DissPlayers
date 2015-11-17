package de.sourcepark.services;

import java.util.Map;
import java.util.Observable;

/**
 * The CandyService class is the abstract base class for all candy services.
 * 
 * A CandyService instance is something that provides handlers (Routes) for 
 * given URLs.
 * @author smatyba
 */
public abstract class CandyService extends Observable{
    /**
     * This service's name.
     */
    private String name = "Just another candy service";

    /**
     * Gets this service's name.
     * @return A String containing the name of this service.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets this service's name.
     * @param name The new name for this service.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** flag indicating, wheter this service is active and usable at the moment or not (default: true)*/ 
    private boolean enabled = true;

    /**
     * Get the value of enabled (the flag indicating, wheter this service is active and usable at the moment or not)
     *
     * @return the value of enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set enabled = true (the flag indicating, wheter this service is active and usable at the moment or not) 
     */
    public final void enable() {
        this.enabled=true;
        if (routeMap!=null) {
            for(final Map.Entry<HTTPMethod, Map<String, CandyRoute>> entry : getRouteMap().entrySet()) {
                for(final Map.Entry<String, CandyRoute> mapping : entry.getValue().entrySet()) {
                    mapping.getValue().enable();
                }
            }
        }
    }
    
    /**
     * Set enabled = false (the flag indicating, wheter this service is active and usable at the moment or not) 
     */
    public final void disable() {
        this.enabled=false;
        if (routeMap!=null) {
            for(final Map.Entry<HTTPMethod, Map<String, CandyRoute>> entry : getRouteMap().entrySet()) {
                for(final Map.Entry<String, CandyRoute> mapping : entry.getValue().entrySet()) {
                    mapping.getValue().disable();
                }
            }
        }
    }
    
    private RouteMap routeMap;

    /**
     * Get the RouteMap of this CandyService
     *
     * @return the routeMap
     */
    public RouteMap getRouteMap() {
        return routeMap;
    }
    
    /**
     * When implemented in a class, this method returns the routes this service 
     * provides and sets the local RouteMap.
     * 
     * The {@code RouteMap} this method returns contains a mapping between URLs 
     * and the corresponding route instances mapped to the appropriate HTTP methods.
     * @return An instance of {@code RouteMap} representing all routes this service
     * can handle.
     */
    public abstract RouteMap initializeRoutes();  
      
}
