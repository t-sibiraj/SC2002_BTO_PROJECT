public class Enquiry {
    private String content;
    private boolean isResolved;
    private String reply;

    private BTOProject project;
    private Applicant applicant;

    public Enquiry(String content, BTOProject p, Applicant a){
        this.content = content;
        this.project = p;
        this.applicant = a;
    }

    /*
     * setter function
     */
    public void setReply(String reply){
        this.reply = reply;
    }

    public void setContent(String content){
        this.content = content;
    }

    /*
     * getter function
     */
    public String getReply() { return this.reply; }
    public String getContent() { return this.content; }

}
