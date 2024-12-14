package com.community.tool_library.services;

import com.community.tool_library.dtos.LoanDTO;
import com.community.tool_library.models.Loan;

import java.util.List;

public interface LoanService {
    LoanDTO checkoutItem(Long itemId, Long borrowerId);

    LoanDTO returnItem(Long itemId, Long borrowerId);

    List<LoanDTO> getLoansForUser(Long userId);

    List<LoanDTO> getActiveLoans();

    Loan getLoanEntity(Long loanId);
}
