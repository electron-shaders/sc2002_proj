package observer;

/**
 * The Notification class represents a notification message with details about
 * the subject and object involved in an action.
 */
public class Notification {
    /**
     * The name of the subject that this notification is related to.
     * Example: "Patient"
     */
    private String subjectName;

    /**
     * The unique identifier for the subject.
     */
    private String subjectId;

    /**
     * The action or state described by this notification.
     * Example: "created"
     */
    private String verb;

    /**
     * The name of the object that the subject casts action to.
     * Example: "Appointment"
     */
    private String objectName;


    /**
     * The unique identifier for the object.
     */
    private String objectId;

    /**
     * A custom message that does not follow the subject-verb-object pattern.
     */
    private String message;

    /**
     * Constructs a new Notification with relevant details about the subject and object.
     *
     * @param subjectName The name of the subject
     * @param subjectId The ID of the subject
     * @param verb The action performed by the subject
     * @param objectName The name of the object
     * @param objectId The ID of the object
     */
    public Notification(String subjectName, String subjectId, String verb, String objectName, String objectId) {
        this.subjectName = subjectName;
        this.subjectId = subjectId;
        this.verb = verb;
        this.objectName = objectName;
        this.objectId = objectId;
    }

    /**
     * Constructs a new Notification with a custom message.
     *
     * @param message The custom message
     */
    public Notification(String message) {
        this.message = message;
    }

    /**
     * Retrieves the name of the subject.
     *
     * @return The name of the subject.
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * Retrieves the ID of the subject related to this notification.
     *
     * @return The ID of the subject.
     */
    public String getSubjectId() {
        return subjectId;
    }

    /**
     * Retrieves the name of the object.
     *
     * @return The name of the object.
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Retrieves the ID of the object.
     *
     * @return The ID of the object.
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Returns a structured message of the Notification object.
     * If the custom message is used to construct this Notification object, it returns it directly.
     * Otherwise, it structures the message in the format:
     * "subjectName (subjectId) verb objectName (objectId)".
     *
     * @return the structured message
     */
    public String toString() {
        if (message != null) {
            return message;
        }
        return subjectName + " (" + subjectId + ") " + verb + " " + objectName + " (" + objectId + ")";
    }
}
