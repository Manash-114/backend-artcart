package com.artcart.services.impl;

import com.artcart.exception.ResourceNotFoundException;
import com.artcart.model.*;
import com.artcart.repository.*;
import com.artcart.request.OrderReq;
import com.artcart.request.ProductAddToCartReq;
import com.artcart.response.CustomerOrderRes;
import com.artcart.response.SellerOrderRes;
import com.artcart.services.OrderService;
import com.artcart.services.SellerService;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepo orderRepo;
    private PaymentRepo paymentRepo;

    private ModelMapper modelMapper;

    private CustomerRepo customerRepo;

    private AddressRepo addressRepo;
    private ProductRepo productRepo;

    private SellerRepo sellerRepo;

    private ProductBelongsToOrderRepo productBelongsToOrderRepo;


    public OrderServiceImpl(OrderRepo orderRepo, PaymentRepo paymentRepo, ModelMapper modelMapper, CustomerRepo customerRepo, AddressRepo addressRepo, ProductRepo productRepo, SellerRepo sellerRepo, ProductBelongsToOrderRepo productBelongsToOrderRepo) {
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
        this.modelMapper = modelMapper;
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.productRepo = productRepo;
        this.sellerRepo = sellerRepo;
        this.productBelongsToOrderRepo = productBelongsToOrderRepo;
    }

    @Override
    public void createOrder(String customerId , OrderReq orderReq) {

        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("customer not found with id" + customerId));

        //find address
        Address address = addressRepo.findById(orderReq.getAddressId()).orElseThrow(() -> new ResourceNotFoundException("address not found with id " + orderReq.getAddressId()));

        Payment payment = new Payment();
        payment.setId(orderReq.getPaymentReq().getId());
        payment.setMode(orderReq.getPaymentReq().getMode());
        payment.setAmount(orderReq.getPaymentReq().getAmount());
        payment.setCustomer(customer);
        payment.setDate(LocalDateTime.now());

        Order order = new Order();
        order.setOrderId(orderReq.getOrderId());
        order.setPayment(payment);
        payment.setOrder(order);
        order.setAddress(address);
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");
        order.setOrderAmount(orderReq.getPaymentReq().getAmount());

        List<ProductAddToCartReq> productAddToCartReqList = orderReq.getProducts();


        List<ProductBelongsToOrder> productBelongsToOrders = new ArrayList<>();

        productAddToCartReqList.forEach((item)->{
            ProductBelongsToOrder p = new ProductBelongsToOrder();
            Product product = productRepo.findById(item.getProductId()).orElseThrow(() -> new ResourceNotFoundException("product not found with id" + item.getProductId()));
            p.setProducts(product);
            p.setOrders(order);
            p.setProductQuantity(item.getQuantity());
            p.setCourierName("notAssigned");
            p.setDeliveryStatus("ORDERED");
            p.setId(UUID.randomUUID().toString());
            productBelongsToOrders.add(p);

        });

        List<Seller> sellerList = new ArrayList<>();
        productBelongsToOrders.forEach((item)->{
            Product products = item.getProducts();
            Seller seller = sellerRepo.findByProducts(products);
            if(!sellerList.contains(seller))
                sellerList.add(seller);
        });

        order.setProducts(productBelongsToOrders);
        order.setSellers(sellerList);
        Order save = orderRepo.save(order);

    }

    @Override
    public List<CustomerOrderRes> getAllOrdersOfCustomer(String customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("customer not found with id" + customerId));
        List<Order> byCustomer = orderRepo.findByCustomer(customer);
        List<CustomerOrderRes> customerOrderResList = new ArrayList<>();

        byCustomer.forEach((o)->{
            CustomerOrderRes customerOrderRes = new CustomerOrderRes();
            customerOrderRes.setOrderId(o.getOrderId());
            customerOrderRes.setOrderDate(o.getOrderDate());
            customerOrderRes.setOrderAmount(o.getOrderAmount());
            customerOrderRes.setAddress(o.getAddress());
            customerOrderRes.setProductsBelongsToOrder(o.getProducts());
            customerOrderResList.add(customerOrderRes);
        });

        return customerOrderResList;
    }


    @Override
    public List<SellerOrderRes> getAllNewOrderOfSeller(Integer sellerId) {

        Seller seller = sellerRepo.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("seller not found with id" + sellerId));
        List<Order> orderBySellerId = orderRepo.findNewOrderBySellerId(seller.getId());
        System.out.println("Size = "+orderBySellerId.size());

        List<SellerOrderRes> resData = new ArrayList<>();

        orderBySellerId.forEach((o)->{
            SellerOrderRes sellerOrderRes = new SellerOrderRes();
            sellerOrderRes.setOrderId(o.getOrderId());
            System.out.println("status "+o.getStatus());
            List<ProductBelongsToOrder> byOrders = productBelongsToOrderRepo.findByOrders(o);

            List<ProductBelongsToOrder> productAccordingToSeller = new ArrayList<>();

            byOrders.forEach((p)->{
                if(p.getProducts().getSeller().getId() == sellerId) {
                    System.out.println("Product name " + p.getProducts().getName());
                    ProductBelongsToOrder map = modelMapper.map(p, ProductBelongsToOrder.class);
                    productAccordingToSeller.add(map);
                }
            });
            sellerOrderRes.setProductsBelongsToOrder(productAccordingToSeller);
            sellerOrderRes.setAddress(o.getAddress());
            resData.add(sellerOrderRes);
        });

    return resData;


    }

    @Override
    public List<SellerOrderRes>  getAllOrderOfSeller(Integer sellerId) {
        Seller seller = sellerRepo.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("seller not found with id" + sellerId));
        List<Order> orderBySellerId = orderRepo.findAllOrderBySellerId(seller.getId());
        List<SellerOrderRes> resData = new ArrayList<>();

        orderBySellerId.forEach((o)->{

            SellerOrderRes sellerOrderRes = new SellerOrderRes();
            sellerOrderRes.setOrderId(o.getOrderId());
            System.out.println("status "+o.getStatus());
            List<ProductBelongsToOrder> byOrders = productBelongsToOrderRepo.findByOrders(o);

            List<ProductBelongsToOrder> productAccordingToSeller = new ArrayList<>();

            byOrders.forEach((p)->{
                if(p.getProducts().getSeller().getId() == sellerId) {
                    System.out.println("Product name " + p.getProducts().getName());
                    ProductBelongsToOrder map = modelMapper.map(p, ProductBelongsToOrder.class);
                    productAccordingToSeller.add(map);
                }
            });
            sellerOrderRes.setProductsBelongsToOrder(productAccordingToSeller);
            sellerOrderRes.setAddress(o.getAddress());
            resData.add(sellerOrderRes);
        });

        return resData;

    }

    @Override
    public void modifyOrder(String orderId, OrderReq newOrderReq) {

    }

    @Override
    public void deleteOrder(String orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order not found with id" + orderId));
        orderRepo.delete(order);
    }
}
