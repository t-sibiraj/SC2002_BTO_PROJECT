public class Account {
    private String password = "Password";
    private String icNo;

    public Account(String icNo){
        this.icNo = icNo;
    }
    
    public void setPassword(String password){
        this.password = password;
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}
