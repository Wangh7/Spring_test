package com.wangh7.wht.controller;


import com.wangh7.wht.pojo.Item;
import com.wangh7.wht.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping("/api/items")
    public List<Item> list() throws Exception {
        return itemService.list();
    }

    @PostMapping("/api/items")
    public Item addOrUpdate(@RequestBody Item item) throws Exception {
        itemService.addOrUpdate(item);
        return item;
    }

    @PostMapping("/api/delete")
    public void delete(@RequestBody Item item) throws Exception {
        itemService.deleteById(item.getItem_id());
    }

}
