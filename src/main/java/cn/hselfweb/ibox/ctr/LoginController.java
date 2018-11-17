package cn.hselfweb.ibox.ctr;

import cn.hselfweb.ibox.db.User;
import cn.hselfweb.ibox.db.UserRepository;
import cn.hselfweb.ibox.db.Validation;
import cn.hselfweb.ibox.db.ValidationRepository;
import cn.hselfweb.ibox.utils.AliyunMessageUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Timestamp;
import java.util.*;

@RepositoryRestController
public class LoginController {

    private final UserRepository userRepository;

    private final ValidationRepository validationRepository;

    @Autowired
    public LoginController(UserRepository userRepository,ValidationRepository validationRepository) {
        this.validationRepository = validationRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "validations/login/{tel}/{password}", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> login(@PathVariable("tel") String tel,
                                     @PathVariable("password") String password,
                                     //HttpSession session0,
                                     HttpServletResponse response) {
        System.out.println("已进入登录请求");
        System.out.println("heelo");
        Map<String, Object> map = new HashMap<String, Object>();
        List<User> userList = userRepository.getAllByTel(tel);
        User user = userList.get(0);
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }
        User user0 = userRepository.getByTelAndPassword(tel, password);
        if (user0 == null) {
            map.put("msg", "用户名或密码错误");
            return map;
        }else{
            //Cookie cookie = new Cookie();
            //cookie.setValue("user");
            //response.addCookie();
            //HttpSession session = response.
            //session.setAttribute("user",user0.getUid());
            map.put("msg","登录成功");
        }
        return map;
    }


    @RequestMapping(value = "/validation/getiden/{tel}", method = RequestMethod.GET)
    public @ResponseBody Map<String, String> getMessage(@PathVariable("tel") String tel){
        System.out.println("已进入验证码发送请求");
        Map<String,String> respon = new HashMap<>();
        Map<String, String> paramMap = new HashMap<>();
        String randomNum = createRandomNum(6);
        String jsonContent = "{\"code\":\"" + randomNum + "\"}";
        System.out.println(jsonContent);
        paramMap.put("phoneNumber",tel);
        paramMap.put("msgSign","冰箱管家");
        paramMap.put("templateCode", "SMS_151085461");
        paramMap.put("TemplateParam",jsonContent);
        paramMap.put("jsonContent", jsonContent);
        try{
            //SendSmsResponse sendSmsResponse = AliyunMessageUtil.sendSms(paramMap);
            //System.out.println("状态码是： " + sendSmsResponse.getCode());
            //if(sendSmsResponse.getCode().equals("OK")){
                System.out.println("状态码ok");
                Date now = new Date();
                Date dueDate = new Date(now.getTime()+ 300000);
                Validation validation = new Validation();
                validation.setIdentity(randomNum);
                validation.setTel(tel);
                validation.setDuedate(now);
                validationRepository.save(validation);
                respon.put("msg","succeed");
            //}
        }catch(Exception e){
            e.printStackTrace();
            respon.put("msg","failed");
        }
        return respon;
    }

    public static String createRandomNum(int num){
        String randomNumStr = "";
        for(int i = 0; i < num;i ++){
            int randomNum = (int)(Math.random() * 10);
            randomNumStr += randomNum;
        }
        return randomNumStr;
    }


    @RequestMapping(value = "/validations/register/{tel}/{password}/{iden}/{message}/{username}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> register(
            @PathVariable("tel") String tel,
            @PathVariable("password") String password,
            @PathVariable("iden") String iden,
            @PathVariable("iden") String message,
            @PathVariable("username") String username) {

        System.out.println("已经进入注册请求");
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println("电话号码"+tel);
        List<Validation> validationList = validationRepository.findAllByTelOrderByDuedateDesc(tel);
        Validation valiInfo = validationList.get(0);
        if(valiInfo.getIdentity().equals(iden)){
            Date dueDate = validationList.get(0).getDuedate();
            Date now = new Date();
            if(now.compareTo(dueDate) <= 0){
                User user = new User();
                user.setInfo(message);
                user.setTel(tel);
                user.setUserName(username);
                user.setPassword(password);
                userRepository.save(user);
                map.put("msg","注册成功");
            }else{
                map.put("msg","验证码已过期");
            }
        }else{
            map.put("msg","验证码不正确");
        }
        return map;
    }

    @RequestMapping("login/log/{username}/{password}")
    public @ResponseBody String log(
            @PathVariable("username") String username,
            @PathVariable("password") String password
    ){
        HttpHeaders headers=new HttpHeaders();
        return "succsess";
    }
    @RequestMapping("/token")
    public Map<String,String> token(HttpSession session){
        return Collections.singletonMap("token",session.getId());
    }
}
