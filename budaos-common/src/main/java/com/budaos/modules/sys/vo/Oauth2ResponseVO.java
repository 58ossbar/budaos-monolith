package com.budaos.modules.sys.vo;

import java.io.Serializable;

/**
 * @author huj
 * @create 2019-08-15 14:59
 * @email hujun@budaos.com
 * @company 创蓝科技 www.budaos.com
 */
public class Oauth2ResponseVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String access_token;

    private String token_type;

    private String refresh_token;

    private Long expires_in;

    private String scope;

    /**
     * 错误类型，可能有：invalid_grant 等错误
     */
    private String error;

    /**
     * 异常时的描述，可能是：Bad credentials 等错误信息
     */
    private String error_description;

    public Oauth2ResponseVO() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    @Override
    public String toString() {
        return "Oauth2ResponseVO{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", expires_in=" + expires_in +
                ", scope='" + scope + '\'' +
                ", error='" + error + '\'' +
                ", error_description='" + error_description + '\'' +
                '}';
    }
}
