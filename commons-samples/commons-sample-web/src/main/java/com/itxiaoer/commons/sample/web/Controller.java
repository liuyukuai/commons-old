package com.itxiaoer.commons.sample.web;

import com.itxiaoer.commons.core.page.Response;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author : liuyk
 */

@RestController
public class Controller {


    @PostMapping("/samples")
    public Response<String> create() {
        return Response.ok("1");
    }

    @PutMapping("/samples/{id}")
    public Response<String> update(@PathVariable String id) {
        return Response.ok(id);
    }

    @DeleteMapping("/samples/{id}")
    public Response<String> delete(@PathVariable String id) {
        return Response.ok(id);
    }

    @GetMapping("/samples/{id}")
    public Response<String> get(@PathVariable String id) {
        return Response.ok(id);
    }

    @GetMapping("/samples")
    public Response<List<String>> list() {
        return Response.ok(Collections.emptyList());
    }

   
}
