package com.itxiaoer.commons.jpa.page;


import com.itxiaoer.commons.core.page.PageUtils;
import com.itxiaoer.commons.core.page.Paging;
import com.itxiaoer.commons.orm.page.PagingUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
public class PagingUtilTest {

    private Paging pagingEmpty;
    private Paging paging;
    private Paging pagingStr;
    private Paging pagingSort;
    private Paging pagingSorts;

    @Before
    public void before() {
        pagingEmpty = new Paging();
        paging = Paging.of("2", "10");
        pagingStr = Paging.of("abc", "1234a");
        pagingSort = Paging.of("abc", "1234a", "createTime-desc");
        pagingSorts = Paging.of("abc", "1234a", "createTime-desc,publishTime-desc");
    }


    @Test
    public void get() {
        PageRequest pageRequest = PagingUtils.of(pagingEmpty);
        Assert.assertEquals(pageRequest, PageRequest.of(PageUtils.DEFAULT_MIN_PAGE - 1, PageUtils.DEFAULT_PAGE_SIZE));

        pageRequest = PagingUtils.of(paging);
        Assert.assertEquals(pageRequest, PageRequest.of(1, 10));

        // 自定义排序or默认排序
        pageRequest = PagingUtils.of(paging, (sorts ->
                new Sort(Sort.Direction.DESC, "createTime")
        ));
        Assert.assertEquals(pageRequest, PageRequest.of(1, 10, new Sort(Sort.Direction.DESC, "createTime")));

        pageRequest = PagingUtils.of(pagingStr);
        Assert.assertEquals(pageRequest, PageRequest.of(PageUtils.DEFAULT_MIN_PAGE - 1, PageUtils.DEFAULT_PAGE_SIZE));

        pageRequest = PagingUtils.of(pagingSort);
        Assert.assertEquals(pageRequest, PageRequest.of(PageUtils.DEFAULT_MIN_PAGE - 1, PageUtils.DEFAULT_PAGE_SIZE, new Sort(Sort.Direction.DESC, "createTime")));

        pageRequest = PagingUtils.of(pagingSorts);
        Assert.assertEquals(pageRequest, PageRequest.of(PageUtils.DEFAULT_MIN_PAGE - 1, PageUtils.DEFAULT_PAGE_SIZE, new Sort(Sort.Direction.DESC, "createTime", "publishTime")));
    }
}
