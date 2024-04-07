package com.artcart.services.impl;

import com.artcart.exception.ResourceNotFoundException;
import com.artcart.model.Address;
import com.artcart.model.Customer;
import com.artcart.repository.AddressRepo;
import com.artcart.repository.CustomerRepo;
import com.artcart.request.AddressReq;
import com.artcart.response.AddressRes;
import com.artcart.services.CustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepo customerRepo;
    private AddressRepo addressRepo;

    public CustomerServiceImpl(CustomerRepo customerRepo, AddressRepo addressRepo) {
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepo.findByEmail(email);
    }

    @Override
    public AddressRes addNewAddress(String customerId, AddressReq addressReq) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("customer not found with id" + customerId));
        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setCustomer(customer);
        address.setCity(addressReq.getCity());
        address.setState(addressReq.getState());
        address.setZipCode(addressReq.getZipCode());
        address.setStreet(addressReq.getStreet());
        Address save = addressRepo.save(address);

        AddressRes addressRes = new AddressRes();
        addressRes.setId(save.getId());
        addressRes.setCity(save.getCity());
        addressRes.setStreet(save.getStreet());
        addressRes.setState(save.getState());
        addressRes.setZipCode(save.getZipCode());
        return  addressRes;
    }

    @Override
    public AddressRes getSingleAddress(String customerId, String addressId) {
        return null;
    }

    @Override
    public List<AddressRes> getAllAddress(String customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("customer not found with id" + customerId));

        List<Address> addressList = addressRepo.findByCustomer(customer);
        List<AddressRes> addressResList = new ArrayList<>();
        addressList.forEach((item)->{
            AddressRes addressRes = new AddressRes();
            addressRes.setId(item.getId());
            addressRes.setCity(item.getCity());
            addressRes.setStreet(item.getStreet());
            addressRes.setState(item.getState());
            addressRes.setZipCode(item.getZipCode());
            addressResList.add(addressRes);
        });

        return  addressResList;

    }
}
