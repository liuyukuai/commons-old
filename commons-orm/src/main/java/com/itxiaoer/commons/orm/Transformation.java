package com.itxiaoer.commons.orm;

import com.itxiaoer.commons.core.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 最终转换器
 *
 * @author : liuyk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transformation {

    private String[] field;

    private Object value;

    private Operator operator;

    private Operator relation;

    private boolean ignoreEmpty;

}
