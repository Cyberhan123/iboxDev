package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.*;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/icebox")
public class IceBoxController {

    private final IceBoxRepository iceBoxRepository;

    private final UserRepository userRepository;

    @Autowired
    public IceBoxController(IceBoxRepository iceBoxRepository,UserRepository userRepository){
        this.iceBoxRepository = iceBoxRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/query",method = RequestMethod.GET)
    @ResponseBody
    public IceBox query(String macip){
        return  iceBoxRepository.getIceBoxByIceId(macip);
    }

    @RequestMapping(value ="/geticeboxinfo/" ,method= RequestMethod.GET)
    @ResponseBody
    public String getIceBoxInfo(@PathVariable("macip") String macip){

        IceBox iceBox = iceBoxRepository.getIceBoxByIceId(macip);
        Long fid = iceBox.getFid();

        return "success";
    }

}
