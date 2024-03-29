package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.*;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.RequestState;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.SolutionState;
import antoniogiovanni.marchese.CapstoneBackend.repository.SolutionRepository;
import antoniogiovanni.marchese.CapstoneBackend.utility.GenerateInvoiceString;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Service
public class SolutionService {

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    GenerateInvoiceString generateInvoiceString;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender emailSender;

    @Value("${upload.dir}")
    private String uploadDir;

    public RequestService getRequestService(){
        return this.requestService;
    }

    public boolean canSaveSolution(Long requestId, Teacher teacher){
        Request request = requestService.findById(requestId);
        List<Solution> solutionList = request.getSolutionList();
        boolean ok = true;
        for(Solution solution: solutionList){
            if(solution.getTeacher().getId() == teacher.getId()){
                ok = false;
                break;
            }
        }
        return ok;
    }

    public Solution save(MultipartFile file, Long requestId, Long price, Teacher teacher){

        String originalFileName = file.getOriginalFilename();

        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String filePath;
        long n = 1;
        File f = new File(uploadDir + File.separator + "solutions"+ File.separator + n + fileExtension);

        while(f.exists()){
            n++;
            f = new File(uploadDir + File.separator + "solutions"+ File.separator + n + fileExtension);
        }

        try {
            byte[] bytes = file.getBytes();
            filePath = uploadDir + File.separator + "solutions"+ File.separator + n + fileExtension;
            Files.write(Path.of(filePath), bytes);

        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("problems during upload");
        }

        Solution solution = new Solution();
        solution.setSolutionUrl(filePath);
        Request request = requestService.findById(requestId);
        boolean ok = false;
        List<Subject> subjectList = teacher.getSubjectList();
        for(Subject subject: subjectList){
            if(subject.equals(request.getSubject())){
                ok = true;
                break;
            }
        }
        if(!ok){
            throw new UnauthorizedException("cannot send solution for subjects that not are in your list!");
        }

        solution.setRequest(request);
        solution.setState(SolutionState.PENDING);
        solution.setTeacher((Teacher) userService.findById(teacher.getId()));
        solution.setPrice(price);
        emailService.sendEmail(
                request.getStudent().getEmail(),
                "Nuova Soluzione per la tua richiesta",
                "Salve,\nun insegnante ha inserito una soluzione per la tua richista "+
                        request.getTitle() +"\n\ncontrolla il tuo profilo.");
        return solutionRepository.save(solution);
    }

    public List<Solution> getSolutionsByRequestId(Long requestId, Student student){
        Request request = requestService.findById(requestId);
        if(request.getStudent().getId() != student.getId()){
            throw new UnauthorizedException("you can read only solutions of your requests");
        }
        return solutionRepository.getSolutionsByRequestId(requestId);
    }

    public Solution getSolutionByRequestIdAndTeacher(Long requestId,Teacher teacher){
        //return solution only if exists for this teacher
        Request request = requestService.findById(requestId);
        List<Solution> solutionList = request.getSolutionList();
        Solution s = null;
        boolean ok = false;
        for(Solution solution: solutionList){
            if(solution.getTeacher().getId() == teacher.getId()){
                ok = true;
                s = solution;
            }
        }
        if(!ok){
            throw new UnauthorizedException("you don't have solutions");
        }
        return s;
    }

    public Solution findById(Long id){
        return solutionRepository.findById(id).orElseThrow(()->new NotFoundException(id));
    }

    public Solution acceptSolution(Long solutionId, String idPaymentIntent, Student student) throws IOException, MessagingException {
        Solution solution = this.findById(solutionId);
        Request request = requestService.findById(solution.getRequest().getId());
        if(solution.getRequest().getStudent().getId() != student.getId()){
            throw new UnauthorizedException("you can accept only solutions of your requests");
        }
        //update solution url from solution to request
        request.setSolutionUrl(solution.getSolutionUrl());
        request.setRequestState(RequestState.CLOSED);
        request.setPaymentId(idPaymentIntent);
        List<Solution> solutionList = request.getSolutionList();
        Iterator<Solution> it = solutionList.iterator();
        Teacher teacher = null;
        while (it.hasNext()){
            Solution s = it.next();
            if(s.getId() == solutionId){
                s.setState(SolutionState.ACCEPTED);
                teacher = s.getTeacher();
            }else{
                s.setState(SolutionState.REJECTED);
                emailService.sendEmail(s.getTeacher().getEmail(),"Soluzione Rifiutata","Salve,\nla tua soluzione è stata rifiutata\n\ncontrolla il tuo profilo.");
            }
        }
        request.setSolutionList(solutionList);
        requestService.save(request);
        //we need to generate pdf invoice from html template
        generateInvoiceString.setRequest(request);
        generateInvoiceString.setStudent(request.getStudent());
        generateInvoiceString.setSolutionAndTeacher();
        generateInvoiceString.setInvoiceNumber(invoiceService.getMaxInvoiceNumber()+1);
        String fileExtension = ".pdf";

        String filePath = "";
        long n = 1;
        File f = new File(uploadDir + File.separator + "invoices"+ File.separator + n + fileExtension);

        while(f.exists()){
            n++;
            f = new File(uploadDir + File.separator + "invoices"+ File.separator + n + fileExtension);
        }
        filePath = uploadDir + File.separator + "invoices"+ File.separator + n + fileExtension;
        OutputStream fileOutputStream = new FileOutputStream(filePath);
        HtmlConverter.convertToPdf(generateInvoiceString.getInvoice(), fileOutputStream);
        fileOutputStream.close();
        //save invoice to DB
        Invoice invoice = new Invoice();
        invoice.setInvoiceFileUrl(filePath);
        invoice.setNumber(invoiceService.getMaxInvoiceNumber()+1);
        invoice.setIssuingDate(LocalDate.now());
        invoice.setRequest(request);
        invoice.setTotal(solution.getPrice());
        invoiceService.save(invoice);
        //send email with invoice to student
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(request.getStudent().getEmail());
        helper.setSubject("Fattura Richiesta");
        helper.setText("Salve "+request.getStudent().getName()+",\nGrazie per il tuo acquisto.\ntrovi la fattura in allegato.");
        FileSystemResource file = new FileSystemResource(new File(filePath));
        helper.addAttachment(file.getFilename(), file);
        emailSender.send(message);

        //send email with invoice to teacher
        MimeMessage message2 = emailSender.createMimeMessage();
        MimeMessageHelper helper2 = new MimeMessageHelper(message2, true);
        helper2.setTo(teacher.getEmail());
        helper2.setSubject("Fattura Soluzione Accettata");
        helper2.setText("Salve "+ teacher.getName() +" "+teacher.getSurname()+",\nla tua soluzione è stata acquistata\n\ntrovi la fattura in allegato.");
        helper2.addAttachment(file.getFilename(), file);
        emailSender.send(message2);

        return solutionRepository.save(solution);
    }
}
