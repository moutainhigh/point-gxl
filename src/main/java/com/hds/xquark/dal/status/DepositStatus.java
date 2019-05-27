package com.hds.xquark.dal.status;

public enum DepositStatus {
  CREATED(1),

  IN_PROGRESSING(2),

  SUCCESS(3),

  ERROR(4);

  private final int code;

  DepositStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
