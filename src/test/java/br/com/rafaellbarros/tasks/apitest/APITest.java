package br.com.rafaellbarros.tasks.apitest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

public class APITest {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8001/tasks-backend";
    }

    @Test
    public void deveRetornarTarefas() {
        RestAssured.given()
                .when()
                    .get("/todo")
                .then()
                    .statusCode(200)
                ;
    }

    @Test
    public void deveAdicionarTarefaComSucesso() {
        RestAssured.given()
                .body("{ \"task\": \"Teste via API\", \"dueDate\": \""+LocalDate.now()+"\"}")
                    .contentType(ContentType.JSON)
                .when()
                    .post("/todo")
                .then()
                    .statusCode(201)
        ;
    }

    @Test
    public void naoDeveAdicionarTarefaInvalida() {
        RestAssured.given()
                .body("{ \"task\": \"Teste via API\", \"dueDate\": \""+LocalDate.now().minusDays(1)+"\"}")
                    .contentType(ContentType.JSON)
                .when()
                    .post("/todo")
                .then()
                    .statusCode(400)
                    .body("message", CoreMatchers.is("Due date must not be in past"))
        ;
    }

    @Test
    public void deveRemoverTarefaComSucesso() {

        final Integer id = RestAssured.given()
                .body("{ \"task\": \"Tarefa para remoção\", \"dueDate\": \""+LocalDate.now()+"\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/todo")
                .then()
                // .log().all()
                .statusCode(201)
                .extract().path("id")
        ;

        System.out.println(id);

        // remover
        RestAssured.given()
                .when()
                .delete("/todo/"+id)
                .then()
                .statusCode(204);


    }
}


