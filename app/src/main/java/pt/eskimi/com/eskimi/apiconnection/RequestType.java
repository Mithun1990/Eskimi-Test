package pt.eskimi.com.eskimi.apiconnection;

/**
 * Created by Naim on 7/19/2018.
 */

public interface RequestType {
     int POST = 1;
     int GET = 2;
     int DELETE = 3;
     int PUT = 4;

     int INTERNET_OR_SERVER_ERROR = 500;
     int SERVER_GIVEN_ERROR = 300;
     int JSON_PARSING_ERROR = 200;
}
