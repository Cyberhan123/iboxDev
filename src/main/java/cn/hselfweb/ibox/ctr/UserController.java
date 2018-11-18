package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.IceOrder;
import cn.hselfweb.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
    ResponseEntity<?> getIceBoxUserInfo(
            @PathVariable("macip") String macip
    ) {
        System.out.println("helloworld");
        List<IceOrder> iceOrders = new ArrayList<IceOrder>();
        IceBox iceBox = iceBoxRepository.getIceBoxByIceId(macip);
        Long fid = iceBox.getFid();
        Family family = familyRepository.getOne(fid);
        Long uid = family.getUid();
        System.out.println("管理员uid:" + uid);
        List<User> users = userRepository.findAllByFid(fid);
        System.out.println(users.get(0).getUid());
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            user.setPassword("*****");
            IceOrder iceOrder = new IceOrder();
            iceOrder.setUser(user);
            System.out.println(user.getUid());
            if (user.getUid().equals(uid)) {
                iceOrder.setAdmin(0);
                System.out.println("***");
            } else {
                System.out.println("...");
                iceOrder.setAdmin(1);
            }
            iceOrders.add(iceOrder);
        }
        Resources<IceOrder> resources = new Resources<>(iceOrders);
        resources.add(linkTo(methodOn(UserController.class).getIceBoxUserInfo(macip)).withSelfRel());
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(value = "users/putheadurl/{headurl}", method = RequestMethod.GET)
    public @ResponseBody
    Map<String,String> saveHeadUrl(
            @PathVariable("headurl") String headUrl,
            HttpServletRequest request
    ){
        Map<String,String> respon = new HashMap<>();
        HttpSession session =  request.getSession();
        Long uid =  (Long)session.getAttribute("user");
        User user = userRepository.findByUid(uid);
        user.setHeadUrl(headUrl);
        User user0 = userRepository.save(user);
        if(user0.getHeadUrl() == headUrl){
            respon.put("msg","succeed");
        }else{
            respon.put("msg","failed");
        }
        return respon;
    }


}
