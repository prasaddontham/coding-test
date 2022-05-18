package com.acme.mytrader.execution;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.logging.Logger;

@AllArgsConstructor
@Getter

public class ExecutionServiceImpl implements ExecutionService {

    private final int number;

    @Override
    public void buy(String security, double price, int volume) {
        System.out.println("BUY method executed for " + security + " @ $ " + price + " for " + volume + " number of securities");
    }

    @Override
    public void sell(String security, double price, int volume) {
        System.out.println("Not required for now!");
    }


}
