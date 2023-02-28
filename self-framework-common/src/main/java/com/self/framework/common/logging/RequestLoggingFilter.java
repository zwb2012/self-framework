package com.self.framework.common.logging;

import com.self.framework.common.spring.SpringContextUtils;
import com.self.framework.common.utils.SelfStringUtils;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author wenbo.zhuang
 * @date 2022/02/17 18:55
 **/
@SuppressWarnings("all")
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String DEFAULT_EVENT_BEFORE_REQUEST = "BEFORE_REQUEST";
    private static final String DEFAULT_EVENT_AFTER_REQUEST = "AFTER_REQUEST";
    private static final String DEFAULT_FIELD_PLACEHOLDER = "-";
    private static final char DEFAULT_LOG_FIELD_SEPARATOR_1 = '\u0001';
    private static final char DEFAULT_LOG_FIELD_SEPARATOR_2 = '\u0002';
    private static final String PINTPOINT_SEPARATOR = "^";
    private static final String CONFIG_KEY_ENABLE = "request.log.servlet.enable";
    private static final String CONFIG_KEY_LENGTH = "request.log.maxpayloadlength";
    private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 3072;
    private static final String COOKIE_KEY_STOREID = "storeId";
    private int maxPayloadLength = DEFAULT_MAX_PAYLOAD_LENGTH;

    public RequestLoggingFilter() {
    }

    public void setMaxPayloadLength(int maxPayloadLength) {
        Assert.isTrue(maxPayloadLength >= 0, "'maxPayloadLength' should be larger than or equal to 0");
        this.maxPayloadLength = maxPayloadLength;
    }

    public int getMaxPayloadLength() {
        String strMaxpayloadlength = ((Environment) SpringContextUtils.getBean(Environment.class)).getProperty(CONFIG_KEY_LENGTH);

        try {
            this.maxPayloadLength = Integer.valueOf(strMaxpayloadlength);
        } catch (NumberFormatException var3) {
        }

        return this.maxPayloadLength;
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String enable = ((Environment) SpringContextUtils.getBean(Environment.class)).getProperty(CONFIG_KEY_ENABLE);
        if (Boolean.TRUE.toString().equalsIgnoreCase(enable)) {
            long startTime = System.currentTimeMillis();
            boolean isFirstRequest = !this.isAsyncDispatch(request);
            HttpServletRequest requestToUse = request;
            HttpServletResponse responseToUse = response;
            if (isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
                requestToUse = new ContentCachingRequestWrapper(request, this.getMaxPayloadLength());
                responseToUse = new ContentCachingResponseWrapper(response);
            }

            boolean shouldLog = this.shouldLog((HttpServletRequest) requestToUse);
            if (shouldLog && isFirstRequest) {
            }

            try {
                filterChain.doFilter((ServletRequest) requestToUse, (ServletResponse) responseToUse);
            } finally {
                if (shouldLog && !this.isAsyncStarted((HttpServletRequest) requestToUse)) {
                    this.afterRequest((HttpServletRequest) requestToUse, (HttpServletResponse) responseToUse, this.getAfterMessage((HttpServletRequest) requestToUse, (HttpServletResponse) responseToUse, startTime));
                }

            }
        } else {
            filterChain.doFilter(request, response);
        }

    }

    protected String getBeforeMessage(HttpServletRequest request, long startTime) {
        return this.createMessage(request, startTime);
    }

    protected String getAfterMessage(HttpServletRequest request, HttpServletResponse response, long startTime) {
        return this.createMessage(request, response, startTime);
    }

    private String buildRequestId(String requestType, long requestId, String method) {
        return requestType + DEFAULT_LOG_FIELD_SEPARATOR_1 + requestId + DEFAULT_LOG_FIELD_SEPARATOR_1 + method;
    }

    protected String createMessage(HttpServletRequest request, long startTime) {
        StringBuilder sb = new StringBuilder();
        long requestId = startTime % 10000L;
        sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(this.buildRequestId(DEFAULT_EVENT_BEFORE_REQUEST, requestId, request.getMethod()));
        String queryString = request.getQueryString();
        if (queryString != null && queryString.length() > 0) {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(request.getRequestURI()).append("?").append(queryString);
        } else {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(request.getRequestURI());
        }

        String client = request.getRemoteAddr();
        if (client != null && client.length() > 0) {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(client);
        } else {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(DEFAULT_FIELD_PLACEHOLDER);
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(session.getId());
        } else {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(DEFAULT_FIELD_PLACEHOLDER);
        }

        sb.append(this.assembleRequestHeaders(request));
        return sb.toString();
    }

    protected String createMessage(HttpServletRequest request, HttpServletResponse response, long startTime) {
        long requestId = startTime % 10000L;
        StringBuilder sb = new StringBuilder();
        sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(this.buildRequestId(DEFAULT_EVENT_AFTER_REQUEST, requestId, request.getMethod()));
        String queryString = request.getQueryString();
        if (queryString != null && queryString.length() > 0) {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(request.getRequestURI()).append("?").append(queryString);
        } else {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(request.getRequestURI());
        }

        String client = request.getRemoteAddr();
        if (client != null && client.length() > 0) {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(client);
        } else {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(DEFAULT_FIELD_PLACEHOLDER);
        }

        String storeId = DEFAULT_FIELD_PLACEHOLDER;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            Cookie[] var12 = cookies;
            int var13 = cookies.length;

            for (int var14 = 0; var14 < var13; ++var14) {
                Cookie cookie = var12[var14];
                if (COOKIE_KEY_STOREID.equals(cookie.getName())) {
                    storeId = cookie.getValue();
                    break;
                }
            }
        }

        sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(storeId);
        String sessionId = DEFAULT_FIELD_PLACEHOLDER;
        HttpSession session = request.getSession(false);
        if (session != null) {
            sessionId = session.getId();
        }

        sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(sessionId);
        String traceId = DEFAULT_FIELD_PLACEHOLDER;
        String tId = this.assembleApmTraceId(request);
        if (tId != null && tId.length() > 0) {
            traceId = tId;
        }

        sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(traceId);
        sb.append(this.assembleRequestHeaders(request));
        ContentCachingRequestWrapper reqWrapper = (ContentCachingRequestWrapper) WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (reqWrapper != null) {
            byte[] buf = reqWrapper.getContentAsByteArray();
            this.buildBuffer(buf, sb, reqWrapper.getCharacterEncoding());
        } else {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(DEFAULT_FIELD_PLACEHOLDER);
        }

        sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(response.getStatus());
        sb.append(this.assembleResponseHeaders(response));
        ContentCachingResponseWrapper resWrapper = (ContentCachingResponseWrapper) WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (resWrapper != null) {
            byte[] buf = resWrapper.getContentAsByteArray();
            this.buildBuffer(buf, sb, resWrapper.getCharacterEncoding());

            try {
                resWrapper.copyBodyToResponse();
            } catch (IOException var20) {
                this.logger.error(var20.getMessage(), var20);
            }
        } else {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(DEFAULT_FIELD_PLACEHOLDER);
        }

        long duration = System.currentTimeMillis() - startTime;
        sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(duration);
        return sb.toString();
    }

    private boolean shouldLog(HttpServletRequest request) {
        return true;
    }

    protected void beforeRequest(HttpServletRequest request, String message) {
        this.logger.info(message);
    }

    protected void afterRequest(HttpServletRequest request, HttpServletResponse response, String message) {
        this.logger.info(message);
    }

    private String assembleRequestHeaders(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        HttpHeaders headers = (new ServletServerHttpRequest(request)).getHeaders();
        if (headers != null && headers.size() > 0) {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1);
            int headerIdx = 0;

            for (Iterator var5 = headers.entrySet().iterator(); var5.hasNext(); ++headerIdx) {
                Map.Entry<String, List<String>> entrySet = (Map.Entry) var5.next();
                if (headerIdx > 0) {
                    sb.append(DEFAULT_LOG_FIELD_SEPARATOR_2);
                }

                String key = (String) entrySet.getKey();
                List<String> values = (List) entrySet.getValue();
                if (values != null && values.size() > 0) {
                    int valueSize = values.size();

                    for (int i = 0; i < valueSize; ++i) {
                        if (i == 0) {
                            sb.append(key).append("=").append((String) values.get(i));
                        } else {
                            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_2).append(key).append("=").append((String) values.get(i));
                        }
                    }
                }
            }
        } else {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(DEFAULT_FIELD_PLACEHOLDER);
        }

        return sb.toString();
    }

    private String assembleResponseHeaders(HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> headerNames = response.getHeaderNames().iterator();

        int headerIndex;
        for (headerIndex = 0; headerNames.hasNext(); ++headerIndex) {
            if (headerIndex == 0) {
                sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1);
            } else {
                sb.append(DEFAULT_LOG_FIELD_SEPARATOR_2);
            }

            String key = (String) headerNames.next();
            Collection<String> headers = response.getHeaders(key);
            if (headers != null && headers.size() > 0) {
                Iterator<String> iterator = headers.iterator();

                for (int headerIdx = 0; iterator.hasNext(); ++headerIdx) {
                    String header = (String) iterator.next();
                    if (headerIdx == 0) {
                        sb.append(key).append("=").append(header);
                    } else {
                        sb.append(DEFAULT_LOG_FIELD_SEPARATOR_2).append(key).append("=").append(header);
                    }
                }
            }
        }

        if (headerIndex == 0) {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(DEFAULT_FIELD_PLACEHOLDER);
        }

        return sb.toString();
    }

    private void buildBuffer(byte[] buf, StringBuilder sb, String characterEncoding) {
        if (buf != null && buf.length > 0) {
            int length = Math.min(buf.length, this.getMaxPayloadLength());

            String payload;
            try {
                payload = (new String(buf, 0, length, characterEncoding)).replace("\n", "");
            } catch (UnsupportedEncodingException var7) {
                payload = "[unknown]";
            }

            if (payload != null && payload.length() > 0) {
                sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(SelfStringUtils.unicodeToString(payload));
            } else {
                sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(DEFAULT_FIELD_PLACEHOLDER);
            }

        } else {
            sb.append(DEFAULT_LOG_FIELD_SEPARATOR_1).append(DEFAULT_FIELD_PLACEHOLDER);
        }
    }

    private String assembleApmTraceId(ServletRequest req) {
        while (req instanceof ServletRequestWrapper) {
            req = ((ServletRequestWrapper) req).getRequest();
        }

        if (req instanceof RequestFacade) {
            RequestFacade sr = (RequestFacade) req;

            try {
                Field f = ReflectionUtils.findField(sr.getClass(), "request");
                if (f == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                f.setAccessible(true);
                Object o = ReflectionUtils.getField(f, sr);
                if (o == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                Field fta = ReflectionUtils.findField(o.getClass(), "_$PINPOINT$_com_navercorp_pinpoint_plugin_tomcat_TraceAccessor");
                if (fta == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                fta.setAccessible(true);
                Object ta = ReflectionUtils.getField(fta, o);
                if (ta == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                Field span = ReflectionUtils.findField(ta.getClass(), "span");
                if (span == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                span.setAccessible(true);
                Object spanObj = ReflectionUtils.getField(span, ta);
                if (spanObj == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                Field traceRoot = ReflectionUtils.findField(spanObj.getClass(), "traceRoot");
                if (traceRoot == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                traceRoot.setAccessible(true);
                Object traceRootObj = ReflectionUtils.getField(traceRoot, spanObj);
                if (traceRootObj == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                Field traceId = ReflectionUtils.findField(traceRootObj.getClass(), "traceId");
                if (traceId == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                traceId.setAccessible(true);
                Object traceIdObj = ReflectionUtils.getField(traceId, traceRootObj);
                if (traceIdObj == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                Class<? extends Object> traceIdClass = traceIdObj.getClass();
                Field agentId = ReflectionUtils.findField(traceIdClass, "agentId");
                if (agentId == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                agentId.setAccessible(true);
                Object agentIdVal = ReflectionUtils.getField(agentId, traceIdObj);
                Field agentStartTime = ReflectionUtils.findField(traceIdClass, "agentStartTime");
                if (agentStartTime == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                agentStartTime.setAccessible(true);
                Object agentStartTimeVal = ReflectionUtils.getField(agentStartTime, traceIdObj);
                if (agentStartTimeVal == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                Field transactionSequence = ReflectionUtils.findField(traceIdClass, "transactionSequence");
                if (transactionSequence == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                transactionSequence.setAccessible(true);
                Object transactionSequenceVal = ReflectionUtils.getField(transactionSequence, traceIdObj);
                if (transactionSequenceVal == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                Field spanId = ReflectionUtils.findField(traceIdClass, "spanId");
                if (spanId == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                spanId.setAccessible(true);
                Object spanIdVal = ReflectionUtils.getField(spanId, traceIdObj);
                if (spanIdVal == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                Field parentSpanId = ReflectionUtils.findField(traceIdClass, "parentSpanId");
                if (parentSpanId == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                parentSpanId.setAccessible(true);
                Object parentSpanIdVal = ReflectionUtils.getField(parentSpanId, traceIdObj);
                if (parentSpanIdVal == null) {
                    return DEFAULT_FIELD_PLACEHOLDER;
                }

                StringBuilder sbTraceId = new StringBuilder();
                sbTraceId.append(agentIdVal).append(PINTPOINT_SEPARATOR).append(agentStartTimeVal).append(PINTPOINT_SEPARATOR).append(transactionSequenceVal).append(DEFAULT_LOG_FIELD_SEPARATOR_2).append(spanIdVal).append(DEFAULT_LOG_FIELD_SEPARATOR_2).append(parentSpanIdVal);
                return sbTraceId.toString();
            } catch (Exception var25) {
            }
        }

        return DEFAULT_FIELD_PLACEHOLDER;
    }
}
