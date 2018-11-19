package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.hselfweb.ibox.ctr.IceBoxController.getRandom;

@RestController
public class IceBoxController {

    private final IceBoxRepository iceBoxRepository;

    private final UserRepository userRepository;

    private final FamilyRepository familyRepository;


    @Autowired
    public IceBoxController(IceBoxRepository iceBoxRepository, UserRepository userRepository, FamilyRepository familyRepository) {
        this.iceBoxRepository = iceBoxRepository;
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }

    @RequestMapping(value = "/geticeboxinfo/{macip}", method = RequestMethod.GET)
    @ResponseBody
    public IceBox query(@PathVariable("macip") String macip) {
        return iceBoxRepository.getIceBoxByIceId(macip);
    }

    @RequestMapping(value = "iceboxes/createbox/{nickname}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> createBox(@PathVariable("nickname") String nickName,
                                        HttpServletRequest request) {
        Map<String,Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long)session.getAttribute("user");
        User user = userRepository.findByUid(uid);
        List<Family> families = familyRepository.findAllByUid(uid);
        Family family = new Family();
        Long fid;
        if(families.size() == 0){
            family.setName(user.getUserName()+"的家庭");
            family.setUid(uid);
            Family family1 = familyRepository.save(family);
            fid = family1.getFid();
        }else{
            fid = families.get(0).getFid();
        }
        String iceId = getRandom();
        IceBox iceBox = new IceBox(iceId,fid,nickName);
        return respon;
    }

    private static String getRandom(){

        return "32";
    }
}
