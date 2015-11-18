/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sourcepark.services;

import lombok.Data;

/**
 *
 * @author lsotoudeh
 */
@Data()
public class User {
    
    private String nickname;
    
    private String cardId;
    
    private String salutation;
    
    private boolean maintenanceStaff;

    public User() {
    }
    
    
    /**
     * Get the value of cardId
     *
     * @return the value of cardId
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * Set the value of cardId
     *
     * @param cardId new value of cardId
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }


    /**
     * Get the value of nickname
     *
     * @return the value of nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Set the value of nickname
     *
     * @param nickname new value of nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    /**
     * Get the value of salutation
     *
     * @return the value of salutation
     */
    public String getSalutation() {
        return salutation;
    }

    /**
     * Set the value of salutation
     *
     * @param salutation new value of salutation
     */
    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    /**
     * @return the maintenanceStaff
     */
    public boolean isMaintenanceStaff() {
        return maintenanceStaff;
    }

    /**
     * @param maintenanceStaff the maintenanceStaff to set
     */
    public void setMaintenanceStaff(boolean maintenanceStaff) {
        this.maintenanceStaff = maintenanceStaff;
    }

}
