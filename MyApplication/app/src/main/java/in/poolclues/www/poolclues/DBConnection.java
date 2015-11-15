package in.poolclues.www.poolclues;

/**
 * Created by Shyam Yadav on 18-Oct-15.
 */
public class DBConnection {

    final String APIURL="http://api.poolclues.anip.xyz:8080/";
    public  final String URLReg=APIURL+"/register";
    public final String URLEmailVerify="/verify";
    public  final String URLForgotPwd=APIURL+"/forgotpassword/";
    public  final String URLLogin=APIURL+"/authenticate";
    public  final String URLLogOut=APIURL+"/logout/";
    public  final String URLAddPhoneNO="/addphone/";
    public  final String URLProductList=APIURL+"/products/list";
    public final String URLPwdUpdEnd="/change/password/2";
    public  final String URLCreatPool=APIURL+"pool/create";
    public final String URLFetchPoolList="/pool/list/";
    public  final String URLAddMoneyToWallet= "/wallet/add";
    public  final String URLWalletBalance= "/wallet";
    public final String URLPaymentDetails="/wallet/history";
    public final String URLAddContributors="/event/104/invite";

    //public  final String URLServerSts= "http://188.166.249.229:8080/";


    public  final String URLServerSts=APIURL;
    public  final String URLPwdUpd=APIURL;




}
