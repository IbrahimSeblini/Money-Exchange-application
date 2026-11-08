module com.ims12.exchange {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires java.sql;
    requires gson;
    requires retrofit2.converter.gson;
    requires java.desktop;
    requires java.prefs;

    opens com.ims12.exchange to javafx.fxml;
    opens com.ims12.exchange.api to gson;
    exports com.ims12.exchange;
    opens com.ims12.exchange.api.model to javafx.base, gson;
    exports com.ims12.exchange.rates;
    opens com.ims12.exchange.rates to javafx.fxml;
    opens com.ims12.exchange.login to javafx.fxml;
    opens com.ims12.exchange.register to javafx.fxml;
    opens com.ims12.exchange.transactions to javafx.fxml;

}