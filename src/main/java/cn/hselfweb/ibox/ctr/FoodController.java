package cn.hselfweb.ibox.ibox.ctr;

import cn.hselfweb.ibox.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Food")
public class FoodController {

    @Autowired
    private FamilyRepository foodRepository;

    @Autowired
    private IceBoxRepository iceBoxRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private OfficialCardRepository officialCardRepository;

    @RequestMapping("/getallfoodlist")
    @ResponseBody
    public String getAllFoodlist(String macip){
        List<Record> recordList = recordRepository.findAllByIceIdAndOpFlag(macip,0);
        for(int i = 0; i < recordList.size(); i++){
            Long uuid = recordList.get(i).getUuid();
            Optional<OfficialCard> = officialCardRepository.findById(uuid);
        }

        return
    }

    @RequestMapping("/",method = RequestMethod.POST){

    }

}
