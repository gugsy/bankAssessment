package com.lithium.assessment.controller;

import com.lithium.assessment.logic.LogicFunctions;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
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
}
