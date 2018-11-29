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
import java.util.UUID;

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

    /**
     * 获取冰箱信息
     * @param macip 冰箱唯一标识
     * @return IceBox
     */
    @RequestMapping(value = "/geticeboxinfo/{macip}", method = RequestMethod.GET)
    @ResponseBody
    public IceBox query(@PathVariable("macip") String macip) {
        return iceBoxRepository.getIceBoxByIceId(macip);
    }

    @RequestMapping(value = "/iceboxes/createicebox/{nickname}/{fid}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> createBox(@PathVariable("nickname") String nickName,
                                        @PathVariable("fid") Long fid,
                                        HttpServletRequest request) {
        Map<String,Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long)session.getAttribute("user");
        User user = userRepository.findByUid(uid);
        if(fid == null){
            Family family = new Family();
            family.setName(user.getUserName()+"的家庭");
            family.setUid(uid);
            family.setRole(1L);
            Family family1 = familyRepository.save(family);
            fid = family1.getFid();
            if(fid != null){
                respon.put("fid",fid);
                System.out.println("默认家庭创建成功");
            }else{
                respon.put("code",2);
                respon.put("msg","默认家庭创建失败");
            }
        }
        String iceId = getRandom();
        IceBox iceBox = new IceBox(iceId,fid,nickName);
        IceBox iceBox0 = iceBoxRepository.save(iceBox);
        if(iceBox0.getIceId() != null){
            respon.put("code",1);
            respon.put("msg","冰箱创建成功");
        }else{
            respon.put("code",0);
            respon.put("msg","冰箱创建失败");
        }
        return respon;
    }

    public static String getRandom(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-","");
    }

    /**
     * 删除冰箱
     * @param iceId 冰箱参数
     * @return
     */
    @RequestMapping(value = "iceboxes/delect/{iceId}", method = RequestMethod.GET)
    public @ResponseBody
    IceBox delect(
            @PathVariable("iceID")String iceId
    ){
        return iceBoxRepository.deleteByIceId(iceId);
    }
}
