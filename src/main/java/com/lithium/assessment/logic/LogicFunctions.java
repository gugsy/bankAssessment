package com.lithium.assessment.logic;

import com.lithium.assessment.entities.BankUser;
import com.lithium.assessment.entities.CurrentAccountModel;
import com.lithium.assessment.entities.SavingsAccountModel;
import com.lithium.assessment.repository.BankUserRepository;
import com.lithium.assessment.serviceImpl.BankUserServiceImpl;
import com.lithium.assessment.util.Constants;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Service
public class LogicFunctions {
    @Autowired
    private BankUserServiceImpl bankUserService;
    @Autowired
    private BankUserRepository bankUserRepository;
    @Autowired
    private  Constants constants;

    private static final Logger log = (Logger) LoggerFactory.getLogger(LogicFunctions.class);


    BankUser bankUser = new BankUser();
    SavingsAccountModel savingsAccount = new SavingsAccountModel();
    CurrentAccountModel currentAccount = new CurrentAccountModel();

    public LogicFunctions(BankUserServiceImpl bankUserService, BankUserRepository bankUserRepository, Constants constants) {
        this.bankUserService = bankUserService;
        this.bankUserRepository = bankUserRepository;
        this.constants = constants;
    }

    public Map<String,Object> openAccount(Map<String,Object>userInfo) throws ParseException {
        log.info("This is the received json {}",userInfo);
        JSONObject obj = new JSONObject(userInfo);
        Map<String,Object> userinfo = obj.getJSONObject("personalInfo").toMap();
        log.info("This is the map with personal infor {}",userinfo);
        Map<String, Object> savingsInfo = obj.getJSONObject("savingsInfo").toMap();
        String depositAmount = savingsInfo.get("depositAmount").toString();
        Double depositedAmount = Double.valueOf(depositAmount);
        Map<String,Object> currentInfo = obj.getJSONObject("currentInfo").toMap();
        Map<String,Object> successResponse = new HashMap<>();
        successResponse.put(constants.SUCCESSHEADER,constants.SUCCESSMESSAGE);
        String emailProvided = userinfo.get("email").toString();
        log.info("This is provided email {}",emailProvided);
            bankUser = new BankUser();
        if(bankUserService.getByEmail(emailProvided) == null) {
            //create account

//do a check for minimum deposit
            if (depositedAmount > Double.valueOf(1000)) {
                log.info("About to save savings account information");
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


            //else return successResponse;
            log.info("About to save current account information");

            currentAccount = new CurrentAccountModel();
            String accountcurrentNumber = currentInfo.get("currentAccountNumber").toString();
            currentAccount.setAccountCurrentNumber(Long.parseLong(accountcurrentNumber));
            String currentDeposit = currentInfo.get("currentDeposit").toString();
            currentAccount.setDepositAmount(Double.parseDouble(currentDeposit));
            currentAccount.setOpeningDate(LocalDate.now());
            currentAccount.setCurrentBalance(Double.parseDouble(currentDeposit));
            currentAccount.setMaximumOverdraft(10000.0);
            currentAccount.setOverdraftBalance(10000.0);
            currentAccount.setTransactionDate(LocalDate.now());
            // currentAccount.setBankUser(bankUser);


            log.info("About to save user information");

            bankUser.setEmail(userinfo.get("email").toString());
            bankUser.setSurname(userinfo.get("surname").toString());
            bankUser.setName(userinfo.get("name").toString());
            bankUser.setCurrentAccountModel(currentAccount);
            bankUser.setSavingsAccountModel(savingsAccount);
            log.info("About to save new user {}", bankUser.toString());
            bankUserRepository.save(bankUser);

        }}

        return  successResponse;

    }
}
