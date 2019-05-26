package me.devld.tour.service;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.poi.Poi;
import me.devld.tour.dto.poi.PoiType;
import me.devld.tour.dto.poi.WeatherForecast;
import me.devld.tour.dto.poi.WeatherLive;
import org.springframework.data.domain.Page;

public interface PoiService {

    /**
     * 查找某个位置周围的 POI
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param radius    半径(米)
     * @param poiType   POI 类型
     * @param pageParam 分页
     * @return POI list
     */
    Page<Poi> findPoiAround(double longitude, double latitude, int radius, PoiType poiType, PageParam pageParam);

    /**
     * 获取某地区天气预报
     *
     * @param locationId locationId
     * @return 天气预报
     */
    WeatherForecast getWeatherForecast(int locationId);

    /**
     * 获取某地区实时天气
     *
     * @param locationId locationId
     * @return 实时天气
     */
    WeatherLive getWeatherLive(int locationId);

}
