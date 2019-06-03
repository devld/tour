package me.devld.tour.service;

import me.devld.tour.dto.PageParam;
import org.springframework.data.domain.Page;

public interface StatisticsService {

    Page<Long> getHotSpotId(PageParam pageParam);

    Page<Long> getRecommendSpotId(long userId, PageParam pageParam);

    Page<Long> getHotTravelNotesId(PageParam pageParam);

}
