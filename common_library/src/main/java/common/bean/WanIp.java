package common.bean;

/**
 * Created by Alick on 2016/3/16.
 */
public class WanIp {

    /**
     * code : 0
     * data : {"country":"中国","country_id":"CN","area":"华北","area_id":"100000","region":"北京市","region_id":"110000","city":"北京市","city_id":"110100","county":"","county_id":"-1","isp":"移动","isp_id":"100025","ip":"223.72.151.91"}
     */

    private int code;
    /**
     * country : 中国
     * country_id : CN
     * area : 华北
     * area_id : 100000
     * region : 北京市
     * region_id : 110000
     * city : 北京市
     * city_id : 110100
     * county :
     * county_id : -1
     * isp : 移动
     * isp_id : 100025
     * ip : 223.72.151.91
     */

    private DataEntity data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String country;
        private String country_id;
        private String area;
        private String area_id;
        private String region;
        private String region_id;
        private String city;
        private String city_id;
        private String county;
        private String county_id;
        private String isp;
        private String isp_id;
        private String ip;

        public void setCountry(String country) {
            this.country = country;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public void setCounty_id(String county_id) {
            this.county_id = county_id;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }

        public void setIsp_id(String isp_id) {
            this.isp_id = isp_id;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getCountry() {
            return country;
        }

        public String getCountry_id() {
            return country_id;
        }

        public String getArea() {
            return area;
        }

        public String getArea_id() {
            return area_id;
        }

        public String getRegion() {
            return region;
        }

        public String getRegion_id() {
            return region_id;
        }

        public String getCity() {
            return city;
        }

        public String getCity_id() {
            return city_id;
        }

        public String getCounty() {
            return county;
        }

        public String getCounty_id() {
            return county_id;
        }

        public String getIsp() {
            return isp;
        }

        public String getIsp_id() {
            return isp_id;
        }

        public String getIp() {
            return ip;
        }
    }
}
