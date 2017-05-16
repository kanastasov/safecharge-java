package com.safecharge.request.builder;

import javax.validation.ConstraintViolationException;

import com.safecharge.model.MerchantInfo;
import com.safecharge.request.SafechargeRequest;
import com.safecharge.util.ChecksumUtils;
import com.safecharge.util.Constants;
import com.safecharge.util.RequestUtils;

/**
 * Copyright (C) 2007-2017 SafeCharge International Group Limited.
 *
 * @author <a mailto:nikolad@safecharge.com>Nikola Dichev</a>
 * @since 2/23/2017
 */
public abstract class SafechargeBuilder<T extends SafechargeBuilder<T>> {

    protected MerchantInfo merchantInfo;
    private String clientRequestId;
    private String internalRequestId;
    private String sessionToken;

    /**
     * @param sessionToken
     * @return
     */
    public T addSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
        return (T) this;
    }

    /**
     * @param clientRequestId
     * @return
     */
    public T addClientRequestId(String clientRequestId) {
        this.clientRequestId = clientRequestId;
        return (T) this;
    }

    public T addInternalRequestId(String internalRequestId) {
        this.internalRequestId = internalRequestId;
        return (T) this;
    }

    public T addMerchantInfo(MerchantInfo merchantInfo) {
        this.merchantInfo = merchantInfo;
        return (T) this;
    }

    public T addMerchantInfo(String merchantKey, String merchantId, String merchantSiteId, String serverHost, Constants.HashAlgorithm hashAlgorithm) {
        this.merchantInfo = new MerchantInfo(merchantKey, merchantId, merchantSiteId, serverHost, hashAlgorithm);
        return (T) this;
    }

    protected <T extends SafechargeRequest> T build(T safechargeRequest) {
        String timestamp = RequestUtils.calculateTimestamp();
        safechargeRequest.setMerchantId(merchantInfo != null ? merchantInfo.getMerchantId() : null);
        safechargeRequest.setMerchantSiteId(merchantInfo != null ? merchantInfo.getMerchantSiteId() : null);
        safechargeRequest.setServerHost(merchantInfo != null ? merchantInfo.getServerHost() : null);
        safechargeRequest.setSessionToken(sessionToken);
        safechargeRequest.setTimeStamp(timestamp);
        safechargeRequest.setClientRequestId(clientRequestId);
        safechargeRequest.setInternalRequestId(internalRequestId);
        safechargeRequest.setChecksum(
                ChecksumUtils.calculateChecksum(safechargeRequest, merchantInfo != null ? merchantInfo.getMerchantKey() : "", Constants.CHARSET_UTF8,
                        merchantInfo != null ? merchantInfo.getHashAlgorithm() : null));
        return safechargeRequest;
    }

    /**
     * @return
     */
    public abstract SafechargeRequest build() throws ConstraintViolationException;
}