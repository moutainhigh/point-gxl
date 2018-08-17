package com.hds.xquark.dal.status;

public enum DepositStatus {
  CREATED(0), SUCCEED(1), FAILED(4);

  private final int code;

  DepositStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

}
