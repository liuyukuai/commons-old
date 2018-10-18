package com.itxiaoer.commons.jpa.page;

import com.itxiaoer.commons.orm.Queryable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : liuyk
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuery implements Queryable {
    private String name;
    private String id;
}
