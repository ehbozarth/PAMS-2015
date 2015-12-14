package com.pams.controllers;

import com.pams.entities.Club;
import com.pams.services.ItemRepository;
import com.pams.entities.User;
import com.pams.services.UserRepository;
import com.pams.utils.PasswordHash;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * Created by MattBrown on 12/8/15.
 */
@RestController
public class PAMController {

    @Autowired
    UserRepository users;

    @Autowired
    ItemRepository clubs;

    @PostConstruct
    public void loadData(){
        String fileContent = readFile("golf.csv");
        String[] lines = fileContent.split("\n");
        if (clubs.count() == 0){
            for (String line : lines){
                if (line == lines [0])
                    continue;
                String columns[] = line.split(",");
                Club club = new Club();
                club.serialNumber = Integer.valueOf(columns[0]);
                club.maker = columns[1];
                club.clubType = columns[2];
                club.year = Integer.valueOf(columns[3]);
                club.color = columns[4];
                clubs.save(club);
            }

        }
    }
    static String readFile(String fileName){
        File f = new File(fileName);
        try {
            FileReader fr = new FileReader(f);
            int fileSize = (int) f.length();
            char[] fileContent = new char[fileSize];
            fr.read(fileContent);
            return new String(fileContent);
        }catch (Exception e){
            return null;
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User login(
            @RequestBody User user,
            HttpSession session
    ) throws Exception {
        User tempUser = users.findOneByUsername(user.username);
        if (tempUser == null) {
            tempUser = new User();
            tempUser.username = user.username;
            tempUser.password = PasswordHash.createHash(user.password);
            tempUser.accessLevel = User.AccessLevel.ADMIN;
            users.save(tempUser);
        }
        else if (!PasswordHash.validatePassword(user.password, tempUser.password)){
                    throw new Exception ("Wrong password!");
        }
        session.setAttribute("username", user.username);
        return tempUser;
    }

    @RequestMapping(path = "/create-user", method = RequestMethod.POST)
    public User addUser(
            @RequestBody User user,
            HttpSession session
    ) throws Exception {
        User tempUser = users.findOneByUsername(user.username);
        if (tempUser == null){
            tempUser = new User();
            tempUser.username = user.username;
            tempUser.password = PasswordHash.createHash(user.password);
            tempUser.accessLevel = user.getAccessLevel();
            tempUser.companyName = user.companyName;
            tempUser.address = user.address;
            tempUser.city = user.city;
            tempUser.state = user.state;
            tempUser.zip = user.zip;
            tempUser.email = user.email;
            users.save(tempUser);
        }
        else if (!PasswordHash.validatePassword(user.password, tempUser.password)){
            throw new Exception ("Wrong password!");
        }
        session.setAttribute("username", user.username);
        return tempUser;
    }
    @RequestMapping(path = "/find-user" , method = RequestMethod.GET)
    public User findUser(
            @RequestBody User user
    )throws Exception{
        return users.findOneByUsername(user.username);
    }

    @RequestMapping(path = "/edit-user", method = RequestMethod.POST)
    public void editUser(
            @RequestBody User user,
            HttpSession session
    ) throws Exception {
        if (session.getAttribute("username") == null) {
            throw new Exception("You cannot edit!");
        }
        users.save(user);
    }

    @RequestMapping(path = "/delete-user/{id}", method = RequestMethod.DELETE)
    public void deleteUser(
            @PathVariable("id") int id,
            HttpSession session
    ) throws Exception {
        if (session.getAttribute("username") == null) {
            throw new Exception("You cannot delete!");
        }
        users.delete(id);
    }

    /*@RequestMapping(path ="/create-club", method = RequestMethod.POST)
    public Club addClub(
            @RequestBody Club club,
            HttpSession session
    )throws Exception{
        if (session.getAttribute("username") == null){
            throw new Exception ("You cannot create this club!");
        }
        //Club thisClub = findClub(club);
        if (thisClub == null){
            thisClub = new Club();
            thisClub.serialNumber = club.serialNumber;
            thisClub.maker = club.maker;
            thisClub.clubType = club.clubType;
            thisClub.year = club.year;
            thisClub.color = club.color;
            clubs.save(club);
        }
        return club;
    }*/

    @RequestMapping(path = "/find-club/{id}" , method = RequestMethod.GET)
    public Club findClub(
            @PathVariable ("id") int id
    )throws Exception{
        return clubs.findOne(id);
    }

    @RequestMapping(path = "/edit-club", method = RequestMethod.POST)
    public void editClub(
            @RequestBody Club club,
            HttpSession session
    )throws Exception{
        if (session.getAttribute("username") == null){
            throw new Exception ("You cannot edit this club!");
        }
        clubs.save(club);
    }
    @RequestMapping(path = "/delete-club/{id}", method = RequestMethod.DELETE)
    public void deleteClub(
            @PathVariable ("id") int id,
            HttpSession session
    )throws Exception{
        if (session.getAttribute("username") == null){
            throw new Exception ("You cannot delete this club!");
        }
        clubs.delete(id);
    }




















    @RequestMapping(path = "/edit-inventory", method = RequestMethod.POST)
    public void editInventory(
            @RequestBody Club club,
            HttpSession session
    )throws Exception{
        if (session.getAttribute("username") == null){
            throw new Exception ("You cannot edit!");
        }
        clubs.save(club);
    }

    @RequestMapping(path = "/delete-inventory/{id}", method = RequestMethod.DELETE)
    public void deleteInventory(
            @PathVariable("id") int id,
            HttpSession session
    )throws Exception{
        if (session.getAttribute("username") == null){
            throw new Exception ("You cannot delete!");
        }
        clubs.delete(id);
    }



     /* @RequestMapping(path = "/create-retailer", method = RequestMethod.POST)
    public void addRetailerUser(
            @RequestBody User user,
            HttpSession session
    ) throws Exception {
        User tempUser = users.findOneByUsername(user.username);
        if (tempUser == null){
            users.save(user);
        }
        session.setAttribute("username", user.username);
    }

    @RequestMapping(path = "create-joe", method = RequestMethod.POST)
    public void addJoeUser(
            @RequestBody User user,
            HttpSession session
    ) throws Exception {
        User tempUser = users.findOneByUsername(user.username);
        if (tempUser == null){
            users.save(user);
        }
    }
    */

    /*@RequestMapping("/delete-retailer")
    public void deleteRetailerUser(
            HttpSession session,
            int id
    ) throws Exception {
        if (session.getAttribute("username") == null) {
            throw new Exception("You cannot delete!");
        }
        users.delete(id);
    }

    @RequestMapping("/delete-joe")
    public void deleteJoeUser(
            HttpSession session,
            int id
    ) throws Exception {
        if (session.getAttribute("username") == null) {
            throw new Exception("You cannot delete!");
        }
        users.delete(id);
    }*/
//    @RequestMapping("/import-file")
//    public void importFile(HttpSession session, MultipartFile file) throws IOException {
//        if(clubs.count() == 0){
//            String fileContentItems = new String(file.getBytes());
//            String [] lineItems = fileContentItems.split("\n");
//
//            for (String linesItems : lineItems){
//                if (linesItems == lineItems[0])
//                    continue;
//                String [] columns = linesItems.split(",");
//                Club item = new Club();
//                item.serialNumber = columns[0];
//                item.productModel = columns [1];
//                item.companyUser = columns [2];
//                clubs.save(item);
//            }
//        }
//    }
    /*@RequestMapping(path = "/edit-retailer/{id}", method = RequestMethod.POST)
    public void editRetailerUser(
            @RequestBody User user,
            HttpSession session
    ) throws Exception {
        if (session.getAttribute("username") == null) {
            throw new Exception("You cannot edit!");
        }
        users.save(user);
    }

    @RequestMapping(path = "/edit-joe/{id}", method = RequestMethod.POST)
    public void editJoeUser(
            @RequestBody User user,
            HttpSession session
    ) throws Exception {
        if (session.getAttribute("username") == null) {
            throw new Exception("You cannot edit!");
        }
        users.save(user);
    }*/
     /* @RequestMapping(path = "/upload-club-inventory", method = RequestMethod.POST)
    public void uploadClubsInventory(
            @RequestBody Club club,
            HttpSession session,
            MultipartFile file
    )throws Exception{
        if (session.getAttribute("username") == null) {
            throw new Exception("You cannot edit!");
        }
        File f = File.createTempFile("file", file.getOriginalFilename(), new File ("public"));
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(file.getBytes());

        Club itemFile = new Club();
        itemFile.fileName = f.getName();
        clubs.save(club);
    }*/

    /*@RequestMapping(path = "/create-balls-inventory", method = RequestMethod.POST)
    public void addBallsInventory(
            @RequestBody Balls balls,
            HttpSession session,
            MultipartFile file
    )throws Exception{

    }*/
}
