package cn.com.clickhouse.controller;

import cn.com.clickhouse.pojo.UisTest;
import cn.com.clickhouse.service.UisTestService;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public String insert(@RequestParam(value = "startDate", required = false) Date startDate,
                         @RequestParam(value = "endDate", required = false) Date endDate,
                         @RequestParam(value = "dayNum", defaultValue = "10000", required = false) Integer dayNum){
        if(startDate == null) {
            startDate = DateUtils.addYears(new Date(), -2);
        }
        if(endDate == null) {
            endDate = DateUtils.addDays(new Date(), 1);
        }
        return JSONArray.toJSONString(uisTestService.insert(startDate, endDate, dayNum));
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //CustomDateEditor为自定义日期编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }
}
