package com.ticc.webapiservice.service;

import com.ticc.webapiservice.dto.response.TransactionResponseDTO;

import java.util.List;

public interface TransactionService {
    List<TransactionResponseDTO> getTransactionDetails(String id);
}
