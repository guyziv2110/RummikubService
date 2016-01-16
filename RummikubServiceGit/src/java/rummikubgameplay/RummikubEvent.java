/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import ws.rummikub.Event;

/**
 *
 * @author guy
 */
public class RummikubEvent {
    private ws.rummikub.Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getEventInGameId() {
        return eventInGameId;
    }

    public void setEventInGameId(int eventInGameId) {
        this.eventInGameId = eventInGameId;
    }
    private int eventInGameId;
}
