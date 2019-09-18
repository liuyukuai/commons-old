package com.itxiaoer.commons.wx.js;

import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.wx.WxProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : liuyk
 */
@RestController
public class JsTokenController {

    @Resource
    private JsTokenService jsTokenService;

    @Resource
    private WxProperties wxProperties;

    @GetMapping("/wx/jsToken")
    public Response<JsSign> jsSign(String url) {
        return Response.ok(jsTokenService.sign(url));
    }


    @RequestMapping("/wx/properties")
    public Response<WxProperties> properties() {
        return Response.ok(wxProperties);
    }
}
