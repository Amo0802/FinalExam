package com.example.FinalExam.product.validators;

import com.example.FinalExam.exceptions.ErrorMessages;
import com.example.FinalExam.exceptions.ProductNotValidException;
import com.example.FinalExam.product.model.Product;
import org.springframework.util.StringUtils;

public class ProductValidator {

    public static void execute(Product product) throws ProductNotValidException {
        if(StringUtils.isEmpty(product.getName())){
            throw new ProductNotValidException(ErrorMessages.NAME_REQUIRED.getMessage());
        }

        if(product.getDescription().length() < 20){
            throw new ProductNotValidException(ErrorMessages.DESCRIPTION_LENGTH.getMessage());
        }

        if(product.getPrice() == null || product.getPrice() < 0.00){
            throw new ProductNotValidException(ErrorMessages.PRICE_CANNOT_BE_NEGATIVE.getMessage());
        }

        if(product.getManufacturer().isEmpty()){
            throw new ProductNotValidException(ErrorMessages.MANUFACTURER_REQUIRED.getMessage());
        }

        if(product.getRegion() == null){
            throw new ProductNotValidException(ErrorMessages.REGION_REQUIRED.getMessage());
        }
    }
}
