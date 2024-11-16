package observer;

/**
 * The IPublisher interface defines the methods that a publisher in the observer pattern must implement.
 */
public interface IPublisher {
    /**
     * Adds a given subscriber to the publisher's subscriber list.
     *
     * @param subscriber The subscriber to be added
     */
    public void subscribe(ISubscriber subscriber);

    /**
     * Removes a given subscriber from the publisher's subscriber list.
     *
     * @param subscriber The subscriber to be removed
     */
    public void unsubscribe(ISubscriber subscriber);

    /**
     * Notifies all subscribers of this publisher with the given notification.
     *
     * @param notification The notification to be sent to subscribers
     */
    public void notifySubscribers(Notification notification);
}
