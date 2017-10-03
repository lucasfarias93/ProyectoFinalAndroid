package com.mercadopago.uicontrollers;

import android.content.Context;

import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentMethodSearchItem;
import com.mercadopago.model.Site;
import com.mercadopago.model.Token;
import com.mercadopago.uicontrollers.payercosts.PayerCostRow;
import com.mercadopago.uicontrollers.payercosts.PayerCostViewController;
import com.mercadopago.uicontrollers.paymentmethods.PaymentMethodOffEditableRow;
import com.mercadopago.uicontrollers.paymentmethods.PaymentMethodViewController;
import com.mercadopago.uicontrollers.paymentmethods.card.PaymentMethodOnEditableRow;

/**
 * Created by mreverter on 29/4/16.
 */
public class ViewControllerFactory {

    public static PaymentMethodViewController getPaymentMethodOnEditionViewController(Context context, PaymentMethod paymentMethod, Token token) {
        return new PaymentMethodOnEditableRow(context, paymentMethod, token);
    }

    public static PaymentMethodViewController getPaymentMethodOffEditionViewController(Context context, PaymentMethod paymentMethod) {
        return new PaymentMethodOffEditableRow(context, paymentMethod);
    }

    public static PaymentMethodViewController getPaymentMethodOffEditionViewController(Context context, PaymentMethodSearchItem item) {
        return new PaymentMethodOffEditableRow(context, item);
    }

    public static PayerCostViewController getPayerCostEditionViewController(Context context, Site site) {
        return new PayerCostRow(context, site);
    }

}
