package com.itxiaoer.commons.core.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : liuyk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class Sort {
    private String name;
    private String direction;
}
