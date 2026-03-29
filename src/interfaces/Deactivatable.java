package interfaces;

public interface Deactivatable {
    void deactivate(String reason);
    void activate();
    boolean isActive();
    String getStatus();
}
