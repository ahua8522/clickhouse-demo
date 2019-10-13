package cn.com.clickhouse.service;

import cn.com.clickhouse.pojo.UisTest;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

public interface UisTestService {

    void loadIndData() throws Exception;

    int count();

    long insert(Date startDate, Date entDate, Integer dayNum);
}
