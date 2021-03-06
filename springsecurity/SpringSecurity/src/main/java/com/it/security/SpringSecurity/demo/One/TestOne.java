package com.it.security.SpringSecurity.demo.One;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security是如何完成身份认证的？
 * 1 用户名和密码被过滤器获取到，封装成 Authentication,通常情况下是 UsernamePasswordAuthenticationToken这个实现类。
 * <p>
 * 2 AuthenticationManager 身份管理器负责验证这个 Authentication
 * <p>
 * 3 认证成功后， AuthenticationManager身份管理器返回一个被填充满了信息的（包括上面提到的权限信息，身份信息，细节信息，但密码通常会被移除） Authentication实例。
 * <p>
 * 4 SecurityContextHolder安全上下文容器将第3步填充了信息的 Authentication，通过SecurityContextHolder.getContext().setAuthentication(…)方法，设置到其中。
 *
 * @author fengqigui, 简单的例子
 * @Date 2018/1/5 11:41
 */
public class TestOne {

    private static AuthenticationManager am = new SampleAuthenticationManager();

    public static void main(String[] arg) throws Exception {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Please enter your username:");
            String name = in.readLine();

            System .out. println( "Please enter your password:");

            String password =in.readLine();

            try{
                Authentication request = new UsernamePasswordAuthenticationToken( name , password);

                Authentication result = am .authenticate( request);

                SecurityContextHolder.getContext().setAuthentication(result);

                break;

            } catch(AuthenticationException e){

                System.out.println("Authentication failed: "+ e.getMessage());

            }

        }

        System.out.println("Successfully authenticated. Security context contains: "+ SecurityContextHolder.getContext().getAuthentication());
    }
}

class SampleAuthenticationManager implements AuthenticationManager{
    static final List<GrantedAuthority> AUTHORITIES=new ArrayList<GrantedAuthority>();

    static{
        AUTHORITIES.add(new SimpleGrantedAuthority( "ROLE_USER"));
    }

    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        if(auth .getName().equals(auth.getCredentials())){

            return new UsernamePasswordAuthenticationToken( auth.getName(), auth.getCredentials(),AUTHORITIES);

        }

        throw new BadCredentialsException("Bad Credentials");
    }

}
