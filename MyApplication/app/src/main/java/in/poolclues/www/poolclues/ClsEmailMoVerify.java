package in.poolclues.www.poolclues;

/**
 * Created by Shyam Yadav on 24-Oct-15.
 */
public class ClsEmailMoVerify {


    private String authtoken;
    private String verification_code;


    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String value) {
        this.authtoken = value;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String value) {
        this.verification_code = value;
    }
}
