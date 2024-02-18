//package com.intuit.commentsService.config.interceptor;
//
//import com.intuit.commentsService.exception.CommentsServiceException;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import static com.intuit.commentsService.constant.ExceptionConstant.EXCEEDED_REQUESTS_PER_SECOND;
//
//public class ThrottleInterceptor implements HandlerInterceptor {
//
//    private static final int MAX_REQUESTS_PER_SECOND = 1;
//
//    private final Map<String, Long> userLastRequestTimestamps = new ConcurrentHashMap<>();
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String userId = getUserIdFromRequest(request);
//        long currentTime = System.currentTimeMillis();
//
//        if (userLastRequestTimestamps.containsKey(userId)) {
//            long lastRequestTime = userLastRequestTimestamps.get(userId);
//            long timeElapsed = currentTime - lastRequestTime;
//
//            if (timeElapsed < 1000 / MAX_REQUESTS_PER_SECOND) {
//                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
//                throw new CommentsServiceException(EXCEEDED_REQUESTS_PER_SECOND);
//            }
//        }
//
//        userLastRequestTimestamps.put(userId, currentTime);
//        return true;
//    }
//
//    private String getUserIdFromRequest(HttpServletRequest request) {
//       return request.getHeader("Authorization");
//    }
//}
//
