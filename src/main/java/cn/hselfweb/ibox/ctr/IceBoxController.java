package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.IceBox;
import cn.hselfweb.ibox.db.IceBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/icebox")
public class IceBoxController {
    @Autowired
    private IceBoxRepository iceBoxRepository;

    @RequestMapping("/query")
    @ResponseBody
    public IceBox query(String macip){
        return  iceBoxRepository.getIceBoxByIceId(macip);
    }
}
