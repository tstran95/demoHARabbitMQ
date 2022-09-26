package vnpay.vn.harabbit.constant;

public class Constant {
    public static final String HOST = "localhost";
    public static final int RETRY_DELAY = 60000;

    public static final String EXCHANGE = "custom.myexchange1";
    public static final String ALTER_EXCHANGE = "custom.myalterexchange1";
    public static final String ROUTING_KEY = "doesntmatter";
    public static final String QUEUE = "custom.myqueue1";
    public static final String ALTER_QUEUE = "custom.myalterqueue1";
    public static final String QUEUE_NAME = "mainQueue";
    public static final String DEAD_LETTER_EXCHANGE_NAME = "DeadLetterExchange";
    public static final String DEAD_LETTER_QUEUE_NAME = "retryQueue";
    public static final String DIRECT_EXCHANGE_NAME = "DefaultExchange";
    public static final String ROUTING_KEY_DIRECT_NAME = "main-routing";
    public static final String ROUTING_KEY_DEAD_LETTER_NAME = "retry-routing";
    public static final String FILL_INPUT = "Please fill all input";
    public static final String ERROR_CODE = "01";
    public static final String SUCCESS_CODE = "00";
    public static final String FAIL = "FAIL";
    public static final String SUCCESS = "SUCCESS";
    public static final String SEND_QUEUE_DONE = "Send message to Queue successfully!!!!";
    public static final String SUB_DONE = "Subscribe done";
}
