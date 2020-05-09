package com.MiniShogi.game.serializers;

import com.MiniShogi.game.Piece;
import com.MiniShogi.game.Player;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class PlayerJsonSerializer extends JsonSerializer<Player> {

    @Override
    public void serialize(Player player, JsonGenerator jsonGen,
                          SerializerProvider serializerProvider) throws IOException, JsonProcessingException{
        jsonGen.writeStartObject();

        jsonGen.writeStringField("name", player.getName());
        jsonGen.writeArrayFieldStart("capturedList");
        for(Piece p : player.getCaptured()){
            jsonGen.writeStartObject();
            jsonGen.writeStringField("name", p.getName());

            jsonGen.writeObjectField("location", p.getLocation());
            jsonGen.writeEndObject();
        }
        jsonGen.writeEndArray();

        jsonGen.writeArrayFieldStart("onBoardList");
        for(Piece p : player.getOnBoard()){
            jsonGen.writeStartObject();
            jsonGen.writeStringField("name", p.getName());

            jsonGen.writeObjectField("location", p.getLocation());
            jsonGen.writeEndObject();
        }
        jsonGen.writeEndArray();

        jsonGen.writeEndObject();
    }

}
