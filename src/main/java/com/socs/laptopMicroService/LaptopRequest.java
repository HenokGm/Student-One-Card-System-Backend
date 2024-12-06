package com.socs.laptopMicroService;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LaptopRequest {

    private String serialNumber;
    private String studentId;
    private String brand;
    private String color;
    private String model;
    private String picture;
}
