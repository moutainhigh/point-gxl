package com.hds.xquark.service.error;

import java.util.HashMap;
import java.util.Map;

public enum GlobalErrorCode {
  //
  SUCESS(200, "Success"),

  //访问权限
  POINT_NOT_SUPPORT(8000000, "积分形式不支持"),

  POINT_NOT_ENOUGH(8000002, "积分不足"),

  COMMISSION_NOT_ENOUGH(8000003, "德分不足"),

  POINT_RECORD_NOT_FOUNT(8000004, "找不到积分记录"),

  COMMISSION_RECORD_NOT_FOUNT(8000005, "找不到德分记录"),

  //
  NOT_FOUND(404, "Resource not found"),
  //
  INTERNAL_ERROR(500, "Server internal error"),

  // 第三方应用调用openapi签名错误
  SIGN_ERROR(21001, "签名错误"),

  // 第三方应用调用openapi获取用户失败
  USER_NOT_EXIST(21002, "获取用户失败"),

  //
  INVALID_ARGUMENT(11001, "Invalid argument"),
  //错误的参数，原参数已修改， 页面需重新刷新
  INVALID_ARGUMENT_2(11002, "Invalid argument"),
  //
  THIRDPLANT_BUZERROR(700, "Business error"),

  //
  UNKNOWN(-1, "Unknown error");

  private static final Map<Integer, GlobalErrorCode> values = new HashMap<Integer, GlobalErrorCode>();

  static {
    for (GlobalErrorCode ec : GlobalErrorCode.values()) {
      values.put(ec.errorCode, ec);
    }
  }

  private int errorCode;
  private String error;

  private GlobalErrorCode(int errorCode, String error) {
    this.errorCode = errorCode;
    this.error = error;
  }

  public static GlobalErrorCode valueOf(int code) {
    GlobalErrorCode ec = values.get(code);
    if (ec != null) {
      return ec;
    }
    return UNKNOWN;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public String getError() {
    return error;
  }

  public String render() {
    return errorCode + ":" + error;
  }

}
