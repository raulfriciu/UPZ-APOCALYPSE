package edu.upc.dsa;

import edu.upc.dsa.exception.IncorrectPasswordException;
import edu.upc.dsa.exception.UserNotRegisteredException;
import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.models.Credenciales;
import edu.upc.dsa.models.User;

import java.util.HashMap;
import java.util.List;

public interface GameManager {
    public int addUser(String name, String mail, String password);
    User registrarUser(User user) throws EmailUsedException;
    //User login(String email, String password) throws UserNotRegisteredException, IncorrectPasswordException;
    public User Login(Credenciales credenciales) throws UserNotRegisteredException, IncorrectPasswordException;
    public int deleteUser(Credenciales credenciales);
    public User updateUser(String mail, String newName, String newPassword, String newMail);
    public User getUser(String email);
    List<User> getallusers();
    public int size();

}
