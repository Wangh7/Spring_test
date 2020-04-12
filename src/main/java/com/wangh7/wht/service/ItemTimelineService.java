package com.wangh7.wht.service;

import com.wangh7.wht.dao.ItemTimelineDAO;
import com.wangh7.wht.pojo.ItemTimeline;
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
}
