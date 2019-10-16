package cn.com.clickhouse.service.imp;

import cn.com.clickhouse.mapper.UisTestMapper;
import cn.com.clickhouse.pojo.UisTest;
import cn.com.clickhouse.service.UisTestService;
import cn.com.clickhouse.uitil.FileUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.*;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UisTestServiceImpl implements UisTestService {
    @Autowired
    private UisTestMapper uisTestMapper;
    @Autowired
    private ThreadPoolExecutor insertBathPool;

    @Autowired(required = false)
    @Qualifier("insertDataQueues")
    private BlockingQueue<List<UisTest>> insertDataQueues;

    private JSONArray indList;

    @Override
    public void loadIndData() throws Exception {
        try {
            String s = FileUtils.readClassPathFile("ind.json");
            JSONObject jsonObject = JSONObject.parseObject(s);
            indList = (JSONArray)jsonObject.get("dmIndicators");
            log.info("ind size:[{}]", indList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int count() {
        return uisTestMapper.selectCount();
    }

    @Override
    public long insert(Date startDate, Date entDate, Integer dayNum) {
        long between = DateUtil.between(startDate, entDate, DateUnit.DAY);
        if(between > 0) {
            for(int i=0; i <= between; i++) {
                Date date = DateUtils.addDays(startDate, i);
                List<UisTest> uisTests = Lists.newArrayList();
                for(int j = 0; j < dayNum; j ++ ) {
                    uisTests.add(getInsertInd(date));
//                    try {
//                        insertDataQueues.put(Lists.newArrayList(getInsertInd(date)));
//                    } catch (IntwwerruptedException e) {
//                        log.error("插入Queue失败！", e);
//                    }
                    if((j != 0 && j%3000 ==0) || j == dayNum-1){
                        List<UisTest> objects = uisTests.stream().sorted(Comparator.comparing(UisTest::getIndicatorName)).sorted(Comparator.comparing(UisTest::getLabel)).collect(Collectors.toList());
                        log.info("Date:[{}], Size:[{}]", DateUtil.format(date, DatePattern.NORM_DATE_FORMAT), objects.size());
                        uisTestMapper.insertBath(objects);
                        uisTests.clear();
//                        try {
//                            insertDataQueues.put(objects);
////                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            log.error("插入Queue失败！", e);
//                        }
                    }
                }
            }
        }
        return (between+1) * dayNum;
    }

//    @PostConstruct
    public void listenerDataChange() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("开始监听Queue中数据变化！");
                while (true) {
                    try {
                        List<UisTest> take = insertDataQueues.take();
                        if(CollUtil.isNotEmpty(take)) {
                            insertBathPool.execute(new InsertBathThread(take));
                        }
                    } catch (InterruptedException e) {
                        log.error("从Queue中获取数据失败！", e);
                    }
                }

            }
        }).start();
    }

    class InsertBathThread implements Runnable {
        private List<UisTest> uisTests;

        public InsertBathThread(List<UisTest> uisTests) {
            this.uisTests = uisTests;
        }

        @Override
        public void run() {
            try {
                uisTests = uisTests.stream().sorted(Comparator.comparing(UisTest::getIndicatorName)).sorted(Comparator.comparing(UisTest::getLabel)).collect(Collectors.toList());
                uisTestMapper.insertBath(uisTests);
                log.info("Thread:[{}], size:[{}]", Thread.currentThread().getName(), uisTests.size());
            } catch (Exception e) {
                log.error("插入失败！", e);
            }
        }
    }

    private UisTest getInsertInd(Date date) {
        int i = RandomUtils.nextInt(indList.size() - 1);
        JSONObject jsonObject = (JSONObject)indList.get(i);
        int paramIndex = RandomUtil.randomInt(1, 5);
        Date randomDate = randomDayTime(date);
        UisTest uisTest = UisTest.builder().orderId("orderId" + String.valueOf(RandomUtils.nextInt(999999999)))
                .objectPk("objectPk" + String.valueOf(RandomUtils.nextInt(999999999)))
                .idNumber(String.valueOf(RandomUtils.nextInt(999999999)))
                .phone(String.valueOf(RandomUtils.nextInt(999999999)))
                .userName("userName" + String.valueOf(RandomUtils.nextInt(999999999)))
                .workflowCode("workflowCode" + paramIndex)
                .workflowName("workflowName" + paramIndex)
                .bizChannel("bizChannel" + paramIndex)
                .bizCode("bizCode" + paramIndex)
                .bizChannelName("channelName" + paramIndex)
                .bizName("bizName" + paramIndex)
                .indicatorType(jsonObject.getString("valueType"))
                .indicatorName(jsonObject.getString("indiName"))
                .indicatorAo("ao:" + jsonObject.getString("indiCode"))
                .indicatorValue(getIndValue(jsonObject.getString("valueType")))
                .label(RandomUtil.randomInt(0, 2))
                .createTime(randomDate).updateTime(randomDate).transTime(randomDate)
                .build();
        return uisTest;
    }

    private String getIndValue(String valueType) {
        String result = "";
        switch (valueType){
            case "C":
                // 字符型
                result = RandomUtil.randomString(15);
                break;
            case "N":
                // 数值
                result = String.valueOf(RandomUtil.randomInt(0,2000));
                break;
            case "B":
                // boolean
                result = String.valueOf(RandomUtils.nextBoolean());
                break;
            case "T":
                //时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(2015, 1, 1);
                DateTime dateTime = RandomUtil.randomDay(-1000, 10);
                result = String.valueOf(dateTime.getTime());
                break;
            case "A":
                // 集合类型
                List<String> strings = RandomUtil.randomEles(Lists.newArrayList(RandomUtil.randomString(10),
                        RandomUtil.randomString(10),
                        RandomUtil.randomString(10),
                        RandomUtil.randomString(10)),
                        RandomUtil.randomInt(1, 4));
                result = JSONArray.toJSONString(strings);
                break;
            default:
                break;
        }
        return result;
    }

    private Date randomDayTime(Date date) {
        String format = DateUtil.format(date, "yyyy-MM-dd 00:00:00");
        DateTime dateTime = RandomUtil.randomDate(DateUtil.parseDate(format), DateField.SECOND, 0, 86300);
        return dateTime;
    }


}
