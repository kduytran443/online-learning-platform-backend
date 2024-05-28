package com.kduytran.categoryservice.runner;

import com.kduytran.categoryservice.dto.CreateCategoryDTO;
import com.kduytran.categoryservice.repository.CategoryRepository;
import com.kduytran.categoryservice.service.ICategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class InitDatabaseRunner {

    @Bean
    CommandLineRunner initDatabase(ICategoryService categoryService, CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.findAll().size() != 0) {
                return;
            }
            // Create "Development" category
            CreateCategoryDTO developmentDTO = new CreateCategoryDTO();
            developmentDTO.setName("Development");
            developmentDTO.setCode("development"); // Sử dụng trong URL
            developmentDTO.setDescription("Explore the world of software development, covering various aspects from web and mobile development to game development and software engineering.");
            developmentDTO.setParentCategoryId(null); // Đây là category gốc
            UUID developmentId = categoryService.create(developmentDTO);

            // Create "Web Development" subcategory
            CreateCategoryDTO webDevelopmentDTO = new CreateCategoryDTO();
            webDevelopmentDTO.setName("Web Development");
            webDevelopmentDTO.setCode("web-development"); // Sử dụng trong URL
            webDevelopmentDTO.setDescription("Learn how to build dynamic and responsive websites using technologies such as HTML, CSS, JavaScript, and popular frameworks like React, Angular, and Vue.js.");
            webDevelopmentDTO.setParentCategoryId(developmentId.toString()); // "Web Development" là con của "Development"
            UUID webDevelopmentId = categoryService.create(webDevelopmentDTO);

            // Create sub-subcategories of "Web Development"
            CreateCategoryDTO frontEndDTO = new CreateCategoryDTO();
            frontEndDTO.setName("Front-End Development");
            frontEndDTO.setCode("front-end-development");
            frontEndDTO.setDescription("Master the front-end skills required to create engaging user interfaces and interactive web applications using HTML, CSS, and JavaScript.");
            frontEndDTO.setParentCategoryId(webDevelopmentId.toString());
            UUID frontEndId = categoryService.create(frontEndDTO);

            // Create "Back-End Development" subcategory
            CreateCategoryDTO backEndDTO = new CreateCategoryDTO();
            backEndDTO.setName("Back-End Development");
            backEndDTO.setCode("back-end-development");
            backEndDTO.setDescription("Learn how to build server-side applications and APIs using back-end technologies such as Node.js, Express.js, and databases like MongoDB and SQL.");
            backEndDTO.setParentCategoryId(webDevelopmentId.toString());
            UUID backEndId = categoryService.create(backEndDTO);

            // Create "Full Stack Development" subcategory
            CreateCategoryDTO fullStackDTO = new CreateCategoryDTO();
            fullStackDTO.setName("Full Stack Development");
            fullStackDTO.setCode("full-stack-development");
            fullStackDTO.setDescription("Master both front-end and back-end development skills to become a versatile developer capable of building complete web applications from start to finish.");
            fullStackDTO.setParentCategoryId(webDevelopmentId.toString());
            UUID fullStackId = categoryService.create(fullStackDTO);

            // Create "Mobile Development" subcategory
            CreateCategoryDTO mobileDevelopmentDTO = new CreateCategoryDTO();
            mobileDevelopmentDTO.setName("Mobile Development");
            mobileDevelopmentDTO.setCode("mobile-development");
            mobileDevelopmentDTO.setDescription("Learn to create mobile applications for iOS, Android, or cross-platform using frameworks like Flutter and React Native.");
            mobileDevelopmentDTO.setParentCategoryId(developmentId.toString()); // Parent category is "Development"
            UUID mobileDevelopmentId = categoryService.create(mobileDevelopmentDTO);

            // Create "Game Development" subcategory
            CreateCategoryDTO gameDevelopmentDTO = new CreateCategoryDTO();
            gameDevelopmentDTO.setName("Game Development");
            gameDevelopmentDTO.setCode("game-development");
            gameDevelopmentDTO.setDescription("Master the skills to develop your own games using popular game engines such as Unity and Unreal Engine.");
            gameDevelopmentDTO.setParentCategoryId(developmentId.toString()); // Parent category is "Development"
            UUID gameDevelopmentId = categoryService.create(gameDevelopmentDTO);

            // Create "Software Engineering" subcategory
            CreateCategoryDTO softwareEngineeringDTO = new CreateCategoryDTO();
            softwareEngineeringDTO.setName("Software Engineering");
            softwareEngineeringDTO.setCode("software-engineering");
            softwareEngineeringDTO.setDescription("Understand software development methodologies, design patterns, and best practices to build robust and scalable software solutions.");
            softwareEngineeringDTO.setParentCategoryId(developmentId.toString()); // Parent category is "Development"
            UUID softwareEngineeringId = categoryService.create(softwareEngineeringDTO);

            // Create "Development Tools" subcategory
            CreateCategoryDTO developmentToolsDTO = new CreateCategoryDTO();
            developmentToolsDTO.setName("Development Tools");
            developmentToolsDTO.setCode("development-tools");
            developmentToolsDTO.setDescription("Explore essential development tools and technologies such as version control systems, IDEs, and testing frameworks.");
            developmentToolsDTO.setParentCategoryId(developmentId.toString()); // Parent category is "Development"
            UUID developmentToolsId = categoryService.create(developmentToolsDTO);

            // Create "IT & Software" category
            CreateCategoryDTO itSoftwareDTO = new CreateCategoryDTO();
            itSoftwareDTO.setName("IT & Software");
            itSoftwareDTO.setCode("it-software");
            itSoftwareDTO.setDescription("Explore the world of information technology (IT) and software, covering a wide range of topics including IT certification, network security, operating systems, databases, and software testing.");
            itSoftwareDTO.setParentCategoryId(null); // Đây là category gốc
            UUID itSoftwareId = categoryService.create(itSoftwareDTO);

            // Create "IT Certification" subcategory
            CreateCategoryDTO itCertificationDTO = new CreateCategoryDTO();
            itCertificationDTO.setName("IT Certification");
            itCertificationDTO.setCode("it-certification");
            itCertificationDTO.setDescription("Prepare for and obtain industry-recognized certifications in information technology (IT), including CompTIA A+, Cisco CCNA, AWS Certified Solutions Architect, and Microsoft Azure.");
            itCertificationDTO.setParentCategoryId(itSoftwareId.toString()); // Đây là subcategory của "IT & Software"
            UUID itCertificationId = categoryService.create(itCertificationDTO);

            // Create "Network & Security" subcategory
            CreateCategoryDTO networkSecurityDTO = new CreateCategoryDTO();
            networkSecurityDTO.setName("Network & Security");
            networkSecurityDTO.setCode("network-security");
            networkSecurityDTO.setDescription("Explore networking concepts and security principles, including cybersecurity fundamentals, ethical hacking, network security, and penetration testing.");
            networkSecurityDTO.setParentCategoryId(itSoftwareId.toString()); // Đây là subcategory của "IT & Software"
            UUID networkSecurityId = categoryService.create(networkSecurityDTO);

            // Create "Operating Systems" subcategory
            CreateCategoryDTO operatingSystemsDTO = new CreateCategoryDTO();
            operatingSystemsDTO.setName("Operating Systems");
            operatingSystemsDTO.setCode("operating-systems");
            operatingSystemsDTO.setDescription("Learn about different operating systems, their features, and administration tasks, including Linux administration, Windows Server, macOS troubleshooting, and UNIX.");
            operatingSystemsDTO.setParentCategoryId(itSoftwareId.toString()); // Đây là subcategory của "IT & Software"
            UUID operatingSystemsId = categoryService.create(operatingSystemsDTO);

            // Create "Databases" subcategory
            CreateCategoryDTO databasesDTO = new CreateCategoryDTO();
            databasesDTO.setName("Databases");
            databasesDTO.setCode("databases");
            databasesDTO.setDescription("Discover various types of databases, database management systems (DBMS), and database administration, including SQL fundamentals, NoSQL databases, and data warehousing.");
            databasesDTO.setParentCategoryId(itSoftwareId.toString()); // Đây là subcategory của "IT & Software"
            UUID databasesId = categoryService.create(databasesDTO);

            // Create "Software Testing" subcategory
            CreateCategoryDTO softwareTestingDTO = new CreateCategoryDTO();
            softwareTestingDTO.setName("Software Testing");
            softwareTestingDTO.setCode("software-testing");
            softwareTestingDTO.setDescription("Gain expertise in software testing methodologies, techniques, and tools, including test automation, performance testing, mobile app testing, and Selenium WebDriver.");
            softwareTestingDTO.setParentCategoryId(itSoftwareId.toString()); // Đây là subcategory của "IT & Software"
            UUID softwareTestingId = categoryService.create(softwareTestingDTO);
        };
    }

}
