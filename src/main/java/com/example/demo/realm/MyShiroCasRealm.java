package com.example.demo.realm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.ApiConfigProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Data
@Slf4j
public class MyShiroCasRealm extends CasRealm {

    private ApiConfigProperties apiConfigProperties;

    /**
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("##################执行Shiro权限认证##################");
        //先获取access_token
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("grant_type","client_credentials");
        paramMap.put("scope","read");
        paramMap.put("client_id",apiConfigProperties.getAuthorizationAppKey());
        paramMap.put("client_secret",apiConfigProperties.getAuthorizationAppSecret());
        String resultStr=HttpUtil.post(apiConfigProperties.getUrlPrefix()+apiConfigProperties.getAccessTokenUrl(),paramMap);
        String accessToken= JSON.parseObject(resultStr).getString("access_token");
        //获取权限
        Map<String,String> headMap=new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","Bearer "+accessToken);
        paramMap.clear();
        paramMap.put("psId",apiConfigProperties.getPsId());
        paramMap.put("username",super.getAvailablePrincipal(principalCollection));
        resultStr= HttpRequest.post(apiConfigProperties.getUrlPrefix()+apiConfigProperties.getPermissionsUrl()).addHeaders(headMap).body(JSON.toJSONString(paramMap)).execute().body();
        log.info("resultStr{}",resultStr);
        JSONObject jsonObject=JSON.parseObject(resultStr);
        if(jsonObject.getBoolean("success")){
            JSONObject data=jsonObject.getJSONObject("data");
            if(data==null){
                return null;
            }
            JSONArray rolePermissions=data.getJSONArray("rolePermissions");
            if (CollectionUtil.isEmpty(rolePermissions)){
                return null;
            }
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            Iterator iterator=rolePermissions.iterator();
            while(iterator.hasNext()){
                JSONObject object=(JSONObject) iterator.next();
                info.addRole(object.getString("permissionValue"));
                JSONArray permissions=object.getJSONArray("permissions");
                if(CollectionUtil.isNotEmpty(permissions)){
                    Iterator objectIterator=permissions.iterator();
                    while (objectIterator.hasNext()){
                        JSONObject permission=(JSONObject)objectIterator.next();
                        info.addStringPermission(permission.getString("permissionValue"));
                    }
                }
            }
            log.info(info.getStringPermissions().toString());
            //存入session一份
            Session session= SecurityUtils.getSubject().getSession();
            session.setAttribute("permissions",info.getStringPermissions());
            return info;
        }
        return null;
    }
}
