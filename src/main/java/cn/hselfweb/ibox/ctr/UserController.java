package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.IceOrder;
import cn.hselfweb.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class UserController {
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final IceBoxRepository iceBoxRepository;

    @Autowired
    public UserController(FamilyRepository familyRepository, UserRepository userRepository, IceBoxRepository iceBoxRepository) {
        this.iceBoxRepository = iceBoxRepository;
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }

    @RequestMapping(value = "users/geticeboxuserinfo/{macip}", method = RequestMethod.GET)
    public @ResponseBody
    List<IceOrder>  getIceBoxUserInfo(
            @PathVariable("macip") String macip
    ) {
        System.out.println("helloworld");
        List<IceOrder> iceOrders = new ArrayList<IceOrder>();
        IceBox iceBox = iceBoxRepository.getIceBoxByIceId(macip);
        Long fid = iceBox.getFid();
        List<Family> families = familyRepository.findAllByFid(fid);
        for(int i = 0; i < families.size(); i++){
            Family family = families.get(i);
            Long uid = family.getUid();
            Long admin = family.getRole();
            User user = userRepository.findByUid(uid);
            IceOrder iceOrder = new IceOrder();
            iceOrder.setUser(user);
            iceOrder.setAdmin(admin);
            iceOrders.add(iceOrder);
        }
        return iceOrders;
    }



    @RequestMapping(value = "users/putheadurl",params={"headUrl"}, method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> saveHeadUrl(
            String headUrl,
            HttpServletRequest request
    ) {
        System.out.println(headUrl);
        Map<String,Object> respon = new HashMap<>();
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        User user = userRepository.findByUid(uid);
        user.setHeadUrl(headUrl);
        User user0 = userRepository.save(user);
        if (user0.getHeadUrl() == headUrl) {
            respon.put("code",1);
            respon.put("msg", "succeed");
        } else {
            respon.put("code",0);
            respon.put("msg", "failed");
        }
        return respon;
    }

    /**
     *  获取当前用户信息
     * @param request
     * @return
     */
    @RequestMapping(value = "users/getUserInfo", method = RequestMethod.GET)
    public @ResponseBody
    User getUserInfo(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Long uid = (Long) session.getAttribute("user");
        return userRepository.findByUid(uid);
    }
}
