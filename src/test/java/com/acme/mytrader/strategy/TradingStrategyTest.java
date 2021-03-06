package com.acme.mytrader.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceSourceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.SneakyThrows;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class TradingStrategyTest {

  @SneakyThrows
  @Test
  public void testAutoBuyForSuccessfulBuy() throws InterruptedException {
    ExecutionService tradeExecutionService = Mockito.mock(ExecutionService.class);
    PriceSourceImpl priceSource = new MockPriceSource("IBM", 25.00);
    ArgumentCaptor<String> securityCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Double> priceCaptor = ArgumentCaptor.forClass(Double.class);
    ArgumentCaptor<Integer> volumeCaptor = ArgumentCaptor.forClass(Integer.class);
    TradingStrategy tradingStrategy = new TradingStrategy(tradeExecutionService, priceSource);
    List<SecurityModel> input = Arrays.asList(new SecurityModel("IBM", 50.00, 10));
    tradingStrategy.orderAutoProcess(input);
    verify(tradeExecutionService, times(1))
            .buy(securityCaptor.capture(), priceCaptor.capture(), volumeCaptor.capture());
    assertThat(securityCaptor.getValue()).isEqualTo("IBM");
    assertThat(priceCaptor.getValue()).isEqualTo(25.00);
    assertThat(volumeCaptor.getValue()).isEqualTo(10);
  }

  @SneakyThrows
  @Test
  public void testAutoBuyForNotSuccessfulBuy() throws InterruptedException {
    ExecutionService tradeExecutionService = Mockito.mock(ExecutionService.class);
    PriceSourceImpl priceSource = new MockPriceSource("IBM", 25.00);

    TradingStrategy tradingStrategy = new TradingStrategy(tradeExecutionService, priceSource);
    List<SecurityModel> input = Arrays.asList(new SecurityModel("APPL", 50.00, 10));
    tradingStrategy.orderAutoProcess(input);
    verifyZeroInteractions(tradeExecutionService);
  }

  private class MockPriceSource extends PriceSourceImpl {

    String security;
    double price;

    MockPriceSource(String security, double price) {
      this.security = security;
      this.price = price;
    }

    private final List<PriceListener> priceListeners = new CopyOnWriteArrayList<>();

    @Override
    public void addPriceListener(PriceListener listener) {
      priceListeners.add(listener);
    }

    @Override
    public void removePriceListener(PriceListener listener) {
      priceListeners.remove(listener);
    }

    @Override
    public void run() {
      priceListeners.forEach(priceListener -> priceListener.priceUpdate(security, price));
    }
  }
}
