package com.community.tool_library.controllers;

import com.community.tool_library.dtos.ItemDTO;
import com.community.tool_library.services.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String listItems(Model model) {

        List<ItemDTO> allItems = itemService.searchItems("");
        model.addAttribute("itemlist", allItems);
        return "items";
    }
}
