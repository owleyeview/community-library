package com.community.tool_library.controllers;

import com.community.tool_library.dtos.ItemDTO;
import com.community.tool_library.services.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String listItems(@RequestParam(name="q", required=false) String query, Model model) {
        List<ItemDTO> items;
        if (query != null && !query.trim().isEmpty()) {
            items = itemService.searchItems(query.trim());
        } else {
            items = itemService.searchItems("");
        }
        model.addAttribute("itemlist", items);
        return "items";
    }

    @GetMapping("/items/{id}")
    public String itemDetails(@PathVariable Long id, Model model) {

        ItemDTO item = itemService.getItem(id);
        model.addAttribute("item", item);
        return "itemdetail";
    }
}
