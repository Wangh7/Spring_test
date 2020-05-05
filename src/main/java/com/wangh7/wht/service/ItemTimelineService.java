package com.wangh7.wht.service;

import com.wangh7.wht.dao.ItemTimelineDAO;
import com.wangh7.wht.pojo.ItemTimeline;
import com.wangh7.wht.utils.DateTimeUtils;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemTimelineService {
    @Autowired
    ItemTimelineDAO itemTimelineDAO;

    public List<ItemTimeline> getItemTimeline(int item_id, String status) {
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        return itemTimelineDAO.findAllByItemIdAndStatus(item_id, status, sort);
    }

    public List<ItemTimeline> getItemTimelinenos(int item_id) {
        return itemTimelineDAO.findAllByItemId(item_id);
    }

    public boolean isExist(int item_id) {
        if(itemTimelineDAO.findAllByItemId(item_id).isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    public boolean addOrUpdate(ItemTimeline itemTimeline) {
        try {
            itemTimelineDAO.save(itemTimeline);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Transactional
    public void deleteAll(int item_id) {
        itemTimelineDAO.deleteAllByItemId(item_id);
    }

    public void sellEntityItem(int item_id,int status) {
        ItemTimeline itemTimeline = new ItemTimeline();
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        itemTimeline.setItemId(item_id);
        itemTimeline.setStatus("S");
        itemTimeline.setTimestamp(dateTimeUtils.getTimeLong()); //时间itemTimeline2.setStatus("S");
        switch (status) {
            case 1:
                itemTimeline.setContent("买家已付款，请尽快发货");break;
            case 2:
                itemTimeline.setContent("已发货至平台");break;
            case 3:
                itemTimeline.setContent("平台已收货，等待平台审核");break;
            case 4:
                itemTimeline.setContent("平台审核通过");
                itemTimeline.setIcon("el-icon-check");
                itemTimeline.setType("success");break;
            case 5:
                itemTimeline.setContent("平台审核未通过，卡片已退回，请注意查收");
                itemTimeline.setIcon("el-icon-close");
                itemTimeline.setType("danger");break;
            case 6:
                itemTimeline.setContent("信息审核通过，已上架平台");
                itemTimeline.setType("success");
                itemTimeline.setIcon("el-icon-check");break;
            case 7:
                itemTimeline.setContent("系统审核通过");
                itemTimeline.setType("success");
                itemTimeline.setIcon("el-icon-check");break;
            case 8:
                itemTimeline.setContent("卖家确认退回收货");
                itemTimeline.setIcon("el-icon-more");
                itemTimeline.setType("primary");break;
            case 9:
                itemTimeline.setContent("用户修改商品信息");break;
            case 10:
                itemTimeline.setContent("用户创建商品信息");break;
            case 11:
                itemTimeline.setContent("系统审核未通过");
                itemTimeline.setType("danger");
                itemTimeline.setIcon("el-icon-close");break;
        }
        itemTimelineDAO.save(itemTimeline);
    }

    public void buyEntityItem(int item_id, int status) {
        ItemTimeline itemTimeline = new ItemTimeline();
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        itemTimeline.setItemId(item_id);
        itemTimeline.setTimestamp(dateTimeUtils.getTimeLong()); //时间
        itemTimeline.setStatus("B");
        switch (status) {
            case 1:
                itemTimeline.setContent("等待卖家发货");break;
            case 2:
                itemTimeline.setContent("卖家已发货至平台");break;
            case 3:
                itemTimeline.setContent("平台已收货，等待审核");break;
            case 4:
                itemTimeline.setContent("平台审核通过，已发货");break;
            case 5:
                itemTimeline.setContent("买家确认收货");
                itemTimeline.setIcon("el-icon-check");
                itemTimeline.setType("success");break;
            case 6:
                itemTimeline.setContent("商品审核出现问题，交易关闭，您的资金已退回");
                itemTimeline.setIcon("el-icon-close");
                itemTimeline.setType("danger");break;
            case 7:
                itemTimeline.setContent("用户查看密码");
                itemTimeline.setType("primary");
                itemTimeline.setIcon("el-icon-more");break;
            case 8:
                itemTimeline.setContent("用户提交订单");break;
            case 9:
        }
        itemTimelineDAO.save(itemTimeline);
    }

    public void priceTimeline(int item_id, Money m, double discountItem, double discountTime, int status){
        ItemTimeline itemTimeline = new ItemTimeline();
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        itemTimeline.setItemId(item_id);
        itemTimeline.setTimestamp(dateTimeUtils.getTimeLong()); //时间
        itemTimeline.setType("warning");
        itemTimeline.setIcon("el-icon-coin");
        switch (status) {
            case 1:
                itemTimeline.setContent("资金已到账，退款：" + m + "元");
                itemTimeline.setStatus("B");break;
            case 2:
                itemTimeline.setContent("资金已到账，收入：" + m + "元。（商品折扣：" + discountItem + "，时间折扣：" + discountTime+"）");
                itemTimeline.setStatus("S");break;
            case 3:
                itemTimeline.setContent("订单支付完成，支出：" + m + "元。（商品折扣：" + discountItem + "，时间折扣：" + discountTime+"）");
                itemTimeline.setStatus("B");break;
        }
        itemTimelineDAO.save(itemTimeline);

    }
}
