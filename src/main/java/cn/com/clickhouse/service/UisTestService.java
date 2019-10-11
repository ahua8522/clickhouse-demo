package cn.com.clickhouse.service;

import javax.xml.crypto.Data;
import java.util.Date;

public interface UisTestService {

    void loadIndData() throws Exception;

    int count();

    void insert(Date startDate, Date entDate, Integer dayNum);
}
