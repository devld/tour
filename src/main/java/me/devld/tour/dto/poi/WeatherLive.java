package me.devld.tour.dto.poi;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.devld.tour.entity.District;

import java.util.Date;

public class WeatherLive {

    @JsonUnwrapped
    private Weather weather;

    private District district;

    private Long reportTime;

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Long getReportTime() {
        return reportTime;
    }

    public void setReportTime(Long reportTime) {
        this.reportTime = reportTime;
    }
}
