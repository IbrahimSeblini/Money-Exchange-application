package com.ims12.exchange.register;

import com.ims12.exchange.Authentication;
import com.ims12.exchange.api.ExchangeService;
import com.ims12.exchange.api.model.Token;
import com.ims12.exchange.api.model.User;
import com.ims12.exchange.OnPageCompleteListener;
import com.ims12.exchange.PageCompleter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.awt.*;

public class Register implements PageCompleter {
    public TextField usernameTextField;
    public TextField passwordTextField;
    private OnPageCompleteListener onPageCompleteListener;

    @Override
    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }

    public void register(ActionEvent actionEvent) {
        User user = new User(usernameTextField.getText(),
                passwordTextField.getText());
        ExchangeService.exchangeApi().addUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                ExchangeService.exchangeApi().authenticate(user).enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        Authentication.getInstance().saveToken(response.body().getToken());
                        Platform.runLater(() -> {
                            if (onPageCompleteListener != null) {
                                onPageCompleteListener.onPageCompleted();
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<Token> call, Throwable throwable) {
                    }
                });
            }
            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
            }
        });
    }
}