package com.itxiaoer.commons.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * excel数据对象
 *
 * @author liuyk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sheets {

    /**
     * sheet页名称
     */
    private String name;

    /**
     * 表头数据
     */
    private List<String> headers;

    /**
     * 具体数据集合
     */
    private List<List<String>> rows;


}
