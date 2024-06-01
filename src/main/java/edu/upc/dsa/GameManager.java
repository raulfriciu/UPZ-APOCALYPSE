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
<<<<<<< HEAD
    public User Login(Credenciales credenciales) throws UserNotRegisteredException, IncorrectPasswordException;
    public int deleteUser(Credenciales credenciales);
    public User updateUser(String mail, String newName, String newPassword, String newMail);
    public User getUser(String email);
    List<User> getallusers();
    public int size();
    List<Item> Shop();
    public void addDenuncia(Denuncia denuncia);
    public List<Denuncia> getDenuncias();
    List<FAQ> getPreguntasFrequentes();
    public void addPreguntasFrequentes(List<FAQ> faqs);
    int realizarConsulta(Question consulta);
    List<Question> getLQuestions();
=======
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

>>>>>>> c090a840db8f76b5898d03c380f3a787fe72808a
}
