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

/**
 * 冰箱管理
 */
@RestController
@RequestMapping
public class FamilyController {

    private final FamilyRepository familyRepository;

    private final UserRepository userRepository;



    @Autowired
    public FamilyController(FamilyRepository familyRepository, UserRepository userRepository) {
        this.familyRepository = familyRepository;
        this.userRepository = userRepository;
    }

    /**
     *创建家庭
     * @param familyName  家庭名称
     * @param request 带有用户信息的cookie
     * @return {code:0/1/2 msg:失败/成功/已创建}
     */
    @RequestMapping(value = "/families/createfamily/{familyname}", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> creatFamily(
            @PathVariable("familyname") String familyName,
            HttpServletRequest request
    ) {
        Map<String, Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        List<Family> families = familyRepository.findAllByUid(uid);
        List<Family> aa = familyRepository.findAllByOrderByFidDesc();
        Long fid;
        if(aa.size() > 0){
            fid = aa.get(0).getFid() + 1;
        }else{
            fid = 0L;
        }
        Family family = new Family();
        if (families.size() == 0) {
            family.setName(familyName);
            family.setUid(uid);
            family.setRole(1L);
            family.setFid(fid);
            Family family1 = familyRepository.save(family);
            if (family1.getFid() != null) {
                respon.put("fid",family.getFid());
                respon.put("code", 1);
                respon.put("msg", "家庭创建成功");
            } else {
                respon.put("code", 0);
                respon.put("msg", "家庭创建失败");
            }
        } else {
            respon.put("code", 2);
            respon.put("msg", "你已经创建过家庭了");
        }
        return respon;
    }

    /**
     * 获取当前用户所在家庭数据
     * @param request uidSession
     * @return List Family
     */
    @RequestMapping(value = "families/getFamilyInfo", method = RequestMethod.GET)
    public @ResponseBody
    List<Family> getFamilyInfo(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        return familyRepository.findAllByUid(uid);
    }

    /**
     * 获取当前用户在哪些家庭中
     * @param request uidSession
     * @return List Family
     */
    @RequestMapping(value = "families", method = RequestMethod.GET)
    public @ResponseBody
    List<Family> getFamilies(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        return familyRepository.findAllByUid(uid);
    }

    /**
     *
     * @param fid     家庭id
     * @param request uidSession
     * @return Object Family
     */

    @RequestMapping(value = "families/outFamily", method = RequestMethod.POST)
    @ResponseBody
    public Family outFamily(
            Long fid,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        return familyRepository.deleteByFidAndUid(fid, uid);
    }

    /**
     * 邀请人加入家庭
     * @param tel     电话
     * @param fid     家庭id
     * @param request uidSession
     * @return Object  Family
     */
    @RequestMapping(value = "families/invitation", method = RequestMethod.POST)
    @ResponseBody
    public Family invitation(
            String tel,
            Long fid,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        User user = userRepository.findByTel(tel);
        Family family = familyRepository.findByFidAndUid(fid, uid);
        family.setName(family.getName());
        family.setUid(user.getUid());
        family.setFid(fid);
        return familyRepository.save(family);
    }
}
