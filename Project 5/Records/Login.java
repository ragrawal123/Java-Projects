package Records;

import java.io.Serializable;
import Enums.*;

public record Login (String role, String email, String password, RequestType requestType)
implements Serializable {
}
