package com.example.intuitmoviebooking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Document("users")
public class User {

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userBookings")
    private List<Booking> userBookings;

    @Id
    @NotNull
    @JsonProperty("mailId")
    private String mailId;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

//    public User(String userName, String mailId) {
//        this.userName = userName;
//        this.mailId = mailId;
//    }
}
