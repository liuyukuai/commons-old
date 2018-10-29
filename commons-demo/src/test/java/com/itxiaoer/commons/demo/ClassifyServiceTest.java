package com.itxiaoer.commons.demo;

import com.itxiaoer.commons.demo.neo4j.dto.ClassifyDto;
import com.itxiaoer.commons.demo.neo4j.entity.Classify;
import com.itxiaoer.commons.demo.neo4j.entity.DownRelation;
import com.itxiaoer.commons.demo.neo4j.entity.UpRelation;
import com.itxiaoer.commons.demo.service.MockClassifyService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
// 方法执行顺序，按照方法名
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClassifyServiceTest {

    @Autowired
    private MockClassifyService mockClassifyService;

    @Test
    public void t01_create() {
        ClassifyDto aDto = new ClassifyDto();
        aDto.setName("分类A");
        Classify A = this.mockClassifyService.create(aDto);
        Classify _A = new Classify();
        _A.setId(1L);
        _A.setName("分类A");
//        Assert.assertEquals(A, _A);

        ClassifyDto bDto = new ClassifyDto();
        bDto.setName("分类B");
        Classify B = this.mockClassifyService.create(bDto);


        ClassifyDto cDto = new ClassifyDto();
        cDto.setName("分类C");
        Classify C = this.mockClassifyService.create(cDto);

        ClassifyDto dDto = new ClassifyDto();
        dDto.setName("分类D");
        Classify D = this.mockClassifyService.create(dDto);


        ClassifyDto eDto = new ClassifyDto();
        eDto.setName("分类E");
        Classify E = this.mockClassifyService.create(eDto);

        UpRelation upRelation = UpRelation.of(B, A);
        this.mockClassifyService.create(upRelation);


        DownRelation downRelation = DownRelation.of(A, B);
        this.mockClassifyService.create(downRelation);

        upRelation = UpRelation.of(C, B);
        this.mockClassifyService.create(upRelation);

        downRelation = DownRelation.of(B, C);
        this.mockClassifyService.create(downRelation);


        upRelation = UpRelation.of(D, C);
        this.mockClassifyService.create(upRelation);


        downRelation = DownRelation.of(C, D);
        this.mockClassifyService.create(downRelation);
    }


    //    @Test
    public void t99_delete() {
        for (int i = 1; i < 21; i++) {
            this.mockClassifyService.delete(String.valueOf(i));
        }
    }

}
