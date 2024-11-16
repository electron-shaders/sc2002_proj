package observer;

/**
 * The ISubscriber interface defines the methods that a subscriber in the observer pattern must implement.
 */
public interface ISubscriber {
    /**
     * This method is called by publisher to update the subscriber with a new notification.
     *
     * @param notification The new notification
     */
    public void update(Notification notification);
}
