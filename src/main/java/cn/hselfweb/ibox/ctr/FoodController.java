package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.FoodInfo;
import cn.hselfweb.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 食物管理
 */
@RestController
public class FoodController {


    private final FoodRepository foodRepository;


    private final RecordRepository recordRepository;


    private final OfficialCardRepository officialCardRepository;


    private final UnOfficialCardRepository unOfficialCardRepository;

    private final IceBoxRepository iceBoxRepository;


    @Autowired
    public FoodController(FoodRepository foodRepository, RecordRepository recordRepository, OfficialCardRepository officialCardRepository, UnOfficialCardRepository unOfficialCardRepository, IceBoxRepository iceBoxRepository) {
        this.foodRepository = foodRepository;
        this.officialCardRepository = officialCardRepository;
        this.recordRepository = recordRepository;
        this.unOfficialCardRepository = unOfficialCardRepository;
        this.iceBoxRepository = iceBoxRepository;
    }

    private static List<Record> removeDuplicateOrder(List<Record> orderList) {
        Set<Record> set = new TreeSet<Record>(Comparator.comparing(Record::getUuid));
        set.addAll(orderList);
        return new ArrayList<>(set);
    }

    /**
     * 获取冰箱食物列表信息
     * @param macip 冰箱唯一标识
     * @return 食物信息列表
     */
    @RequestMapping(value = "/foods/getallfoodlist", params={"macip"},method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getAllFoodlist(String macip) {

        HashMap<String,Object> map = new HashMap<>();
        System.out.println("已进入获取食物列表");
        List<FoodInfo> foodInfoList = new ArrayList<>();
        List<Record> recordList = recordRepository.findAllByIceId(macip);
        for (Record data : recordList) {
            System.out.println(data.getUuid());
        }
        recordList = removeDuplicateOrder(recordList);

        for (int i = 0; i < recordList.size(); i++) {
            String uuid = recordList.get(i).getUuid();
            System.out.println("第 uuid" + i + recordList.get(i).getUuid());
            List<OfficialCard> officialCard = officialCardRepository.getOfficialCardByUuidIs(uuid);
            if (officialCard.size() == 0) {
                System.out.println("官方卡里没有找到");
                UnOfficialCard unOfficialCard = unOfficialCardRepository.getByUuid(uuid);
                System.out.println(unOfficialCard.getFoodWeight());
                FoodInfo foodInfo = new FoodInfo();
                if (unOfficialCard.getFoodWeight() > 0) {
                    foodInfo.setWeight(unOfficialCard.getFoodWeight());
                    foodInfo.setFoodName(unOfficialCard.getFoodName());
                    foodInfo.setFoodUrl(unOfficialCard.getFoodUrl());
                    foodInfo.setType(unOfficialCard.getType());
                    foodInfo.setTime(unOfficialCard.getFoodTime());
                    foodInfo.setPercent(unOfficialCard.getPercent());
                    List<Record> records = recordRepository.findByUuidOrderByOpDateDesc(uuid);
                    Date OpDate = records.get(0).getOpDate();
                    foodInfo.setStartTime(OpDate);
                    foodInfo.setFoodPhotoUrl(records.get(0).getFoodPhoto());
                    foodInfoList.add(foodInfo);
                }

            } else {
                //System.out.println(officialCard.toString());
                Long foodId = officialCard.get(0).getFoodId();
                System.out.println("第 foodid" + i + foodId);
                FoodInfo foodInfo = new FoodInfo();
                Food food = foodRepository.getAllByFoodId(foodId);
                if (food.getFoodWeight() == 0) {
                    officialCard.remove(i);
                } else {
                    foodInfo.setWeight(food.getFoodWeight());//食材重量
                    foodInfo.setFoodName(food.getFoodName());//食材二级分类
                    foodInfo.setFoodUrl(food.getFoodUrl());//二级分类图标
                    Long day = food.getFoodTime();//用于计算保质期
                    //foodInfo.setComment(food.getComment());//食材描述
                    foodInfo.setType(food.getType());//存储方式
                    foodInfo.setTime(food.getFoodTime());
                    foodInfo.setPercent((food.getPercent()));
                    List<Record> records = recordRepository.findByUuidOrderByOpDateDesc(uuid);
                    Date OpDate = records.get(0).getOpDate();
                    foodInfo.setStartTime(OpDate);
                    foodInfo.setFoodPhotoUrl(records.get(0).getFoodPhoto());
                    foodInfoList.add(foodInfo);
                }
            }
        }
        if(foodInfoList == null){
            map.put("code",0);
            map.put("msg","冰箱没有食材");
        }else{
            map.put("code",1);
            map.put("msg","食材获取成功");
        }
        map.put("data",foodInfoList);
        return map;
    }

    /**
     * food record 双表存入及更新接口
     *
     * @param macip       就是冰箱的ice_id
     * @param foodName    食物名称
     * @param uuid        官方卡或非官方卡的索引ID
     * @param comment     介绍
     * @param foodTime    食物存储时间
     * @param type        当前的操作是冷冻还是冷藏
     * @param opFlag      是首次存入还是续存
     * @param opDate      存入食物的时间
     * @param foodParent  食物的上一级父亲
     * @param foodPhoto   食物的照片链接
     * @param foodWeight  食物的重量
     * @param foodPercent 食物距离过期的百分比
     * @param taretWeight 皮重
     * @return 返回操作成功或者失败
     */
    @RequestMapping(value = "/foods/putFoodDataIn", method = RequestMethod.POST)
    @ResponseBody
    public List<String> putFoodData(
            String macip,
            String foodName,
            String uuid,
            String comment,
            Long foodTime,
            Long type,
            Long opFlag,
            Date opDate,
            Long foodParent,
            String foodPhoto,
            Long foodWeight,
            double foodPercent,
            Long taretWeight
    ) {
        List<String> message = new ArrayList<>();
        Long foodId = (Calendar.getInstance().getTimeInMillis());
        Food food = new Food();
        IceBox iceBox = iceBoxRepository.getIceBoxByIceId(macip);
        OfficialCard officialCard = new OfficialCard();
        UnOfficialCard unOfficialCard = new UnOfficialCard();
        Record record = new Record();
        food.setFoodId(foodId);
        food.setFoodName(foodName);
        food.setComment(comment);
        food.setFoodTime(foodTime);
        food.setFoodUrl(foodPhoto);
        food.setFoodWeight(foodWeight);
        food.setType(type);
        food.setPercent(foodPercent);
        food.setFoodParent(foodParent);
        if (officialCardRepository.existsByUuid(uuid)) {
            officialCard.setFoodId(foodId);
            officialCard.setUuid(uuid);
            officialCardRepository.save(officialCard);
            foodRepository.save(food);
        } else {
            unOfficialCard.setUuid(uuid);
            unOfficialCard.setFid(iceBox.getFid());
            unOfficialCard.setFoodUrl(foodPhoto);
            unOfficialCard.setType(type);
            unOfficialCard.setFoodTime(foodTime);
            unOfficialCard.setFoodName(foodName);
            unOfficialCard.setFoodWeight(foodWeight);
            unOfficialCard.setPercent(foodPercent);
            unOfficialCardRepository.save(unOfficialCard);
        }
        record.setUuid(uuid);
        record.setIceId(macip);
        record.setFid(iceBox.getFid());
        record.setOpFlag(opFlag);
        record.setOpDate(opDate);
        record.setTareWeight(taretWeight);
        record.setFoodWeight(foodWeight);
        record.setFoodPhoto(foodPhoto);
        recordRepository.save(record);
        message.add("success");
        return message;
    }

}
