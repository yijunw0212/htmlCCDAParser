import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.*;

/**
 * Created by JWang on 3/21/2017.
 */
class DemogParser extends ParserBase {

    public String parseDemogs(Document doc, String delimiter) {
        EventsParser formatter = new EventsParser();
        Elements alldemogs = doc.select(".header_table").select("td").not(".td_label");

        HashMap<String, String> demogElements = new HashMap<String, String>();

        int count = 1;
        while (alldemogs.hasText()) {
            String Label = formatter.getPlainText(alldemogs.first()).trim();
            String Content = formatter.getPlainText(alldemogs.next().first()).trim();
            if(demogElements.containsKey(Label)){
            Label = Label + Integer.toString(count++);
            }
            demogElements.put(Label,Content);
            alldemogs.remove(alldemogs.first());
            alldemogs.remove(alldemogs.first());
        }

        String Result =
                demogElements.get("Document Id")
                + delimiter + demogElements.get("Patient")
                + delimiter + demogElements.get("Sex")
                + delimiter + TransformDate(demogElements.get("Date of birth"))
                + delimiter + TransformDate(demogElements.get("Date of expiration"))
                + delimiter + demogElements.get("Race")
                + delimiter + demogElements.get("Ethnicity")
                + delimiter + demogElements.get("Insurance Providers")
                + delimiter + demogElements.get("Patient IDs")
                + delimiter + demogElements.get("Contact info")
                + delimiter + TransformDate(demogElements.get("Document Created:"))
                + delimiter + demogElements.get("Author")
                + delimiter + demogElements.get("Contact info1")
                + delimiter + demogElements.get("Document maintained by")
                + delimiter + demogElements.get("Contact info2");

        return Result;
    }
}
