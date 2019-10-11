package cn.com.clickhouse.service.imp;

import ch.qos.logback.core.util.FileUtil;
import cn.com.clickhouse.mapper.UisTestMapper;
import cn.com.clickhouse.pojo.UisTest;
import cn.com.clickhouse.service.UisTestService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Service
@Slf4j
public class UisTestServiceImpl implements UisTestService {
    @Autowired
    private UisTestMapper uisTestMapper;

    private JSONArray indList;
    @Override
    public void loadIndData() throws Exception {
        @Cleanup
        FileOutputStream fos = null;
        try {
            URL resource = this.getClass().getClassLoader().getResource("ind.json");
            File file = new File(resource.toURI());
            FileInputStream fileInputStream = new FileInputStream(file);
            JSONObject jsonObject = JSONObject.parseObject(IOUtils.toString(fileInputStream, "UTF-8"));
            indList = (JSONArray)jsonObject.get("dmIndicators");
            log.info("ind size:[{}]", indList.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int count() {
        return uisTestMapper.selectCount();
    }

    @Override
    public void insert(Date startDate, Date entDate, Integer dayNum) {
        UisTest insertInd = getInsertInd(startDate);
        List<UisTest> uisTests = Lists.newArrayList(getInsertInd(startDate), getInsertInd(new Date()));
        log.info(JSONArray.toJSONString(uisTests));
        uisTestMapper.insertBath(uisTests);
    }

    private UisTest getInsertInd(Date date) {
        int i = RandomUtils.nextInt(indList.size() - 1);
        JSONObject jsonObject = (JSONObject)indList.get(i);
        UisTest uisTest = UisTest.builder().orderId("orderId" + String.valueOf(RandomUtils.nextInt(999999999)))
                .objectPk("objectPk" + String.valueOf(RandomUtils.nextInt(999999999)))
                .idNumber(String.valueOf(RandomUtils.nextInt(999999999)))
                .phone(String.valueOf(RandomUtils.nextInt(999999999)))
                .userName("userName" + String.valueOf(RandomUtils.nextInt(999999999)))
                .transTime(date).workflowCode("workflowCode")
                .workflowName("workflowName")
                .bizChannel("bizChannel")
                .bizCode("bizCode")
                .bizChannelName("bizChannelame")
                .bizName("bizName")
                .indicatorType(jsonObject.getString("valueType"))
                .indicatorName(jsonObject.getString("indiName"))
                .indicatorAo("ao")
                .indicatorValue("qqqqq")
                .label(1)
                .createTime(date).updateTime(date)
                .build();
        return uisTest;
    }


}
