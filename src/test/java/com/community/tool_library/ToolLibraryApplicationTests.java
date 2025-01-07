package com.community.tool_library;

import com.community.tool_library.dtos.*;
import com.community.tool_library.services.ItemService;
import com.community.tool_library.services.LoanService;
import com.community.tool_library.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // to ensure that each test rolls back DB changes afterward
class ToolLibraryApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private LoanService loanService;

	private Long testUserId;
	private Long testItemId;

	@BeforeEach
	void setUpTestData() {
		// Create a user
		UserDTO newUser = userService.registerUser(
				new UserRegistrationDTO(
						"testUser",
						"test@example.com",
						"password123"
				)
		);
		testUserId = newUser.id();

		// Create an item
		itemService.createItem(
				new NewItemDTO(
					"Test Drill",
					"Basic drill for drilling holes",
					BigDecimal.valueOf(50.00),
					testUserId
				),
				testUserId
		);

		// Get the item ID
		List<ItemDTO> foundItems = itemService.searchItems("Test Drill");
		assertFalse(foundItems.isEmpty(), "Item not found - check that createItem or searchItems is functioning");

		ItemDTO createdItem = foundItems.getFirst();
		testItemId = createdItem.id();
		assertNotNull(testItemId, "Created item should have a non-null ID");
	}

	@Test
	@DisplayName("borrowItem() - successful loan when item is available")
	void testBorrowItem_Success() {
		// Action
		LoanDTO loanDTO = loanService.borrowItem(testItemId, testUserId);

		// Assertion
		assertNotNull(loanDTO, "LoanDTO should not be null");
		assertEquals(testItemId, loanDTO.itemId(), "LoanDTO should reference the correct item");
		assertEquals(testUserId, loanDTO.borrowerId(), "LoanDTO should reference the correct borrower");
		assertFalse(loanDTO.returned(), "LoanDTO should not be marked as returned");

		// Check that the item is no longer available
		ItemDTO item = itemService.getItem(testItemId);
		assertFalse(item.available(), "Item should be marked as unavailable after loan");
	}

	@Test
	@DisplayName("borrowItem() - exception when item is not available")
	void testBorrowItem_ItemUnavailable() {
		// Arrange
		loanService.borrowItem(testItemId, testUserId); // borrow the item once

		// Action & Assertion
		assertThrows(RuntimeException.class, () -> loanService.borrowItem(testItemId, testUserId),
				"Should throw exception when item is not available");
	}
}
