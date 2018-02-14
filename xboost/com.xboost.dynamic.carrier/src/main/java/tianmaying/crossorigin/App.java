package tianmaying.crossorigin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App  implements Filter {

    public static void main(String[] args) throws UnknownHostException {
        String ip="";
     String address="";
     
//     InetAddress addr = InetAddress.getLocalHost();
//     ip=addr.getHostAddress()+"";//获得本机IP
//     address=addr.getHostName()+"";//获得本机名称
//     System.out.println( address+"---cow  boy--"+ip);
   //输出服务的一些提示信息到控制台
   System.out.println("RESTful WebService服务已经启动");
   System.out.println("服务访问地址: http://" + ip + ":8383/helloworld");
        SpringApplication.run(App.class, args);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String origin = (String) servletRequest.getRemoteHost()+":"+servletRequest.getRemotePort();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
        response.setHeader("Access-Control-Allow-Credentials","true");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}