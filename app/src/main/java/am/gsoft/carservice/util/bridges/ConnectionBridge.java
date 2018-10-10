package am.gsoft.carservice.util.bridges;

public interface ConnectionBridge {

    boolean checkNetworkAvailableWithError();

    boolean isNetworkAvailable();
}