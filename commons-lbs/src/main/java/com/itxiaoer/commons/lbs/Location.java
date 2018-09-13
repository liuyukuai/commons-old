package com.itxiaoer.commons.lbs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地理位置
 *
 * @author : liuyk
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    /**
     * 地址
     */
    private String address;
    /**
     * 能精确理解的地址类型
     */
    private String level;

    /**
     * 经度值
     */
    private String lng;
    /**
     * 纬度值
     */
    private String lat;
}
