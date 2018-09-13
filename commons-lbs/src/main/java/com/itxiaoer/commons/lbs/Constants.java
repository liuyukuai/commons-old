package com.itxiaoer.commons.lbs;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
public class Constants {


    public static class Baidu {
        /**
         * 微信标准
         */
        public static final String STANDARD_WE_CHAT = "1";

        public static final String SUCCESS_STATUS = "0";

        public static final String KEY_OF_STATUS = "status";

        public static final String KEY_OF_RESULT = "result";

        public static final String KEY_OF_LOCATION = "location";

        public static final String KEY_OF_LNG = "lng";

        public static final String KEY_OF_LAT = "lat";

        public static final String KEY_OF_LEVEL = "level";

        public static final String ADDRESS_URL = "http://api.map.baidu.com/geocoder/v2/?address=%s&output=json&ak=%s";

        public static final String LOCATION_URL = "http://api.map.baidu.com/geocoder/v2/?location=%s,%s&output=json&pois=1&ak=%s";

        public static final String LOCATION_COVERT_URL = "http://api.map.baidu.com/geoconv/v1/?coords=%s,%s&from=%s&to=5&ak=%s";

        public static final String KEY_FORMATTED_ADDRESS = "formatted_address";


    }

}
