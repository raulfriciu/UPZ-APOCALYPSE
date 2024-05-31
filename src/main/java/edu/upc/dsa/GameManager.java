package edu.upc.dsa;

import edu.upc.dsa.exception.IncorrectPasswordException;
import edu.upc.dsa.exception.UserNotRegisteredException;
import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.models.*;

import java.util.HashMap;
import java.util.List;

public interface GameManager {
    //public int addUser(String name, String mail, String password);
    User registrarUser(User user) throws EmailUsedException;
    User Login(Credenciales credenciales) throws UserNotRegisteredException, IncorrectPasswordException;
    List<Item> Shop();

    /*
    public void deleteUser(String name, String password);
    public User getUser(String name, String password);
    public User getUserByEmail(String email);
    public List<User> findAll();
    public User updateUser(User t);


     */
    public int size();

    List<FAQ> getPreguntasFrequentes();
    public void addPreguntasFrequentes(List<FAQ> faqs);

}
