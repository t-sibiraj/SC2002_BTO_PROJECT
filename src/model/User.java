package model;

/**
 * Represents a generic user in the system.
 * Stores personal information including NRIC, name, age, marital status, and password.
 */
public class User {

    /** The NRIC (unique ID) of the user. */
    private String nric;

    /** The password used for user authentication. */
    private String password;

    /** The full name of the user. */
    private String name;

    /** The age of the user. */
    private int age;

    /** Marital status of the user. */
    private boolean isMarried;

    /**
     * Constructs a new User with the given attributes.
     *
     * @param name The full name of the user.
     * @param nric The user's NRIC (unique identifier).
     * @param age The user's age.
     * @param isMarried Whether the user is married.
     * @param password The user's account password.
     */
    public User(String name, String nric, int age, boolean isMarried, String password) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.isMarried = isMarried;
        this.password = password;
    }

    /**
     * Gets the NRIC of the user.
     *
     * @return The NRIC.
     */
    public String getNric() {
        return nric;
    }

    /**
     * Gets the name of the user.
     *
     * @return The full name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the age of the user.
     *
     * @return The age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Checks if the user is married.
     *
     * @return True if married, false otherwise.
     */
    public boolean isMarried() {
        return isMarried;
    }

    /**
     * Updates the user's password.
     *
     * @param newPassword The new password to set.
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Verifies if the provided password matches the user's current password.
     *
     * @param input The input password to check.
     * @return True if the passwords match, false otherwise.
     */
    public boolean checkPassword(String input) {
        return password.equals(input);
    }

    /**
     * Returns a string representation of the user.
     *
     * @return A formatted string containing name, NRIC, age, and marital status.
     */
    @Override
    public String toString() {
        return "Name: " + name + ", NRIC: " + nric + ", Age: " + age + ", Married: " + isMarried;
    }
}