package az.unitech.unitech.controller;

import az.unitech.unitech.custom.TransferException;
import az.unitech.unitech.dto.AccountDto;
import az.unitech.unitech.dto.CreateAccountDto;
import az.unitech.unitech.custom.CustomUser;
import az.unitech.unitech.dto.TransferDto;
import az.unitech.unitech.entity.Account;
import az.unitech.unitech.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final ObjectMapper mapper;

    @PostMapping
    public ResponseEntity<AccountDto> save(@AuthenticationPrincipal CustomUser customUser, @RequestBody CreateAccountDto newAccount, @RequestParam Long id) {
        if (id.equals(customUser.getId())) {
            return accountService.save(mapper.convertValue(newAccount, Account.class), id)
                    .map(account -> ResponseEntity.ok(mapper.convertValue(account, AccountDto.class)))
                    .orElseGet(() -> new ResponseEntity("Account already exists", HttpStatus.BAD_REQUEST));
        } else return new ResponseEntity("Permission denied", HttpStatus.BAD_REQUEST);

    }

    // Task 3 //
    @GetMapping("/user/{id}")
    public ResponseEntity<List<AccountDto>> getAccounts(@AuthenticationPrincipal CustomUser customUser, @PathVariable Long id) {
        if (id.equals(customUser.getId())) {
            return ResponseEntity.ok(accountService.getAccounts(id).stream().map(account -> mapper.convertValue(account, AccountDto.class)).toList());
        } else return new ResponseEntity("Permission denied", HttpStatus.BAD_REQUEST);
    }

    // Task 4 //
    @PostMapping("/transfer")
    public ResponseEntity<AccountDto> transfer(@AuthenticationPrincipal CustomUser customUser, @RequestBody TransferDto transferDto) {
        try {
            return ResponseEntity.ok(mapper.convertValue(accountService.transfer(transferDto, customUser.getId()), AccountDto.class));
        } catch (TransferException e) {
            return new ResponseEntity(e.getMessage(), e.getHttpStatus());
        }
    }
}
