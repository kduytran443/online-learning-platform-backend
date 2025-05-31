package com.kduytran.authmanagementservice.event;

import com.kduytran.authmanagementservice.entity.SignUpEntity;

public class UserRegisteredEvent extends BaseEvent {

    public UserRegisteredEvent(SignUpEntity source) {
        super(source);
    }

    @Override
    public SignUpEntity getSource() {
        return (SignUpEntity) this.source;
    }
}
