package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.ItemTimeline;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTimelineDAO extends JpaRepository<ItemTimeline, Integer> {
    List<ItemTimeline> findAllByItemIdAndStatus(int item_id, String status, Sort sort);

    List<ItemTimeline> findAllByItemId(int item_id);

    void deleteAllByItemId(int item_id);
}
