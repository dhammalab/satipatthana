package com.dhammalab.satipatthna.domain;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created by Tretyakov on 14.07.2014.
 */
public class SessionTest extends TestCase {

    private Session session;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        session = new Session(0);
        fillSessionWithEvents();
    }

    public void testEventTypeCount() {
        assertEquals(4, session.getCompletedNotesCount(NoteType.SEE_IN));
        assertEquals(1, session.getCompletedNotesCount(NoteType.HEAR_IN));
        assertEquals(0, session.getCompletedNotesCount(NoteType.FEEL_IN));
        assertEquals(2, session.getCompletedNotesCount(NoteType.SEE_OUT));
        assertEquals(0, session.getCompletedNotesCount(NoteType.HEAR_OUT));
        assertEquals(0, session.getCompletedNotesCount(NoteType.FEEL_OUT));
    }

    public void testCalculateNotingSpeed() {
        List<Bin> bins = session.calculateNotingSpeed(5000);
        assertEquals(4, bins.size());
        assertEquals(5, bins.get(0).getValue());
        assertEquals(2, bins.get(1).getValue());
        assertEquals(2, bins.get(2).getValue());
        assertEquals(0, bins.get(3).getValue());
    }

    public void testCalculateNoteLengths() {
        List<Bin> bins = session.calculateNoteLengths(2);
        assertEquals(2, bins.size());
        assertEquals(500, bins.get(0).getStartTime());
        assertEquals(4500, bins.get(0).getEndTime());
        assertEquals(5, bins.get(0).getValue());
        assertEquals(4500, bins.get(1).getStartTime());
        assertEquals(8500, bins.get(1).getEndTime());
        assertEquals(2, bins.get(1).getValue());
    }

    public void testGetLatestEventForType() {
        assertNull(session.getLatestEvent(NoteType.FEEL_OUT));
        assertEquals(15500, session.getLatestEvent(NoteType.SEE_OUT).getTime());
        assertEquals(12000, session.getLatestEvent(NoteType.HEAR_IN).getTime());
    }

    private void fillSessionWithEvents() {
        session.addEvent(new Event(NoteType.SEE_IN, 0, true));
        session.addEvent(new Event(NoteType.SEE_IN, 500, false));
        session.addEvent(new Event(NoteType.SEE_IN, 1300, true));
        session.addEvent(new Event(NoteType.SEE_IN, 1800, false));
        session.addEvent(new Event(NoteType.SEE_OUT, 2000, true));
        session.addEvent(new Event(NoteType.FEEL_IN, 2100, true));
        session.addEvent(new Event(NoteType.SEE_IN, 3000, true));
        session.addEvent(new Event(NoteType.SEE_OUT, 5000, false));
        session.addEvent(new Event(NoteType.HEAR_IN, 5500, true));
        session.addEvent(new Event(NoteType.SEE_IN, 5900, false));
        session.addEvent(new Event(NoteType.SEE_OUT, 7000, true));
        session.addEvent(new Event(NoteType.SEE_IN, 11000, true));
        session.addEvent(new Event(NoteType.HEAR_IN, 12000, false));
        session.addEvent(new Event(NoteType.SEE_IN, 13300, false));
        session.addEvent(new Event(NoteType.SEE_IN, 14400, true));
        session.addEvent(new Event(NoteType.SEE_OUT, 15500, false));
    }

}
