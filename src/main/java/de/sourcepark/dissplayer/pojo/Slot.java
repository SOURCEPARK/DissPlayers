/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.sourcepark.dissplayer.pojo;

import lombok.Data;

/**
 *
 * @author jnaperkowski
 */
@Data()
public class Slot {

    private String slotNo;

    private String prize;

    private boolean btcAllowed;

    private String btcRcvAddress;

    private String errorCode;

    private String errorName;

    private String errorMessage;

    public Slot() {

    }

    public String getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(String slotNo) {
        this.slotNo = slotNo;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public boolean isBtcAllowed() {
        return btcAllowed;
    }

    public void setBtcAllowed(boolean btcAllowed) {
        this.btcAllowed = btcAllowed;
    }

    public String getBtcRcvAddress() {
        return btcRcvAddress;
    }

    public void setBtcRcvAddress(String btcRcvAddress) {
        this.btcRcvAddress = btcRcvAddress;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
