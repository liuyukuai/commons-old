package com.itxiaoer.commons.core.page;

import com.itxiaoer.commons.core.Exclude;
import com.itxiaoer.commons.core.Transfer;
import com.itxiaoer.commons.core.function.ThirdFunction;
import com.itxiaoer.commons.core.util.Lists;
import lombok.*;
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
@Slf4j
@Builder
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class Paging implements Transfer<String, String> {

    private static final String DESC = "desc";

    private static final String ASC = "asc";
    /**
     * 分页页数，默认值1，最小值为1
     */
    @Setter
    @Getter
    @Exclude
    private String page;
    /**
     * 每页条数，默认值10
     */
    @Setter
    @Getter
    @Exclude
    private String size;

    /**
     * 排序规则 eg: xxx-desc, xxx-asc
     */
    @Setter
    @Getter
    @Exclude
    private String sort;

    public Paging() {
    }

    private Paging(String page, String size, String sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public static Paging of() {
        return new Paging();
    }


    public static Paging of(String page, String size) {
        return of(page, size, "");
    }


    public static Paging of(String page, String size, String sort) {
        return new Paging(page, size, sort);
    }

    /**
     * 解析page对象,若参数错误，默认desc
     *
     * @param thirdFunction 解析函数
     * @param <R>           解析后的对象
     * @param <F>           function
     * @return 返回解析结果
     */
    public <R, F extends ThirdFunction<R, Integer, Integer, List<Sort>>> R get(F thirdFunction) {
        if (StringUtils.isBlank(sort)) {
            return thirdFunction.apply(PageUtils.page(page), PageUtils.size(size), Lists.newArrayList());
        }
        List<Sort> sorts = Arrays.stream(sort.split(",")).map(s -> {
            try {
                String[] split = s.split("-");
                if (split.length == 1) {
                    return new Sort(this.apply(split[0]), DESC);
                }
                String direction = split[1];
                direction = !DESC.equalsIgnoreCase(direction) && !ASC.equalsIgnoreCase(direction) ? DESC : direction;
                return new Sort(this.apply(split[0]), direction);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }).collect(Collectors.toList());
        return thirdFunction.apply(PageUtils.page(page), PageUtils.size(size), sorts);
    }

    /**
     * 将排序的类型转换为数据库对应的值
     * 默认排序名称和数据库名称相同
     *
     * @param name 排序的名称
     * @return 返回数据库的名称
     */
    @Override
    public String apply(String name) {
        return name;
    }
}
