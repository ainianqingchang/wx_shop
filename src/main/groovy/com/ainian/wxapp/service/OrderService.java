package com.ainian.wxapp.service;

import com.ainian.wxapp.dto.*;
import com.ainian.wxapp.exception.WebException;
import com.ainian.wxapp.finvar.ErrorCode;
import com.ainian.wxapp.repository.OrderProductRepository;
import com.ainian.wxapp.repository.OrderRepository;
import com.ainian.wxapp.repository.ProductRepository;
import com.ainian.wxapp.repository.UserAddressRepository;
import com.ainian.wxapp.utils.CommonUtils;
import com.ainian.wxapp.vo.OProduct;
import com.ainian.wxapp.vo.OrderSnap;
import com.ainian.wxapp.vo.OrderStatus;
import com.ainian.wxapp.vo.ProductStatus;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserAddressRepository userAddressRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Transactional
    public OrderStatus place(User user, List<OProduct> oProducts){
        List<Long> ids=  oProducts.stream().map(OProduct::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(ids);
        OrderStatus orderStatus = getOrderStatus(products, oProducts);
        if(!orderStatus.getPass()){
            return orderStatus;
        }
        //开始创建订单
        OrderSnap orderSnap = snapOrder(user, orderStatus, products);
        Order order = createOrder(user, orderSnap, oProducts);
        orderStatus.setOrderId(order.getId());
        orderStatus.setOrderNo(order.getOrderNo());
        orderStatus.setCreateTime(order.getCreateTime());
        return orderStatus;
    }

    public OrderStatus checkOrderStock(Long orderId){
        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);
        List<OProduct> oProducts = orderProducts.stream().map(e -> {
            OProduct o = new OProduct();
            o.setOrderId(e.getId().getOrder().getId());
            o.setProductId(e.getId().getProduct().getId());
            return o;
        }).collect(Collectors.toList());
        List<Long> ids=  oProducts.stream().map(OProduct::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(ids);
        OrderStatus orderStatus = getOrderStatus(products, oProducts);
        return orderStatus;
    }


    private Order createOrder(User user, OrderSnap orderSnap, List<OProduct> oProducts){
        Order order=new Order();
        String orderNo = CommonUtils.makeOrderNo();
        order.setOrderNo(orderNo);
        order.setUser(user);
        order.setSnapAddress(JSONObject.toJSONString( orderSnap.getUserAddress()));
        order.setTotalPrice(orderSnap.getOrderPrice());
        order.setTotalCount(orderSnap.getTotalCount());
        order.setSnapImg(orderSnap.getSnapImg());
        order.setSnapName(orderSnap.getSnapName());
        order.setSnapItems(JSONObject.toJSONString(orderSnap.getpStatus()));
        orderRepository.saveAndFlush(order);
        List<OrderProduct> orderProducts=new ArrayList<>();

        for(OProduct p:oProducts){
            p.setOrderId(order.getId());

            OrderProduct op=new OrderProduct();
            Order o=new Order();
            o.setId(p.getOrderId());
            Product p1=new Product();
            p1.setId(p.getProductId());

            OrderProductKey key=new OrderProductKey(o,p1);
            op.setId(key);
            op.setCount(p.getCount());
            orderProducts.add(op);
        }

        orderProductRepository.saveAll(orderProducts);
        return order;

    }

    
    private OrderSnap snapOrder(User user,OrderStatus orderStatus,List<Product> products){
        OrderSnap orderSnap=new OrderSnap();
        orderSnap.setOrderPrice(orderStatus.getOrderPrice());
        orderSnap.setTotalCount(orderStatus.getTotalCount());
        orderSnap.setpStatus(orderStatus.getpStatus());
        UserAddress userAddress=userAddressRepository.findByUser(user);
        if(userAddress==null) throw new WebException("用户地址不存在");
        orderSnap.setUserAddress(userAddress);
        orderSnap.setSnapName(products.get(0).getName());
        orderSnap.setSnapImg(products.get(0).getMainImgUrl());
        if(products.size()>1){
            orderSnap.setSnapName(orderSnap.getSnapImg()+"等");
        }
        return orderSnap;
    }

    private OrderStatus getOrderStatus(List<Product>  products,List<OProduct> oProducts){


        OrderStatus orderStatus=new OrderStatus();
        for(OProduct p:oProducts){
            ProductStatus productStatus = getProductStatus(p.getProductId(), p.getCount(), products);
            if(!productStatus.getHasStock()){
                orderStatus.setPass(false);
            }
            orderStatus.setOrderPrice(orderStatus.getOrderPrice().add(productStatus.getTotalPrice()));
            orderStatus.setTotalCount(orderStatus.getTotalCount()+productStatus.getCount());
            orderStatus.getpStatus().add(productStatus);
        }
        return orderStatus;
    }

    private ProductStatus getProductStatus(Long oPid,Long oCount,List<Product> products){
        int pIndex=-1;
        ProductStatus productStatus=new ProductStatus();
        for(int i=0;i<products.size();i++){
            if(oPid.equals(products.get(i).getId())){
                pIndex=i;
            }

        }
        if(pIndex==-1){
            throw new WebException(ErrorCode.ORDER_NOT_FOUND);
        }else{
            Product p=products.get(pIndex);
            productStatus.setId(p.getId());
            productStatus.setCount(oCount);
            BigDecimal boCount=new BigDecimal(oCount);
            productStatus.setTotalPrice(p.getPrice().multiply(boCount));
            if(p.getStock()-oCount>=0){
                productStatus.setHasStock(true);
            }
        }
        
        return productStatus;
    }
}
