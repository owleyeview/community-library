package com.community.tool_library.services;

import com.community.tool_library.dtos.LoanDTO;
import com.community.tool_library.models.Loan;

import java.util.List;

public interface LoanService {
    LoanDTO checkoutItem(Long itemId, Long borrowerId);

    LoanDTO returnItem(Long itemId, Long borrowerId);

    List<LoanDTO> getLoansByUser(Long userId);

    List<LoanDTO> getActiveLoansByUser(Long userId);

    List<LoanDTO> getAllActiveLoans();

    Loan getLoanEntity(Long loanId);
}
