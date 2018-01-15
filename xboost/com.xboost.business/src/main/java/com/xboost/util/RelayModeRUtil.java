package com.xboost.util;
import com.mckinsey.sf.constants.IConstants;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;


public class RelayModeRUtil extends Thread implements IConstants {
    private String RserverIp;
    private String RShellPath;
    public RelayModeRUtil(String rserverIp){
        this.RserverIp = rserverIp;
    }
    public void  run() throws RuntimeException{
       /* try{
            RConnection c = new RConnection();

            REXP x = c.eval("R.version.string");

            System.out.println(x.asString());
            callRserve();
        } catch (Exception e) {

            e.printStackTrace();

        }*/
        try {
            callRserve();
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        }
    }
    protected void callRserve() throws RserveException, REXPMismatchException {
        RConnection rConnection = new RConnection(RserverIp);
        rConnection.eval("source("+RShellPath+")");

//        String rv = rConnection.eval("R.version.string").asString();
//        System.out.println(rv);
//
//        double [] arr = rConnection.eval("rnorm(10)").asDoubles();
//        for(double d : arr) {
//            System.out.println(d);
//        }
    }
}
