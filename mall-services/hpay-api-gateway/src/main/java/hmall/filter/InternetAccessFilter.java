package hmall.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 由于此API网关后端的服务部分只允许内网访问，此过滤器根据请求的uri和请求来源过滤访问权限。
 * 阿里云上外网的Nginx会在header中加入from-internet=true，根据头信息判断如果是外网的代理，则只允许访问指定的uri
 *
 * @author alaowan
 *         Created at 2017/8/22 16:19
 * @description
 */
public class InternetAccessFilter extends ZuulFilter {

    private Set<String> permittedURI;

    public InternetAccessFilter() {
        Set<String> uris = new HashSet<>();
        uris.add("/hpay/v1/return/alipayReturn");
        uris.add("/hpay/v1/return/wechatpayReturn");
        uris.add("/hpay/v1/return/unionpayReturn");
        uris.add("/thirdParty/v1/thirdPartyQQ");
        uris.add("/thirdParty/v1/thirdPartyWechat");
        uris.add("/hmall-logistics-service/i/consignment/rrsOrderHfs");
        permittedURI = Collections.synchronizedSet(uris);
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        return request.getHeader("from-internet") != null;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if (!permittedURI.contains(request.getRequestURI())) {
            ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
            ctx.setResponseStatusCode(401);// 返回错误码
            ctx.setResponseBody("{\"result\":\"No permission!\"}");// 返回错误内容
            ctx.set("isSuccess", false);
        }
        return null;
    }
}
