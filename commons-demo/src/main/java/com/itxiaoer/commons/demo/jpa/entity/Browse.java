package com.itxiaoer.commons.demo.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : liuyk
 */
@Data
@Entity
@Table(name = "SYS_BROWSE")
@NoArgsConstructor
@AllArgsConstructor
public class Browse {
    @Id
    private String id;
    private Long count;

}
