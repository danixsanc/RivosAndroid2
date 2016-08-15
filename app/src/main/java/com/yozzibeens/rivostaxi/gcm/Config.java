package com.YozziBeens.rivostaxi.gcm;

/**
 * Created by aneh on 8/14/2014.
 */
public interface Config {
    // used to share GCM regId with application server - using php app server
    static final String APP_SERVER_URL = "http://appm.rivosservices.com/push_notifications.php";
    // Google Project Number
    static final String GOOGLE_PROJECT_ID = "1001209534751";
    static final String MESSAGE_KEY = "msg";
}
