package com.itxiaoer.commons.demo.repository;

import com.itxiaoer.commons.demo.entity.Browse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author : liuyk
 */
public interface BrowseRepository extends JpaRepository<Browse, String>, JpaSpecificationExecutor<Browse> {


}
