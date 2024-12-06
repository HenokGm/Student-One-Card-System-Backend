package com.socs.laptopMicroService;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "laptop") // MongoDB collection name
public class Laptop {

        @Id
        private String serialNumber; // MongoDB uses String for _id by default
        private String studentId;
        private String brand;
        private String color;
        private String model;
        private String picture;

        @CreatedDate
        @Field(name = "createDate", targetType = FieldType.DATE_TIME)
        private LocalDateTime createDate;

        @LastModifiedDate
        @Field(name = "lastModified", targetType = FieldType.DATE_TIME)
        private LocalDateTime lastModified;

        @CreatedBy
        @Field(name = "createdBy")
        private String createdBy; // Use String if the ID is not Integer

        @LastModifiedBy
        @Field(name = "lastModifiedBy")
        private String lastModifiedBy; // Use String if the ID is not Integer
}
