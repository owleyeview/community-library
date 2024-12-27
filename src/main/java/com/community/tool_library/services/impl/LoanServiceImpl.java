package com.community.tool_library.services.impl;

import com.community.tool_library.dtos.LoanDTO;
import com.community.tool_library.models.Item;
import com.community.tool_library.models.Loan;
import com.community.tool_library.models.User;
import com.community.tool_library.repositories.LoanRepository;
import com.community.tool_library.services.ItemService;
import com.community.tool_library.services.LoanService;
import com.community.tool_library.services.UserService;
import com.community.tool_library.services.WaitlistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final WaitlistService waitlistService;

    public LoanServiceImpl(LoanRepository loanRepository, ItemService itemService, UserService userService, WaitlistService waitlistService) {
        this.loanRepository = loanRepository;
        this.itemService = itemService;
        this.userService = userService;
        this.waitlistService = waitlistService;
    }

    @Override
    public LoanDTO borrowItem(Long itemId, Long borrowerId) {
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

        // Check if user had a hold on this item
        List<Long> waitlistUserIds = waitlistService.getWaitlistUserIdsByItem(itemId);

        // If so, delete the WaitlistEntry
        if (waitlistUserIds.contains(borrowerId)) {
            waitlistService.cancelHold(itemId, borrowerId);
        }

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

        // Notify next user in waitlist
        waitlistService.notifyNextUser(itemId);

        return mapToDTO(updatedLoan);
    }

    @Override
    public List<LoanDTO> getLoansByUser(Long userId) {
        List<Loan> loans = loanRepository.findByBorrowerId(userId);
        return loans.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> getActiveLoansByUser(Long userId) {
        List<Loan> activeLoans = loanRepository.findByBorrowerIdAndReturnedFalse(userId);
        return activeLoans.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<Long> getActiveLoanItemIdsByUser(Long userId) {
        List<Loan> activeLoans = loanRepository.findByBorrowerIdAndReturnedFalse(userId);
        return activeLoans.stream()
                .map(loan -> loan.getItem().getId())
                .toList();
    }

    @Override
    public List<LoanDTO> getLoansByItem(Long itemId) {
        List<Loan> loans = loanRepository.findByItemId(itemId);
        return loans.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> getAllActiveLoans() {
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
