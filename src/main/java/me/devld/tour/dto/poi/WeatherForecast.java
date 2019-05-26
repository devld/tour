package me.devld.tour.dto.poi;

import me.devld.tour.entity.District;

import java.util.List;

public class WeatherForecast {

    private Long reportTime;

    private District district;

    private List<WeatherWrapper> weathers;

    public WeatherForecast(Long reportTime, District district, List<WeatherWrapper> weathers) {
        this.reportTime = reportTime;
        this.district = district;
        this.weathers = weathers;
    }

    public Long getReportTime() {
        return reportTime;
    }

    public void setReportTime(Long reportTime) {
        this.reportTime = reportTime;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public List<WeatherWrapper> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<WeatherWrapper> weathers) {
        this.weathers = weathers;
    }

    public static class WeatherWrapper {
        private Long date;

        /**
         * 白天天气
         */
        private Weather day;

        /**
         * 夜间天气
         */
        private Weather night;

        public WeatherWrapper(Long date, Weather day, Weather night) {
            this.date = date;
            this.day = day;
            this.night = night;
        }

        public Long getDate() {
            return date;
        }

        public void setDate(Long date) {
            this.date = date;
        }

        public Weather getDay() {
            return day;
        }

        public void setDay(Weather day) {
            this.day = day;
        }

        public Weather getNight() {
            return night;
        }

        public void setNight(Weather night) {
            this.night = night;
        }
    }

}
