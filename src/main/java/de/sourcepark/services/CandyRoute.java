/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sourcepark.services;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 *
 * @author lsotoudeh
 */
public abstract class CandyRoute implements Route {
    
    /** flag indicating, wheter this Route is active and usable at the moment or not */ 
    private boolean enabled;

    /** default constructor initializes an enabled CandyRoute */
    public CandyRoute() {
        enable();
    }

    /**
     * Get the value of enabled (the flag indicating, wheter this Route is active and should respond at the moment or not)
     *
     * @return the value of enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set enabled = true (the flag indicating, wheter this Route is active and should respond at the moment or not) 
     */
    public final void enable() {
        this.enabled = true;
    }
    
    /**
     * Set enabled = false (the flag indicating, wheter this Route is active and should respond at the moment or not) 
     */
    public final void disable() {
        this.enabled = false;
    }

    @Override
    public abstract Object handle(Request rqst, Response rspns) throws Exception;
    
    
    
}
