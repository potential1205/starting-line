package com.example.gogoma.data.dto

data class KakaoPayReadyRequest(
    val orderId: String,
    val itemName: String,
    val totalAmount: String
)

data class KakaoPayReadyResponse(
    val tid: String,
    val next_redirect_app_url: String,
    val next_redirect_mobile_url: String,
    val next_redirect_pc_url: String
)

data class KakaoPayApproveRequest(
    val orderId: String,
    val tid: String,
    val pgToken: String
)

data class KakaoPayApproveResponse(
    val aid: String,
    val tid: String,
    val cid: String,
    val partner_order_id: String,
    val partner_user_id: String,
    val payment_method_type: String,
    val item_name: String,
    val item_code: String,
    val quantity: Int,
    val created_at: String,
    val approved_at: String,
    val payload: String
)