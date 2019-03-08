package com.hds.xquark.dal.type;

import com.hds.xquark.dal.model.*;

/**
 * created by
 *
 * @author wangxinhua at 18-6-15 下午12:46 积分运作类 目前支持积分/德分 用于区分积分/德分/其他
 */
public enum PointOperateType {
  POINT {
    @Override
    public Class<? extends BasePointCommTotal> getTotalClazz() {
      return PointTotal.class;
    }

    @Override
    public Class<? extends BasePointCommRecord> getRecordClazz() {
      return PointRecord.class;
    }
  },
  COMMISSION {
    @Override
    public Class<? extends BasePointCommTotal> getTotalClazz() {
      return CommissionTotal.class;
    }

    @Override
    public Class<? extends BasePointCommRecord> getRecordClazz() {
      return CommissionRecord.class;
    }
  };

  public abstract Class<? extends BasePointCommTotal> getTotalClazz();

  public abstract Class<? extends BasePointCommRecord> getRecordClazz();
}
