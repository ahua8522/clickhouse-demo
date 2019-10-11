package cn.com.clickhouse.mapper;

import cn.com.clickhouse.pojo.UisTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: wanqh
 * @date: created in 2019/10/9 20:52
 * @version:
 * @since:
 */
public interface UisTestMapper {
    int selectCount();

    void insertJson(@Param("jsonString")String jsonString);

    void insertBath(@Param("indList") List<UisTest> indList);
    /*-- 计算数值类型的最大最小值
select max(indicator_value), min(indicator_value) from uid_ind_test where indicator_type='N';
select max(indicator_value), min(indicator_value) from uid_ind_test where indicator_type='T';
-- 数值类型的区间处理

-- 时间类型

-- 统计Boolean类型
select
       a.indicator_value as "indicatorValue",
       sum(a.cnt) as "cnt",
       sum(a.emptytag) as "emptytag",
       sum(a.goodtag) as "goodtag",
       sum(a.badtag) as "badtag"
from (
    select indicator_value,1 cnt,
           case when label=0 then 1 else 0 end goodtag,
           case when label=1 then 1 else 0 end badtag,
           case when label is null then 1 else 0 end emptytag
    from uid_ind_test where indicator_type='B'
    )a group by a.indicator_value;

-- 统计字符型
select
       a.indicator_value as "indicatorValue",
       sum(a.cnt) as "cnt",
       sum(a.emptytag) as "emptytag",
       sum(a.goodtag) as "goodtag",
       sum(a.badtag) as "badtag"
from (
    select indicator_value,1 cnt,
           case when label=0 then 1 else 0 end goodtag,
           case when label=1 then 1 else 0 end badtag,
           case when label is null then 1 else 0 end emptytag
    from uid_ind_test where indicator_type='C'
    )a group by a.indicator_value;
--

*/
}
