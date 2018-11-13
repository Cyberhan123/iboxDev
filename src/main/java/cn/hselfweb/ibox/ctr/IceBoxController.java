package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.FamilyRepository;
import cn.hselfweb.ibox.db.IceBox;
import cn.hselfweb.ibox.db.IceBoxRepository;
import cn.hselfweb.ibox.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
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
}
