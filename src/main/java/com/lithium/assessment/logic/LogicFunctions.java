package com.lithium.assessment.logic;

import com.lithium.assessment.entities.BankUser;
import com.lithium.assessment.entities.CurrentAccountModel;
import com.lithium.assessment.entities.SavingsAccountModel;
import com.lithium.assessment.serviceImpl.BankUserServiceImpl;
import com.lithium.assessment.util.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class LogicFunctions {
    @Autowired
    BankUserServiceImpl bankUserService;
    @Autowired
    Constants constants;
    @Autowired
    BankUser bankUser;
    @Autowired
    SavingsAccountModel savingsAccount;
    @Autowired
    CurrentAccountModel currentAccount;



    public Map<String,Object> openAccount(JSONObject jsonObject) throws ParseException {

        JSONObject obj = new JSONObject(jsonObject);
        Map<String,Object> userinfo = obj.getJSONObject("personalInfo").toMap();
        Map<String, Object> savingsInfo = obj.getJSONObject("savingsInfo").toMap();
        String depositAmount = savingsInfo.get("depositAmount").toString();
        Double depositedAmount = Double.valueOf(depositAmount);
        Map<String,Object> currentInfo = obj.getJSONObject("currentInfo").toMap();
        Map<String,Object> successResponse = new HashMap<>();
        successResponse.put(constants.SUCCESSHEADER,constants.SUCCESSMESSAGE);
        String emailProvided = userinfo.get("email").toString();
            bankUser = new BankUser();
        if(bankUserService.getByEmail(emailProvided).equals(null)) {
            //create account

//do a check for minimum deposit
            if(depositedAmount>Double.valueOf(1000)) {
                savingsAccount = new SavingsAccountModel();
                String accountNumber = savingsInfo.get("savingsAccountNumber").toString();
                savingsAccount.setAccountSavingsNumber(Long.valueOf(accountNumber));
                savingsAccount.setCurrentBalance(depositedAmount);
                savingsAccount.setDepositAmount(depositedAmount);
                savingsAccount.setOpeningDate(LocalDate.now());
                savingsAccount.setTransactionDate(LocalDate.now());
                savingsAccount.setCurrentBalance(depositedAmount);
                savingsAccount.setOverdraftBalance(Double.valueOf(100000));
                savingsAccount.setWithdrawnAmount(0);
               // savingsAccount.setBankUser(bankUser);


            }
            else return successResponse;

            currentAccount = new CurrentAccountModel();
            String accountNumber = currentInfo.get("currentAccountNumber").toString();
            currentAccount.setAccountCurrentNumber(Long.parseLong(accountNumber));
            String currentDeposit = currentInfo.get("currentDeposit").toString();
            currentAccount.setDepositAmount(Double.parseDouble(currentDeposit));
            currentAccount.setOpeningDate(LocalDate.now());
            currentAccount.setCurrentBalance(Double.parseDouble(currentDeposit));
            currentAccount.setMaximumOverdraft(10000.0);
            currentAccount.setOverdraftBalance(10000.0);
            currentAccount.setTransactionDate(LocalDate.now());
           // currentAccount.setBankUser(bankUser);
            return  successResponse;
        }

        bankUser.setEmail(userinfo.get("email").toString());
        bankUser.setSurname(userinfo.get("surname").toString());
        bankUser.setName(userinfo.get("name").toString());
        bankUser.setCurrentAccountModel(currentAccount);
        bankUser.setSavingsAccountModel(savingsAccount);


        return  successResponse;

    }
}
