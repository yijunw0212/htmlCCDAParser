import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * Created by JWang on 3/22/2017.
 */
public class ParserBase {

    public String getPlainText(Element element) {
        ParserBase.FormattingVisitor formatter = new ParserBase.FormattingVisitor();
        NodeTraversor traversor = new NodeTraversor(formatter);
        traversor.traverse(element);

        return formatter.toString();
    }

    public String TransformDate(String inputDate){
        DateFormat format = new SimpleDateFormat("d");
        String s = "";
        if(inputDate.length()==0) return "";
        if(inputDate.equals("present")){
            //Date date = format.parse(DateTime.now());
            Format outPutFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
            s = outPutFormatter.format(new Date());
            return s;
        }

        String convertedDate = inputDate.substring(inputDate.indexOf(' ')+1,inputDate.length());
        if(convertedDate.contains("st") ) {
            format = new SimpleDateFormat("MMM d'st', yyyy HH:mm", Locale.ENGLISH);
        }else if(convertedDate.contains("nd")){
            format = new SimpleDateFormat("MMM d'nd', yyyy HH:mm", Locale.ENGLISH);
        }else if(convertedDate.contains("rd")){
            format = new SimpleDateFormat("MMM d'rd', yyyy HH:mm", Locale.ENGLISH);
        }else if(convertedDate.contains("th")){
            format = new SimpleDateFormat("MMM d'th', yyyy HH:mm", Locale.ENGLISH);
        }
        try {
            Date date = format.parse(inputDate);
            Format outPutFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
            s = outPutFormatter.format(date);
        }
        catch (Exception e){
            System.out.println(e);
        }
        return s;
    }
    // the formatting rules, implemented in a breadth-first DOM traverse
    private class FormattingVisitor implements NodeVisitor {
        private static final int maxWidth = 80;
        private int width = 0;
        private StringBuilder accum = new StringBuilder(); // holds the accumulated text

        // hit when the node is first seen
        public void head(Node node, int depth) {
            String name = node.nodeName();
            if (node instanceof TextNode)
                append(((TextNode) node).text()); // TextNodes carry all user-readable text in the DOM.
            else if (name.equals("li"))
                append("\n * ");
            else if (name.equals("dt"))
                append("  ");
            else if (StringUtil.in(name, "p", "h1", "h2", "h3", "h4", "h5", "tr"))
                append("\n");
        }

        // hit when all of the node's children (if any) have been visited
        public void tail(Node node, int depth) {
            String name = node.nodeName();
            if (StringUtil.in(name, //"br",
                    "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5","td"))
                append("\n");
        }

        // appends text to the string builder with a simple word wrap method
        private void append(String text) {
            if (text.startsWith("\n"))
                width = 0; // reset counter if starts with a newline. only from formats above, not in natural text
            if (text.equals(" ") &&
                    (accum.length() == 0 || StringUtil.in(accum.substring(accum.length() - 1), " ", "\n")))
                return; // don't accumulate long runs of empty spaces

            if (text.length() + width > maxWidth) { // won't fit, needs to wrap
                String words[] = text.split("\\s+");
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    boolean last = i == words.length - 1;
                    if (!last) // insert a space if not the last word
                        word = word + " ";
                    if (word.length() + width > maxWidth) { // wrap and reset counter
                        accum.append("\n").append(word);
                        width = word.length();
                    } else {
                        accum.append(word);
                        width += word.length();
                    }
                }
            } else { // fits as is, without need to wrap text
                accum.append(text);
                width += text.length();
            }
        }

        @Override
        public String toString() {
            return accum.toString();
        }

    }
}
