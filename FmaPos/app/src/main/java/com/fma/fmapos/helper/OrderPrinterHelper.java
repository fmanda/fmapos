package com.fma.fmapos.helper;

import android.content.Context;

import com.fma.fmapos.controller.ControllerProduct;
import com.fma.fmapos.model.ModelOrder;
import com.fma.fmapos.model.ModelOrderItem;
import com.fma.fmapos.model.ModelProduct;

/**
 * Created by fmanda on 08/22/17.
 */

public class OrderPrinterHelper extends PrinterHelper {
    public OrderPrinterHelper(Context context) {
        super(context);
    }

    public void PrintOrder(ModelOrder modelOrder){
        if (!ConnectPrinter()) return;

        ControllerProduct controllerproduct = new ControllerProduct(context);
        PrintLine(alignCenter(), Boolean.FALSE);
        PrintLine("COMPANY : XXXX");
        PrintLine("ORDER NO : " );
        PrintLine(getDoubleSeparator());
        PrintLine(alignLeft(), Boolean.FALSE);


        for (ModelOrderItem item : modelOrder.getItems()) {
            ModelProduct product = item.getProduct();
            if (product == null) product = controllerproduct.retrieveProduct(item.getProduct_id());

            int qty = ((Double) item.getQty()).intValue();
            Double lineTotal = item.getTotal();
            String description = product.getName() + " x" + String.valueOf(qty) +" ";
            String subtotal = CurrencyHelper.format(lineTotal);
            PrintLine(mergeLeftRight(description, subtotal));
        }
        PrintLine(getSingleSeparator());
        PrintLine(mergeLeftRight("TOTAL", CurrencyHelper.format(modelOrder.getSummary()) ));
        PrintLine("\n");
        ClosePrinter();
    }

    private String mergeLeftRight(String sLeft, String sRight){
        int i = sLeft.length();
        int j = sRight.length();

        int paramInt = 0;
        while (paramInt < 32 - (i + j) )
        {
            sLeft += " ";
            paramInt += 1;
        }
        sLeft += sRight;
        return sLeft;
    }
}
