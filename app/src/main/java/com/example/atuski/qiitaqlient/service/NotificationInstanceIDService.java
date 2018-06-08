package com.example.atuski.qiitaqlient.service;

import android.content.SharedPreferences;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.example.atuski.qiitaqlient.R;
import com.example.atuski.qiitaqlient.api.RegisterDeviceTokenQlient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.schedulers.Schedulers;

public class NotificationInstanceIDService extends FirebaseInstanceIdService {

    private static final String APPLICATION_ARN = "arn:aws:sns:ap-northeast-1:209970830514:app/GCM/LightQiita";
    private static final String ENDPOINT = "https://sns.ap-northeast-1.amazonaws.com";
    private static final String ACCESS_KEY = "AKIAI4DII32SIT7OOWAA";
    private static final String SECRET_KEY = "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";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();

        Log.v("NotificationInstanceIDService", token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        AmazonSNSClient client = new AmazonSNSClient(generateAWSCredentials());
        client.setEndpoint(ENDPOINT);
        //SharedPreferenceに保存したendpointArnが存在したらそちらから取得するようにしてもOK
        String endpointArn = createEndpointArn(token, client);
        HashMap<String, String> attr = new HashMap<>();
        attr.put("Token", token);
        attr.put("Enabled", "true");
        SetEndpointAttributesRequest req = new SetEndpointAttributesRequest().withEndpointArn(endpointArn).withAttributes(attr);
        client.setEndpointAttributes(req);
    }

    private String createEndpointArn(String token, AmazonSNSClient client) {
        String endpointArn;
        try {
            System.out.println("Creating platform endpoint with token " + token);
            CreatePlatformEndpointRequest cpeReq =
                    new CreatePlatformEndpointRequest()
                            .withPlatformApplicationArn(APPLICATION_ARN)
                            .withToken(token);
            CreatePlatformEndpointResult cpeRes = client
                    .createPlatformEndpoint(cpeReq);
            endpointArn = cpeRes.getEndpointArn();
        } catch (InvalidParameterException ipe) {
            String message = ipe.getErrorMessage();
            System.out.println("Exception message: " + message);
            Pattern p = Pattern
                    .compile(".*Endpoint (arn:aws:sns[^ ]+) already exists " +
                            "with the same token.*");
            Matcher m = p.matcher(message);
            if (m.matches()) {
                // The platform endpoint already exists for this token, but with
                // additional custom data that
                // createEndpoint doesn't want to overwrite. Just use the
                // existing platform endpoint.
                endpointArn = m.group(1);
            } else {
                // Rethrow the exception, the input is actually bad.
                throw ipe;
            }
        }
        storeEndpointArn(endpointArn);
        return endpointArn;
    }

    private AWSCredentials generateAWSCredentials() {
        return new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return ACCESS_KEY;
            }

            @Override
            public String getAWSSecretKey() {
                return SECRET_KEY;
            }
        };
    }

    private void storeEndpointArn(String endpointArn) {
        //SharedPreferenceにでもendpointArnを保存して、次回以降はcreateEndpointArnの処理を省略しても良い(公式はその方式になってる)
        SharedPreferences data = getSharedPreferences(getResources().getString(R.string.FCM_REGISTRATION), getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        Log.v("storeEndpointArn", endpointArn);
        editor.putString(getResources().getString(R.string.FCM_INSTANCE_ID), endpointArn);
        editor.apply();

        // AmazonSNSのPlatformApplicationにdeviceTokenを登録し、ARNを生成しておく。
        HashMap<String, String> postParameter = new HashMap<>();
        postParameter.put("deviceToken", endpointArn);
        RegisterDeviceTokenQlient
                .getInstance()
                .registerDeviceToken(postParameter)
                .subscribeOn(Schedulers.io());
    }

    private String getEndPointArn() {
        //SharedPreferenceからendpointArnを取得して、次回以降はcreateEndpointArnの処理を省略しても良い(公式はその方式になってる)
        return null;
    }
}
