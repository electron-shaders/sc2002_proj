package observer;

public class Notification {
    private String subjectName;
    private String subjectId;
    private String verb;
    private String objectName;
    private String objectId;
    private String message;

    public Notification(String subjectName, String subjectId, String verb, String objectName, String objectId) {
        this.subjectName = subjectName;
        this.subjectId = subjectId;
        this.verb = verb;
        this.objectName = objectName;
        this.objectId = objectId;
    }

    public Notification(String message) {
        this.message = message;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getObjectId() {
        return objectId;
    }

    public String toString() {
        if (message != null) {
            return message;
        }
        return subjectName + " (" + subjectId + ") " + verb + " " + objectName + " (" + objectId + ")";
    }
}
