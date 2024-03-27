package az.unitech.unitech.repository;

import az.unitech.unitech.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency,String> {
}
