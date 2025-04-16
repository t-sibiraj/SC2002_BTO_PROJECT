public class User{
    private int age;
    private boolean isMarried;
    private int id;
    private static int idCounter = 4579; //random base value

    /*
     * Constructor for User
     * @param age the age of the User
     * @param isMarried marriage status of the User
     */
    public User(int age, boolean isMarried){
        this.id = idCounter++;
        this.age = age;
        this.isMarried = isMarried;
    }

    /*
     * current age of User
     * returns age of User
     */
    public int getAge(){
        return this.age;
    }

    /*
     * marriage status of User
     * returns marriage status of User
     */
    public boolean isMarried(){
        return this.isMarried;
    }

    /*
     * id of User
     * returns id of User
     */
    public int getId(){
        return this.id;
    }
}