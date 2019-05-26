package me.devld.tour.service.impl;

import me.devld.tour.config.AppConfig;
import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.poi.*;
import me.devld.tour.entity.District;
import me.devld.tour.entity.Location;
import me.devld.tour.exception.BadRequestException;
import me.devld.tour.exception.NotFoundException;
import me.devld.tour.exception.ServerErrorException;
import me.devld.tour.exception.ThirdPartException;
import me.devld.tour.service.DistrictService;
import me.devld.tour.service.PoiService;
import me.devld.tour.util.JsonUtil;
import me.devld.tour.util.MapUtil;
import me.devld.tour.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AMapPoiServiceImpl implements PoiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AMapPoiServiceImpl.class);

    private final AppConfig.AMapConfig config;
    private final RestTemplate restTemplate;
    private final DistrictService districtService;

    /**
     * <a href="https://lbs.amap.com/api/webservice/guide/api/search#around">周边搜索 API</a>
     */
    private static final String API_FIND_POI_AROUND = "https://restapi.amap.com/v3/place/around?" +
            "key={apiKey}&types={potTypeCode}&location={longitude},{latitude}&radius={radius}&offset={offset}&page={page}";
    private static final String API_WEATHER = "https://restapi.amap.com/v3/weather/weatherInfo?" +
            "key={key}&city={city}&extensions={ext}";

    public AMapPoiServiceImpl(AppConfig appConfig, DistrictService districtService) {
        this.config = appConfig.getaMapConfig();
        this.districtService = districtService;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Page<Poi> findPoiAround(double longitude, double latitude, int radius, PoiType poiType, PageParam pageParam) {
        Pageable pageable = pageParam.toPageable();
        DecimalFormat formatter = new DecimalFormat("#.0#####");
        String result;
        try {
            result = restTemplate.getForObject(API_FIND_POI_AROUND, String.class,
                    config.getApiKey(), config.getPoiCode().get(poiType),
                    formatter.format(longitude), formatter.format(latitude), radius,
                    pageable.getPageSize(), pageable.getPageNumber() + 1);
        } catch (RestClientException e) {
            throw new ServerErrorException("msg.call_api_failed", e);
        }
        Map<String, Object> resp;
        try {
            resp = JsonUtil.readAsMap(result);
        } catch (RuntimeException e) {
            throw new ThirdPartException("decode poi response failed", e);
        }
        checkResponse(resp);
        return processPoiResult(resp, pageable);
    }

    @Override
    public WeatherForecast getWeatherForecast(int locationId) {
        District district = resolveDistrict(locationId);
        Map<String, Object> forecast = extractWeatherData(queryWeather("all", district.getCode()), "forecasts");
        try {
            return new WeatherForecast(
                    TimeUtil.parseNormalTime(MapUtil.getString(forecast, "reporttime"), TimeUtil.ZONE_CHINA),
                    district,
                    MapUtil.getListMap(forecast, "casts").stream()
                            .map(this::mapWeatherWrapper)
                            .sorted(Comparator.comparing(WeatherForecast.WeatherWrapper::getDate))
                            .collect(Collectors.toList())
            );
        } catch (DateTimeParseException e) {
            throw new ThirdPartException("invalid datetime", e);
        }
    }

    @Override
    public WeatherLive getWeatherLive(int locationId) {
        District district = resolveDistrict(locationId);
        Map<String, Object> live = extractWeatherData(queryWeather("base", district.getCode()), "lives");
        WeatherLive weatherLive = new WeatherLive();
        try {
            weatherLive.setReportTime(TimeUtil.parseNormalTime(MapUtil.getString(live, "reporttime"), TimeUtil.ZONE_CHINA));
        } catch (DateTimeParseException e) {
            throw new ThirdPartException("invalid date time", e);
        }
        Weather weather = new Weather();

        weather.setWeather(MapUtil.getString(live, "weather"));
        weather.setTemperature(MapUtil.getString(live, "temperature"));
        weather.setWind(MapUtil.getString(live, "winddirection"));
        weather.setWindPower(MapUtil.getString(live, "windpower"));
        weather.setHumidity(MapUtil.getString(live, "humidity"));

        weatherLive.setWeather(weather);
        weatherLive.setDistrict(district);

        return weatherLive;
    }

    private WeatherForecast.WeatherWrapper mapWeatherWrapper(Map<String, Object> map) {
        Weather day = new Weather();
        day.setWeather(MapUtil.getString(map, "dayweather"));
        day.setTemperature(MapUtil.getString(map, "daytemp"));
        day.setWind(MapUtil.getString(map, "daywind"));
        day.setWindPower(MapUtil.getString(map, "daypower"));

        Weather night = new Weather();
        night.setWeather(MapUtil.getString(map, "nightweather"));
        night.setTemperature(MapUtil.getString(map, "nighttemp"));
        night.setWind(MapUtil.getString(map, "nightwind"));
        night.setWindPower(MapUtil.getString(map, "nightpower"));

        try {
            return new WeatherForecast.WeatherWrapper(TimeUtil.parseNormalDate(MapUtil.getString(map, "date"), TimeUtil.ZONE_CHINA), day, night);
        } catch (DateTimeParseException e) {
            throw new ThirdPartException("invalid date", e);
        }
    }

    private Map<String, Object> extractWeatherData(Map<String, Object> resp, String key) {
        List<Map<String, Object>> list = MapUtil.getListMap(resp, key);
        if (list.isEmpty()) {
            throw new NotFoundException("msg.no_weather_data");
        }
        return list.get(0);
    }

    private Map<String, Object> queryWeather(String ext, String adCode) {
        String result = restTemplate.getForObject(API_WEATHER, String.class,
                config.getApiKey(), adCode, ext);
        Map<String, Object> resp;
        try {
            resp = JsonUtil.readAsMap(result);
        } catch (RuntimeException e) {
            throw new ThirdPartException("decode weather response failed", e);
        }
        checkResponse(resp);
        return resp;
    }

    private District resolveDistrict(int locationId) {
        District district = districtService.getById(locationId);
        if (district == null) {
            throw new BadRequestException();
        }
        return district;
    }

    private Page<Poi> processPoiResult(Map<String, Object> resp, Pageable pageable) {
        try {
            int total = Integer.parseInt((String) resp.get("count"));
            return new PageImpl<>(
                    MapUtil.getListMap(resp, "pois").stream().map(this::mapAMapPoi).collect(Collectors.toList()),
                    pageable, total);
        } catch (RuntimeException e) {
            throw new ThirdPartException("convert json failed", e);
        }
    }

    private Poi mapAMapPoi(Map<String, Object> aMapPoi) {
        Poi poi = new Poi();
        poi.setId(MapUtil.getString(aMapPoi, "id"));
        poi.setName(MapUtil.getString(aMapPoi, "name"));
        poi.setType(Arrays.asList(MapUtil.getString(aMapPoi, "type").split(";")));

        Location location = new Location();
        String locationStr = MapUtil.getString(aMapPoi, "location");
        String[] locationTemp = locationStr.split(",");
        if (locationTemp.length == 2) {
            location.setLongitude(Double.valueOf(locationTemp[0]));
            location.setLatitude(Double.valueOf(locationTemp[1]));
        }

        location.setLocation(MapUtil.getString(aMapPoi, "address"));
        poi.setLocation(location);

        poi.setPhone(Arrays.asList(MapUtil.getString(aMapPoi, "tel").split(";")));
        poi.setDistance(Integer.valueOf(MapUtil.getString(aMapPoi, "distance")));

        poi.setPhotos(MapUtil.getListMap(aMapPoi, "photos").stream().map(e -> MapUtil.getString(e, "url")).collect(Collectors.toList()));

        Map<String, Object> bizExtMap = MapUtil.getMap(aMapPoi, "biz_ext");
        Poi.BizExt bizExt = new Poi.BizExt();
        bizExt.setRating(MapUtil.getString(bizExtMap, "rating"));
        bizExt.setLowestPrice(MapUtil.getString(bizExtMap, "lowest_price"));
        poi.setBizExt(bizExt);

        return poi;
    }

    private void checkResponse(Map<String, Object> resp) {
        if (!"1".equals(resp.get("status"))) {
            LOGGER.warn("AMap api failed: {}, {}", resp.get("infocode"), resp.get("info"));
            throw new ServerErrorException("msg.call_api_failed");
        }
    }

}
