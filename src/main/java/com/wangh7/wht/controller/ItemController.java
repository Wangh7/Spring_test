package com.wangh7.wht.controller;


import com.wangh7.wht.pojo.Item;
import com.wangh7.wht.pojo.ItemType;
import com.wangh7.wht.service.ItemService;
import com.wangh7.wht.service.ItemTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {
    @Autowired
    ItemService itemService;
    @Autowired
    ItemTypeService itemTypeService;

    @CrossOrigin
    @GetMapping("api/items/types")
    public List<ItemType> listType() throws Exception {
        return itemTypeService.list();
    }

    @CrossOrigin
    @GetMapping("api/items")
    public List<Item> list() throws Exception {
        return itemService.list();
    }

    @CrossOrigin
    @PostMapping("api/items")
    public Item addOrUpdate(@RequestBody Item item) throws Exception {
        itemService.addOrUpdate(item);
        return item;
    }

    @CrossOrigin
    @PostMapping("api/delete")
    public void delete(@RequestBody Item item) throws Exception {
        itemService.deleteById(item.getItemId());
    }

}
