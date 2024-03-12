package com.example.demo.controller.util;

public enum ResponseCode {
    SUCCESSFUL(0),
    SYSTEM_ERROR(1),
    FORMAT_ERROR(1000),
//    FORMAT_ERROR(1001), // 資料格式錯誤
    TOKEN_ERROR(1002), // Token驗證失敗
    LOGIN_FAIL(1003), // 登入失敗(密碼錯誤)
    NOT_FOUND(1004),
    DUPLICATE(1005), // 資料重複
    AUTH_ERROR(1006),
    OPERATION_NOT_ALLOWED(2000), // 操作不允許
    PERMISSION_DENIED(2001), // 無權限
    MEANINGLESS_OPERATION(2002), // 無意義的操作 ex. 重複邀請、重複加入
    SAVE_REFUSE(2003),  // 存取被拒
    UPDATE_FAIL(2004);


    private int rawValue;

    private ResponseCode(int rawValue){
        this.rawValue = rawValue;
    }

    public int rawValue(){
        return rawValue;
    }

//    case opNotSupport = "2000" // 操作不支援
//    case saveRefuse = "2001" // 存取被拒
//    case updateFail = "2002" // 更新失敗
//    case exceedMax = "2003" // 超過上限
//    case opNotAllowed = "2004" // 操作不允許 （ex. 無權限)
//    case statusErr = "3000" // 狀態不正確
//    case appNotSupport = "3001" // APP版本不支援
//    case defaultStatus = "" // 自訂找不到狀態碼時

}
