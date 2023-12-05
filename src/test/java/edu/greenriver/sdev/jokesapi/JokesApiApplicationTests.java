package edu.greenriver.sdev.jokesapi;

import edu.greenriver.sdev.jokesapi.model.Joke;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JokesApiApplicationTests
{
    @LocalServerPort
    private int port;


    @Autowired
    private TestRestTemplate rest;

    @Test
    public void contextLoads()
    {
    }

    @Test
    public void getAllJokes()
    {
        String endpoint = "http://localhost:" + port + "/jokes";
        HttpEntity request = new HttpEntity(new HttpHeaders());
        ResponseEntity<Joke[]> response = rest.exchange(endpoint, HttpMethod.GET, request, Joke[].class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        System.out.println(Arrays.toString(response.getBody()));
        assertTrue(response.getBody().length > 0);
    }

    @Test
    public void addJoke()
    {
        String endpoint = "http://localhost:" + port + "/jokes";

        //set JSON header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //pass joke into HttpEntity as request body
        Joke newJoke = new Joke("Knock knock!");
        HttpEntity request = new HttpEntity(newJoke, headers);

        ResponseEntity<Joke> response = rest.exchange(endpoint, HttpMethod.POST, request, Joke.class);
        Joke saved = response.getBody();

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        System.out.println(saved);
        assertTrue(saved.getJokeText().equals("Knock knock!"));
    }

    @Test
    public void updateJoke()
    {
        String endpoint = "http://localhost:" + port + "/jokes";

        //set JSON header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //pass joke into HttpEntity as request body
        Joke jokeToUpdate = new Joke(1, "Knock knock, who is there?");
        HttpEntity request = new HttpEntity(jokeToUpdate, headers);

        ResponseEntity<Joke> response = rest.exchange(endpoint, HttpMethod.PUT, request, Joke.class);
        Joke updated = response.getBody();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        System.out.println(updated);
        assertTrue(updated.getJokeText().equals("Knock knock, who is there?"));
        assertEquals(jokeToUpdate.getId(), updated.getId());
    }

    @Test
    public void deleteJoke()
    {
        String endpoint = "http://localhost:" + port + "/jokes";

        //set JSON header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //pass joke into HttpEntity as request body
        Joke jokeToDelete = new Joke(2, "");
        HttpEntity request = new HttpEntity(jokeToDelete, headers);

        ResponseEntity<Joke> response = rest.exchange(endpoint, HttpMethod.DELETE, request, Joke.class);
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);

        //verify the Joke is gone
        endpoint += "/2";
        request = new HttpEntity(headers);
        response = rest.exchange(endpoint, HttpMethod.GET, request, Joke.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void getMissingJokeById()
    {
        String endpoint = "http://localhost:" + port + "/jokes/6000";

        //set JSON header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<Joke> response = rest.exchange(endpoint, HttpMethod.GET, request, Joke.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void getJokeById()
    {
        String endpoint = "http://localhost:" + port + "/jokes/3";

        //set JSON header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<Joke> response = rest.exchange(endpoint, HttpMethod.GET, request, Joke.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody().getJokeText());
    }
}
