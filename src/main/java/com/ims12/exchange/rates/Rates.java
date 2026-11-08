package com.ims12.exchange.rates;

import com.ims12.exchange.Authentication;
import com.ims12.exchange.api.ExchangeService;
import com.ims12.exchange.api.model.ExchangeRates;
import com.ims12.exchange.api.model.Transaction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rates {
    public Label buyUsdRateLabel;
    public Label sellUsdRateLabel;
    public TextField lbpTextField;
    public TextField usdTextField;
    public ToggleGroup transactionType;

    public void initialize() {
        fetchRates();
    }

    private void fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(new Callback<ExchangeRates>() {
            @Override
            public void onResponse(Call<ExchangeRates> call, Response<ExchangeRates> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ExchangeRates exchangeRates = response.body();
                    Platform.runLater(() -> {
                        buyUsdRateLabel.setText(exchangeRates.lbpToUsd.toString());
                        sellUsdRateLabel.setText(exchangeRates.usdToLbp.toString());
                    });
                } else {
                    System.err.println("Failed to fetch rates. HTTP Status: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ExchangeRates> call, Throwable throwable) {
                System.err.println("Network error: " + throwable.getMessage());
            }
        });
    }

    public void addTransaction(ActionEvent actionEvent) {
        try {
            String usdInput = usdTextField.getText().trim();
            String lbpInput = lbpTextField.getText().trim();
            if (!usdInput.matches("^\\d+(\\.\\d{1,2})?$") || !lbpInput.matches("^\\d+(\\.\\d{1,2})?$")) {
                throw new IllegalArgumentException("Invalid currency values");
            }
            float usdAmount = Float.parseFloat(usdInput);
            float lbpAmount = Float.parseFloat(lbpInput);
            if (usdAmount <= 0 || lbpAmount <= 0) {
                throw new IllegalArgumentException("Amounts must be greater than zero.");
            }
            boolean isSellingUsd = ((RadioButton) transactionType.getSelectedToggle()).getText().equals("Sell USD");
            Transaction transaction = new Transaction(usdAmount, lbpAmount, isSellingUsd);

            // Get authentication token if available
            String userToken = Authentication.getInstance().getToken();
            String authHeader = userToken != null ? "Bearer " + userToken : null;

            ExchangeService.exchangeApi().addTransaction(authHeader, transaction).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    fetchRates();
                    Platform.runLater(() -> {
                        usdTextField.setText("");
                        lbpTextField.setText("");
                    });
                }
                @Override
                public void onFailure(Call<Object> call, Throwable throwable) {
                    System.err.println("Transaction failed: " + throwable.getMessage());
                }
            });
        } catch (IllegalArgumentException e) {
            System.err.println("Input error: " + e.getMessage());
        }
    }
}