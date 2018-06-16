package com.hds.xquark.service.error;

/**
 * 业务逻辑异常。抛向前端，方便不同的客户端，不同的处理方式
 *
 * @author jamesp
 */
public class BizException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final GlobalErrorCode errorCode;

  public BizException(GlobalErrorCode ec, String message, Throwable cause) {
    super(message, cause);
    errorCode = ec;
  }

  public BizException(GlobalErrorCode ec) {
    this(ec, ec.getError());
  }

  public BizException(GlobalErrorCode ec, String message) {
    this(ec, message, null);
  }

  public BizException(GlobalErrorCode ec, Throwable cause) {
    this(ec, null, cause);
  }

  public GlobalErrorCode getErrorCode() {
    return errorCode;
  }
}
