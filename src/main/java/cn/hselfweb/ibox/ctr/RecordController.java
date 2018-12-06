package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.Record;
import cn.hselfweb.ibox.db.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
/**
 * @author Cyberhan
 *
 * @version v1
 */
@RestController
public class RecordController {
    private final RecordRepository recordRepository;

    @Autowired
    public RecordController(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    /**
     * 取出操作
     * @param version 当前接口版本
     * @param iceId   冰箱的id 就是macip
     * @param uuid    uuid 就是卡牌id
     * @return 是否正确取出
     */
    @RequestMapping(value = "records/putfooddataout/{version}/{macip}/{uuid}", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> putFoodDataOut(
            @PathVariable("version") String version,
            @PathVariable("macip") String iceId,
            @PathVariable("uuid") String uuid
    ) {
        List<String> message = new ArrayList<>();
        if (version.equals("v1")) {
            if (recordRepository.existsByIceIdAndUuid(iceId, uuid)) {
                Record record = recordRepository.findByIceIdAndUuid(iceId, uuid);
                if (!record.getOpFlag().equals((long) 2)) {
                    record.setOpFlag((long) 2);
                    recordRepository.save(record);
                    message.add("success!");
                }
            } else {
                message.add("the food is not exist or it already had been put out!");
            }
            Resources<String> resources = new Resources<>(message);
            resources.add(linkTo(methodOn(RecordController.class).putFoodDataOut(version, iceId, uuid)).withSelfRel());
            return ResponseEntity.ok(resources);
        } else {
            message.add("please input right version for current api");
            Resources<String> resources = new Resources<>(message);
            resources.add(linkTo(methodOn(RecordController.class).putFoodDataOut(version, iceId, uuid)).withSelfRel());
            return ResponseEntity.ok(resources);
        }
    }
}
