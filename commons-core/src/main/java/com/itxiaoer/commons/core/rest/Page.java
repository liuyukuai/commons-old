package com.itxiaoer.commons.core.rest;

import com.itxiaoer.commons.core.util.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * page
 *
 * @author : liuyk
 */
@Data
@Slf4j
@SuppressWarnings("unused")
public class Page {

    private static final String DESC = "desc";

    private static final String ASC = "asc";
    /**
     * 分页页数，默认值1，最小值为1
     */
    private String page;
    /**
     * 每页条数，默认值10
     */
    private String size;

    /**
     * 排序规则 eg: xxx-desc,xxx-asc
     */
    private String sort;

    /**
     * 解析page对象,若参数错误，默认desc
     *
     * @param thirdFunction 解析函数
     * @param <R>          解析后的对象
     * @return 返回解析结果
     */
    public <R> R get(ThirdFunction<R, Integer, Integer, List<Sort>> thirdFunction) {
        if (StringUtils.isBlank(sort)) {
            return thirdFunction.apply(PageUtils.page(page), PageUtils.size(size), Lists.newArrayList());
        }
        List<Sort> sorts = Arrays.stream(sort.split(",")).map(s -> {
            try {
                String[] split = s.split("-");
                if (split.length == 1) {
                    return new Sort(split[0], DESC);
                }
                String direction = split[1];
                direction = !DESC.equalsIgnoreCase(direction) && !ASC.equalsIgnoreCase(direction) ? DESC : direction;
                return new Sort(split[0], direction);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }).collect(Collectors.toList());
        return thirdFunction.apply(PageUtils.page(page), PageUtils.size(size), sorts);
    }

}
