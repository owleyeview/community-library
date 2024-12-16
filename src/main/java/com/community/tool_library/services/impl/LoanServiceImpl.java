package com.community.tool_library.services.impl;

import com.community.tool_library.dtos.LoanDTO;
import com.community.tool_library.models.Item;
import com.community.tool_library.models.Loan;
import com.community.tool_library.models.User;
import com.community.tool_library.repositories.LoanRepository;
import com.community.tool_library.services.ItemService;
import com.community.tool_library.services.LoanService;
import com.community.tool_library.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final ItemService itemService;
    private final UserService userService;

    public LoanServiceImpl(LoanRepository loanRepository, ItemService itemService, UserService userService) {
        this.loanRepository = loanRepository;
        this.itemService = itemService;
        this.userService = userService;
    }

    @Override
    public LoanDTO checkoutItem(Long itemId, Long borrowerId) {
        // Get item and validate availability
        Item item = itemService.getItemEntity(itemId);
        if (!item.isAvailable()) {
            throw new RuntimeException("Item is not available for checkout.");
        }

        // Get borrower to ensure user exists
        User borrower = userService.getUserEntity(borrowerId);

        // Create loan entity
        Loan loan = Loan.builder()
                .item(item)
                .borrower(borrower)
                .returned(false)
                .build();

        // Update item availability
        itemService.updateAvailability(itemId, false);

        // Save loan
        Loan newLoan = loanRepository.save(loan);

        return mapToDTO(newLoan);
    }

    @Override
    public LoanDTO returnItem(Long itemId, Long borrowerId) {
        // Get item and ensure it's currently borrowed by this user
        Item item = itemService.getItemEntity(itemId);
        if (item.isAvailable()) {
            throw new RuntimeException("Item is already marked as available.");
        }

        // Find the active loan

        Loan activeLoan = loanRepository.findByItemIdAndBorrowerIdAndReturnedFalse(itemId, borrowerId)
                .orElseThrow(() -> new RuntimeException("No active loan found for this item and user."));

        // Mark loan as returned
        activeLoan.setReturned(true);
        Loan updatedLoan = loanRepository.save(activeLoan);

        // Update item availability
        itemService.updateAvailability(itemId, true);

        return mapToDTO(updatedLoan);
    }

    @Override
    public List<LoanDTO> getLoansForUser(Long userId) {
        List<Loan> loans = loanRepository.findByBorrowerId(userId);
        return loans.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> getActiveLoans() {
        List<Loan> activeLoans = loanRepository.findByReturnedFalse();
        return activeLoans.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public Loan getLoanEntity(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + loanId));
    }

    // helper methods
    private LoanDTO mapToDTO(Loan loan) {
        return new LoanDTO(
                loan.getId(),
                loan.getItem().getId(),
                loan.getBorrower().getId(),
                loan.isReturned(),
                loan.getCreatedAt(),
                loan.getUpdatedAt()
        );
    }
}
