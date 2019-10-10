package cn.com.clickhouse.controller;

import cn.com.clickhouse.service.UisTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class UisTestController {

    @Autowired
    private UisTestService uisTestService;

    @GetMapping("/getList")
    public int getList() {
        return uisTestService.count();
    }
}
