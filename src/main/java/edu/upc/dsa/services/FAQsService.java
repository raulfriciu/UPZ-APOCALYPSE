package edu.upc.dsa.services;

import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.models.FAQ;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Api(value = "/FAQs", description = "Endpoint to FAQs Service")
@Path("/")

public class FAQsService {
    private GameManager gm;

    public FAQsService() {
        this.gm = GameManagerImpl.getInstance();
        List<FAQ> preguntasFrequentes = new LinkedList<>();
        preguntasFrequentes.add(new FAQ("¿Como se llaman los creadores?", "Pere, Andrea y Raul"));
        preguntasFrequentes.add(new FAQ("¿Porque crearon este juego?", "Nos gusta programar"));
        gm.addPreguntasFrequentes(preguntasFrequentes);
    }

        @GET
        @ApiOperation(value = "get all FAQs", notes = "asdasd")
        @ApiResponses(value = {
                @ApiResponse(code = 201, message = "Successful", response = FAQ.class, responseContainer="List"),
        })

        @Path("/FAQs")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getPreguntasFrequentes() {
            List<FAQ> faqs = this.gm.getPreguntasFrequentes();

            GenericEntity<List<FAQ>> entity = new GenericEntity<List<FAQ>>(faqs) {};
            return Response.status(201).entity(entity).build();
        }
    }



