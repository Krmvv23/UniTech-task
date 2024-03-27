package az.unitech.unitech.service;

import az.unitech.unitech.custom.TransferException;
import az.unitech.unitech.dto.TransferDto;
import az.unitech.unitech.entity.Account;
import az.unitech.unitech.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;

    public Optional<Account> save(Account account, Long id) {
        if (accountRepository.checkNumber(account.getNumber())) {
            return userService.findUserById(id)
                    .map(user -> {
                        account.setUser(user);
                        account.setActive(true);
                        return Optional.of(accountRepository.save(account));
                    }).orElse(Optional.empty());
        } else return Optional.empty();
    }

    public List<Account> getAccounts(Long userId) {
        return accountRepository.getAccountsByUserIdAndActiveTrueAndDateIsAfter(userId, LocalDate.now());
    }

    public Account transfer(TransferDto transferDto, Long userId) {
        Account from = accountRepository.findAccountByNumber(transferDto.getFrom())
                .orElseThrow(() -> new TransferException("Account not found", HttpStatus.NOT_FOUND));
        Account to = accountRepository.findAccountByNumber(transferDto.getTo())
                .orElseThrow(() -> new TransferException("Account not found", HttpStatus.NOT_FOUND));

        if (!from.getUser().getId().equals(userId))
            throw new TransferException("Permission denied", HttpStatus.BAD_REQUEST);

        if (from.getNumber() == to.getNumber())
            throw new TransferException("You cannot transfer to the same account", HttpStatus.BAD_REQUEST);

        if (!to.isActive())
            throw new TransferException("Account is not active",HttpStatus.BAD_REQUEST);

        return calculate(from,to, transferDto.getAmount());
    }

    public Account calculate(Account from, Account to, double amount){
        BigDecimal fromBalance = BigDecimal.valueOf(from.getBalance());
        BigDecimal toBalance = BigDecimal.valueOf(to.getBalance());
        BigDecimal amountDecimal = BigDecimal.valueOf(amount);

        if (fromBalance.subtract(amountDecimal).doubleValue() < 0) {
            throw new TransferException("There is not enough funds in the account",HttpStatus.BAD_REQUEST);
        }

        from.setBalance(fromBalance.subtract(amountDecimal).doubleValue());
        to.setBalance(toBalance.add(amountDecimal).doubleValue());

        Account account = accountRepository.save(from);
        accountRepository.save(to);
        return account;
    }



}
