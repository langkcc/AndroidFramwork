package com.juefeng.android.framework.http;

import com.juefeng.android.framework.common.base.BaseEntity;
import com.juefeng.android.framework.http.annotations.BaseElement;
import com.juefeng.android.framework.http.annotations.Key;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/29
 * Time: 15:14
 * Description:
 */
@BaseElement(key = "encrypto")
public class EncryptoEntity extends BaseEntity {



    /**
     * encrypto flag
     */
    @Key(key = "encrypto_flag")
    private boolean encrypto;

    /**
     * encrypto text
     */
    @Key(key = "encrypto_text")
    private String encryptoText;


    public EncryptoEntity() {
    }

    public EncryptoEntity(boolean encrypto, String encryptoText) {
        this.encrypto = encrypto;
        this.encryptoText = encryptoText;
    }

    public String getEncryptoText() {
        return encryptoText;
    }

    public void setEncryptoText(String encryptoText) {
        this.encryptoText = encryptoText;
    }

    public boolean isEncrypto() {
        return encrypto;
    }

    public void setEncrypto(boolean encrypto) {
        encrypto = encrypto;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EncryptoEntity){
            if (obj.hashCode() == this.hashCode()){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "encrypto :" + encrypto + ",encryptoText :" + encryptoText;
    }
}
