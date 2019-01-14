package anonymization;

import java.util.List;

public interface DeIdentifier {

    List<String> getDeIdentifiedText(List<String> text);
}
