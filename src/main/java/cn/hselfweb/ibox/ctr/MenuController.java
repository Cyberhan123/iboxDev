package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.Menu;
import cn.hselfweb.ibox.utils.MenuUnit;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MenuController {

    @RequestMapping(value = "menus/{foodName}", method = RequestMethod.GET)
    public @ResponseBody
    List<Menu> select(
            @PathVariable String foodName
    ) {
        MenuUnit menuUnit = new MenuUnit();
        List<Menu> menus = menuUnit.meiShiJieHotDownLoad(foodName);
        return menus;
    }
}
