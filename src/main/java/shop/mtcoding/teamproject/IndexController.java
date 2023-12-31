package shop.mtcoding.teamproject;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import shop.mtcoding.teamproject.announcement.Announcement;
import shop.mtcoding.teamproject.announcement.AnnouncementService;
import shop.mtcoding.teamproject.bigjob.BigJob;
import shop.mtcoding.teamproject.bigjob.BigJobService;
import shop.mtcoding.teamproject.company.Company;
import shop.mtcoding.teamproject.company.CompanyService;
import shop.mtcoding.teamproject.reply.Reply;
import shop.mtcoding.teamproject.smalljob.SmallJobService;
import shop.mtcoding.teamproject.user.UserRepository;

import shop.mtcoding.teamproject.user.UserRepository;

@Controller
public class IndexController {

    @Autowired
    private HttpSession session;

    @Autowired
    AnnouncementService announcementService;

    @Autowired
    CompanyService companyService;

    @Autowired
    private BigJobService bigJobService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "0") Integer page1,
            @RequestParam(defaultValue = "0") Integer page2, HttpServletRequest request) {
        Page<Announcement> annPG = announcementService.index공고목록보기(page1);
        Page<Company> comPG = companyService.공고목록보기(page2);
        List<BigJob> bigJobs = bigJobService.BigJobList();
        session.setAttribute("bigjobs", bigJobs);
        request.setAttribute("annPG", annPG.getContent());
        request.setAttribute("comPG", comPG.getContent());
        return "/index";
    }

    @GetMapping("/joinselectForm")
    public String joinselectForm() {
        return "/joinselectForm";
    }

    @GetMapping("/loginselectForm")
    public String loginselectForm() {
        return "/loginselectForm";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    // 네브바 (채용,기업,구직,커뮤)

    @GetMapping("/search/searchAnn")
    public String annSearch() {
        return "/search/searchAnnForm";
    }

    @GetMapping("/search/searchComp")
    public String compSearch() {
        return "/search/searchCompForm";
    }

    @GetMapping("/search/searchResume")
    public String resumeSearch() {
        return "/search/searchResumeForm";
    }

    @GetMapping("/communityIndex")
    public String communityMain() {
        return "/comunity/comunityIndex";
    }

    // 네브바 (기업) (오른쪽햄버거)
    @GetMapping("/ann/annList")
    public String annmanage() {
        return "/ann/annList";
    }

    @GetMapping("/appsgg/companyAppSggList")
    public String compAppSgg() {
        return "/appsgg/companyAppSggList";
    }

    // 네브바 (구직자) (오른쪽햄버거)

    // @GetMapping("/user/updateForm")
    // public String myseeker() {
    //     return "/user/updateForm";
    // }

    @GetMapping("/resume/resumeList")
    public String myresume() {
        return "/resume/resumeList";
    }

    @GetMapping("/appsgg/userAppSggList")
    public String seekerAppSgg() {
        return "/appsgg/userAppSggList";
    }
}
