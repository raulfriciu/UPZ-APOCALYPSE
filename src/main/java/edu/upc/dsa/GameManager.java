package edu.upc.dsa;

import edu.upc.dsa.exception.IncorrectPasswordException;
import edu.upc.dsa.exception.UserNotRegisteredException;
import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.models.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface GameManager {
    int registrarUser(User user) ;
    User login(Credenciales credenciales) throws UserNotRegisteredException, IncorrectPasswordException;
    void deleteUser(Credenciales credenciales) throws UserNotRegisteredException, IncorrectPasswordException;
    List<Item> Shop();
    int size();

    //MINIM2
    void addDenuncia(Denuncia denuncia);
    List<Denuncia> getDenuncias();
    List<FAQ> getPreguntasFrequentes();
    void addPreguntasFrequentes(List<FAQ> faqs);
    int realizarConsulta(Question consulta);
    List<Question> getLQuestions();
}
