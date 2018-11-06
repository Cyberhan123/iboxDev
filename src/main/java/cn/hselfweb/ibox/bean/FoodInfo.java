package cn.hselfweb.ibox.ibox.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class FoodInfo {
    private String foodName;
    private String foodUrl;
    private String parentFoodUrl;
    private String parentFoodNmae;
    private Long weight;
    private Date startTime;//存入日期
    private Long type;//存储方式
}
