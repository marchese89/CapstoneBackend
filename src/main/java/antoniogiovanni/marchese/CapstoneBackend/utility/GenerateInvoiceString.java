package antoniogiovanni.marchese.CapstoneBackend.utility;

import antoniogiovanni.marchese.CapstoneBackend.model.*;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.SolutionState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Getter
@Setter
public class GenerateInvoiceString {
    @Setter(AccessLevel.NONE)
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Setter(AccessLevel.NONE)
    DecimalFormat df = new DecimalFormat("0.00");
    private Request request;
    private Student student;
    @Setter(AccessLevel.NONE)
    private Teacher teacher;
    private long invoiceNumber;

    @Setter(AccessLevel.NONE)
    private Solution solution;
    public void setSolutionAndTeacher(){
        List<Solution> solutionList = request.getSolutionList();
        for(Solution s: solutionList){
            if(s.getState() == SolutionState.ACCEPTED){
                solution = s;
                break;
            }
        }
        teacher = solution.getTeacher();
    }
    public String getInvoice() {
        String template = "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"" +
                "align=\"center\"" +
                "style=\"border-collapse: collapse;\"" +
                "RULES=none FRAME=none border=\"0\">" +
                "<tr style=\"height:100px\">" +
                "<td align=\"center\" colspan=\"3\">" +
                "<h1>Fattura</h1>" +
                "</td>" +
                "</tr>" +
                "<tr style=\"height:30px\">" +
                "<td align=\"left\">" +
                "<font size=4 >" + teacher.getName() + " " + teacher.getSurname() + "</font>" +
                "</td>" +
                "<td></td>" +
                "</tr>" +
                "<tr style=\"height:30px\">" +
                "<td align=\"left\">" +
                "<font size=4 >" + teacher.getAddress().getStreet() + ", " + teacher.getAddress().getHouseNumber() + "</font>" +
                "</td>" +
                "<td></td>" +
                "</tr>" +
                "<tr style=\"height:30px\">" +
                "<td align=\"left\">" +
                "<font size=4 >" + teacher.getAddress().getPostalCode() + " - " + teacher.getAddress().getCity() + " (" + teacher.getAddress().getProvince() + ")</font>" +
                "</td>" +
                "<td></td>" +
                "</tr>" +
                "<tr style=\"height:30px\">" +
                "<td align=\"left\">" +
                "<font size=4 >PARTITA IVA:&nbsp;" + teacher.getPiva() + "</font>" +
                "</td>" +
                "<td></td>" +
                "</tr>" +
                "<tr style=\"height:30px\">" +
                "<td align=\"left\">" +
                "<font size=4 >COD. FISC: " + teacher.getCf() + "</font>" +
                "</td>" +
                "<td></td>" +
                "</tr>" +
                "<tr style=\"height:30px\">" +
                "<td></td>" +
                "<td align=\"right\">" +
                "<h2>Cliente</h2>" +
                "</td>" +
                "</tr>" +
                "<tr style=\"height:30px\">" +
                "<td></td>" +
                "<td align=\"right\">" +
                "<font size=4 >" + student.getName() + "&nbsp;" + student.getSurname() + "</font>" +
                "</td>" +
                "</tr>" +
                "<tr style=\"height:30px\">" +
                "<td></td>" +

                "<td align=\"right\">" +
                "<font size=4 >" + student.getAddress().getStreet() + ", " + student.getAddress().getHouseNumber() + "</font>" +
                "</td>" +
                "</tr>" +
                "<tr style=\"height:30px\">" +
                "<td></td>" +

                "<td align=\"right\">" +
                "<font size=4 >" + student.getAddress().getPostalCode() + " - " + student.getAddress().getCity() + "&nbsp;(" + student.getAddress().getProvince() + ")</font>" +
                "</td>" +
                "</tr>" +
                "<tr style=\"height:30px\">" +
                "<td></td>" +

                "<td align=\"right\">" +
                "<font size=4 >CF:&nbsp;" + student.getCf() + "</font>" +
                "</td>" +
                "</tr>" +
                "<tr style=\"height:30px\">" +
                "<td align=\"left\">" +
                "<font size=4 ><b>DATA: </b></font>" + LocalDate.now().format(formatter) + "</td>" +

                "<td></td>" +
                "</tr>" +
                "<tr style=\"height:100px\">" +
                "<td align=\"left\" style=\"vertical-align:top\">" +
                "<font size=4 ><b>FATTURA: </b></font>" + invoiceNumber + "</td>" +

                "<td></td>" +
                "</tr>" +
                "<tr>" +


                "<td align=\"left\" colspan=\"2\">" +
                "<table  rules=\"all\" border=\"1\" align=\"right\" style=\"width:100%\" >" +
                "<tr style=\"height:50px\">" +

                "<td align=\"center\">" +
                "<font size=4 ><b>&nbsp;DESCRIZIONE&nbsp;</b></font>" +
                "</td>" +
                "<td align=\"center\">" +
                "<font size=4 ><b>&nbsp;PREZZO&nbsp;</b></font>" +
                "</td>" +
                "<td align=\"center\">" +
                "<font size=4 ><b>&nbsp;QTA&nbsp;</b></font>" +
                "</td>" +
                "<td align=\"center\">" +
                "<font size=4 ><b>&nbsp;IMPORTO&nbsp;</b></font>" +
                "</td>" +
                "</tr>" +
                "<tr><td align=\"center\">" +
                "<font size=4 >Supporto Didattico</font>" +
                "</td>" +
                "<td align=\"center\">" +
                "<font size=4 >" + df.format(solution.getPrice() / 100) + "&euro;</font>" +
                "</td>" +
                "<td align=\"center\">" +
                "<font size=4 >1</font>" +
                "</td>" +
                "<td align=\"center\">" +
                "<font size=4 ><b>" + df.format(solution.getPrice() / 100) + "&euro;</b></font></td>" +
                "</tr>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "<tr style=\"height:50px\">" +
                "<td></td>" +

                "<td align=\"right\">" +
                "<font size=3 >IMPONIBILE:&nbsp; </font>" +
                "<font size=3 >" + df.format(solution.getPrice() / 100 / 1.04) + "&nbsp;&euro;</font>" +
                "</td>" +
                "</tr>" +
                "<tr style=\"height:50px\">" +
                "<td></td>" +

                "<td align=\"right\">" +
                "<font size=3 >Rivalsa Inps 4%:&nbsp;</font>" +
                "<font size=3 >" + df.format((solution.getPrice() / 100 / 1.04) * 4 / 100) + "&nbsp;&euro;</font>" +
                "</td>" +
                "</tr>" +
                "<tr style=\"height:50px\">" +
                "<td></td>" +

                "<td align=\"right\">" +
                "<font size=3 ><b>TOTALE</b>&nbsp;</font>" +
                "<font size=3 ><b>" + df.format(solution.getPrice() / 100) + "&nbsp;&euro;</b></font>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>" +
                "<font size=3 >Imposta di bollo &euro; 2,00 su originale</font>" +
                "</td>" +
                "<td>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>" +
                "<font size=3 >su Importi superiori ad &euro; 77,47</font>" +
                "</td>" +
                "<td>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>" +
                "<font size=3 ><b>NOTE</b></font>" +
                "</td>" +
                "<td>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>" +
                "<font size=3 >Pagamento online tramite Carta di Credito</font>" +
                "</td>" +
                "<td>" +
                "</td>" +
                "</tr>" +
                "<tr align=\"center\" style=\"height:200px\" >" +
                "<td colspan=\"2\">" +
                "<font size=3 >&nbsp;</font>" +
                "</td>" +
                "</tr>" +
                "<tr align=\"center\">" +
                "<td colspan=\"2\">" +
                "<font size=3 ><b>Operazione in franchigia da Iva art. 1 cc. 54-89 L. 190/2014 –</b></font>" +
                "</td>" +
                "</tr>" +
                "<tr align=\"center\">" +
                "<td colspan=\"2\">" +
                "<font size=3 ><b>Non soggetta a ritenuta d’acconto ai sensi del c. 67 L. 190/2014</b></font>" +
                "</td>" +
                "</tr>" +
                "</table>";
        return template;

    }

}
