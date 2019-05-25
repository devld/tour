package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.controller.Request;
import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.poi.Poi;
import me.devld.tour.dto.poi.PoiType;
import me.devld.tour.dto.poi.WeatherForecast;
import me.devld.tour.dto.poi.WeatherLive;
import me.devld.tour.service.PoiService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ApiController
@RequestMapping("/poi")
public class PoiController {

    private final PoiService poiService;

    public PoiController(PoiService poiService) {
        this.poiService = poiService;
    }

    @Request("查询某位置附近的POI")
    @GetMapping("/around")
    public Page<Poi> findPoiAround(
            @RequestParam("lng") double lng,
            @RequestParam("lat") double lat,
            @RequestParam(value = "radius", defaultValue = "0") int radius,
            @RequestParam(value = "type") PoiType type,
            PageParam pageParam) {
        return poiService.findPoiAround(lng, lat, radius, type, pageParam);
    }

    @Request("查询天气预报")
    @GetMapping("/weather/forecast")
    public WeatherForecast queryWeatherForecast(@RequestParam("lid") int locationId) {
        return poiService.getWeatherForecast(locationId);
    }

    @Request("查询实时天气")
    @GetMapping("/weather/live")
    public WeatherLive queryWeatherLive(@RequestParam("lid") int locationId) {
        return poiService.getWeatherLive(locationId);
    }

}
