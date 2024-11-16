package view;

/**
 * The IView interface represents a view component in the application.
 * The view classes implementing this interface should provide the implementation
 * for the launch method, which is the entry point of a view.
 */
public interface IView{
    /**
     * Launches the view.
     */
    public void launch();
}