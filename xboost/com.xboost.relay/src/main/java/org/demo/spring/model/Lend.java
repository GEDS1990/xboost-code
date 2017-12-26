package org.demo.spring.model;

/**
 * Created by Administrator on 2017/6/28 0028.
 */
public class Lend {
    public Long id;
    public String lendCode;
    public String lendName;
    public String lendDesc;
    public String lendInterest;
    public int lendInterestType;
    public int lendPersonId;
    public String lendTime;
    public int status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLendCode() {
        return lendCode;
    }

    public void setLendCode(String lendCode) {
        this.lendCode = lendCode;
    }

    public String getLendName() {
        return lendName;
    }

    public void setLendName(String lendName) {
        this.lendName = lendName;
    }

    public String getLendDesc() {
        return lendDesc;
    }

    public void setLendDesc(String lendDesc) {
        this.lendDesc = lendDesc;
    }

    public String getLendInterest() {
        return lendInterest;
    }

    public void setLendInterest(String lendInterest) {
        this.lendInterest = lendInterest;
    }

    public int getLendInterestType() {
        return lendInterestType;
    }

    public void setLendInterestType(int lendInterestType) {
        this.lendInterestType = lendInterestType;
    }

    public int getLendPersonId() {
        return lendPersonId;
    }

    public void setLendPersonId(int lendPersonId) {
        this.lendPersonId = lendPersonId;
    }

    public String getLendTime() {
        return lendTime;
    }

    public void setLendTime(String lendTime) {
        this.lendTime = lendTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
