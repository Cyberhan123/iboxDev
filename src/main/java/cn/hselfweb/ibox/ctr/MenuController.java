package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.bean.Menu;
import cn.hselfweb.ibox.utils.MenuUnit;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @author Cyberhan
 *
 * @version v1
 */
@RestController
public class MenuController {
    /**
     * 获取食谱
     * @param foodName 食物名称
     * @return
     */
    @RequestMapping(value = "menus/{foodName}", method = RequestMethod.POST)
    public @ResponseBody
    List<Menu> select(
            @PathVariable String foodName
    ) {
        MenuUnit menuUnit = new MenuUnit();
        List<Menu> menus = menuUnit.meiShiJieHotDownLoad(foodName);
        return menus;
    }
}
