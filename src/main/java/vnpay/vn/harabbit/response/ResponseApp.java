package vnpay.vn.harabbit.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author sontt1
 * Date:9/14/2022
 * Time:5:05 PM
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseApp {
    private String code;
    private String message;
    private String description;
}
