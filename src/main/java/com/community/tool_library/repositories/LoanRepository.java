package com.community.tool_library.repositories;

import com.community.tool_library.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByBorrowerId(Long borrowerId);
    List<Loan> findByItemId(Long itemId);
    List<Loan> findByReturnedFalse();

    Optional<Loan> findByItemIdAndBorrowerIdAndReturnedFalse(Long itemId, Long borrowerId);

    List<Loan> findByBorrowerIdAndReturnedFalse(Long userId);
}
