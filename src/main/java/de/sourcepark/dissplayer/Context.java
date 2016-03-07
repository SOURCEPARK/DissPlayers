package de.sourcepark.dissplayer;

import de.sourcepark.services.User;

/**
 * Created by jnaperkowski on 13.11.15.
 */
public class Context {
    private final static Context instance = new Context();
    

    public static Context getInstance() {
        return instance;
    }


    public enum PaymentType {
        Card,
        Bitcoin
    }

    private String activeOrderNumber;
    private PaymentType activePaymentType;
    private User activeUser;    

    public String getActiveOrderNumber() {
        return this.activeOrderNumber;
    }

    public void setActiveOrderNumber(String activeOrderNumber) {
        this.activeOrderNumber = activeOrderNumber;
    }

    public PaymentType getPaymentType() {
        return this.activePaymentType;
    }

    public void setPaymentType(PaymentType activePaymentType) {
        this.activePaymentType = activePaymentType;
    }
        /**
     * @return the activeUser
     */
    public User getActiveUser() {
        return activeUser;
    }

    /**
     * @param activeUser the activeUser to set
     */
    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }


}
