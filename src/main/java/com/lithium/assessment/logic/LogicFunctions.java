package com.lithium.assessment.logic;

import com.lithium.assessment.entities.*;
import com.lithium.assessment.repository.BankUserRepository;
import com.lithium.assessment.repository.CurrentAccountRepository;
import com.lithium.assessment.repository.SavingsAccountRepository;
import com.lithium.assessment.repository.TransactionHistoryRepository;
import com.lithium.assessment.serviceImpl.BankUserServiceImpl;
import com.lithium.assessment.serviceImpl.TransactionDetailsServiceImpl;
import com.lithium.assessment.util.Constants;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class LogicFunctions {
    @Autowired
    private BankUserServiceImpl bankUserService;
    @Autowired
    private BankUserRepository bankUserRepository;
    @Autowired
    private CurrentAccountRepository currentAccountRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private  Constants constants;
    @Autowired
    private TransactionDetailsServiceImpl transactionDetailsService;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    private static final Logger log = (Logger) LoggerFactory.getLogger(LogicFunctions.class);


    BankUser bankUser = new BankUser();
    SavingsAccountModel savingsAccount = new SavingsAccountModel();
    CurrentAccountModel currentAccount = new CurrentAccountModel();
    TransactionDetails transactionDetails = new TransactionDetails();

    public LogicFunctions(BankUserServiceImpl bankUserService, BankUserRepository bankUserRepository, CurrentAccountRepository currentAccountRepository, SavingsAccountRepository savingsAccountRepository, Constants constants, TransactionHistoryRepository transactionHistoryRepository, TransactionHistoryRepository transactionHistoryRepository1) {
        this.bankUserService = bankUserService;
        this.bankUserRepository = bankUserRepository;
        this.currentAccountRepository = currentAccountRepository;
        this.savingsAccountRepository = savingsAccountRepository;
        this.constants = constants;
        this.transactionHistoryRepository = transactionHistoryRepository1;
        this.transactionDetailsService = transactionDetailsService;
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



        }
        }

        return  successResponse;

    }

    public void withdrawFromSavings(Map<String, Object> withdrawInformation) {

        JSONObject jsonObject = new JSONObject(withdrawInformation);
        log.info("information on withdrawal amount");
        Map<String,Object> savingsWithdraw = jsonObject.getJSONObject("withdrawalInformation").toMap();
        String account =savingsWithdraw.get("accountType").toString();
        ACCOUNTTYPE accounttype = ACCOUNTTYPE.valueOf(savingsWithdraw.get("accountType").toString());
        double overdraftAmount = -100000;
        String email = savingsWithdraw.get("email").toString();
        String amountWithdrawn = savingsWithdraw.get("amountWithdrawn").toString();
        Double amountToWithdraw = Double.valueOf(amountWithdrawn);
        BankUser bankUser = bankUserService.getByEmail(email);
        if(accounttype.equals(ACCOUNTTYPE.SAVINGSACCOUNT)){
        savingsAccount = bankUser.getSavingsAccountModel();
        Double currentBalance = savingsAccount.getCurrentBalance();
        double availableBalance = currentBalance - amountToWithdraw;
        if(availableBalance > 1000){

            log.info("Available balance to be updated {}",availableBalance);
            savingsAccount.setCurrentBalance(availableBalance);
            log.info("About to update current balance");
            savingsAccountRepository.save(savingsAccount);

            //update currentBalance
            //save transaction to transaction history
            transactionDetails = new TransactionDetails();
            transactionDetails.setEmail(email);
            transactionDetails.setLocalDate(LocalDate.now());
            transactionDetails.setAccountType(ACCOUNTTYPE.valueOf(account));
            transactionDetails.setAccountNumber(savingsAccount.getAccountSavingsNumber());
            transactionDetails.setOpeningBalance(currentBalance);
            transactionDetails.setDeposit(90);
            transactionDetails.setWithdrawal(amountToWithdraw);
            transactionDetails.setBalance(availableBalance);
            transactionHistoryRepository.save(transactionDetails);

        }
        else{

            log.info("insufficient funds");
        }
        }

        else if (accounttype.equals(ACCOUNTTYPE.CURRENTACCOUNT)){
            currentAccount = bankUser.getCurrentAccountModel();
            double currentBalance = currentAccount.getCurrentBalance();
            double balanceEnquired = currentBalance- overdraftAmount;
            if(currentBalance -amountToWithdraw >0){
                double afterBalance = currentBalance-amountToWithdraw;
                log.info("Balance after withdrawing {}",afterBalance);
                //update currentbalance
                currentAccount.setCurrentBalance(afterBalance);
                currentAccountRepository.save(currentAccount);
                //save to tranactionhistory
                transactionDetails = new TransactionDetails();
                transactionDetails.setEmail(email);
                transactionDetails.setLocalDate(LocalDate.now());
                transactionDetails.setAccountType(ACCOUNTTYPE.valueOf(account));
                transactionDetails.setAccountNumber(currentAccount.getAccountCurrentNumber());
                transactionDetails.setOpeningBalance(currentBalance);
                transactionDetails.setDeposit(90);
                transactionDetails.setWithdrawal(amountToWithdraw);
                transactionDetails.setBalance(afterBalance);
                transactionHistoryRepository.save(transactionDetails);
            }


            else if(amountToWithdraw <= balanceEnquired){

                double afterBalance = (-currentBalance)+overdraftAmount+amountToWithdraw;
                log.info("Balance after withdrawing {}",afterBalance);
                currentAccount.setCurrentBalance(afterBalance);
                currentAccountRepository.save(currentAccount);

                //update account
                log.info("About to save transaction details");
                //save transaction history
                transactionDetails = new TransactionDetails();
                transactionDetails.setTransactionId(1212);
                transactionDetails.setEmail(email);
                transactionDetails.setLocalDate(LocalDate.now());
                transactionDetails.setAccountType(ACCOUNTTYPE.valueOf(account));
                transactionDetails.setOpeningBalance(currentBalance);
                transactionDetails.setDeposit(90);
                transactionDetails.setWithdrawal(amountToWithdraw);
                transactionDetails.setBalance(afterBalance);
                transactionHistoryRepository.save(transactionDetails);

            }else
                log.info("Insufficient Funds");

            }
            //transaction not possible
        }
        


    public void depositMoney(Map<String, Object> depositInformation) {
        JSONObject jsonObject = new JSONObject(depositInformation);
        Map<String,Object>depositInfo = jsonObject.getJSONObject("depositInformation").toMap();
        ACCOUNTTYPE accounttype = ACCOUNTTYPE.valueOf(depositInfo.get("accountType").toString());
        String email = depositInfo.get("email").toString();
        String amountDeposited = depositInfo.get("amountDeposited").toString();
        Double amountToDeposit = Double.valueOf(amountDeposited);
        BankUser bankUser  = bankUserService.getByEmail(email);
        if(accounttype.equals(ACCOUNTTYPE.CURRENTACCOUNT)){
            currentAccount = bankUser.getCurrentAccountModel();
            Double currentBalance = currentAccount.getCurrentBalance();
            Double avaliableBalance = currentBalance + amountToDeposit;
            log.info("Available balance after deposit {}",avaliableBalance);
            //update balance
            currentAccount.setCurrentBalance(avaliableBalance);
            currentAccountRepository.save(currentAccount);

            //save to transaction history
            transactionDetails = new TransactionDetails();
            transactionDetails.setEmail(email);
            transactionDetails.setLocalDate(LocalDate.now());
            transactionDetails.setAccountType(accounttype);
            transactionDetails.setAccountNumber(currentAccount.getAccountCurrentNumber());
            transactionDetails.setOpeningBalance(currentBalance);
            transactionDetails.setDeposit(amountToDeposit);
            transactionDetails.setWithdrawal(0);
            transactionDetails.setBalance(avaliableBalance);
            transactionHistoryRepository.save(transactionDetails);

        }

        else if(accounttype.equals(ACCOUNTTYPE.SAVINGSACCOUNT)){
            savingsAccount = bankUser.getSavingsAccountModel();
            Double currentBalance = savingsAccount.getCurrentBalance();
            Double avaliableBalance = currentBalance + amountToDeposit;
            log.info("Available balance after deposit {}",avaliableBalance);
            //update balance
            savingsAccount.setCurrentBalance(avaliableBalance);
            savingsAccountRepository.save(savingsAccount);
            //save to transaction history
            transactionDetails = new TransactionDetails();
            transactionDetails.setEmail(email);
            transactionDetails.setLocalDate(LocalDate.now());
            transactionDetails.setAccountType(accounttype);
            transactionDetails.setAccountNumber(savingsAccount.getAccountSavingsNumber());
            transactionDetails.setOpeningBalance(currentBalance);
            transactionDetails.setDeposit(amountToDeposit);
            transactionDetails.setWithdrawal(0);
            transactionDetails.setBalance(avaliableBalance);
            transactionHistoryRepository.save(transactionDetails);



        }





    }

    public List<TransactionDetails> getTransactionHistory(String email) {
        log.info("This is the received email to be used to retrieve {}",email);
        List<TransactionDetails> retrievedList = transactionDetailsService.getByEmail(email);
        List<TransactionDetails> finalList = new ArrayList<>();
        log.info("This is the retrieved list size {}",retrievedList.size());
        for (TransactionDetails td:retrievedList
             ) {
            finalList.add(td);


        }
        return finalList;
    }

   /* public TransactionDetails setTransactionDetails(BankUser bankUser){
        transactionDetails = new TransactionDetails();
        transactionDetails.setTransactionId(1212);
        transactionDetails.setEmail(bankUser.getEmail());
        transactionDetails.setLocalDate(LocalDate.now());
        transactionDetails.setAccountType(accounttype);
        transactionDetails.setOpeningBalance(currentBalance);
        transactionDetails.setDeposit(90);
        transactionDetails.setWithdrawal(amountToWithdraw);
        transactionDetails.setBalance(afterBalance);
        return transactionHistoryRepository.save(transactionDetails);


    }*/

}
