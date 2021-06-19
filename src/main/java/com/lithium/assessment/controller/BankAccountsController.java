package com.lithium.assessment.controller;

import com.lithium.assessment.entities.TransactionDetails;
import com.lithium.assessment.logic.LogicFunctions;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Logger log = (Logger) LoggerFactory.getLogger(BankAccountsController.class);


    @PostMapping("/openAccount")
    public String openUserAccounts(@RequestBody Map<String,Object>userinfo) throws ParseException {
        log.info("This is the received map {}",userinfo);

        logicFunctions.openAccount(userinfo);


    return "Success";

}

@PostMapping("/withdrawFromAccount")
    public String withdrawSavingsAccount(@RequestBody Map<String,Object>withdrawInformation){
        log.info("Information to withdraw money from savings account {}",withdrawInformation);
        logicFunctions.withdrawFromSavings(withdrawInformation);

        return "successfully withdrawn";
}

@PostMapping("/depositIntoAccount")

    public  String depositIntoAccount(@RequestBody Map<String,Object>depositInformation){
    log.info("Information to deposit money from savings account {}",depositInformation);
    logicFunctions.depositMoney(depositInformation);

    return "successfully deposited";

    }

@PostMapping("/getTransactionHistory/{email}")
    public List<TransactionDetails> getTransactionHistory(@PathVariable (value = "email") String email){
        log.info("Email for history to be retrieved {}",email);
       return logicFunctions.getTransactionHistory(email);


    }

}
