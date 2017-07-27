import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;

import javax.measure.*;
import javax.measure.quantity.Length;


/**
 * Created by JWang on 3/21/2017.
 */
class EventsParser extends ParserBase{

    public List<String> parseEvents(Document doc,char delimiter,String DocumentId) {
        List<String> eventList = new LinkedList<String>();
        EventsParser formatter = new EventsParser();
        Elements allEvents = doc.select("#patient_information_by_section").select("tr");

        for (Element element : allEvents) {
            if(element.children().not("th").size()==0){continue;}
            Elements  allTds= element.select("td");
            Element[] subElements = allTds.toArray(new Element[]{});
            String result = GetResultsForEachEvent(subElements,formatter);
//            String resultType = GetResultsType(result);
//            String resultValue = GetResultValue(result);
//            String resultUnit = GetResultUnit(result);

            String toAdd =
                    DocumentId
                    + delimiter + GetSectionName(GetDescriptionForEachEvent(subElements,formatter))
                    + delimiter + GetDescriptionForEachEvent(subElements,formatter)
                    + delimiter + GetCodesForEachEvent(subElements,formatter)
                    + delimiter + GetStartDateForEachEvent(subElements,formatter)
                    + delimiter + GetEndDateForEachEvent(subElements,formatter)
                    + delimiter + GetStatusForEachEvent(subElements,formatter)
//                    + delimiter + resultType
//                    + delimiter + resultValue
//                    + delimiter + resultUnit
                    + delimiter + result
                    + delimiter + GetFieldsForEachEvent(subElements,formatter);

            eventList.add(toAdd);
        }
        return eventList;
    }

    public String GetCodesForEachEvent(Element[] elements,EventsParser formatter){
        Elements codesForEvent = elements[1].select("b");
        StringBuilder sb = new StringBuilder();
        for(Element e:codesForEvent) {
            String codeText = formatter.getPlainText(e);
            sb.append(codeText.trim()+",");
        }
        return sb.deleteCharAt(sb.length()-1).toString().trim();
    }

    public String GetDescriptionForEachEvent(Element[] elements, EventsParser formatter){
        Element descriptionLines = elements[0].select("td").first();
        String description = formatter.getPlainText(descriptionLines).trim();
        return description;
    }

    public String GetStartDateForEachEvent(Element[] elements, EventsParser formatter){
        String DateString = formatter.getPlainText(elements[2]);
        String toConvert = DateString.substring(0,DateString.indexOf('-')).trim();

        return TransformDate(toConvert);
    }

    public String GetEndDateForEachEvent(Element[] elements, EventsParser formatter){
        String DateString = formatter.getPlainText(elements[2]);
        String toConvert = DateString.substring(DateString.indexOf('-')+1).trim();

        return TransformDate(toConvert);

    }



    private String SetFirstLetterToUpperCase(String string){
        return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
    }


    public String GetResultsForEachEvent(Element[] elements, EventsParser formatter){
        String ResultString = formatter
                .getPlainText(elements[4])
                .trim()
                .replace("\n","")
                .replaceAll("\\(.*?\\)",",")
                .replace(" ","");
        String result = "";
        if(ResultString.length()>0) {
            if(ResultString.charAt(ResultString.length()-1)==','){
            result= ResultString.substring(0,ResultString.length()-2);
            }
            else{
                result = ResultString;
        }
        }
        return result;
    }

    public String GetStatusForEachEvent(Element[] elements, EventsParser formatter){
        String StatusString = formatter.getPlainText(elements[3]).trim();
        if(StatusString.length() == 0)
            return "";
        else
            return SetFirstLetterToUpperCase(StatusString);
    }

    public String GetFieldsForEachEvent(Element[] elements, EventsParser formatter){
        StringBuilder sb = new StringBuilder();
        for(Element field: elements[5].children()){
            Elements temp = field.getAllElements().not("i").not("dl").not("b");
            //temp.remove(0);
            for(Element subfield : temp) {

               String FieldsString = formatter.getPlainText(subfield).replaceAll("\\(.*?\\)","");

                if(subfield.is("dt")) {

                    sb.append("#"+FieldsString.trim() + "");
                }
                else
                sb.append(FieldsString.trim() + "");

                //sb.append(subfield.toString());
            }
        }
//        if(sb.length()!=0)
//        sb.deleteCharAt(sb.lastIndexOf(";"));

        return sb.toString();
    }


//    public String GetResultsType(String resultString){
//    return "";
//    }
//
//    public String GetResultUnit(String resultString){
//        return "";
//    }
//
//    public String GetResultValue(String resultString){
//        return "";
//    }

    public String GetSectionName(String description){
       // Element[] allSectionNames = elements.toArray(new Element[]{});
        String convertedDescription;
        if(description.contains(":")) {
            int validStart = description.indexOf(':') + 1;
            int validEnd = description.length();
            convertedDescription = description.substring(validStart,validEnd).trim();
        }
        else{
            convertedDescription = description;
        }

        if (convertedDescription.equals("Diagnosis")) {
            return "Diagnoses";
        } else if (convertedDescription.equals("Procedure")) {
            return "Procedures";
        } else if (convertedDescription.equals("Diagnostic Study")) {
            return "Diagnostic Studies";
        } else if (convertedDescription.equals("Encounter")) {
            return "Encounters";
        } else if (convertedDescription.equals("Patient Characteristic")) {
            return "Patient Characteristics";
        } else if (convertedDescription.equals("Intervention")) {
            return "Interventions";
        } else if (convertedDescription.equals("Physical Exam")) {
            return "Physical Exams";
        } else if (convertedDescription.equals("Laboratory Test")) {
            return "Laboratory Tests";
        } else if (convertedDescription.equals("Medication")) {
            return "Medications";
        } else if (convertedDescription.equals("Risk Category Assessment")) {
            return "Risk Category Assessments";
        } else if (convertedDescription.equals("Immunization")) {
            return "Immunizations";
        } else if (convertedDescription.equals("Device")) {
            return "Devices";
        } else if (convertedDescription.equals("Communication From Provider To Provider")) {
            return "Communication From Provider To Providers";
        } else if (convertedDescription.equals("Medication Allergy")) {
            return "Medication Allergies";
        } else if (convertedDescription.equals("Communication From Patient To Provider")) {
            return "Communication From Patient To Providers";
        } else if (convertedDescription.equals("Functional Status")) {
            return "Functional Statuses";
        } else if (convertedDescription.equals("Procedure Intolerance")) {
            return "Procedure Intolerances";
        } else if (convertedDescription.equals("Communication From Provider To Patient")) {
            return "Communication From Provider To Patients";
        } else if (convertedDescription.equals("Substance")) {
            return "Substances";
        } else if (convertedDescription.equals("Medication Intolerance")) {
            return "Medication Intolerances";
        } else {
            return "";
        }
    }


//    public void test(){
//        Unit​<Length> sourceUnit = ​METRE;
//        Unit​<Length> targetUnit = ​CENTI(METRE)​;
//        UnitConverter converter = sourceUnit.getConverterTo(targetUnit);
//        double​ length1 = 4.0;
//        double​ length2 = 6.0;
//        double​ result1 = converter.convert(length1);
//        double​ result2 = converter.convert(length2);
//
//    }
}
