package com.el.ariby.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 오픈웨더맵 데이터를 받아오는 모델 클래스
 * @link { https://openweathermap.org/forecast5#JSON }
 */
public class OpenWeather {

    public class WeatherResult {
        private String cod;
        private double message;
        private int cnt;
        private List<TimeWeather> list;
        private CityVo city;

        public WeatherResult(String cod, int message, int cnt, List<TimeWeather> list, CityVo city) {
            this.cod = cod;
            this.message = message;
            this.cnt = cnt;
            this.list = list;
            this.city = city;
        }

        public String getCod() {
            return cod;
        }

        public void setCod(String cod) {
            this.cod = cod;
        }

        public double getMessage() {
            return message;
        }

        public void setMessage(double message) {
            this.message = message;
        }

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        public List<TimeWeather> getList() {
            return list;
        }

        public void setList(List<TimeWeather> list) {
            this.list = list;
        }

        public CityVo getCity() {
            return city;
        }

        public void setCity(CityVo city) {
            this.city = city;
        }
    }

    public class TimeWeather implements Serializable {
        private int dt;
        private MainVo main;
        private List<WeatherVo> weather;
        private CloudsVo clouds;
        private WindVo wind;
        private SnowVo snow;
        private RainVo rain;
        private SysVo sys;
        @SerializedName("dt_txt")
        private String dtTxt;

        public TimeWeather(int dt, MainVo main, List<WeatherVo> weather, CloudsVo clouds, WindVo wind, SnowVo snow, RainVo rain, SysVo sys, String dtTxt) {
            this.dt = dt;
            this.main = main;
            this.weather = weather;
            this.clouds = clouds;
            this.wind = wind;
            this.snow = snow;
            this.rain = rain;
            this.sys = sys;
            this.dtTxt = dtTxt;
        }

        public int getDt() {
            return dt;
        }

        public void setDt(int dt) {
            this.dt = dt;
        }

        public MainVo getMain() {
            return main;
        }

        public void setMain(MainVo main) {
            this.main = main;
        }

        public List<WeatherVo> getWeather() {
            return weather;
        }

        public void setWeather(List<WeatherVo> weather) {
            this.weather = weather;
        }

        public CloudsVo getClouds() {
            return clouds;
        }

        public void setClouds(CloudsVo clouds) {
            this.clouds = clouds;
        }

        public WindVo getWind() {
            return wind;
        }

        public void setWind(WindVo wind) {
            this.wind = wind;
        }

        public SnowVo getSnow() {
            return snow;
        }

        public void setSnow(SnowVo snow) {
            this.snow = snow;
        }

        public RainVo getRain() {
            return rain;
        }

        public void setRain(RainVo rain) {
            this.rain = rain;
        }

        public SysVo getSys() {
            return sys;
        }

        public void setSys(SysVo sys) {
            this.sys = sys;
        }

        public String getDtTxt() {
            return dtTxt;
        }

        public void setDtTxt(String dtTxt) {
            this.dtTxt = dtTxt;
        }
    }

    public class MainVo implements Serializable {
        private double temp;
        @SerializedName("temp_min")
        private double tempMin;
        @SerializedName("temp_max")
        private double tempMax;
        private double pressure;
        @SerializedName("sea_level")
        private double seaLevel;
        @SerializedName("grnd_level")
        private double grndLevel;
        private double humidity;
        @SerializedName("temp_kf")
        private double tempKf;

        public MainVo(double temp, double tempMin, double tempMax, double pressure, double seaLevel, double grndLevel, double humidity, double tempKf) {
            this.temp = temp;
            this.tempMin = tempMin;
            this.tempMax = tempMax;
            this.pressure = pressure;
            this.seaLevel = seaLevel;
            this.grndLevel = grndLevel;
            this.humidity = humidity;
            this.tempKf = tempKf;
        }

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getTempMin() {
            return tempMin;
        }

        public void setTempMin(double tempMin) {
            this.tempMin = tempMin;
        }

        public double getTempMax() {
            return tempMax;
        }

        public void setTempMax(double tempMax) {
            this.tempMax = tempMax;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }

        public double getSeaLevel() {
            return seaLevel;
        }

        public void setSeaLevel(double seaLevel) {
            this.seaLevel = seaLevel;
        }

        public double getGrndLevel() {
            return grndLevel;
        }

        public void setGrndLevel(double grndLevel) {
            this.grndLevel = grndLevel;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        public double getTempKf() {
            return tempKf;
        }

        public void setTempKf(double tempKf) {
            this.tempKf = tempKf;
        }
    }

    public class WeatherVo implements Serializable {
        private int id;
        private String main;
        private String description;
        private String icon;

        public WeatherVo(int id, String main, String description, String icon) {
            this.id = id;
            this.main = main;
            this.description = description;
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public class WindVo implements Serializable {
        private double speed;
        private double deg;

        public WindVo(double speed, double deg) {
            this.speed = speed;
            this.deg = deg;
        }

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getDeg() {
            return deg;
        }

        public void setDeg(double deg) {
            this.deg = deg;
        }
    }

    public class CloudsVo implements Serializable {
        private int all;

        public CloudsVo(int all) {
            this.all = all;
        }

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }
    }

    public class SnowVo implements Serializable {
        @SerializedName("3h")
        private double threeH;

        public SnowVo(double threeH) {
            this.threeH = threeH;
        }

        public double getThreeH() {
            return threeH;
        }

        public void setThreeH(double threeH) {
            this.threeH = threeH;
        }
    }

    public class RainVo implements Serializable {
        @SerializedName("3h")
        private double threeH;

        public RainVo(double threeH) {
            this.threeH = threeH;
        }

        public double getThreeH() {
            return threeH;
        }

        public void setThreeH(double threeH) {
            this.threeH = threeH;
        }
    }

    public class SysVo implements Serializable {
        private String pod;

        public SysVo(String pod) {
            this.pod = pod;
        }

        public String getPod() {
            return pod;
        }

        public void setPod(String pod) {
            this.pod = pod;
        }
    }

    public class CityVo implements Serializable {
        private int id;
        private String name;
        private String country;
        private CoordVo coord;

        public CityVo(int id, String name, String country, CoordVo coord) {
            this.id = id;
            this.name = name;
            this.country = country;
            this.coord = coord;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public CoordVo getCoord() {
            return coord;
        }

        public void setCoord(CoordVo coord) {
            this.coord = coord;
        }
    }

    public class CoordVo implements Serializable {
        private double lon;
        private double lat;

        public CoordVo(double lon, double lat) {
            this.lon = lon;
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }
}
