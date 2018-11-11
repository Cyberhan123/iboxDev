package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.IceOrder;
import cn.hselfweb.ibox.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class IceBoxController {

    private final IceBoxRepository iceBoxRepository;

    private final UserRepository userRepository;

    private final FamilyRepository familyRepository;


    @Autowired
    public IceBoxController(IceBoxRepository iceBoxRepository,UserRepository userRepository,FamilyRepository familyRepository){
        this.iceBoxRepository = iceBoxRepository;
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }

    @RequestMapping(value = "/query",method = RequestMethod.GET)
    @ResponseBody
    public IceBox query(String macip){
        return  iceBoxRepository.getIceBoxByIceId(macip);
    }

    @RequestMapping(value ="iceBoxes/geticeboxinfo/{macip}" ,method= RequestMethod.GET)
    public @ResponseBody ResponseEntity<?>  getIceBoxInfo(@PathVariable("macip") String macip){

        System.out.println("helloworld");
        List<IceOrder> iceOrders= new ArrayList<IceOrder>();
        IceBox iceBox = iceBoxRepository.getIceBoxByIceId(macip);
        Long fid = iceBox.getFid();
        Family family = familyRepository.getOne(fid);
        Long uid = family.getUid();
        System.out.println("管理员uid:"+uid);
        List<User> users = userRepository.getAllByFid(fid);
        System.out.println(users.get(0).getUid());
        for(int i = 0; i < users.size(); i++){
            User user = users.get(i);
            IceOrder iceOrder = new IceOrder();
            iceOrder.setUser(user);
            System.out.println(user.getUid());
            if(user.getUid().equals(uid)){
                iceOrder.setAdmin(0);
                System.out.println("***");
            }else{
                System.out.println("...");
                iceOrder.setAdmin(1);
            }
            iceOrders.add(iceOrder);
        }
        Resources<IceOrder> resources=new Resources<>(iceOrders);
        resources.add(linkTo(methodOn(IceBoxController.class).getIceBoxInfo(macip)).withSelfRel());
        return ResponseEntity.ok(resources);

    }

}
