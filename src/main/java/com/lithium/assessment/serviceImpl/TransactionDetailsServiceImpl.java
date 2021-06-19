package com.lithium.assessment.serviceImpl;

import com.lithium.assessment.entities.TransactionDetails;
import com.lithium.assessment.repository.TransactionHistoryRepository;
import com.lithium.assessment.service.TransactionDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionDetailsServiceImpl implements TransactionDetailsService {

    @Autowired
    private TransactionHistoryRepository transactionDetailsRepository;
    @Override
    public List<TransactionDetails> getByEmail(String email) {
        return transactionDetailsRepository.getByEmail(email);
    }
}
