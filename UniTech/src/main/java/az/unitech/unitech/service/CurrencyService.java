package az.unitech.unitech.service;

import az.unitech.unitech.entity.Currency;
import az.unitech.unitech.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public List<Currency> saveAll(List<Currency> currencies){
        return currencyRepository.saveAll(currencies);
    }
}
