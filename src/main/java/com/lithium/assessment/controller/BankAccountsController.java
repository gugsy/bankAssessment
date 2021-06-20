package com.lithium.assessment.controller;

import com.lithium.assessment.entities.TransactionDetails;
import com.lithium.assessment.logic.LogicFunctions;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


@Controller
public class BankAccountsController {

    @Autowired
    private LogicFunctions logicFunctions;
    private static final Logger log =  LoggerFactory.getLogger(BankAccountsController.class);

    public BankAccountsController(LogicFunctions logicFunctions) {
        this.logicFunctions = logicFunctions;
    }


    @PostMapping("/openAccount")
    @ApiOperation(notes = "Endpoint to open an account",
            value = "openAccount", nickname = "openAccount",
            tags = {"Bank Assessment"})
    public ResponseEntity openUserAccounts(@RequestBody Map<String, Object> userinfo) throws ParseException {
        log.info("This is the received map {}", userinfo);
        logicFunctions.openAccount(userinfo);
        return new ResponseEntity<>("Your account has been successfully created", HttpStatus.OK);

    }

    @PostMapping("/withdrawFromAccount")
    @ApiOperation(notes = "Endpoint to withdraw from an account",
            value = "withdrawal", nickname = "withdrawal",
            tags = {"Bank Assessment"})
    public ResponseEntity withdrawSavingsAccount(@RequestBody Map<String, Object> withdrawInformation) {
        log.info("Information to withdraw money from savings account {}", withdrawInformation);
        logicFunctions.withdrawFromSavings(withdrawInformation);
        return new ResponseEntity<>("You have successfully withdrawn ", HttpStatus.OK);
    }

    @PostMapping("/depositIntoAccount")
    @ApiOperation(notes = "Endpoint to deposit money into account",
            value = "deposit", nickname = "deposit",
            tags = {"Bank Assessment"})
    public ResponseEntity depositIntoAccount(@RequestBody Map<String, Object> depositInformation) {
        log.info("Information to deposit money from savings account {}", depositInformation);
        logicFunctions.depositMoney(depositInformation);
        return new ResponseEntity<>("Your successfully deposited", HttpStatus.OK);

    }

    @PostMapping("/getTransactionHistory/{email}")
    @ApiOperation(notes = "Endpoint to get transaction history",
            value = "transaction history", nickname = "transaction history",
            tags = {"Bank Assessment"})
    public List<TransactionDetails> getTransactionHistory(@PathVariable(value = "email") String email) {
        log.info("Email for history to be retrieved {}", email);
        return logicFunctions.getTransactionHistory(email);


    }

}
