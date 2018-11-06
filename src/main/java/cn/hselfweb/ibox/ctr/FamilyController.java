package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.Family;
import cn.hselfweb.ibox.db.FamilyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Family")
public class FamilyController {
    @Autowired
    private FamilyRepository familyRepository;

    @RequestMapping("/queryall")
    @ResponseBody
    public List<Family> queryAll() {
        List<Family> families = familyRepository.findAll();
        return families;
    }
}
