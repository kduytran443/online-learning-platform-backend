package com.kduytran.olpcommon.service;

import java.security.PublicKey;

public interface AbstractJwtService {
    PublicKey getPublicKey();
    void init();
}
