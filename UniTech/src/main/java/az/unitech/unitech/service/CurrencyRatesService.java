package az.unitech.unitech.service;

import az.unitech.unitech.entity.Currency;
import az.unitech.unitech.entity.CurrencyRates;
import az.unitech.unitech.repository.CurrencyRatesRepository;
import az.unitech.unitech.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyRatesService {
    private final CurrencyRatesRepository currencyRatesRepository;
    private final CurrencyService currencyService;
    public List<Currency> thirdPartyApi() {
        DecimalFormat formatter = new DecimalFormat("0.00");
        double randomDollar = Double.parseDouble(formatter.format(Math.random() * (2 - 1.6) + 1.6));
        double randomTl = Double.parseDouble(formatter.format(Math.random() * (10 - 5) + 5));

        return List.of(Currency.builder().currency("USD/AZN").value(randomDollar).build(),
                Currency.builder().currency("TL/AZN").value(randomTl).build());
    }

    public List<Currency> getCurrencyRates() {
        CurrencyRates currencyRates = currencyRatesRepository.findById(1L)
                .orElseGet(() -> {
                    List<Currency> currencies = thirdPartyApi();
                    LocalDateTime now = LocalDateTime.now();
                    return currencyRatesRepository.save(CurrencyRates.builder()
                            .currencies(currencies)
                            .date(now.withSecond(59)).build());
                });


        if (currencyRates.getDate().isBefore(LocalDateTime.now())){
            currencyRates.setCurrencies(currencyService.saveAll(thirdPartyApi()));
            currencyRates.setDate(LocalDateTime.now().withSecond(59));
            return  currencyRatesRepository.save(currencyRates).getCurrencies();
        }else {
            return currencyRates.getCurrencies();
        }
    }
}
