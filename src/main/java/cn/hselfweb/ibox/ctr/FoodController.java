package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.FoodInfo;
import cn.hselfweb.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
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

    @RequestMapping(value = "/foods/getallfoodlist/{macip}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getAllFoodlist(@PathVariable("macip") String macip) {

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
                    foodInfo.setComment(food.getComment());//食材描述
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
        Resources<FoodInfo> resources = new Resources<>(foodInfoList);
        resources.add(linkTo(methodOn(FoodController.class).getAllFoodlist(macip)).withSelfRel());
        return ResponseEntity.ok(resources);
    }

    private static List<Record> removeDuplicateOrder(List<Record> orderList) {
        Set<Record> set = new TreeSet<Record>(new Comparator<Record>() {
            @Override
            public int compare(Record a, Record b) {
                // 字符串则按照asicc码升序排列
                return a.getUuid().compareTo(b.getUuid());
            }
        });
        set.addAll(orderList);
        return new ArrayList<Record>(set);
    }

    /**
     * food record 双表存入及更新接口
     *
     * @param vision      接口版本 当前为v1
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
    @RequestMapping(value = "/foods/putFoodDataIn/{vision}/{macip}/{foodName}/{UUId}/{comment}/{foodTime}/{type}/{opFlag}/{opDate}/{foodphoto}/{foodWeight}/{foodParent}/{foodPercent}/{taretWeight}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> putFoodData(
            @PathVariable("vision") String vision,
            @PathVariable("macip") String macip,
            @PathVariable("foodName") String foodName,
            @PathVariable("UUId") String uuid,
            @PathVariable("comment") String comment,
            @PathVariable("foodTime") Long foodTime,
            @PathVariable("type") Long type,
            @PathVariable("opFlag") Long opFlag,
            @PathVariable("opDate") Date opDate,
            @PathVariable("foodParent") Long foodParent,
            @PathVariable("foodphoto") String foodPhoto,
            @PathVariable("foodWeight") Long foodWeight,
            @PathVariable("foodPercent") double foodPercent,
            @PathVariable("taretWeight") Long taretWeight
    ) {
        List<String> message = new ArrayList<>();
        Long foodId = (Calendar.getInstance().getTimeInMillis());
        Food food = new Food();
        IceBox iceBox = iceBoxRepository.getIceBoxByIceId(macip);
        OfficialCard officialCard = new OfficialCard();
        UnOfficialCard unOfficialCard = new UnOfficialCard();
        Record record = new Record();
        if (vision.equals("v1")) {
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
            Resources<String> resources = new Resources<String>(message);
            resources.add(linkTo(methodOn(FoodController.class).putFoodData(vision, macip, foodName, uuid, comment, foodTime, type, opFlag, opDate, foodParent, foodPhoto, foodWeight, foodPercent, taretWeight)).withSelfRel());
            return ResponseEntity.ok(resources);
        } else {
            message.add("unsuccess");
            Resources<String> resources = new Resources<String>(message);
            resources.add(linkTo(methodOn(FoodController.class).putFoodData(vision, macip, foodName, uuid, comment, foodTime, type, opFlag, opDate, foodParent, foodPhoto, foodWeight, foodPercent, taretWeight)).withSelfRel());
            return ResponseEntity.ok(resources);
        }
    }
}
