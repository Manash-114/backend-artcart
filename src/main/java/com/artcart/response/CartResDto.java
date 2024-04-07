package com.artcart.response;

import com.artcart.model.Customer;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResDto {

    private String id;
    private Integer price;
    private String customerId;
}
