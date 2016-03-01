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

    private boolean cardAllowed;

    private String btcRcvAddress;

    private String itemsRemaining;

    private String errorCode;

    private String errorName;

    private String errorMessage;

    public Slot() {

    }

    public String getSlotNo() {
        return slotNo;
    }

    public String getPrize() {
        return prize;
    }

    public boolean isBtcAllowed() {
        return btcAllowed;
    }

    public boolean isCardAllowed() {
        return cardAllowed;
    }

    public String getBtcRcvAddress() {
        return btcRcvAddress;
    }

    public String getItemsRemaining() {
        return itemsRemaining;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorName() {
        return errorName;
    }
}
