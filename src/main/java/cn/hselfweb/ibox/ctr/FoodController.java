package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.FoodInfo;
import cn.hselfweb.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class FoodController {


    private final FoodRepository foodRepository;


    private final RecordRepository recordRepository;


    private final OfficialCardRepository officialCardRepository;

    @Autowired
    public FoodController(FoodRepository foodRepository, RecordRepository recordRepository, OfficialCardRepository officialCardRepository) {
        this.foodRepository = foodRepository;
        this.officialCardRepository = officialCardRepository;
        this.recordRepository = recordRepository;
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
        Long b = 12L;

        for (int i = 0; i < recordList.size(); i++) {
            Long uuid = recordList.get(i).getUuid();
            System.out.println("第 uuid" + i + recordList.get(i).getUuid());
            List<OfficialCard> officialCard = officialCardRepository.getOfficialCardByUuidIs(uuid);
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
                foodInfo.setPercent((food.getPercent()));
                List<Record> records = recordRepository.findByUuidOrderByOpDateDesc(uuid);
                Date OpDate = records.get(0).getOpDate();
                foodInfo.setStartTime(OpDate);
                foodInfo.setFoodPhotoUrl(records.get(0).getFoodPhoto());
                foodInfoList.add(foodInfo);
            }
        }
        Resources<FoodInfo> resources = new Resources<>(foodInfoList);
        resources.add(linkTo(methodOn(FoodController.class).getAllFoodlist(macip)).withSelfRel());
        return ResponseEntity.ok(resources);
    }

    public static List<Record> removeDuplicateOrder(List<Record> orderList) {
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

//    @RequestMapping(value = "/foods/putFoodDataIn/{vision}/{macip}/{UUId}/{comment}/{startTime}/{type}/{opFlag}/{foodphoto}/{foodWeight}/{taretWeight}", method = RequestMethod.GET)
//    public @ResponseBody
//    ResponseEntity<?> putFoodData(
//            @PathVariable("vision") String vision,
//            @PathVariable("macip") String macip,
//            @PathVariable("UUId") String uuid,
//            @PathVariable("comment") String comment,
//            @PathVariable("startTime") Long startTime,
//            @PathVariable("type") String type,
//            @PathVariable("opFlag") String opFlag,
//            @PathVariable("foodphoto") Long foodPhoto,
//            @PathVariable("foodWeight") Long foodWeight,
//            @PathVariable("taretWeight") Long taretWeight
//    ) {
//
////        Resources<String> resources=new Resources<String>(message);
////        resources.add(linkTo(methodOn(FoodController.class).putFoodData(vision, macip, uuid, comment, startTime, type, opFlag, foodPhoto, foodWeight, taretWeight)).withSelfRel());
////        return ResponseEntity.ok(resources);
//        return new Resources<String>("123");
//    }
}
