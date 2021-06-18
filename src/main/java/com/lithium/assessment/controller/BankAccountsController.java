package com.lithium.assessment.controller;

import com.lithium.assessment.entities.BankUser;
import com.lithium.assessment.logic.LogicFunctions;
import org.springframework.stereotype.Controller;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


@Controller
public class BankAccountsController {


    private LogicFunctions logicFunctions;

    public BankAccountsController(LogicFunctions logicFunctions) {
        this.logicFunctions = logicFunctions;
    }

    @PostMapping("/openAccount")
    public String openUserAccounts(@RequestBody Map<String,String>userinfo){
    JSONObject jsonObject = new JSONObject(userinfo);
    logicFunctions.openAccount(jsonObject);


    return "Success";
}
}
