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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


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
    private Constants constants;
    @Autowired
    private TransactionDetailsServiceImpl transactionDetailsService;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
    @Value("${bank.minimumDeposit}")
    private double minimumDeposit;

    @Value("${bank.minimumBalance}")
    private double minimumBalance;

    @Value("${bank.overDraftBalance}")
    private double overDraftBalance;
    @Value("${bank.deposit}")
    private double depoist;
    @Value("${bank.withdraw}")
    private double withdraw;


    private static final Logger log = LoggerFactory.getLogger(LogicFunctions.class);


    BankUser bankUser = new BankUser();
    SavingsAccountModel savingsAccount = new SavingsAccountModel();
    CurrentAccountModel currentAccount = new CurrentAccountModel();
    TransactionDetails transactionDetails = new TransactionDetails();

    public LogicFunctions(BankUserServiceImpl bankUserService, BankUserRepository bankUserRepository, CurrentAccountRepository currentAccountRepository, SavingsAccountRepository savingsAccountRepository, Constants constants, TransactionHistoryRepository transactionHistoryRepository) {
        this.bankUserService = bankUserService;
        this.bankUserRepository = bankUserRepository;
        this.currentAccountRepository = currentAccountRepository;
        this.savingsAccountRepository = savingsAccountRepository;
        this.constants = constants;
        this.transactionHistoryRepository = transactionHistoryRepository;

    }

    public Map<String, Object> openAccount(Map<String, Object> userInfo) throws ParseException {
        log.info("This is the received json {}", userInfo);
        JSONObject obj = new JSONObject(userInfo);
        Map<String, Object> userinfo = obj.getJSONObject("personalInfo").toMap();
        log.info("This is the map with personal information {}", userinfo);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dob = userinfo.get("dob").toString();
        LocalDate date = LocalDate.parse(dob, df);
         String depositAmount = userinfo.get("depositSavingsAmount").toString();
        Double depositedAmount = Double.valueOf(depositAmount);
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put(constants.SUCCESSHEADER, constants.SUCCESSMESSAGE);
        String emailProvided = userinfo.get("email").toString();
        log.info("This is provided email {}", emailProvided);
        bankUser = new BankUser();
        if (bankUserService.getByEmail(emailProvided) == null) {

            if (depositedAmount > minimumDeposit) {
                log.info("About to save savings account information");
                savingsAccount = new SavingsAccountModel();
                savingsAccount.setAccountSavingsNumber(generateAccountNumber());
                savingsAccount.setCurrentBalance(depositedAmount);
                savingsAccount.setDepositAmount(depositedAmount);
                savingsAccount.setOpeningDate(LocalDate.now());
                savingsAccount.setTransactionDate(LocalDate.now());
                savingsAccount.setCurrentBalance(depositedAmount);
                savingsAccount.setOverdraftBalance(overDraftBalance);
                savingsAccount.setWithdrawnAmount(0);
                log.info("About to save current account information");
                currentAccount = new CurrentAccountModel();
                currentAccount.setAccountCurrentNumber(generateAccountNumber());
                String currentDeposit = userinfo.get("currentDeposit").toString();
                currentAccount.setDepositAmount(Double.parseDouble(currentDeposit));
                currentAccount.setOpeningDate(LocalDate.now());
                currentAccount.setCurrentBalance(Double.parseDouble(currentDeposit));
                currentAccount.setMaximumOverdraft(overDraftBalance);
                currentAccount.setOverdraftBalance(overDraftBalance);
                currentAccount.setTransactionDate(LocalDate.now());
                log.info("About to save user information");
                bankUser.setEmail(userinfo.get("email").toString());
                bankUser.setSurname(userinfo.get("surname").toString());
                bankUser.setName(userinfo.get("name").toString());
                bankUser.setDob(date);
                bankUser.setCurrentAccountModel(currentAccount);
                bankUser.setSavingsAccountModel(savingsAccount);
                log.info("About to save new user {}", bankUser.toString());
                bankUserRepository.save(bankUser);


            } else
                log.info("amount not enough");
        }

        return successResponse;

    }

    public void withdrawFromSavings(Map<String, Object> withdrawInformation) {

        JSONObject jsonObject = new JSONObject(withdrawInformation);
        log.info("information on withdrawal amount");
        Map<String, Object> savingsWithdraw = jsonObject.getJSONObject("withdrawalInformation").toMap();
        String account = savingsWithdraw.get("accountType").toString();
        ACCOUNTTYPE accounttype = ACCOUNTTYPE.valueOf(savingsWithdraw.get("accountType").toString());
        String email = savingsWithdraw.get("email").toString();
        String amountWithdrawn = savingsWithdraw.get("amountWithdrawn").toString();
        Double amountToWithdraw = Double.valueOf(amountWithdrawn);
        BankUser bankUser = bankUserService.getByEmail(email);
        if (accounttype.equals(ACCOUNTTYPE.SAVINGSACCOUNT)) {
            savingsAccount = bankUser.getSavingsAccountModel();
            Double currentBalance = savingsAccount.getCurrentBalance();
            double availableBalance = currentBalance - amountToWithdraw;
            if (availableBalance > minimumBalance) {

                log.info("Available balance to be updated {}", availableBalance);
                savingsAccount.setCurrentBalance(availableBalance);
                log.info("About to update current balance");
                savingsAccountRepository.save(savingsAccount);

                setTransactionDetails(email, ACCOUNTTYPE.valueOf(account), currentBalance, depoist, amountToWithdraw, availableBalance);

            } else {

                log.info("insufficient funds");
            }
        } else if (accounttype.equals(ACCOUNTTYPE.CURRENTACCOUNT)) {
            currentAccount = bankUser.getCurrentAccountModel();
            double currentBalance = currentAccount.getCurrentBalance();
             if (currentBalance - amountToWithdraw > 0) {
                double afterBalance = currentBalance - amountToWithdraw;
                log.info("Balance after withdrawing {}", afterBalance);
                currentAccount.setCurrentBalance(afterBalance);
                log.info("About to update balance");
                currentAccountRepository.save(currentAccount);

                setTransactionDetails(email, ACCOUNTTYPE.valueOf(account), currentBalance, depoist, amountToWithdraw, afterBalance);
            } else if (amountToWithdraw <= (currentBalance - overDraftBalance)) {

                double afterBalance = (-currentBalance) + overDraftBalance + amountToWithdraw;
                log.info("Balance after withdrawing {}", afterBalance);
                currentAccount.setCurrentBalance(afterBalance);
                currentAccountRepository.save(currentAccount);
                log.info("About to save transaction details");
                setTransactionDetails(email, ACCOUNTTYPE.valueOf(account), currentBalance, depoist, amountToWithdraw, afterBalance);

            } else
                log.info("Insufficient Funds");

        }
        //transaction not possible
    }


    public void depositMoney(Map<String, Object> depositInformation) {
        JSONObject jsonObject = new JSONObject(depositInformation);
        Map<String, Object> depositInfo = jsonObject.getJSONObject("depositInformation").toMap();
        ACCOUNTTYPE accounttype = ACCOUNTTYPE.valueOf(depositInfo.get("accountType").toString());
        String email = depositInfo.get("email").toString();
        String amountDeposited = depositInfo.get("amountDeposited").toString();
        Double amountToDeposit = Double.valueOf(amountDeposited);
        BankUser bankUser = bankUserService.getByEmail(email);
        if (accounttype.equals(ACCOUNTTYPE.CURRENTACCOUNT)) {
            currentAccount = bankUser.getCurrentAccountModel();
            Double currentBalance = currentAccount.getCurrentBalance();
            Double avaliableBalance = currentBalance + amountToDeposit;
            log.info("Available balance after deposit {}", avaliableBalance);
            //update balance
            currentAccount.setCurrentBalance(avaliableBalance);
            currentAccountRepository.save(currentAccount);


            setTransactionDetails(email, accounttype, currentBalance, amountToDeposit, withdraw, avaliableBalance);


        } else if (accounttype.equals(ACCOUNTTYPE.SAVINGSACCOUNT)) {
            savingsAccount = bankUser.getSavingsAccountModel();
            Double currentBalance = savingsAccount.getCurrentBalance();
            Double avaliableBalance = currentBalance + amountToDeposit;
            log.info("Available balance after deposit {}", avaliableBalance);
            savingsAccount.setCurrentBalance(avaliableBalance);
            savingsAccountRepository.save(savingsAccount);
            setTransactionDetails(email, accounttype, currentBalance, amountToDeposit, withdraw, avaliableBalance);

        }


    }


    public List<TransactionDetails> getTransactionHistory(String email) {
        log.info("This is the received email to be used to retrieve {}", email);
        List<TransactionDetails> retrievedList = transactionDetailsService.getByEmail(email);
        List<TransactionDetails> finalList = new ArrayList<>();
        log.info("This is the retrieved list size {}", retrievedList.size());
        for (TransactionDetails td : retrievedList
        ) {
            finalList.add(td);


        }
        return retrievedList;
    }

    public TransactionDetails setTransactionDetails(String email, ACCOUNTTYPE accounttype, double currentBalance, double depositAmount, double amountToWithdraw, double afterBalance) {
        transactionDetails = new TransactionDetails();
        transactionDetails.setEmail(bankUser.getEmail());
        transactionDetails.setLocalDate(LocalDate.now());
        transactionDetails.setAccountType(accounttype);
        transactionDetails.setOpeningBalance(currentBalance);
        transactionDetails.setDeposit(depositAmount);
        transactionDetails.setWithdrawal(amountToWithdraw);
        transactionDetails.setBalance(afterBalance);
        return transactionHistoryRepository.save(transactionDetails);


    }

    public Long generateAccountNumber(){

        Random random = new Random();
       long accountNumber =100000000+ random.nextLong();
       return  accountNumber;

    }


}