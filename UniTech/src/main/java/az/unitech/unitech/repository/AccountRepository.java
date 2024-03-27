package az.unitech.unitech.repository;

import az.unitech.unitech.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    List<Account> getAccountsByUserIdAndActiveTrueAndDateIsAfter(Long id, LocalDate localDate);
    @Query("select case when count(a)>0 then false else true end from Account a where a.number = :number")
    boolean checkNumber(long number);
    Optional<Account> findAccountByNumber(long number);
}
