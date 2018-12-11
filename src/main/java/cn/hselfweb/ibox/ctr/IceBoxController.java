package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static cn.hselfweb.ibox.ctr.IceBoxController.getRandom;
/**
 * @author Cyberhan
 *
 * @version v1
 */
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
    @RequestMapping(value = "/geticeboxinfo", method = RequestMethod.POST)
    @ResponseBody
    public IceBox query(String macip) {
        return iceBoxRepository.getIceBoxByIceId(macip);
    }

    /**
     * 创建冰箱
     * @param nickName 冰箱昵称
     * @param fid 家庭 如没有家庭创建默认家庭时此参数不用提供
     * @param request userSession
     * @return {code:0/1/2,msg:../../..}
     */
    @RequestMapping(value = "/iceboxes/createicebox", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> createBox(String nickName,
                                        Long fid,
                                        HttpServletRequest request) {
        Map<String,Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long)session.getAttribute("user");
        User user = userRepository.findByUid(uid);
        List<Family> aa = familyRepository.findAllByOrderByFidDesc();
        Long fid0;
        if(aa.size() > 0){
            fid0 = aa.get(0).getFid() + 1;
        }else{
            fid0 = 0L;
        }
        if(fid == null){
            Family family = new Family();
            family.setName(user.getUserName()+"的家庭");
            family.setUid(uid);
            family.setRole(1L);
            family.setFid(fid0);
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
     * @return 返回冰箱实体 iceBox
     */
    @RequestMapping(value = "iceboxes/delect", method = RequestMethod.POST)
    @ResponseBody
    public IceBox delect(String iceId){
        return iceBoxRepository.deleteByIceId(iceId);
    }


    /**
     * 获取我的所有冰箱id
     * @param request 请求session
     * @return {code:1/0,msg:成功/没有冰箱,iceId:仅成功时返回}
     */
    @RequestMapping(value = "iceboxes/getmyiceboxid", method = RequestMethod.GET)
    public @ResponseBody
    Map<String,Object> getBoxId(HttpServletRequest request){
        ArrayList<String> list = new ArrayList<String>();
        Map<String,Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        System.out.println(uid);
        List<Family> families = familyRepository.findAllByUid(uid);
        int length = families.size();
        System.out.println("length长度"+length);
        if(length > 0){
            for(int i = 0; i < length; i++){
                Long fid = families.get(i).getFid();
                System.out.println(("fid为" + fid));
                List<IceBox> iceBoxes = iceBoxRepository.getAllByFid(fid);
                for(int j = 0; j < iceBoxes.size(); j++){
                    IceBox iceBox = iceBoxes.get(j);
                    String iceId = iceBox.getIceId();
                    list.add(iceId);
                }
            }
            respon.put("code",1);
            respon.put("iceId",list);
            respon.put("msg","获取冰箱Id成功");
        }
        else{
            respon.put("code",0);
            respon.put("msg","还没有冰箱");
        }
        return respon;
    }
}
