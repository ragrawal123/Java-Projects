package Records;
import Enums.*;
import java.io.Serializable;

public record Request(RequestType type, Object object) implements Serializable {

}
