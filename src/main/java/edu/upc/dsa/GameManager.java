package edu.upc.dsa;

import edu.upc.dsa.exception.IncorrectPasswordException;
import edu.upc.dsa.exception.UserNotRegisteredException;
import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.models.Credenciales;
import edu.upc.dsa.models.User;

import java.util.HashMap;
import java.util.List;

public interface GameManager {
    public User registrarUser(User user) throws EmailUsedException;
    User Login(String email, String password) throws UserNotRegisteredException, IncorrectPasswordException;
    public void deleteUser(String name, String password);
    public User getUser(String name, String password);
    public User getUserByEmail(String email);
    public List<User> findAll();
    public User updateUser(User t);


    public int size();
}
