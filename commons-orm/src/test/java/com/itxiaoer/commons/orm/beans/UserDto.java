package com.itxiaoer.commons.orm.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String name;
    private boolean boy;
}
