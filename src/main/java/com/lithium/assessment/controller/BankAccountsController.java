package com.lithium.assessment.controller;

import com.lithium.assessment.logic.LogicFunctions;
import org.json.JSONObject;
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

    @PostMapping("/openAccount")
    public String openUserAccounts(@RequestBody Map<String,String>userinfo) throws ParseException {
    JSONObject jsonObject = new JSONObject(userinfo);
    logicFunctions.openAccount(jsonObject);


    return "Success";
}
}
