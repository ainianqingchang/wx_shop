package com.ainian.wxapp.service;


import com.ainian.wxapp.dto.Order;
import com.ainian.wxapp.dto.OrderProductKey;
import com.ainian.wxapp.dto.Product;
import com.ainian.wxapp.dto.User;
import com.ainian.wxapp.exception.WebException;
import com.ainian.wxapp.finvar.OrderStatusEnum;
import com.ainian.wxapp.repository.OrderRepository;
import com.ainian.wxapp.repository.ProductRepository;
import com.ainian.wxapp.utils.MD5Utils;
import com.ainian.wxapp.vo.OrderStatus;
import com.ainian.wxapp.vo.ProductStatus;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PayService {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Transactional
    public JSONObject pay(Long orderId, User user) throws Exception {
        checkOrderValid(orderId,user);
        OrderStatus orderStatus = orderService.checkOrderStock(orderId);
        Order order = orderRepository.findById(orderId).orElse(null);
        if(!orderStatus.getPass()){
            return JSONObject.parseObject(JSONObject.toJSONString(orderStatus));
        }
        String openId=user.getOpenId();
        Map<String, String> stringStringMap =makePreOrder(orderId,order.getOrderNo(), order.getTotalPrice());
        Map<String,Object> hashMap=new HashMap<>();
        for(Map.Entry<String, String> entry : stringStringMap.entrySet()){
            hashMap.put(entry.getKey(),entry.getValue());
        }
        JSONObject json=new JSONObject(hashMap);
        return json;
    }

    private Map<String, String> makePreOrder(Long orderId,String orderNo, BigDecimal totalPrice) throws Exception {
        MyConfig myConfig=new MyConfig();
        WXPay wxpay = new WXPay(myConfig);

        Map<String, String> data = new TreeMap<>();
        data.put("body", "支付订单");
        data.put("out_trade_no", orderNo);
        data.put("device_info", "JSAPI");
        data.put("fee_type", "CNY");
        BigDecimal bigDecimal = new BigDecimal(100);
        data.put("total_fee", totalPrice.multiply(bigDecimal).toString());
        data.put("notify_url", "http://wx_app.ahut.work/api/v1/pay/notify");


        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            recordPrepareId(resp,orderId);
            sign(resp);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sign(Map<String, String> resp){
        String key="jasidfosdfasdf";
        String wxPaySign = MD5Utils.createWxPaySign(key, (SortedMap<String, String>) resp);
        resp.put("paySign",wxPaySign);
    }

    private void recordPrepareId(Map<String, String> resp,Long orderId){
        String prepayId=resp.get("prepay_id");
        Order order = orderRepository.findById(orderId).orElse(null);
        order.setPrepayId(prepayId);
    }



    private void checkOrderValid(Long orderId,User user){
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order==null)
            throw new WebException("订单不存在");
        if(!order.getUser().getId().equals(user.getId())){
            throw new WebException("用户不匹配");
        }
        if(order.getStatus().equals(OrderStatusEnum.PAID.getCode())){
            throw new WebException("订单已经完成");
        }
    }

    public void receiveNotify(Map<String, String> params) {
        String resultCode=params.get("result_code");
        if(resultCode.equals("SUCCESS")){
            String orderNo=params.get("out_trade_no");
            Order o = orderRepository.findByOrderNo(orderNo);
            OrderStatus orderStatus = orderService.checkOrderStock(o.getId());
            if(orderStatus.getPass()){
                updateOrderStatus(o.getId(),true);
                reduceStock(orderStatus);
            }else{
                updateOrderStatus(o.getId(),false);
            }
        }
    }
    private void updateOrderStatus(Long orderId,Boolean success){
        OrderStatusEnum status=success?
                OrderStatusEnum.PAID:
                OrderStatusEnum.PAID_BUT_OUT_OF;
        Order o = orderRepository.findById(orderId).orElse(null);
        o.setStatus(status.getCode());
        orderRepository.save(o);
    }

    private void reduceStock(OrderStatus orderStatus){
        List<ProductStatus> productStatuses =orderStatus.getpStatus();
        for(ProductStatus pS:productStatuses){
            Product one = productRepository.getOne(pS.getId());
            one.setStock(one.getStock()-pS.getCount());
            productRepository.save(one);
        }
    }


}
