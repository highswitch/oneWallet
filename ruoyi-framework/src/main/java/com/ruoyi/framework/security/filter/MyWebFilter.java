package com.ruoyi.framework.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.filter.RepeatedlyRequestWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * token过滤器 验证token有效性
 * 
 * @author ruoyi
 */
@Component
public class MyWebFilter extends OncePerRequestFilter
{
    private Logger logger = LoggerFactory.getLogger(getClass());

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        logger.info("MyWebFilter");
        String mod = request.getMethod();
        String nowParams = "";
        ServletRequest requestWrapper = null;
        logger.info("method:" + mod);

        if (request instanceof HttpServletRequest
                && StringUtils.equalsAnyIgnoreCase(mod, "POST"))
        {
            requestWrapper = new RepeatedlyRequestWrapper(request, response);
            nowParams = HttpHelper.getBodyString(requestWrapper);
        }

        // body参数为空，获取Parameter的数据
        if (StringUtils.isEmpty(nowParams))
        {
            nowParams = JSONObject.toJSONString(request.getParameterMap());
        }
        String url = request.getRequestURI();
        String token = request.getHeader(header);
        if (StringUtils.isNotBlank(token)){
            token = "not null";
        } else {
            token = "is null";
        }
        String time = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss.SSS", new Date());
        String ip = ServletUtils.getRequestIP(request);
        String ret = String.format(getActionInfo(), time, mod, url, ip, nowParams, token);
        System.out.println(ret);
        if (null == requestWrapper)
        {
            chain.doFilter(request, response);
        }
        else
        {
            chain.doFilter(requestWrapper, response);
        }
    }
    private String getActionInfo(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("---------------------- %s ----------------------");
        stringBuffer.append("\r\n");
        stringBuffer.append("Url         : %s %s");
        stringBuffer.append("\r\n");
        stringBuffer.append("IP          : %s");
        stringBuffer.append("\r\n");
        stringBuffer.append("Parameter   : %s");
        stringBuffer.append("\r\n");
        stringBuffer.append("Token       : %s");
        stringBuffer.append("\r\n");
        stringBuffer.append("---------------------------------------------------------------------");
        return stringBuffer.toString();
    }
}
