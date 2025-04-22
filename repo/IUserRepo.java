package repo;

import model.*;

/**
 * An interface for user repositories that manage user objects.
 * Extends {@code IRepo} to support CSV operations.
 *
 * @param <T> The type of user (must extend {@code User}).
 */
public interface IUserRepo<T extends User> extends IRepo {

    /**
     * Creates a new user and adds it to the repository.
     * The implementation should handle user input and validation.
     */
    public void createUser();

    /**
     * Deletes a user from the repository based on their NRIC.
     *
     * @param nric The NRIC of the user to be deleted.
     */
    public void deleteUser(String nric);

    /**
     * Retrieves a user by their NRIC.
     *
     * @param nric The NRIC of the user to retrieve.
     * @return The user associated with the given NRIC, or {@code null} if not found.
     */
    public T getUser(String nric);

    /**
     * Adds an existing user to the repository.
     *
     * @param user The user object to add.
     */
    public void addUser(T user);
}