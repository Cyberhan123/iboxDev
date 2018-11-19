package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.Family;
import cn.hselfweb.ibox.db.FamilyRepository;
import cn.hselfweb.ibox.db.User;
import cn.hselfweb.ibox.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class FamilyController {

    private final FamilyRepository familyRepository;

    private final UserRepository userRepository;


    @Autowired
    public FamilyController(FamilyRepository familyRepository,UserRepository userRepository) {
        this.familyRepository = familyRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/iceboxes/createfamily/{familyname}", method = RequestMethod.GET)
    public @ResponseBody
    Map<String,Object> saveHeadUrl(
            @PathVariable("familyname") String familyName,
            HttpServletRequest request
    ){
        Map<String,Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long)session.getAttribute("user");
        List<Family> families = familyRepository.findAllByUid(uid);
        Family family = new Family();
        if(families.size() == 0){
            family.setName(familyName);
            family.setUid(uid);
            Family family1 = familyRepository.save(family);
            if(family1.getFid() != null){
                respon.put("code",1);
                respon.put("msg","家庭创建成功");
            }else{
                respon.put("code",0);
                respon.put("msg","家庭创建失败");
            }
        }else{
            respon.put("code",2);
            respon.put("msg","你已经创建过家庭了");
        }

        return respon;
    }


}
