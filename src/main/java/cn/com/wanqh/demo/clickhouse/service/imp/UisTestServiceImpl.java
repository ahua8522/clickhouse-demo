package cn.com.wanqh.demo.clickhouse.service.imp;

import cn.com.wanqh.demo.clickhouse.mapper.UisTestMapper;
import cn.com.wanqh.demo.clickhouse.service.UisTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UisTestServiceImpl implements UisTestService {
    @Autowired
    private UisTestMapper uisTestMapper;
    @Override
    public int count() {
        return uisTestMapper.count();
    }
}
