package com.wangh7.wht.controller;


import com.wangh7.wht.pojo.*;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {
    @Autowired
    ItemService itemService;
    @Autowired
    ItemStockService itemStockService;
    @Autowired
    ItemTypeService itemTypeService;
    @Autowired
    ItemSellService itemSellService;
    @Autowired
    UserService userService;
    @Autowired
    ItemTimelineService itemTimelineService;

//    @CrossOrigin
//    @GetMapping("api/items/test")
//    public void test(String name) throws Exception {
//        System.out.println(name);
//    }

    @CrossOrigin
    @GetMapping(value = "/api/items/{typeCode}")
    public List<ItemStock> listByTypeCode(@PathVariable("typeCode") String typeCode) throws Exception {
        if (typeCode.equals("all")) {
            return list();
        } else {
            return itemStockService.listByTypeCode(typeCode);
        }
    }
    @CrossOrigin
    @GetMapping("api/items/types")
    public List<ItemType> listType() throws Exception {
        return itemTypeService.list();
    }

    @CrossOrigin
    @GetMapping("api/items")
    public List<ItemStock> list() throws Exception {
        return itemStockService.list();
    }

    @CrossOrigin
    @PostMapping("api/items")
    public Item addOrUpdate(@RequestBody Item item) throws Exception {
        itemService.addOrUpdate(item);
        return item;
    }

    @CrossOrigin
    @PostMapping("api/items/delete")
    public void delete(@RequestBody Item item) throws Exception {
        itemService.deleteById(item.getItemId());
    }

    @CrossOrigin
    @GetMapping(value = "/api/items/sell")
    public List<ItemSell> userGetItemSellList(){
        int user_id = userService.findByUsername(SecurityUtils.getSubject().getPrincipal().toString()).getId();
        return itemSellService.userList(user_id);
    }

    @CrossOrigin
    @PostMapping(value = "/api/items/sell")
    public Result userAddItemSell(@RequestBody ItemSell itemSell) {
        if(itemSellService.addOrUpdate(itemSell)) {
            return ResultFactory.buildSuccessResult("发布成功");
        } else {
            return ResultFactory.buildFailResult("发布失败");
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/items/sell/delete")
    public void deleteItemSell(@RequestBody ItemSell itemSell) {
        itemTimelineService.deleteAll(itemSell.getItemId());
        itemSellService.deleteById(itemSell.getItemId());
    }

    @CrossOrigin
    @GetMapping(value = "/api/items/timeline")
    public List<ItemTimeline> getItemTimeline(@RequestParam int item_id,@RequestParam String status) {
        return itemTimelineService.getItemTimeline(item_id,status);
    }
}
