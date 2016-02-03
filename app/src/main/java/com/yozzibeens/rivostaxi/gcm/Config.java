package com.yozzibeens.rivostaxi.gcm;

/**
 * Created by aneh on 8/14/2014.
 */
public interface Config {
    // used to share GCM regId with application server - using php app server
    static final String APP_SERVER_URL = "http://test.yozzibeens.com/push.php";
    // Google Project Number
    static final String GOOGLE_PROJECT_ID = "796937516711";
    static final String MESSAGE_KEY = "msg";
}
