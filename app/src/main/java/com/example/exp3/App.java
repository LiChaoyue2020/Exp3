package com.example.exp3;

import java.util.List;

public class App {
    private String time;
    private String date;
    private String message;
    private String status;
    private data data;
    private forecast forecast;
    private CityInfo CityInfo;

    public void setCityInfo(App.CityInfo cityInfo) {
        CityInfo = cityInfo;
    }

    public void setForecast(App.forecast forecast) {
        this.forecast = forecast;
    }


    public App.forecast getForecast() {
        return forecast;
    }


    public App.CityInfo getCityInfo() {
        return CityInfo;
    }

    public void setData(App.data data) {
        this.data = data;
    }

    public App.data getData() {
        return data;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public static class forecast {
        private String date;
        private String ymd;
        private String week;
        private String sunrise;
        private String high;
        private String low;
        private String sunset;
        private String aqi;
        private String fx;
        private String fl;
        private String type;
        private String notice;

        public void setYmd(String ymd) {
            this.ymd = ymd;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public void setFx(String fx) {
            this.fx = fx;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public String getAqi() {
            return aqi;
        }

        public String getFl() {
            return fl;
        }

        public String getFx() {
            return fx;
        }

        public String getHigh() {
            return high;
        }

        public String getLow() {
            return low;
        }

        public String getSunrise() {
            return sunrise;
        }

        public String getNotice() {
            return notice;
        }

        public String getSunset() {
            return sunset;
        }

        public String getType() {
            return type;
        }

        public String getWeek() {
            return week;
        }

        public String getYmd() {
            return ymd;
        }

    }

    public static class data {
        private String shidu;
        private String pm25;
        private String pm10;
        private String quality;
        private String wendu;
        private String ganmao;
        private yesterday yesterday;
        private List<forecast> Forecast;

        public void setYesterday(App.data.yesterday yesterday) {
            this.yesterday = yesterday;
        }

        public App.data.yesterday getYesterday() {
            return yesterday;
        }

        public void setForecast(List<App.forecast> forecast) {
            Forecast = forecast;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public void setPm10(String pm10) {
            this.pm10 = pm10;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public void setShidu(String shidu) {
            this.shidu = shidu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public String getShidu() {
            return shidu;
        }

        public String getPm25() {
            return pm25;
        }

        public String getQuality() {
            return quality;
        }

        public String getGanmao() {
            return ganmao;
        }

        public String getPm10() {
            return pm10;
        }

        public String getWendu() {
            return wendu;
        }

        public List<App.forecast> getForecast() {
            return Forecast;
        }

        public static class yesterday {
            private String date;
            private String ymd;
            private String week;
            private String sunrise;
            private String high;
            private String low;
            private String sunset;
            private String aqi;
            private String fx;
            private String fl;
            private String type;
            private String notice;

            public void setDate(String date) {
                this.date = date;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public void setNotice(String notice) {
                this.notice = notice;
            }

            public void setSunrise(String sunrise) {
                this.sunrise = sunrise;
            }

            public void setSunset(String sunset) {
                this.sunset = sunset;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public void setYmd(String ymd) {
                this.ymd = ymd;
            }

            public String getDate() {
                return date;
            }

            public String getAqi() {
                return aqi;
            }

            public String getFl() {
                return fl;
            }

            public String getFx() {
                return fx;
            }

            public String getHigh() {
                return high;
            }

            public String getLow() {
                return low;
            }

            public String getSunrise() {
                return sunrise;
            }

            public String getNotice() {
                return notice;
            }

            public String getSunset() {
                return sunset;
            }

            public String getType() {
                return type;
            }

            public String getWeek() {
                return week;
            }

            public String getYmd() {
                return ymd;
            }
        }
    }

    public static class CityInfo {
        private String city;
        private String cityId;
        private String parent;
        private String updateTime;

        public void setCity(String city) {
            this.city = city;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getCity() {
            return city;
        }

        public String getCityId() {
            return cityId;
        }

        public String getParent() {
            return parent;
        }

        public String getUpdateTime() {
            return updateTime;
        }
    }
}
