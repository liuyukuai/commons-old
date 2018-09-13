package com.itxiaoer.commons.lbs;

import java.util.Optional;

/**
 * @author : liuyk
 */
public interface LbsService {
    /**
     * 通过纬度值和经度值查询地址位置
     *
     * @param lat 纬度值
     * @param lng 经度值
     * @return location 地理位置
     */
    Optional<Location> byLocation(String lng, String lat);


    /**
     * 地址查询地址位置
     *
     * @param address 地址
     * @return location 地理位置
     */
    Optional<Location> byAddress(String address);

    /**
     * 经纬度转换
     *
     * @param type 其他类型经纬度 google
     * @return location
     */
    Optional<Location> covert(String lng, String lat, String type);

}
