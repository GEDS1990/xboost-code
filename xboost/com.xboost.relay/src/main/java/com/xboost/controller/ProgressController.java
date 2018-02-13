package com.xboost.controller;

import com.xboost.dto.Message;
import com.xboost.pojo.Customer;
import com.xboost.pojo.Progress;
import com.xboost.pojo.User;
import com.xboost.service.CustomerService;
import com.xboost.service.ProgressService;
import com.xboost.service.UserService;
import com.xboost.util.Page;
import com.xboost.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/progress")
public class ProgressController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProgressService progressService;

    /**
     * 跟进首页
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(required = false,defaultValue = "") String userid,@RequestParam(required = false,defaultValue = "") String progress,
                       @RequestParam(required = false,defaultValue = "") String date,@RequestParam(required = false,defaultValue = "")String context,
                       @RequestParam(required = false,defaultValue = "1") String p,
                       Model model) {
        progress = Strings.toUTF8(progress);
        date = Strings.toUTF8(date);
        context = Strings.toUTF8(context);

        List<User> userList = userService.findAllUser();
        List<Customer> customerList = customerService.findCustomerByCurrentUser();


        Page<Progress> page = progressService.findProgressByPageAndParam(userid,progress ,date,context,p);

        model.addAttribute("userList",userList);
        model.addAttribute("page",page);
        model.addAttribute("customerList",customerList);

        model.addAttribute("userid",userid);
        model.addAttribute("progress",progress);
        model.addAttribute("date",date);
        model.addAttribute("context",context);
        return "progress/list";
    }

    /**
     * 保存新跟进记录
     */
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public String save(Progress progress,@RequestParam MultipartFile[] file, RedirectAttributes redirectAttributes) {
        progressService.saveNewProgress(progress,file);

        redirectAttributes.addFlashAttribute("message",new Message(Message.SUCCESS,"添加成功"));
        return "redirect:/progress";
    }
}
