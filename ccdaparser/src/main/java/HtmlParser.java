
import org.jsoup.helper.DataUtil;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by JWang on 3/21/2017.
 */
public class HtmlParser {
    public static void main (String [] args) {
        try {
            PrintWriter demogWriter = new PrintWriter(new FileOutputStream(new File("C:\\Users\\jwang\\Desktop\\QME\\eCQM\\SampleData\\demog.txt"), false));
            PrintWriter eventWriter = new PrintWriter(new FileOutputStream(new File("C:\\Users\\jwang\\Desktop\\QME\\eCQM\\SampleData\\events.txt"), false));

            char delimiter = '|';
            File file = new File("C:\\Users\\jwang\\Desktop\\QME\\eCQM\\SampleData\\html_records");//change your file directory here
            for (File f : file.listFiles()) {
                Document doc = DataUtil.load(f, "UTF-8", f.getPath());
                EventsParser ep = new EventsParser();
                DemogParser dp = new DemogParser();
                String demog = dp.parseDemogs(doc, delimiter);
                demogWriter.println(demog);
                String DocumentId = demog.substring(0, demog.indexOf(delimiter));
                //demogWriter.println(demog);
                for (String eventString : ep.parseEvents(doc, delimiter, DocumentId)) {
                    eventWriter.println(eventString);
                }
            }
            demogWriter.close();
            eventWriter.close();
        } catch (Exception e) {
            System.out.println(e);
        }
//for a particular document, comment out all codes above and uncomment codes below, change the file directories.
//        File file = new File("C:\\Users\\jwang\\Desktop\\QME\\eCQM\\SampleData\\html_records\\1_N_Newborn.html");//change your file directory here
//
//        try {
//            char delimiter = '|';
//            PrintWriter eventWriter = new PrintWriter(new FileOutputStream(new File("C:\\Users\\jwang\\Desktop\\QME\\eCQM\\SampleData\\eventtest.txt"), false));
//            PrintWriter demogWriter = new PrintWriter(new FileOutputStream(new File("C:\\Users\\jwang\\Desktop\\QME\\eCQM\\SampleData\\demogtest.txt"), false));
//            Document doc = DataUtil.load(file, "UTF-8", file.getPath());
//            EventsParser ep = new EventsParser();
//            DemogParser dp = new DemogParser();
//            String demog = dp.parseDemogs(doc, delimiter);
//            demogWriter.println(demog);
//            demogWriter.close();
//            String DocumentId = demog.substring(0, demog.indexOf('|'));
//            //demogWriter.println(demog);
//            for(String e : ep.parseEvents(doc, delimiter, DocumentId)){
//                eventWriter.println(e);
//                //System.out.println(e);
//            }
//            eventWriter.close();
//        } catch (IOException e) {
//            System.out.print(e);
//        }


    }
   // }
}
