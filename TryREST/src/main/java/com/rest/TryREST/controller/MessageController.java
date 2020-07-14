package com.rest.TryREST.controller;

import com.rest.TryREST.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //нотация контроллера
@RequestMapping("message") //все обращения, начинающиеся с message, будут перенапровляться на этот контроллер
public class MessageController {
    private int counter = 4; //счетчик с обозначением, что следующая запись будет 4-ой

    //список предопределенных сообщений
    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>(){{
        add(new HashMap<String, String>(){{put("id", "1"); put("text", "First message");}});
        add(new HashMap<String, String>(){{put("id", "2"); put("text", "Second message");}});
        add(new HashMap<String, String>(){{put("id", "3"); put("text", "Third message");}});
    }};

    @GetMapping
    public List<Map<String, String>> list(){
        return messages;
    }

    @GetMapping("{id}") //mapping для получения единственной записи
    public Map<String, String> getOne(@PathVariable String id){
        //поиск записи в списке сообщений
        return getMessage(id);
    }

    private Map<String, String> getMessage(@PathVariable String id){
        return messages.stream().filter(message -> message.get("id").equals(id)).findFirst().orElseThrow(NotFoundException::new); //.findFirst().orElseThrow(NotFoundException::new) для выдачи ошибки 404
    }

    //добавление
    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message){
       //message идентификатор
        message.put("id", String.valueOf(counter++)); //получаем сообщение от пользователя, добавляем ему новый id, с увеличением текущего счетчика, и после кладем его в список сообщений

        messages.add(message);

        return message;
    }

    //обновление текущей записи
    @PutMapping("{id}")
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> message){
        Map<String, String> messageFromDb = getMessage(id);

        messageFromDb.putAll(message); //обновление полей полями, которые мы получили от пользователя
        messageFromDb.put("id", id);

        return messageFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Map<String, String> message = getMessage(id);

        messages.remove(message);
    }
}
