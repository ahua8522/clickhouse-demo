package cn.com.clickhouse.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UisTest {
    private String orderId;
    private String objectPk;
    private String idNumber;
    private String userName;
    private String phone;
    private Date transTime;
    private String workflowCode;
    private String workflowName;
    private String bizCode;
    private String bizName;
    private String bizChannel;
    private String bizChannelName;
    private Integer label;
    private String indicatorType;
    private String indicatorName;
    private String indicatorAo;
    private String indicatorValue;
    private Date createTime;
    private Date updateTime;
}
