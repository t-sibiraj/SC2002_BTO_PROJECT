package model;

public class User {
    // ======================
    // Fields
    // ======================
    private String nric;
    private String password;
    private String name;
    private int age;
    private boolean isMarried;


    // ======================
    // Constructor
    // ======================
    public User(String name, String nric, int age, boolean isMarried, String password) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.isMarried = isMarried;
        this.password = password;
    }


    // ======================
    // Getters
    // ======================
    public String getNric() {
        return nric;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPassword(){
        return password;
    }

    public boolean isMarried() {
        return isMarried;
    }

    // ======================
    // Setters
    // ======================
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    // ======================
    // Other Methods
    // ======================
    public boolean checkPassword(String input) {
        return password.equals(input);
    }


    // ======================
    // Factory Method
    // ======================

    @Override
    public String toString() {
        return "Name: " + name + ", NRIC: " + nric + ", Age: " + age + ", Married: " + isMarried;
    }
} 
