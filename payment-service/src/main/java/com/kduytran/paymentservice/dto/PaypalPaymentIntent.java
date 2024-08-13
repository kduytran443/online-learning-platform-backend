package com.kduytran.paymentservice.dto;

import lombok.Getter;

@Getter
public enum PaypalPaymentIntent {
	SALE, AUTHORIZE, ORDER
}
