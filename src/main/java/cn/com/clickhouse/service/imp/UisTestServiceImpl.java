package cn.com.clickhouse.service.imp;

import ch.qos.logback.core.util.FileUtil;
import cn.com.clickhouse.mapper.UisTestMapper;
import cn.com.clickhouse.pojo.UisTest;
import cn.com.clickhouse.service.UisTestService;
import cn.hutool.core.date.*;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;


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
    public long insert(Date startDate, Date entDate, Integer dayNum) {
        long between = DateUtil.between(startDate, entDate, DateUnit.DAY);
        if(between > 0) {
            for(int i=0; i <= between; i++) {
                List<UisTest> uisTests = Lists.newArrayList(getInsertInd(startDate), getInsertInd(new Date()));
                Date date = DateUtils.addDays(startDate, i);
                Integer size = 0;
                for(int j = 0; j < dayNum; j++) {
                    uisTests.add(getInsertInd(date));
                    if((j!=0 && j%5000==0) || j == dayNum-1){
                        uisTests = uisTests.stream().sorted(Comparator.comparing(UisTest::getIndicatorType)).collect(Collectors.toList());
                        size += uisTests.size();
                        log.info("size:[{}]", uisTests.size());
                        uisTestMapper.insertBath(uisTests);
                        uisTests.clear();
                    }
                }
                log.info("insert date:[{}],size:[{}]", DateUtil.format(date, DatePattern.NORM_DATE_PATTERN),size);
            }
        }

        return between+1 * dayNum;
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

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement ps = null;
//        ResultSet rs = null;
//            Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
//            Connection connection = DriverManager.getConnection("jdbc:clickhouse://192.168.33.5:8123/credit_ind");
//            String sql = "insert into  uid_ind_test(object_pk, order_id, biz_code,biz_channel, indicator_ao, id_number," +
//                    "user_name, phone, trans_time,workflow_code, workflow_name, biz_name,biz_channel_name, label, indicator_type," +
//                    "indicator_name, create_time, update_time,indicator_value) values " +
//                    "    (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        ps = conn.prepareStatement(sql);
            //优化插入第一步设置手动提交
    }

}
