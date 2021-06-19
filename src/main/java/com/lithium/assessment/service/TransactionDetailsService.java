package com.lithium.assessment.service;

import com.lithium.assessment.entities.TransactionDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionDetailsService {

    List<TransactionDetails> getByEmail(String email);
}
