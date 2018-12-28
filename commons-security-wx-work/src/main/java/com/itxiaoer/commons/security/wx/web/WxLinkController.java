package com.itxiaoer.commons.security.wx.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author : liuyk
 */
@Controller
@SuppressWarnings("unused")
public class WxLinkController {

    @GetMapping("/link")
    public String link(String link) {
        return "redirect:" + (StringUtils.isBlank(link) ? "/" : link);
    }

}
