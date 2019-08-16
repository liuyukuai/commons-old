package com.itxiaoer.commons.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 合并单元格对象
 *
 * @author : liuyk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class CombineCell {

    private boolean combine;

    private int row;

    private int col;
}
