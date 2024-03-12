package com.example.demo.controller.util;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    /*
    200 OK： 請求成功，伺服器返回數據。

201 Created： 資源成功創建，通常在使用POST方法創建資源時返回。

204 No Content： 請求成功，但伺服器不返回內容，通常在使用DELETE方法刪除資源時返回。

400 Bad Request： 請求無效，通常由客戶端引起。

401 Unauthorized： 未經授權，需要驗證。

404 Not Found： 資源未找到。

500 Internal Server Error： 伺服器內部錯誤。
    * */

    private Map<String, Object> result;

    ResponseHandler(){
        result = new HashMap<>();
    }

    ResponseHandler(Map<String, Object> result){
        this.result = result;
    }

    public static ResponseHandler putSuccessfulStatus(){
        return new ResponseHandler().put(ResponseKeys.RC, ResponseCode.SUCCESSFUL.rawValue());
    }

    public static ResponseHandler fail(ResponseCode status, String note){
        return new ResponseHandler().put(ResponseKeys.RC, status.rawValue())
                .put(ResponseKeys.RM, note);
    }

    public ResponseHandler put(String key, Object value){
        result.put(key, value);
        return this;
    }

    public ResponseHandler put(String key, JSONObject value){
        result.put(key, value.toMap());
        return this;
    }

    public ResponseHandler putTimestamp(){
        result.put(ResponseKeys.TIMESTAMP, System.currentTimeMillis());
        return this;
    }

//    public static Response successful(String message){
//        return successful().put("message", message);
//    }

//    public static ResponseHandler successful(){
//        return new ResponseHandler().put(ResponseKey.RC, SUCCESSFUL);
//    }



    public ResponseEntity build(){
        return ResponseEntity.ok(result);
    }

    public ResponseEntity build(HttpStatus status){
        return ResponseEntity
                .status(status)
                .body(result);
    }

    public static ResponseEntity responseCreatedSuccessful(String key , Object data){
        return putSuccessfulStatus().put(key, data).build(HttpStatus.CREATED);
    }

    public static ResponseEntity responseSuccessful(){
        return putSuccessfulStatus().build();
    }

    public static ResponseEntity responseSuccessful(String key , Object data){
        return putSuccessfulStatus().put(key, data).build();
    }

    public static ResponseEntity responseQueryResults(String key , Object data){
        return putSuccessfulStatus().put(key, data).putTimestamp().build();
    }

    public static ResponseEntity responseFail(ResponseCode status, String note){
        return fail(status, note).build();
    }

}
