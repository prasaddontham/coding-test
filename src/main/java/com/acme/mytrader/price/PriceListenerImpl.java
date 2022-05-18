package com.acme.mytrader.price;

import com.acme.mytrader.execution.ExecutionService;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PriceListenerImpl implements PriceListener {

    private final String security;
    private final double triggerLevel;
    private final int quantityToPurchase;
    private final ExecutionService executionService;

    private boolean executedOrNot;

    @Override
    public void priceUpdate(String security, double price) {

        if (ableToBuy(security, price)) {
            executionService.buy(security, price, quantityToPurchase);
            executedOrNot = true;
        }
    }

    private boolean ableToBuy(String security, double price) {
        return (!executedOrNot) && this.security.equals(security) && (price < this.triggerLevel);
    }

}
