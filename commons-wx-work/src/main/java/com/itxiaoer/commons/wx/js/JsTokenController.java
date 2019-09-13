package com.itxiaoer.commons.wx.js;

import com.itxiaoer.commons.core.page.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : liuyk
 */
@RestController
public class JsTokenController {

    @Resource
    private JsTokenService jsTokenService;

    @GetMapping("/wx/jsToken")
    public Response<JsSign> jsSign(String url) {
        return Response.ok(jsTokenService.sign(url));
    }
}
