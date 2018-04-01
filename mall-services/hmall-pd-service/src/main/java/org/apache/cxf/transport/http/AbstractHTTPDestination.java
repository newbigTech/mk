//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.apache.cxf.transport.http;

import com.hand.hmall.common.Beans;
import org.apache.cxf.Bus;
import org.apache.cxf.attachment.AttachmentDataSource;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.util.Base64Exception;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.common.util.PropertyUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.configuration.Configurable;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.continuations.ContinuationProvider;
import org.apache.cxf.continuations.SuspendedInvocationException;
import org.apache.cxf.helpers.HttpHeaderHelper;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.io.AbstractWrappedOutputStream;
import org.apache.cxf.io.CopyingOutputStream;
import org.apache.cxf.io.DelegatingInputStream;
import org.apache.cxf.message.*;
import org.apache.cxf.policy.PolicyDataEngine;
import org.apache.cxf.security.SecurityContext;
import org.apache.cxf.security.transport.TLSSessionInfo;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.AbstractMultiplexDestination;
import org.apache.cxf.transport.Assertor;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.http.policy.impl.ServerPolicyCalculator;
import org.apache.cxf.transport.https.CertConstraints;
import org.apache.cxf.transport.https.CertConstraintsInterceptor;
import org.apache.cxf.transports.http.configuration.HTTPServerPolicy;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.ContextUtils;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.EndpointReferenceUtils;

import javax.net.ssl.SSLSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractHTTPDestination extends AbstractMultiplexDestination implements Configurable, Assertor {
    public static final String HTTP_REQUEST = "HTTP.REQUEST";
    public static final String HTTP_RESPONSE = "HTTP.RESPONSE";
    public static final String HTTP_CONTEXT = "HTTP.CONTEXT";
    public static final String HTTP_CONFIG = "HTTP.CONFIG";
    public static final String HTTP_CONTEXT_MATCH_STRATEGY = "HTTP_CONTEXT_MATCH_STRATEGY";
    public static final String RESPONSE_HEADERS_COPIED = "http.headers.copied";
    public static final String RESPONSE_COMMITED = "http.response.done";
    public static final String REQUEST_REDIRECTED = "http.request.redirected";
    public static final String CXF_CONTINUATION_MESSAGE = "cxf.continuation.message";
    public static final String CXF_ASYNC_CONTEXT = "cxf.async.context";
    public static final String SERVICE_REDIRECTION = "http.service.redirection";
    private static final String HTTP_BASE_PATH = "http.base.path";
    private static final String SSL_CIPHER_SUITE_ATTRIBUTE = "javax.servlet.request.cipher_suite";
    private static final String SSL_PEER_CERT_CHAIN_ATTRIBUTE = "javax.servlet.request.X509Certificate";
    private static final String DECODE_BASIC_AUTH_WITH_ISO8859 = "decode.basicauth.with.iso8859";
    private static final Logger LOG = LogUtils.getL7dLogger(org.apache.cxf.transport.http.AbstractHTTPDestination.class);
    protected final Bus bus;
    protected final String path;
    protected DestinationRegistry registry;
    protected volatile HTTPServerPolicy serverPolicy;
    protected String contextMatchStrategy = "stem";
    protected boolean fixedParameterOrder;
    protected boolean multiplexWithAddress;
    protected CertConstraints certConstraints;
    protected boolean isServlet3;
    protected boolean decodeBasicAuthWithIso8859;
    protected ContinuationProviderFactory cproviderFactory;
    protected boolean enableWebSocket;
    private volatile boolean serverPolicyCalced;

    public AbstractHTTPDestination(Bus b, DestinationRegistry registry, EndpointInfo ei, String path, boolean dp) throws IOException {
        super(b, getTargetReference(getAddressValue(ei, dp), b), ei);
        this.bus = b;
        this.registry = registry;
        this.path = path;

        try {
            ServletRequest.class.getMethod("isAsyncSupported", new Class[0]);
            this.isServlet3 = true;
        } catch (Throwable var7) {
            ;
        }

        this.decodeBasicAuthWithIso8859 = PropertyUtils.isTrue(this.bus.getProperty("decode.basicauth.with.iso8859"));
        this.initConfig();
    }

    private static void propogateSecureSession(HttpServletRequest request, Message message) {
        String cipherSuite = (String) request.getAttribute("javax.servlet.request.cipher_suite");
        if (cipherSuite != null) {
            Certificate[] certs = (Certificate[]) ((Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate"));
            message.put(TLSSessionInfo.class, new TLSSessionInfo(cipherSuite, (SSLSession) null, certs));
        }

    }

    protected static EndpointInfo getAddressValue(EndpointInfo ei) {
        return getAddressValue(ei, true);
    }

    protected static EndpointInfo getAddressValue(EndpointInfo ei, boolean dp) {
        if (dp) {
            String eiAddress = ei.getAddress();
            if (eiAddress == null) {
                try {
                    ServerSocket addr1 = new ServerSocket(0);
                    ei.setAddress("http://localhost:" + addr1.getLocalPort());
                    addr1.close();
                    return ei;
                } catch (IOException var4) {
                    ei.setAddress("http://localhost");
                }
            }

            String addr = StringUtils.addDefaultPortIfMissing(ei.getAddress());
            if (addr != null) {
                ei.setAddress(addr);
            }
        }

        return ei;
    }

    public Bus getBus() {
        return this.bus;
    }

    private AuthorizationPolicy getAuthorizationPolicyFromMessage(String credentials, SecurityContext sc) {
        if (credentials != null && !StringUtils.isEmpty(credentials.trim())) {
            List creds = StringUtils.getParts(credentials, " ");
            String authType = (String) creds.get(0);
            if ("Basic".equals(authType) && creds.size() == 2) {
                String policy = (String) creds.get(1);

                try {
                    byte[] authBytes = Base64Utility.decode(policy);
                    String authDecoded = this.decodeBasicAuthWithIso8859 ? new String(authBytes, StandardCharsets.ISO_8859_1) : new String(authBytes);
                    int idx = authDecoded.indexOf(58);
                    String username = null;
                    String password = null;
                    if (idx == -1) {
                        username = authDecoded;
                    } else {
                        username = authDecoded.substring(0, idx);
                        if (idx < authDecoded.length() - 1) {
                            password = authDecoded.substring(idx + 1);
                        }
                    }

                    Object policy1 = sc.getUserPrincipal() == null ? new AuthorizationPolicy() : new org.apache.cxf.transport.http.AbstractHTTPDestination.PrincipalAuthorizationPolicy(sc);
                    ((AuthorizationPolicy) policy1).setUserName(username);
                    ((AuthorizationPolicy) policy1).setPassword(password);
                    ((AuthorizationPolicy) policy1).setAuthorizationType(authType);
                    return (AuthorizationPolicy) policy1;
                } catch (Base64Exception var12) {
                    ;
                }
            }

            if (sc.getUserPrincipal() != null) {
                org.apache.cxf.transport.http.AbstractHTTPDestination.PrincipalAuthorizationPolicy policy2 = new org.apache.cxf.transport.http.AbstractHTTPDestination.PrincipalAuthorizationPolicy(sc);
                policy2.setAuthorization(credentials);
                policy2.setAuthorizationType(authType);
                return policy2;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    protected final boolean isOneWay(Message message) {
        Exchange ex = message.getExchange();
        return ex == null ? false : ex.isOneWay();
    }

    public void invoke(ServletConfig config, ServletContext context, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object inMessage = this.retrieveFromContinuation(req);
        if (inMessage == null) {
            LOG.fine("Create a new message for processing");
            inMessage = new MessageImpl();
            ExchangeImpl ex = new ExchangeImpl();
            ex.setInMessage((Message) inMessage);
            this.setupMessage((Message) inMessage, config, context, req, resp);
            ex.setSession(new HTTPSession(req));
            ((MessageImpl) inMessage).setDestination(this);
        } else {
            LOG.fine("Get the message from the request for processing");
        }

        this.copyKnownRequestAttributes(req, (Message) inMessage);

        try {
            this.incomingObserver.onMessage((Message) inMessage);
            this.invokeComplete(context, req, resp, (Message) inMessage);
        } catch (SuspendedInvocationException var13) {
            if (var13.getRuntimeException() != null) {
                throw var13.getRuntimeException();
            }
        } catch (Fault var14) {
            Throwable cause = var14.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }

            throw var14;
        } catch (RuntimeException var15) {
            throw var15;
        } finally {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Finished servicing http request on thread: " + Thread.currentThread());
            }

        }

    }

    protected void invokeComplete(ServletContext context, HttpServletRequest req, HttpServletResponse resp, Message m) throws IOException {
        ContinuationProvider p = (ContinuationProvider) m.get(ContinuationProvider.class);
        if (p != null) {
            p.complete();
        }

    }

    private void copyKnownRequestAttributes(HttpServletRequest request, Message message) {
        message.put("http.service.redirection", request.getAttribute("http.service.redirection"));
    }

    protected void setupMessage(Message inMessage, ServletConfig config, ServletContext context, final HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.setupContinuation(inMessage, req, resp);
        final Exchange exchange = inMessage.getExchange();
        DelegatingInputStream in = new DelegatingInputStream(req.getInputStream()) {
            public void cacheInput() {
                if (!this.cached && (exchange.isOneWay() || this.isWSAddressingReplyToSpecified(exchange))) {
                    exchange.getInMessage().put("HTTP.REQUEST", new HttpServletRequestSnapshot(req));
                }

                super.cacheInput();
            }

            private boolean isWSAddressingReplyToSpecified(Exchange ex) {
                AddressingProperties map = ContextUtils.retrieveMAPs(ex.getInMessage(), false, false, false);
                return map != null && !ContextUtils.isGenericAddress(map.getReplyTo());
            }
        };
        inMessage.setContent(DelegatingInputStream.class, in);
        inMessage.setContent(InputStream.class, in);
        inMessage.put("HTTP.REQUEST", req);
        inMessage.put("HTTP.RESPONSE", resp);
        inMessage.put("HTTP.CONTEXT", context);
        inMessage.put("HTTP.CONFIG", config);
        inMessage.put("HTTP_CONTEXT_MATCH_STRATEGY", this.contextMatchStrategy);
        inMessage.put("org.apache.cxf.request.method", req.getMethod());
        String requestURI = req.getRequestURI();
        inMessage.put("org.apache.cxf.request.uri", requestURI);
        String requestURL = fixRequestUrl(req);
        inMessage.put("org.apache.cxf.request.url", requestURL);
        String contextPath = req.getContextPath();
        if (contextPath == null) {
            contextPath = "";
        }

        String servletPath = req.getServletPath();
        if (servletPath == null) {
            servletPath = "";
        }

        String contextServletPath = contextPath + servletPath;
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            inMessage.put(Message.PATH_INFO, contextServletPath + pathInfo);
        } else {
            inMessage.put(Message.PATH_INFO, requestURI);
        }

        int contentType;
        String basePath;
        if (!StringUtils.isEmpty(requestURI)) {
            contentType = requestURL.indexOf(requestURI);
            if (contentType > 0) {
                basePath = requestURL.substring(0, contentType);
                String httpSecurityContext = basePath + contextPath;
                inMessage.put("http.base.path", httpSecurityContext);
            }
        } else if (!StringUtils.isEmpty(servletPath) && requestURL.endsWith(servletPath)) {
            contentType = requestURL.lastIndexOf(servletPath);
            if (contentType > 0) {
                inMessage.put("http.base.path", requestURL.substring(0, contentType));
            }
        }

        String contentType1 = req.getContentType();
        inMessage.put("Content-Type", contentType1);
        this.setEncoding(inMessage, req, contentType1);
        inMessage.put(Message.QUERY_STRING, req.getQueryString());
        inMessage.put("Accept", req.getHeader("Accept"));
        basePath = this.getBasePath(contextServletPath);
        if (!StringUtils.isEmpty(basePath)) {
            inMessage.put(Message.BASE_PATH, basePath);
        }

        inMessage.put(Message.FIXED_PARAMETER_ORDER, Boolean.valueOf(this.isFixedParameterOrder()));
        inMessage.put("org.apache.cxf.async.post.response.dispatch", Boolean.TRUE);
        SecurityContext httpSecurityContext1 = new SecurityContext() {
            public Principal getUserPrincipal() {
                return req.getUserPrincipal();
            }

            public boolean isUserInRole(String role) {
                return req.isUserInRole(role);
            }
        };
        inMessage.put(SecurityContext.class, httpSecurityContext1);
        Headers headers = new Headers(inMessage);
        headers.copyFromRequest(req);
        String credentials = headers.getAuthorization();
        AuthorizationPolicy authPolicy = this.getAuthorizationPolicyFromMessage(credentials, httpSecurityContext1);
        inMessage.put(AuthorizationPolicy.class, authPolicy);
        propogateSecureSession(req, inMessage);
        inMessage.put(CertConstraints.class.getName(), this.certConstraints);
        inMessage.put(Message.IN_INTERCEPTORS, Arrays.asList(new Interceptor[]{CertConstraintsInterceptor.INSTANCE}));
    }

    private String setEncoding(Message inMessage, HttpServletRequest req, String contentType) throws IOException {
        String enc = HttpHeaderHelper.findCharset(contentType);
        if (enc == null) {
            enc = req.getCharacterEncoding();
        }

        if (enc != null && enc.endsWith("\"")) {
            enc = enc.substring(0, enc.length() - 1);
        }

        if (enc != null || "POST".equals(req.getMethod()) || "PUT".equals(req.getMethod())) {
            String normalizedEncoding = HttpHeaderHelper.mapCharset(enc);
            if (normalizedEncoding == null) {
                String m = (new org.apache.cxf.common.i18n.Message("INVALID_ENCODING_MSG", LOG, new Object[]{enc})).toString();
                LOG.log(Level.WARNING, m);
                throw new IOException(m);
            }

            inMessage.put(Message.ENCODING, normalizedEncoding);
        }

        return contentType;
    }

    protected Message retrieveFromContinuation(HttpServletRequest req) {
        return !this.isServlet3 ? (this.cproviderFactory != null ? this.cproviderFactory.retrieveFromContinuation(req) : null) : this.retrieveFromServlet3Async(req);
    }

    protected Message retrieveFromServlet3Async(HttpServletRequest req) {
        try {
            return (Message) req.getAttribute("cxf.continuation.message");
        } catch (Throwable var3) {
            return null;
        }
    }

    protected void setupContinuation(Message inMessage, HttpServletRequest req, HttpServletResponse resp) {
        try {
            if (this.isServlet3 && req.isAsyncSupported()) {
                inMessage.put(ContinuationProvider.class.getName(), new Servlet3ContinuationProvider(req, resp, inMessage));
            } else if (this.cproviderFactory != null) {
                ContinuationProvider p = this.cproviderFactory.createContinuationProvider(inMessage, req, resp);
                if (p != null) {
                    inMessage.put(ContinuationProvider.class.getName(), p);
                }
            }
        } catch (Throwable var5) {
            ;
        }

    }

    protected String getBasePath(String contextPath) throws IOException {
        return StringUtils.isEmpty(this.endpointInfo.getAddress()) ? "" : (new URL(this.endpointInfo.getAddress())).getPath();
    }

    protected Conduit getInbuiltBackChannel(Message inMessage) {
        HttpServletResponse response = (HttpServletResponse) inMessage.get("HTTP.RESPONSE");
        return new org.apache.cxf.transport.http.AbstractHTTPDestination.BackChannelConduit(response);
    }

    private void initConfig() {
        this.cproviderFactory = (ContinuationProviderFactory) this.bus.getExtension(ContinuationProviderFactory.class);
    }

    private synchronized HTTPServerPolicy calcServerPolicyInternal(Message m) {
        HTTPServerPolicy sp = this.serverPolicy;
        if (!this.serverPolicyCalced) {
            PolicyDataEngine pde = (PolicyDataEngine) this.bus.getExtension(PolicyDataEngine.class);
            if (pde != null) {
                sp = (HTTPServerPolicy) pde.getServerEndpointPolicy(m, this.endpointInfo, this, new ServerPolicyCalculator());
            }

            if (null == sp) {
                sp = (HTTPServerPolicy) this.endpointInfo.getTraversedExtensor(new HTTPServerPolicy(), HTTPServerPolicy.class);
            }

            this.serverPolicy = sp;
            this.serverPolicyCalced = true;
        }

        return sp;
    }

    private HTTPServerPolicy calcServerPolicy(Message m) {
        HTTPServerPolicy sp = this.serverPolicy;
        if (!this.serverPolicyCalced) {
            sp = this.calcServerPolicyInternal(m);
        }

        return sp;
    }

    private void cacheInput(Message outMessage) {
        if (outMessage.getExchange() != null) {
            Message inMessage = outMessage.getExchange().getInMessage();
            if (inMessage != null) {
                Object o = inMessage.get("cxf.io.cacheinput");
                DelegatingInputStream in = (DelegatingInputStream) inMessage.getContent(DelegatingInputStream.class);
                if (MessageUtils.isTrue(o)) {
                    Collection atts = inMessage.getAttachments();
                    if (atts != null) {
                        Iterator var6 = atts.iterator();

                        while (var6.hasNext()) {
                            Attachment a = (Attachment) var6.next();
                            if (a.getDataHandler().getDataSource() instanceof AttachmentDataSource) {
                                try {
                                    ((AttachmentDataSource) a.getDataHandler().getDataSource()).cache(inMessage);
                                } catch (IOException var10) {
                                    throw new Fault(var10);
                                }
                            }
                        }
                    }

                    if (in != null) {
                        in.cacheInput();
                    }
                } else if (in != null) {
                    try {
                        IOUtils.consume(in, 16777216);
                    } catch (Exception var9) {
                        ;
                    }
                }

            }
        }
    }

    protected OutputStream flushHeaders(Message outMessage) throws IOException {
        return this.flushHeaders(outMessage, true);
    }

    protected OutputStream flushHeaders(Message outMessage, boolean getStream) throws IOException {
        if (this.isResponseRedirected(outMessage)) {
            return null;
        } else {
            this.cacheInput(outMessage);
            HTTPServerPolicy sp = this.calcServerPolicy(outMessage);
            if (sp != null) {
                (new Headers(outMessage)).setFromServerPolicy(sp);
            }

            ServletOutputStream responseStream = null;
            boolean oneWay = this.isOneWay(outMessage);
            HttpServletResponse response = this.getHttpResponseFromMessage(outMessage);
            int responseCode = this.getReponseCodeFromMessage(outMessage);
            if (responseCode >= 300) {
                String ec = (String) outMessage.get(Message.ERROR_MESSAGE);
                if (!StringUtils.isEmpty(ec)) {
                    response.sendError(responseCode, ec);
                    return null;
                }
            }

            response.setStatus(responseCode);
            (new Headers(outMessage)).copyToResponse(response);
            outMessage.put("http.headers.copied", "true");
            if (this.hasNoResponseContent(outMessage)) {
                response.setContentLength(0);
                response.flushBuffer();
                response.getOutputStream().close();
            } else if (!getStream) {
                response.getOutputStream().close();
            } else {
                responseStream = response.getOutputStream();
            }

            if (oneWay) {
                outMessage.remove("HTTP.RESPONSE");
            }

            return responseStream;
        }
    }

    private int getReponseCodeFromMessage(Message message) {
        Integer i = (Integer) message.get(Message.RESPONSE_CODE);
        if (i != null) {
            return i.intValue();
        } else {
            int code = this.hasNoResponseContent(message) ? 202 : 200;
            message.put(Message.RESPONSE_CODE, Integer.valueOf(code));
            return code;
        }
    }

    private boolean hasNoResponseContent(Message message) {
        boolean ow = this.isOneWay(message);
        boolean pr = MessageUtils.isPartialResponse(message);
        boolean epr = MessageUtils.isEmptyPartialResponse(message);
        return ow && !pr || epr;
    }

    private HttpServletResponse getHttpResponseFromMessage(Message message) throws IOException {
        Object responseObj = message.get("HTTP.RESPONSE");
        if (responseObj instanceof HttpServletResponse) {
            return (HttpServletResponse) responseObj;
        } else {
            String m;
            if (null != responseObj) {
                m = (new org.apache.cxf.common.i18n.Message("UNEXPECTED_RESPONSE_TYPE_MSG", LOG, new Object[]{responseObj.getClass()})).toString();
                LOG.log(Level.WARNING, m);
                throw new IOException(m);
            } else {
                m = (new org.apache.cxf.common.i18n.Message("NULL_RESPONSE_MSG", LOG, new Object[0])).toString();
                LOG.log(Level.WARNING, m);
                throw new IOException(m);
            }
        }
    }

    private boolean isResponseRedirected(Message outMessage) {
        Exchange exchange = outMessage.getExchange();
        return exchange != null && Boolean.TRUE.equals(exchange.get("http.request.redirected"));
    }

    protected boolean contextMatchOnExact() {
        return "exact".equals(this.contextMatchStrategy);
    }

    public void finalizeConfig() {
    }

    public String getBeanName() {
        String beanName = null;
        if (this.endpointInfo.getName() != null) {
            beanName = this.endpointInfo.getName().toString() + ".http-destination";
        }

        return beanName;
    }

    public EndpointReferenceType getAddressWithId(String id) {
        EndpointReferenceType ref = null;
        if (this.isMultiplexWithAddress()) {
            String address = EndpointReferenceUtils.getAddress(this.reference);
            ref = EndpointReferenceUtils.duplicate(this.reference);
            if (address.endsWith("/")) {
                EndpointReferenceUtils.setAddress(ref, address + id);
            } else {
                EndpointReferenceUtils.setAddress(ref, address + "/" + id);
            }
        } else {
            ref = super.getAddressWithId(id);
        }

        return ref;
    }

    public String getId(Map<String, Object> context) {
        String id = null;
        if (this.isMultiplexWithAddress()) {
            String address = (String) context.get(Message.PATH_INFO);
            if (null != address) {
                int afterLastSlashIndex = address.lastIndexOf("/") + 1;
                if (afterLastSlashIndex > 0 && afterLastSlashIndex < address.length()) {
                    id = address.substring(afterLastSlashIndex);
                }
            } else {
                this.getLogger().log(Level.WARNING, (new org.apache.cxf.common.i18n.Message("MISSING_PATH_INFO", LOG, new Object[0])).toString());
            }

            return id;
        } else {
            return super.getId(context);
        }
    }

    public String getContextMatchStrategy() {
        return this.contextMatchStrategy;
    }

    public void setContextMatchStrategy(String contextMatchStrategy) {
        this.contextMatchStrategy = contextMatchStrategy;
    }

    public boolean isFixedParameterOrder() {
        return this.fixedParameterOrder;
    }

    public void setFixedParameterOrder(boolean fixedParameterOrder) {
        this.fixedParameterOrder = fixedParameterOrder;
    }

    public boolean isMultiplexWithAddress() {
        return this.multiplexWithAddress;
    }

    public void setMultiplexWithAddress(boolean multiplexWithAddress) {
        this.multiplexWithAddress = multiplexWithAddress;
    }

    public HTTPServerPolicy getServer() {
        return this.calcServerPolicy((Message) null);
    }

    public void setServer(HTTPServerPolicy server) {
        this.serverPolicy = server;
        if (server != null) {
            this.serverPolicyCalced = true;
        }

    }

    public void assertMessage(Message message) {
        PolicyDataEngine pde = (PolicyDataEngine) this.bus.getExtension(PolicyDataEngine.class);
        pde.assertMessage(message, this.calcServerPolicy(message), new ServerPolicyCalculator());
    }

    public boolean canAssert(QName type) {
        return (new ServerPolicyCalculator()).getDataClassName().equals(type);
    }

    public void releaseRegistry() {
        this.registry = null;
    }

    public String getPath() {
        return this.path;
    }

    protected void activate() {
        synchronized (this) {
            if (this.registry != null) {
                this.registry.addDestination(this);
            }

        }
    }

    protected void deactivate() {
        synchronized (this) {
            if (this.registry != null) {
                this.registry.removeDestination(this.path);
            }

        }
    }

    public void shutdown() {
        synchronized (this) {
            if (this.registry != null) {
                this.registry.removeDestination(this.path);
            }
        }

        super.shutdown();
    }

    /**
     * 修改URL地址，骗过CXF，使生成的WDSL的soap address前面追加serviceId
     *
     * @param request
     * @return
     */
    private String fixRequestUrl(HttpServletRequest request) {
        String serviceId = Beans.getApplicationServiceId();
        if (!request.getRequestURI().startsWith("/" + serviceId)) {
            String port = request.getServerPort() == 80 ? "" : (":" + request.getServerPort());
            return request.getScheme() + "://" + request.getServerName() + port + "/" + serviceId + request.getRequestURI();
        }
        return request.getRequestURL().toString();
    }

    public static final class PrincipalAuthorizationPolicy extends AuthorizationPolicy {
        final SecurityContext sc;

        public PrincipalAuthorizationPolicy(SecurityContext sc) {
            this.sc = sc;
        }

        public Principal getPrincipal() {
            return this.sc.getUserPrincipal();
        }

        public String getUserName() {
            String name = super.getUserName();
            if (name != null) {
                return name;
            } else {
                Principal pp = this.getPrincipal();
                return pp != null ? pp.getName() : null;
            }
        }
    }

    private class WrappedOutputStream extends AbstractWrappedOutputStream implements CopyingOutputStream {
        private Message outMessage;

        WrappedOutputStream(Message m) {
            this.outMessage = m;
        }

        public int copyFrom(InputStream in) throws IOException {
            if (!this.written) {
                this.onFirstWrite();
                this.written = true;
            }

            return this.wrappedStream != null ? IOUtils.copy(in, this.wrappedStream) : IOUtils.copy(in, this, 4096);
        }

        protected void onFirstWrite() throws IOException {
            OutputStream responseStream = org.apache.cxf.transport.http.AbstractHTTPDestination.this.flushHeaders(this.outMessage);
            if (null != responseStream) {
                this.wrappedStream = responseStream;
            }

        }

        public void close() throws IOException {
            if (!this.written && this.wrappedStream == null) {
                OutputStream responseStream = org.apache.cxf.transport.http.AbstractHTTPDestination.this.flushHeaders(this.outMessage, false);
                if (null != responseStream) {
                    this.wrappedStream = responseStream;
                }
            }

            if (this.wrappedStream != null) {
                this.wrappedStream.close();
            }

        }
    }

    public class BackChannelConduit extends AbstractBackChannelConduit {
        protected HttpServletResponse response;

        BackChannelConduit(HttpServletResponse resp) {
//            super(BackChannelConduit.this);
            super();
            this.response = resp;
        }

        public void prepare(Message message) throws IOException {
            message.put("HTTP.RESPONSE", this.response);
            OutputStream os = (OutputStream) message.getContent(OutputStream.class);
            if (os == null) {
                message.setContent(OutputStream.class, org.apache.cxf.transport.http.AbstractHTTPDestination.this.new WrappedOutputStream(message));
            }

        }

        public void close(Message msg) throws IOException {
            super.close(msg);
            if (msg.getExchange() != null) {
                Message m = msg.getExchange().getInMessage();
                if (m != null) {
                    InputStream is = (InputStream) m.getContent(InputStream.class);
                    if (is != null) {
                        try {
                            is.close();
                            m.removeContent(InputStream.class);
                        } catch (IOException var5) {
                            ;
                        }
                    }

                }
            }
        }
    }
}
