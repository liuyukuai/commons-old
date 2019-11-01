
package com.itxiaoer.commons.core;

import lombok.Data;

/**
 * 数据对象
 *
 * @author liuyk
 */
@Data
@SuppressWarnings("unused")
public class Node<K, V> {
    /**
     * 属性名称
     */
    private K name;

    /**
     * 属性值
     */
    private V value;
}
