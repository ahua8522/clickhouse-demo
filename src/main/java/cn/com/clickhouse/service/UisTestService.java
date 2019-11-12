package cn.com.clickhouse.service;

import java.util.Date;

public interface UisTestService {

    void loadIndData() throws Exception;

    long count();

    void insert(Date startDate, Date entDate, Integer dayNum);
}
