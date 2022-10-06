package com.nta.teabreakorder.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.payload.response.ActionNotification;
import com.pusher.rest.Pusher;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class PusherService {
    private static Pusher pusher;
    private final String CHANNEL_NAME = "TEABREAK_ORDER";
    public static final String ORDERS = "ORDERS";
    public static final String ADD_ORDER = "ADD_ORDER";
    public static final String EDIT_ORDER = "EDIT_ORDER";
    public static final String EDIT_ORDERS = "EDIT_ORDERS";
    public static final String REMOVE_ORDER = "REMOVE_ORDER";
    private static final HttpPost httpPost = new HttpPost("https://56a993ba-67f3-4e01-90dd-8d712272dc55.pushnotifications.pusher.com/publish_api/v1/instances/56a993ba-67f3-4e01-90dd-8d712272dc55/publishes");
    private static  CloseableHttpClient httpClient = null;
    static {
        int timeout = 5;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .build();
        httpClient  =
                HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        httpPost.setHeader("Authorization", "Bearer 2DED5CBF63531312723B654759B130898CBD2797624077B497648725EA317C45");
        httpPost.setHeader("Content-Type", "application/json");
    }



    /* PUSHER NOTIFICATION */
    private String CHANNEL_NAME_NOTI = "TEABREAK_ORDER_NOTIFICATION=";
    public static final String NEW_NOTI = "NEW_NOTIFICATION";

    static {
        pusher = new Pusher("1406839", "94e4ab3f1c94d7d3828f", "9f93694957639be2cdad");
        pusher.setCluster("ap1");
        pusher.setEncrypted(true);
    }

    public void triggerEvent(String eventName, Object data) {
        pusher.trigger(CHANNEL_NAME, eventName, data);
    }

    public void triggerNotification(ActionNotification data) {
        pusher.trigger(CHANNEL_NAME_NOTI + data.getUser_id(), NEW_NOTI, data);
    }


    public void sendToBrowserNotify(List<String> users, String title, String content, String icon) throws IOException {

        String json = """
                {
                    "interests": 
                        """ + CommonUtil.getObjectMapper().writeValueAsString(users) + """
                    ,
                    "web": {
                        "notification": {
                            "title": """+ CommonUtil.getObjectMapper().writeValueAsString(title) + """ 
                             ,
                            "body": """+ CommonUtil.getObjectMapper().writeValueAsString(content) + """
                            ,
                            "icon":"""+ CommonUtil.getObjectMapper().writeValueAsString(icon) + """
                            ,
                            "click_action": "https://orderfoodnta.web.app/"
                        }
                    }
                }
                """;
        try  {
            httpPost.setEntity(new StringEntity(json, "UTF-8"));
            httpClient.execute(httpPost);
        } catch (Exception e) {
            httpClient.close();
            e.printStackTrace();
        }
        finally {
            httpPost.releaseConnection();
        }
    }

}
