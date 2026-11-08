package com.ims12.exchange.api;
import com.ims12.exchange.api.model.ExchangeRates;
import com.ims12.exchange.api.model.Token;
import com.ims12.exchange.api.model.Transaction;
import com.ims12.exchange.api.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.util.List;


public interface Exchange {
    @POST("/user")
    Call<User> addUser(@Body User user);
    @POST("/authentication")
    Call<Token> authenticate(@Body User user);
    @GET("/exchangeRate")
    Call<ExchangeRates> getExchangeRates();
    @POST("/transaction")
    Call<Object> addTransaction(@Body String transaction, Transaction transaction1);
    @GET("/transaction")
    Call<List<Transaction>> getTransactions(@Header("Authorization") String authorization);

}
