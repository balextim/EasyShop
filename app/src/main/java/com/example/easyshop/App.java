package com.example.easyshop;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                "Adae2sa3icWNIbCrTIy8OnWIqmPJzukTYJW95XDpkVHgGzpy_XzL2apFiOYhx3kxzgADRdHBz9PbTEKq",
                Environment.SANDBOX,
                CurrencyCode.EUR,
                UserAction.PAY_NOW,
                "com.example.easyshop://paypalpay"
        ));
    }
}
