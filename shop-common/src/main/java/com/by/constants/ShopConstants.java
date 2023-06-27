package com.by.constants;

public interface ShopConstants {

    String GOODS_QUEUE = "goods-queue";

    String GOODS_EXCHANGE = "goods-exchange";

    String GOODS_ROUTING_KEY = "goods.add";

    String EMAIL_EXCHANGE = "email-exchange";

    String EMAIL_QUEUE = "email-queue";

    String EMAIL_ROUTING_KEY = "email.send";

    String SSO_REGISTER_KEY = "register_";

    String JWT_SIGN = "shoe-city";

    String ITEM_QUEUE = "item-queue";

    String ANON_ID = "SHOP-ANON_ID";

    String ORDER_EXCHANGE = "order-exchange";

    String ORDER_QUEUE = "order-ttl-queue";

    String ORDER_DLE_EXCHANGE = "order-dle-exchange";

    String ORDER_DLE_QUEUE = "order-dle-queue";
}
