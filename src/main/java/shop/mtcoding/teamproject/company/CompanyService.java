package shop.mtcoding.teamproject.company;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import shop.mtcoding.teamproject._core.error.ex.MyException;
import shop.mtcoding.teamproject._core.vo.MyPath;
import shop.mtcoding.teamproject.announcement.Announcement;
import shop.mtcoding.teamproject.board.Board;
import shop.mtcoding.teamproject.company.CompanyRequest.UpdateDTO;
import shop.mtcoding.teamproject.company.CompanyRequest.UpdatedetailDTO;
import shop.mtcoding.teamproject.company.CompanyRequest.compJoinDTO;
import shop.mtcoding.teamproject.company.CompanyRequest.companyLoginDTO;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public void compjoin(compJoinDTO joinDTO) {

        // 해시
        String encPassword = BCrypt.hashpw(joinDTO.getPassword(), BCrypt.gensalt());

        Company company = Company.builder()
                .companyId(joinDTO.getCompanyId())
                .companyName(joinDTO.getCompanyName())
                .password(encPassword)
                .email(joinDTO.getEmail())
                .address(joinDTO.getAddress())
                .addressDetail(joinDTO.getAddressDetail())
                .phoneNum(joinDTO.getPhoneNum())
                .homepage(joinDTO.getHomepage())
                .level(2)
                .build();

        System.out.println("해시 : " + encPassword);
        companyRepository.save(company);
    }

    // public Company companylogin(companyLoginDTO compLoginDTO) {
    // System.out.println(compLoginDTO.getCompanyId());
    // Company company =
    // companyRepository.findByCompanyIdAndPassword(compLoginDTO.getCompanyId(),
    // compLoginDTO.getPassword());

    // return company;
    // }

    public Company companylogin(companyLoginDTO compLoginDTO) {
        System.out.println(compLoginDTO.getCompanyId());
        Company company = companyRepository.findByCompanyId(compLoginDTO.getCompanyId());

        return company;
    }

    public Page<Company> 공고목록보기(Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.ASC, "index");
        Page<Company> pageContent = companyRepository.findAll(pageable);
        return pageContent;
    }

    @Transactional
    public Company 기업정보수정(UpdateDTO updateDTO, Integer id) {
        Company company = companyRepository.findById(id).get();

        System.out.println("해시 테스트 1 : " + updateDTO.getPassword());

        // 해시
        String encPassword = BCrypt.hashpw(updateDTO.getPassword(), BCrypt.gensalt());
        company.setPassword(encPassword);

        System.out.println("해시 테스트 2 : " + encPassword);

        // company.setPassword(updateDTO.getPassword());
        company.setAddress(updateDTO.getAddress());
        company.setAddressDetail(updateDTO.getAddressDetail());
        company.setCompanyName(updateDTO.getCompanyName());
        company.setEmail(updateDTO.getEmail());
        company.setPhoneNum(updateDTO.getPhoneNum());
        company.setHomepage(updateDTO.getHomepage());

        return company;

    }

    public Company 회원정보보기(Integer index) {
        return companyRepository.findById(index).get();
    }

    @Transactional
    public Company 기업디테일수정(UpdatedetailDTO updatedetailDTO, Integer id) {
        System.out.println("테스트 3 : 들어옴?" + id);

        UUID uuid = UUID.randomUUID(); // 랜덤한 해시값을 만들어줌
        String fileName = uuid + "_" +
        updatedetailDTO.getPic().getOriginalFilename();
        System.out.println("fileName : " + fileName);

        
        Path filePath = Paths.get(MyPath.IMG_PATH +fileName);
        try {
            System.out.println("테스트 : 트라이!" + updatedetailDTO.getPic().getBytes());
            Files.write(filePath, updatedetailDTO.getPic().getBytes());
        } catch (Exception e) {
        throw new MyException(e.getMessage());
        }

        // 영속화
        Company company = companyRepository.findById(id).get();
        System.out.println("테스트 4 :" + updatedetailDTO.getCompanyName());
        // 변경
        company.setCompanyName(updatedetailDTO.getCompanyName());
        company.setEstablishment(updatedetailDTO.getEstablishment());
        company.setAddress(updatedetailDTO.getAddress());
        company.setAddressDetail(updatedetailDTO.getAddressDetail());
        company.setPhoneNum(updatedetailDTO.getPhoneNum());
        company.setEmail(updatedetailDTO.getEmail());
        company.setHomepage(updatedetailDTO.getHomepage());
        company.setPicUrl(fileName);
        companyRepository.save(company);
        return company;
    }

}
