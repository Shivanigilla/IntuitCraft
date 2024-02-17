package com.intuit.commentsService.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;
import java.util.function.Supplier;

public class TimeUtility {
    public static Long getCurrentTimeStamp(){
        return Instant.now().toEpochMilli() ;
    }
}
