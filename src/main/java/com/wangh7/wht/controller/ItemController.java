package com.wangh7.wht.controller;


import com.wangh7.wht.entity.ItemIds;
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
    ItemBuyService itemBuyService;
    @Autowired
    PasswordService passwordService;
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
    @GetMapping(value = "/api/items/car")
    public Result addShopCar(@RequestParam int item_id) {
        int user_id = userService.findByUsername(SecurityUtils.getSubject().getPrincipal().toString()).getId();
        if(itemBuyService.addShopCar(user_id,item_id)){
            return ResultFactory.buildSuccessResult("success");
        } else {
            return ResultFactory.buildFailResult("商品已在购物车中");
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
    public List<ItemSell> userGetItemSellList() {
        int user_id = userService.findByUsername(SecurityUtils.getSubject().getPrincipal().toString()).getId();
        return itemSellService.userList(user_id);
    }

    @CrossOrigin
    @PostMapping(value = "/api/items/sell")
    public Result userAddItemSell(@RequestBody ItemSell itemSell) {
        if (itemSellService.addOrUpdate(itemSell)) {
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
    @GetMapping(value = "/api/items/buy")
    public List<ItemBuy> userGetItemBuyList() {
        int user_id = userService.findByUsername(SecurityUtils.getSubject().getPrincipal().toString()).getId();
        return itemBuyService.userBuyList(user_id);
    }

    @CrossOrigin
    @GetMapping(value = "/api/items/bought")
    public List<ItemBuy> userGetItemBoughtList() {
        int user_id = userService.findByUsername(SecurityUtils.getSubject().getPrincipal().toString()).getId();
        return itemBuyService.userBoughtList(user_id);
    }

    @CrossOrigin
    @PostMapping(value = "/api/items/buy")
    public Result userItemBuy(@RequestBody ItemIds itemIds) {
        int user_id = userService.findByUsername(SecurityUtils.getSubject().getPrincipal().toString()).getId();
        List<Integer> item_ids = itemIds.getItem_ids();
        switch (itemBuyService.userBuyItem(user_id, item_ids)) {
            case 3:
                return ResultFactory.buildFailResult("余额不足");
            case 1:
                return ResultFactory.buildSuccessResult("success");
            case 0:
                return ResultFactory.buildFailResult("购买失败");
            case 2:
                return ResultFactory.buildFailResult("所选商品部分已被卖出，请重新选择");
            default:
                return ResultFactory.buildFailResult("未知错误");
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/items/buy/delete")
    public void deleteItemBuy(@RequestBody ItemBuy itemBuy) {
        itemBuyService.deleteById(itemBuy.getId());
    }

    @CrossOrigin
    @GetMapping(value = "/api/items/timeline")
    public List<ItemTimeline> getItemTimeline(@RequestParam int item_id, @RequestParam String status) {
        return itemTimelineService.getItemTimeline(item_id, status);
    }
    @CrossOrigin
    @GetMapping(value = "/api/items/pass")
    public String getDecodePass(@RequestParam String pass,@RequestParam int item_id) throws Exception{
        int user_id = userService.findByUsername(SecurityUtils.getSubject().getPrincipal().toString()).getId();
        itemBuyService.userBuyItemPass(user_id,item_id);
        return passwordService.DES(pass,"decode");
    }
}
