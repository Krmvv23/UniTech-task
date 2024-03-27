package az.unitech.unitech.service;

import az.unitech.unitech.entity.Currency;
import az.unitech.unitech.entity.CurrencyRates;
import az.unitech.unitech.repository.CurrencyRatesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CurrencyRatesServiceTest {
    @InjectMocks
    private CurrencyRatesService currencyRatesService;
    @Mock
    private CurrencyRatesRepository currencyRatesRepository;
    @Mock
    private CurrencyService currencyService;

    @Test
    void getCurrencyRates() {

        CurrencyRates currencyRates = CurrencyRates.builder().date(LocalDateTime.now()).currencies(List.of(Currency.builder().build())).build();
        Mockito.when(currencyRatesRepository.findById(1L)).thenReturn(Optional.of(currencyRates));
        Mockito.when(currencyService.saveAll(Mockito.any())).thenReturn(currencyRates.getCurrencies());
        Mockito.when(currencyRatesRepository.save(Mockito.any())).thenReturn(currencyRates);
        assertEquals(currencyRatesService.getCurrencyRates(),currencyRates.getCurrencies());
    }
}