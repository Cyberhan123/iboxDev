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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RepositoryRestController
public class LoginController {

    private final UserRepository userRepository;

    private final ValidationRepository validationRepository;

    @Autowired
    public LoginController(UserRepository userRepository,ValidationRepository validationRepository) {
        this.validationRepository = validationRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/login/{tel}{password}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> login(@PathVariable("tel") String tel,
                                     @PathVariable("password") String password,
                                     HttpSession session0,
                                     HttpServletRequest request) {
        System.out.println(session0);
        System.out.println(request);
        System.out.println("heelo");
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userRepository.getByTel(tel);
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        User user0 = userRepository.getByTelAndPassword(tel, password);
        if (user0 == null) {
            map.put("msg", "用户名或密码错误");
            return map;
        }else{
            HttpSession session = request.getSession();
            session.setAttribute("user",user0.getUid());
            map.put("msg","登录成功");
        }
        return map;
    }


    @RequestMapping(value = "validations/getiden/{tel}", method = RequestMethod.GET)
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
            SendSmsResponse sendSmsResponse = AliyunMessageUtil.sendSms(paramMap);
            System.out.println("状态码是： " + sendSmsResponse.getCode());
            if(sendSmsResponse.getCode().equals("OK")){
                System.out.println("状态码ok");
                Date now = new Date();

                Date dueDate = new Date(now.getTime()+ 300000);
                Validation validation = new Validation();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("过期时间");
                String dd = sdf.format(dueDate);
                System.out.println(dd);

                validation.setIdentity(randomNum);
                validation.setTel(tel);
                validation.setDuedate(dueDate);
                validationRepository.save(validation);
                respon.put("msg","succeed");
            }
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


    @RequestMapping(value = "/register/{tel}{password}{iden}{message}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> register(String tel, String password, String message, HttpServletRequest request) {

        Map<String, Object> map = new HashMap<String, Object>();

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

}
