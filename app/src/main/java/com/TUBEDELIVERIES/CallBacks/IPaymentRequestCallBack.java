package com.TUBEDELIVERIES.CallBacks;

import com.TUBEDELIVERIES.Payment.PayFortData;

public interface IPaymentRequestCallBack {
    void onPaymentRequestResponse(int responsePurchaseCancel, PayFortData payFortData);

}
