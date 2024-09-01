package org.deus.src.dtos.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebsocketMessageDTO {
    private String room;
    private String event;
    private PayloadDTO payload;
}