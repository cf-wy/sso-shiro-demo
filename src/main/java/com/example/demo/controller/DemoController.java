package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class DemoController {


    @GetMapping("authenticated")
    @ResponseBody
    public String authenticated() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return subject.getPrincipal().toString();
        } else {
            return null;
        }
    }


    @GetMapping("role")
    @ResponseBody
    @RequiresRoles("roleAudit")
    public String role() {
        /*Subject subject = SecurityUtils.getSubject();
        try {

            subject.checkRole("bg_audit");
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            return "无bg_audit角色";
        }*/
        return "有audit角色";
    }

    @GetMapping("demoDelete")
    @ResponseBody
    @RequiresPermissions("demo_delete")
    public String permission() {
        /*Subject subject = SecurityUtils.getSubject();
        try {
            subject.checkPermission("audit_button");
        } catch (UnauthorizedException e) {
            log.error(e.getMessage(), e);
            return "无audit_button创建权限";
        }*/
        return "有权限";
    }
    @GetMapping("demoList")
    @ResponseBody
    @RequiresPermissions("demo_list")
    public String demoList() {
        /*Subject subject = SecurityUtils.getSubject();
        try {
            subject.checkPermission("audit_button");
        } catch (UnauthorizedException e) {
            log.error(e.getMessage(), e);
            return "无audit_button创建权限";
        }*/
        return "有权限";
    }

    @GetMapping("/403")
    public String error() {
        return "403";
    }


}
