package cn.com.clickhouse.controller;

import cn.com.clickhouse.service.UisTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Date;

@RestController
@RequestMapping("/test")
public class UisTestController {

    @Autowired
    private UisTestService uisTestService;

    @PostConstruct
    private void loadIndData() throws Exception {
        uisTestService.loadIndData();
    }

    @GetMapping("/getList")
    public int getList() {
        return uisTestService.count();
    }

    @GetMapping("/insert")
    public String insert(){
        uisTestService.insert(new Date(), new Date(), 1000);
        return "success";
    }
}
