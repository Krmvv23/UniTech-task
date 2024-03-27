package az.unitech.unitech.service;

import az.unitech.unitech.custom.TransferException;
import az.unitech.unitech.dto.TransferDto;
import az.unitech.unitech.entity.Account;
import az.unitech.unitech.entity.User;
import az.unitech.unitech.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;

    private final Account account = Account.builder()
            .id(1L)
            .active(true)
            .number(1234123412341234L)
            .cvv(123)
            .balance(200.00)
            .date(LocalDate.now())
            .user(User.builder().id(1L).build())
            .build();

    @Test
    void getAccountsTest() {
        Mockito.when(accountRepository.getAccountsByUserIdAndActiveTrueAndDateIsAfter(1L, LocalDate.now())).thenReturn(List.of(account));
        List<Account> accounts = accountService.getAccounts(1L);
        assertEquals(accounts, List.of(account));
    }

    @Test
    void transferTest(){
        TransferDto transferDto = TransferDto.builder()
                .from(1234123412341234L)
                .to(1234123412341234L)
                .amount(100)
                .build();

        Account account2 =  Account.builder()
                .id(1L)
                .active(true)
                .number(4321432143214321L)
                .cvv(123)
                .balance(200.00)
                .date(LocalDate.now())
                .user(User.builder().id(1L).build())
                .build();

        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Mockito.when(accountRepository.findAccountByNumber(transferDto.getFrom()))
                .thenReturn(Optional.of(account),Optional.of(account2));

        assertEquals(accountService.transfer(transferDto,1L),account);
    }

    @Test
    void transferWhenSameAccountThenException() {
        TransferDto transferDto = TransferDto.builder()
                .from(1234123412341234L)
                .to(1234123412341234L)
                .amount(100)
                .build();

        Mockito.when(accountRepository.findAccountByNumber(transferDto.getFrom())).thenReturn(Optional.of(Account.builder().number(1234123412341234L).user(User.builder().id(1L).build()).build()));

        assertThrows(TransferException.class, () -> accountService.transfer(transferDto, 1L));

    }

    @Test
    public void transferWhenNotFoundAccountThenException() {
        TransferDto transferDto = TransferDto.builder()
                .from(1234123412341234L)
                .to(1234123412341234L)
                .amount(100)
                .build();

        Mockito.when(accountRepository.findAccountByNumber(transferDto.getFrom())).thenReturn(Optional.empty());

        assertThrows(TransferException.class, () -> accountService.transfer(transferDto, 1L));
    }

    @Test
    public void transferWhenDeactiveAccountThenException() {
        TransferDto transferDto = TransferDto.builder()
                .from(1234123412341234L)
                .to(1234123412341234L)
                .amount(100)
                .build();

        Mockito.when(accountRepository.findAccountByNumber(transferDto.getFrom()))
                .thenReturn(Optional.of(Account.builder().number(1234123412341234L).active(true).user(User.builder().id(1L).build()).build()),
                Optional.of(Account.builder().number(4321432143214321L).active(false).user(User.builder().id(1L).build()).build()));

        assertThrows(TransferException.class, () -> accountService.transfer(transferDto, 1L));
    }

    @Test
    public void transferWhenNoEnoughMoneyThenException(){
        TransferDto transferDto = TransferDto.builder()
                .from(1234123412341234L)
                .to(1234123412341234L)
                .amount(100)
                .build();
        Mockito.when(accountRepository.findAccountByNumber(transferDto.getFrom()))
                .thenReturn(Optional.of(Account.builder().number(1234123412341234L).active(true).user(User.builder().id(1L).build()).build()),
                        Optional.of(Account.builder().number(4321432143214321L).balance(10).active(true).user(User.builder().id(2L).build()).build()));
        assertThrows(TransferException.class, () -> accountService.transfer(transferDto, 1L));

    }
}