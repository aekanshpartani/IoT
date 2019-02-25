package com.example.sinha.iot;

/**
 * Created by Seotoolzz on 13/6/17.
 */

public class list {


    private String name;

    private String message,bookingTime;

    private int imageId;



    public list(String name, String message , String bookingTime,int imageId) {
        this.name = name;
        this.message = message;
        this.bookingTime = bookingTime;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return message;
    }

    public String getBookingTime() {
        return bookingTime;
    }

}
