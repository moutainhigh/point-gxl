package com.hds.xquark.dal.model;

import com.hds.xquark.dal.status.PointInfoStatus;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

import static java.math.BigDecimal.ZERO;

/**
 * created by
 *
 * @author wangxinhua at 18-6-14 下午4:47
 */
public abstract class BasePointCommTotal {

  /** id */
  private Long id;

  private Long cpId;

  private PointInfoStatus status;

  private Date createdAt;

  private Date updatedAt;

  /**
   * 从旧的积分信息上构造一个一样的对象
   *
   * @param oldInfo 旧的数据对象
   * @return 新的积分信息
   */
  public static <T extends BasePointCommTotal> T copy(T oldInfo) {
    @SuppressWarnings("unchecked")
    T newInstance = (T) getInstance(oldInfo.getClass());
    BeanUtils.copyProperties(oldInfo, newInstance);
    return newInstance;
  }

  private static <T extends BasePointCommTotal> T getInstance(Class<T> clazz) {
    T info;
    try {
      info = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      // class是抽象父类无法实例化或者缺少默认构造函数
      throw new RuntimeException("积分信息构造失败, 请确保class不是是抽象父类" + "且有默认构造函数", e);
    }
    return info;
  }

  /**
   * 初始化一个积分信息
   *
   * @param cpId 用户id
   * @return 空积分信息
   */
  public static <T extends BasePointCommTotal> T emptyInfo(Long cpId, Class<T> clazz) {
    T info = getInstance(clazz);
    info.setCpId(cpId);
    info.setUsableHds(ZERO);
    info.setUsableEcomm(ZERO);
    info.setUsableViviLife(ZERO);
    info.setFreezedHds(ZERO);
    info.setFreezedViviLife(ZERO);
    info.setFreezedEcomm(ZERO);
    info.setNoWithdrawalHds(ZERO);
    info.setNoWithdrawalViviLife(ZERO);
    info.setNoWithdrawalEcomm(ZERO);
    info.setStatus(PointInfoStatus.ACTIVE);
    return info;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCpId() {
    return cpId;
  }

  public void setCpId(Long cpId) {
    this.cpId = cpId;
  }

  public abstract BigDecimal getUsableHds();

  public abstract void setUsableHds(BigDecimal usableHds);

  public abstract BigDecimal getUsableViviLife();

  public abstract void setUsableViviLife(BigDecimal usableViviLife);

  public abstract BigDecimal getUsableEcomm();

  public abstract void setUsableEcomm(BigDecimal usableEcomm);

  public abstract BigDecimal getFreezedHds();

  public abstract void setFreezedHds(BigDecimal freezedHds);

  public abstract BigDecimal getFreezedViviLife();

  public abstract void setFreezedViviLife(BigDecimal freezedViviLife);

  public abstract BigDecimal getFreezedEcomm();

  public abstract void setFreezedEcomm(BigDecimal freezedEcomm);

  public abstract BigDecimal getNoWithdrawalHds();

  public abstract void setNoWithdrawalHds(BigDecimal noWithdrawalHds);

  public abstract BigDecimal getNoWithdrawalViviLife();

  public abstract void setNoWithdrawalViviLife(BigDecimal noWithdrawalViviLife);

  public abstract BigDecimal getNoWithdrawalEcomm();

  public abstract void setNoWithdrawalEcomm(BigDecimal noWithdrawalEcomm);

  public PointInfoStatus getStatus() {
    return status;
  }

  public void setStatus(PointInfoStatus status) {
    this.status = status;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  protected abstract BasePointCommTotal getInstance();

  /**
   * 获取三网总积分
   *
   * @return 三网总积分
   */
  public BigDecimal getTotal() {
    return getTotalUsable().add(getTotalFreeze());
  }

  public BigDecimal getTotalUsable() {
    return getUsableHds().add(getUsableViviLife()).add(getUsableEcomm()).add(getTotalNoWithdrawal());
  }

  public BigDecimal getTotalFreeze() {
    return getFreezedHds().add(getFreezedViviLife()).add(getFreezedEcomm());
  }

  public BigDecimal getTotalNoWithdrawal() {
    return getNoWithdrawalHds().add(getNoWithdrawalViviLife()).add(getNoWithdrawalEcomm());
  }

  public BigDecimal getTotalWithdrawal() {
    return getUsableHds().add(getUsableViviLife()).add(getUsableEcomm());
  }

  @Override
  public String toString() {
    return "BasePointCommTotal{"
        + "id="
        + id
        + ", cpId="
        + cpId
        + ", status="
        + status
        + ", createdAt="
        + createdAt
        + ", updatedAt="
        + updatedAt
        + '}';
  }
}
