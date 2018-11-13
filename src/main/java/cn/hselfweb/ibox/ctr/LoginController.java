package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.User;
import cn.hselfweb.ibox.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RepositoryRestController
public class LoginController {

    private final UserRepository userRepository;

    @Autowired
    public LoginController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/login/{tel}{password}",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> login(String tel, String password, HttpServletRequest request){

        Map<String,Object> map = new HashMap<String,Object>();
        User user = userRepository.getByTel(tel);
        if(user == null){
            map.put("msg","用户名不存在");
            return map;
        }

        User user0 = userRepository.getByTelAndPassword(tel,password);
        if(user0 == null){
            map.put("msg","用户名或密码错误");
            return map;
        }

        String token = addLoginToken(user.getUid());
        return map;
    }

    public String addLoginToken(Long uid){
        return "";
    }
}
