package vnpay.vn.harabbit.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author sontt1
 * Date:9/28/2022
 * Time:3:58 PM
 */

@Builder
@Getter
@Setter
@ToString
public class RabbitMQBean {
    private String hostPort;
    private String username;
    private String password;
    private String virtualHost;
    private int port;
    private int maxChannel;
}
