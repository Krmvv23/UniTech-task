package az.unitech.unitech.controller;

import az.unitech.unitech.entity.Currency;
import az.unitech.unitech.service.CurrencyRatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyRatesController {
    private final CurrencyRatesService currencyRatesService;

    @GetMapping
    public List<Currency> getCurrencyRates(){
        return currencyRatesService.getCurrencyRates();
    }
}
