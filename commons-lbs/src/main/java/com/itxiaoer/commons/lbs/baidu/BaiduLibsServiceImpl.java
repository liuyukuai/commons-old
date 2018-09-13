package com.itxiaoer.commons.lbs.baidu;

import com.itxiaoer.commons.core.json.JsonUtil;
import com.itxiaoer.commons.lbs.Constants;
import com.itxiaoer.commons.lbs.LbsService;
import com.itxiaoer.commons.lbs.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author : liuyk
 */
@Slf4j
public class BaiduLibsServiceImpl implements LbsService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private BaiduConfig baiduConfig;

    @Override
    public Optional<Location> byLocation(String lng, String lat) {
        String url = String.format(Constants.Baidu.LOCATION_URL, lng, lat, baiduConfig.getAk());
        log.debug("url : {} ", url);
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);
        log.debug("response : {} ", responseEntity);
        Optional<LinkedHashMap> optional = JsonUtil.toBean(responseEntity.getBody(), LinkedHashMap.class);
        if (optional.isPresent()) {
            LinkedHashMap linkedHashMap = optional.get();
            // success
            if (Objects.equals(Constants.Baidu.SUCCESS_STATUS, String.valueOf(linkedHashMap.get(Constants.Baidu.KEY_OF_STATUS)))) {
                LinkedHashMap location = (LinkedHashMap) linkedHashMap.get(Constants.Baidu.KEY_OF_LOCATION);
                String address = String.valueOf(linkedHashMap.get(Constants.Baidu.KEY_FORMATTED_ADDRESS));
                return Optional.of(new Location(address, "", String.valueOf(location.get(Constants.Baidu.KEY_OF_LNG)), String.valueOf(location.get(Constants.Baidu.KEY_OF_LAT))));
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Location> byAddress(String address) {
        String url = String.format(Constants.Baidu.ADDRESS_URL, address, baiduConfig.getAk());
        log.debug("url : {} ", url);
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);
        log.debug("response : {} ", responseEntity);

        Optional<LinkedHashMap> optional = JsonUtil.toBean(responseEntity.getBody(), LinkedHashMap.class);
        if (optional.isPresent()) {

            LinkedHashMap linkedHashMap = optional.get();
            // success
            if (Objects.equals(Constants.Baidu.SUCCESS_STATUS, String.valueOf(linkedHashMap.get(Constants.Baidu.KEY_OF_STATUS)))) {
                LinkedHashMap result = (LinkedHashMap) linkedHashMap.get(Constants.Baidu.KEY_OF_RESULT);
                LinkedHashMap locationMap = (LinkedHashMap) result.get(Constants.Baidu.KEY_OF_LOCATION);
                return Optional.of(new Location(address, String.valueOf(result.get(Constants.Baidu.KEY_OF_LEVEL)), String.valueOf(locationMap.get(Constants.Baidu.KEY_OF_LNG)), String.valueOf(locationMap.get(Constants.Baidu.KEY_OF_LAT))));
            }
        }
        return Optional.empty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Location> covert(String lng, String lat, String type) {

        String url = String.format(Constants.Baidu.LOCATION_COVERT_URL, lng, lat, type, baiduConfig.getAk());
        log.debug("url : {} ", url);
        ResponseEntity<String> responseEntity = new RestTemplate().getForEntity(url, String.class);
        log.debug("response : {} ", responseEntity);

        Optional<LinkedHashMap> optional = JsonUtil.toBean(responseEntity.getBody(), LinkedHashMap.class);
        if (optional.isPresent()) {

            LinkedHashMap linkedHashMap = optional.get();
            // success
            if (Objects.equals(Constants.Baidu.SUCCESS_STATUS, String.valueOf(linkedHashMap.get(Constants.Baidu.KEY_OF_STATUS)))) {
                List<LinkedHashMap> result = (List<LinkedHashMap>) linkedHashMap.get(Constants.Baidu.KEY_OF_RESULT);
                Location location = new Location();
                Object x = result.get(0).get("x");
                Object y = result.get(0).get("y");
                location.setLng(String.valueOf(x));
                location.setLat(String.valueOf(y));
                // query address
                Optional<Location> address = this.byLocation(location.getLng(), location.getLat());
                //
                location.setAddress(address.map(Location::getAddress).orElse("未知"));
                return Optional.of(location);
            }
        }
        return Optional.empty();
    }
}
