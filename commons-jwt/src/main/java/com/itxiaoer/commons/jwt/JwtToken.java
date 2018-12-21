package com.itxiaoer.commons.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : liuyk
 */
@Data
@SuppressWarnings("unused")
public class JwtToken implements Serializable {

    static final long serialVersionUID = 6739574768237935126L;

    private String token;
    private Long expireTime;
    @JsonIgnore
    private Long refreshTime;

    @JsonIgnore
    private int type;

    public JwtToken() {

    }

    public JwtToken(String token, Long expireTime) {
        this(token, expireTime, null);
    }

    public JwtToken(String token, Long expireTime, Long refreshTime) {
        this.expireTime = expireTime;
        this.token = token;
        this.refreshTime = refreshTime;
    }

    public JwtToken(String token, Long expireTime, Long refreshTime, int type) {
        this.expireTime = expireTime;
        this.token = token;
        this.refreshTime = refreshTime;
        this.type = type;
    }

    /**
     * @author : liuyk
     */
    public enum Operation {
        /**
         * 创建
         */
        create(0),
        /**
         * 刷新
         */
        refresh(1),
        /**
         * 销毁
         */
        destroy(-1);

        private int value;

        Operation(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
