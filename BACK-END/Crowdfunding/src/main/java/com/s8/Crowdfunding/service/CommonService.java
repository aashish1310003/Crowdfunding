package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.exceptions.InvaildStatusException;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    public void checkStatus(String status){
        if(!(status.equals("APPROVED") || status.equals("REJECTED") || status.equals("PENDING") || status.equals("CREATED"))){
            throw new InvaildStatusException("Invalid status type");
        }
    }
}
